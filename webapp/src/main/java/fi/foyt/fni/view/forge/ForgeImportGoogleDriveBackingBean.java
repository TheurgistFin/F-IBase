package fi.foyt.fni.view.forge;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.ComparatorUtils;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;

import fi.foyt.fni.drive.DriveManager;
import fi.foyt.fni.materials.FolderController;
import fi.foyt.fni.materials.MaterialController;
import fi.foyt.fni.materials.MaterialPermissionController;
import fi.foyt.fni.persistence.model.materials.Folder;
import fi.foyt.fni.persistence.model.materials.MaterialPublicity;
import fi.foyt.fni.persistence.model.users.User;
import fi.foyt.fni.persistence.model.users.UserToken;
import fi.foyt.fni.security.ForbiddenException;
import fi.foyt.fni.security.LoggedIn;
import fi.foyt.fni.session.SessionController;
import fi.foyt.fni.system.SystemSettingsController;
import fi.foyt.fni.utils.auth.AuthUtils;
import fi.foyt.fni.utils.faces.FacesUtils;

@RequestScoped
@Named
@Stateful
@URLMappings(mappings = { 
  @URLMapping(
    id = "forge-import-google-drive", 
    pattern = "/forge/import-google-drive", 
    viewId = "/forge/import-google-drive.jsf"
  ),
  @URLMapping(
	  id = "forge-import-google-drive-folder", 
		pattern = "/forge/import-google-drive/#{forgeImportGoogleDriveBackingBean.folderId}", 
		viewId = "/forge/import-google-drive.jsf"
  )
})
public class ForgeImportGoogleDriveBackingBean {

  private final static String REQUIRED_SCOPE = "https://www.googleapis.com/auth/drive";
  
  @Inject
  private Logger logger;

  @Inject
  private SessionController sessionController;

  @Inject
  private MaterialController materialController;

  @Inject
  private MaterialPermissionController materialPermissionController;
  
  @Inject
  private SystemSettingsController systemSettingsController;

  @Inject
  private FolderController folderController;

  @Inject
  private DriveManager driveManager;
  
	@SuppressWarnings("unchecked")
  @URLAction
	@LoggedIn
	public void load() throws IOException {
    UserToken userToken = sessionController.getLoggedUserToken();
    User loggedUser = userToken.getUserIdentifier().getUser();
    String contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();

    if (AuthUtils.getInstance().isExpired(userToken) || !AuthUtils.getInstance().isGrantedScope(userToken, REQUIRED_SCOPE)) {
      // We need authorization from Google
      
      String redirectUrl = contextPath + "/forge/import-google-drive";
      FacesContext.getCurrentInstance().getExternalContext().redirect(new StringBuilder()
        .append(contextPath)
        .append("/login?loginMethod=GOOGLE&redirectUrl=")
        .append(URLEncoder.encode(redirectUrl, "UTF-8"))
        .append("&extraScopes=")
        .append(REQUIRED_SCOPE)
        .toString());
    } else {
      Drive drive = driveManager.getUserDrive(userToken.getToken());
      FileList fileList = null;
      root = folderId == null;
      files = new ArrayList<File>();
      
      if (folderId == null) {
        fileList = driveManager.listFiles(drive, "trashed != true and 'root' in parents");
      } else {
        fileList = driveManager.listFiles(drive, "trashed != true and '" + folderId + "' in parents");
      }
      
      for (File file : fileList.getItems()) {
        if (materialController.findGoogleDocumentByCreatorAndDocumentId(loggedUser, file.getId()) == null) {
          files.add(file);
        }
      }
      
      Collections.sort(files, ComparatorUtils.chainedComparator(
        Arrays.asList(
          new MimeTypeComparator("application/vnd.google-apps.folder"),
          new TitleComparator()
        )
      ));
    }
    
    parentFolderId = null;
	}

	public boolean isRoot() {
    return root;
  }
	
	public String getFolderId() {
    return folderId;
  }
	
	public void setFolderId(String folderId) {
    this.folderId = folderId;
  }
	
	public List<File> getFiles() {
    return files;
  }
	
	public List<String> getImportEntryIds() {
    return importEntryIds;
  }
	
	public void setImportEntryIds(List<String> importEntryIds) {
    this.importEntryIds = importEntryIds;
  }

	@LoggedIn
	public void importFiles() throws IOException {
    Folder parentFolder = parentFolderId != null ? folderController.findFolderById(parentFolderId) : null;
    if (parentFolder != null) {
      if (!materialPermissionController.hasModifyPermission(sessionController.getLoggedUser(), parentFolder)) {
        throw new ForbiddenException();
      }
    }

	  String accountUser = System.getProperty("fni-google-drive.accountUser");
    UserToken userToken = sessionController.getLoggedUserToken();
    Drive drive = driveManager.getUserDrive(userToken.getToken());
    User loggedUser = userToken.getUserIdentifier().getUser();
    
    for (String entryId : importEntryIds) {
      if (materialController.findGoogleDocumentByCreatorAndDocumentId(loggedUser, entryId) == null) {
        try {
          File file = driveManager.getFile(drive, entryId);

          if (!driveManager.hasRoles(drive, accountUser, entryId, "owner","reader", "writer")) {
            Permission permission = new Permission();
            permission.setRole("reader");
            permission.setType("user");
            permission.setValue(accountUser);
            driveManager.insertPermission(drive, file.getId(), permission);
            materialController.createGoogleDocument(loggedUser, null, parentFolder, file.getTitle(), file.getId(), file.getMimeType(), MaterialPublicity.PRIVATE);
          }
          
        } catch (IOException e) {
          logger.log(Level.SEVERE, "Communication with Google Drive failed", e);
          FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, FacesUtils.getLocalizedValue("forge.googleDriveImport.importFailure"));
        }
      }
    }
    
    if (parentFolder != null) {
      FacesContext.getCurrentInstance().getExternalContext().redirect(new StringBuilder()
        .append(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath())
        .append("/forge/folders/")
        .append(parentFolder.getPath())
        .toString());
    } else {
      FacesContext.getCurrentInstance().getExternalContext().redirect(new StringBuilder()
        .append(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath())
        .append("/forge/")
        .toString());
    }
	}
	
	public String getFileIcon(File file) {
	  switch (file.getMimeType()) {
	    case "application/vnd.google-apps.folder":
	      return "folder";
	    case "application/vnd.google-apps.document":
	      return "document";
	    case "application/vnd.google-apps.presentation":
	      return "presentation";
	    case "application/vnd.google-apps.spreadsheet":
	      return "spreadsheet";
	    case "application/vnd.google-apps.drawing":
	      return "drawing";	      
	  }
	  
	  return "file";
	}
	
	private boolean root;
	private String folderId;
  private List<File> files;
  private List<String> importEntryIds;
  private Long parentFolderId;
  
  private class MimeTypeComparator implements Comparator<File> {
    
    public MimeTypeComparator(String mimeType) {
      this.mimeType = mimeType;
    }
    
    @Override
    public int compare(File o1, File o2) {
      if (o1.getMimeType().equals(o2.getMimeType()))
        return 0;
      
      if (o1.getMimeType().equals(mimeType))
        return -1;
      
      if (o2.getMimeType().equals(mimeType))
        return 1;

      return 0;
    }
    
    private String mimeType;
  }
  
  private class TitleComparator implements Comparator<File> {

    @Override
    public int compare(File file1, File file2) {
      return file1.getTitle().compareTo(file2.getTitle());
    }

  }
}
