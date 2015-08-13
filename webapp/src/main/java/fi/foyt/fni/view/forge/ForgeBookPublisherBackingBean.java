package fi.foyt.fni.view.forge;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.annotation.Matches;
import org.ocpsoft.rewrite.annotation.Parameter;
import org.ocpsoft.rewrite.annotation.RequestAction;

import fi.foyt.fni.jsf.NavigationController;
import fi.foyt.fni.materials.MaterialController;
import fi.foyt.fni.materials.MaterialPermissionController;
import fi.foyt.fni.persistence.model.materials.BookLayout;
import fi.foyt.fni.persistence.model.materials.Folder;
import fi.foyt.fni.persistence.model.materials.Material;
import fi.foyt.fni.persistence.model.system.SystemSettingKey;
import fi.foyt.fni.persistence.model.users.User;
import fi.foyt.fni.security.LoggedIn;
import fi.foyt.fni.session.SessionController;
import fi.foyt.fni.system.SystemSettingsController;

@RequestScoped
@Named
@Stateful
@Join(path = "/forge/book-publisher/{ownerId}/{urlPath}", to = "/forge/book-publisher.jsf")
@LoggedIn
public class ForgeBookPublisherBackingBean {
  
  @Parameter
  @Matches("[0-9]{1,}")
  private Long ownerId;

  @Parameter
  @Matches("[a-zA-Z0-9_/.\\-:,]{1,}")
  private String urlPath;

  @SuppressWarnings("unused")
  @Inject
  private Logger logger;
  
  @Inject
  private SystemSettingsController systemSettingsController;
  
  @Inject
  private SessionController sessionController;

  @Inject
  private MaterialController materialController;

  @Inject
  private MaterialPermissionController materialPermissionController;

  @Inject
  private NavigationController navigationController;

  @RequestAction
  public String load() {
    if ((getOwnerId() == null) || (getUrlPath() == null)) {
      return navigationController.notFound();
    }

    String completePath = "/materials/" + getOwnerId() + "/" + getUrlPath();
    Material material = materialController.findMaterialByCompletePath(completePath);
    User loggedUser = sessionController.getLoggedUser();

    if (!(material instanceof BookLayout)) {
      return navigationController.notFound();
    }

    if (!materialPermissionController.hasAccessPermission(loggedUser, material)) {
      return navigationController.accessDenied();
    }
    
    materialId = material.getId();
    title = material.getTitle();
    data = ((BookLayout) material).getData();
    styles = ((BookLayout) material).getStyles();
    fonts = ((BookLayout) material).getFonts();
    folders = ForgeViewUtils.getParentList(material);
    googlePublicApiKey = systemSettingsController.getSetting(SystemSettingKey.GOOGLE_PUBLIC_API_KEY);
    
    materialController.markMaterialView(material, loggedUser);
    
    return null;
  }

  public Long getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(Long ownerId) {
    this.ownerId = ownerId;
  }

  public String getUrlPath() {
    return urlPath;
  }

  public void setUrlPath(String urlPath) {
    this.urlPath = urlPath;
  }

  public Long getMaterialId() {
    return materialId;
  }
  
  public void setMaterialId(Long materialId) {
    this.materialId = materialId;
  }

  public List<Folder> getFolders() {
    return folders;
  }
  
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
  
  public String getData() {
    return data;
  }
  
  public void setData(String data) {
    this.data = data;
  }
  
  public String getStyles() {
    return styles;
  }
  
  public void setStyles(String styles) {
    this.styles = styles;
  }
  
  public String getFonts() {
    return fonts;
  }
  
  public void setFonts(String fonts) {
    this.fonts = fonts;
  }
  
  public String getGooglePublicApiKey() {
    return googlePublicApiKey;
  }
  
  public String save() {
    BookLayout bookLayout = materialController.findBookLayout(getMaterialId());
    
    if (!materialPermissionController.hasModifyPermission(sessionController.getLoggedUser(), bookLayout)) {
      return navigationController.accessDenied();
    }
    
    materialController.updateBookLayout(bookLayout, sessionController.getLoggedUser(), getTitle(), getData(), getStyles(), getFonts());
    Folder parentFolder = bookLayout.getParentFolder();
    Long ownerId = parentFolder != null ? parentFolder.getCreator().getId() : bookLayout.getCreator().getId();
    String urlPath = bookLayout.getPath().substring(String.valueOf(ownerId).length() + 1);
    return String.format("/forge/book-publisher.jsf?faces-redirect=true&ownerId=%d&urlPath=%s", ownerId, urlPath);
  }
  
  private Long materialId;
  private List<Folder> folders;
  private String title;
  private String data;
  private String styles;
  private String fonts;
  private String googlePublicApiKey;
}
