package fi.foyt.fni.view.forge;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;

import fi.foyt.fni.materials.FolderController;
import fi.foyt.fni.materials.MaterialController;
import fi.foyt.fni.materials.TitleComparator;
import fi.foyt.fni.persistence.model.materials.Folder;
import fi.foyt.fni.persistence.model.materials.Material;
import fi.foyt.fni.persistence.model.materials.MaterialType;
import fi.foyt.fni.persistence.model.users.User;
import fi.foyt.fni.security.LoggedIn;
import fi.foyt.fni.session.SessionController;
import fi.foyt.fni.users.UserController;

@SuppressWarnings("el-syntax")
@RequestScoped
@Named
@Stateful
@URLMappings(mappings = { 
  @URLMapping(
	  id = "forge-index", 
		pattern = "/forge/", 
		viewId = "/forge/index.jsf"
  ),
	@URLMapping(
	  id = "forge-folder", 
  	pattern = "/forge/folders/#{forgeIndexBackingBean.ownerId}/#{ /[a-zA-Z0-9_\\/\\.\\-]*/ forgeIndexBackingBean.urlName }", 
		viewId = "/forge/index.jsf"
  )
})
public class ForgeIndexBackingBean {

	private static final int MAX_LAST_VIEWED_MATERIALS = 5;
	private static final int MAX_LAST_EDITED_MATERIALS = 5;

	@Inject
	private MaterialController materialController;

	@Inject
	private FolderController folderController;
	
	@Inject
	private UserController userController;
	
	@Inject
	private SessionController sessionController;

	@PostConstruct
	public void init() {
	}

	@URLAction
	@LoggedIn
	public void load() throws FileNotFoundException {
		materialsOpen = true;
		lastViewedOpen = true;
		starredOpen = true;
		lastEditedOpen = true;
		folders = new ArrayList<>();
		
		if (ownerId != null && StringUtils.isNotBlank(urlName)) {
			User owner = userController.findUserById(getOwnerId());
			if (owner == null) {
				throw new FileNotFoundException();
			}
			
			Material material = materialController.findByOwnerAndPath(owner, getUrlName());
			if (material == null) {
				throw new FileNotFoundException();
			}
			
			if (!(material instanceof Folder)) {
				throw new FileNotFoundException();
			}
			
			folderId = material.getId();
			Folder folder = (Folder) material;
			
			while (folder != null) {
			  folders.add(0, folder);
			  folder = folder.getParentFolder();
			};
			
		} else {
			folderId = null;
		}
	}

	public List<Material> getMaterials() {
		Folder folder = null;
		if (getFolderId() != null) {
			folder = folderController.findFolderById(getFolderId());
		}
		
		List<Material> materials = materialController.listMaterialsByFolder(sessionController.getLoggedUser(), folder);
		Collections.sort(materials, new TitleComparator());

		return materials;
	}

	public List<Material> getLastViewedMaterials() {
		return materialController.listViewedMaterialsByUser(sessionController.getLoggedUser(), 0, MAX_LAST_VIEWED_MATERIALS);
	}

	public List<Material> getLastEditedMaterials() {
		return materialController.listModifiedMaterialsByUser(sessionController.getLoggedUser(), 0, MAX_LAST_EDITED_MATERIALS);
	}

	public List<Material> getStarredMaterials() {
		return materialController.listStarredMaterialsByUser(sessionController.getLoggedUser());
	}

	public boolean isStarred(Long materialId) {
		Material material = materialController.findMaterialById(materialId);
		if (material != null) {
			return materialController.isStarred(sessionController.getLoggedUser(), material);
		}

		return false;
	}

	public void starMaterial(Long materialId) {
		Material material = materialController.findMaterialById(materialId);
		if (material != null) {
			User loggedUser = sessionController.getLoggedUser();
			materialController.starMaterial(material, loggedUser);
		}
	}

	public void unstarMaterial(Long materialId) {
		Material material = materialController.findMaterialById(materialId);
		if (material != null) {
			User loggedUser = sessionController.getLoggedUser();
			materialController.unstarMaterial(material, loggedUser);
		}
	}

	public boolean isMaterialsOpen() {
		return materialsOpen;
	}

	public void setMaterialsOpen(boolean materialsOpen) {
		this.materialsOpen = materialsOpen;
	}

	public boolean isLastViewedOpen() {
		return lastViewedOpen;
	}

	public void setLastViewedOpen(boolean lastViewedOpen) {
		this.lastViewedOpen = lastViewedOpen;
	}

	public boolean isStarredOpen() {
		return starredOpen;
	}

	public void setStarredOpen(boolean starredOpen) {
		this.starredOpen = starredOpen;
	}

	public boolean isLastEditedOpen() {
		return lastEditedOpen;
	}

	public void setLastEditedOpen(boolean lastEditedOpen) {
		this.lastEditedOpen = lastEditedOpen;
	}

	public void closeMaterialsList() {
		setMaterialsOpen(false);
	}

	public void openMaterialsList() {
		setMaterialsOpen(true);
	}

	public void closeLastViewedList() {
		setLastViewedOpen(false);
	}

	public void openLastViewedList() {
		setLastViewedOpen(true);
	}

	public void closeLastEditedList() {
		setLastEditedOpen(false);
	}

	public void openLastEditedList() {
		setLastEditedOpen(true);
	}

	public void closeStarredList() {
		setStarredOpen(false);
	}

	public void openStarredList() {
		setStarredOpen(true);
	}

	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}
	
	public Long getOwnerId() {
		return ownerId;
	}
	
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	
	public String getUrlName() {
		return urlName;
	}
	
	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}
	
	public String getMaterialViewer(MaterialType type) {
		switch (type) {
			case DROPBOX_FILE:
			case DROPBOX_FOLDER:
			case DROPBOX_ROOT_FOLDER:
			case UBUNTU_ONE_FILE:
			case UBUNTU_ONE_FOLDER:
			case UBUNTU_ONE_ROOT_FOLDER:
			case GOOGLE_DOCUMENT:
				break;
			case DOCUMENT:
				return "documents";
			case BINARY:
			case FILE:
			case PDF:
				return "binary";
			case FOLDER:
				return "folders";
			case IMAGE:
				return "images";
			case VECTOR_IMAGE:
				return "vectorimages";
		}

		return "todo";
	}
	
	public String getMaterialIcon(MaterialType type) {
		switch (type) {
			case DROPBOX_FILE:
				return "file";
			case DROPBOX_FOLDER:
				return "folder";
			case DROPBOX_ROOT_FOLDER:
				return "dropbox";
			case UBUNTU_ONE_FILE:
				return "file";
			case UBUNTU_ONE_FOLDER:
				return "folder";
			case UBUNTU_ONE_ROOT_FOLDER:
				return "ubuntuone";
			case GOOGLE_DOCUMENT:
				return "file";
			case DOCUMENT:
				return "document";
			case BINARY:
				return "file";
			case FILE:
				return "file";
			case PDF:
				return "pdf";
			case FOLDER:
				return "folder";
			case IMAGE:
				return "image";
			case VECTOR_IMAGE:
				return "vectorimage";
		}

		return null;
	}
	
	public List<Folder> getFolders() {
		return folders;
	}
	
	private boolean materialsOpen;
	private boolean lastViewedOpen;
	private boolean starredOpen;
	private boolean lastEditedOpen;
	private Long folderId;
	private Long ownerId;
	private String urlName;
	private List<Folder> folders;
}
