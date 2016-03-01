package fi.foyt.fni.view.forge;

import java.util.Date;
import java.util.List;

public class PublicMaterialBean {

  public PublicMaterialBean(Long id, String title, String description, String icon, String license, String creativeCommonsIconUrl, Date modified, 
      Long creatorId, String creatorName, Long modifierId, String modifierName, 
      List<String> tags, String viewPath, String editPath, String downloadLink, 
      String path, Boolean viewable) {
    super();
    this.id = id;
    this.title = title;
    this.description = description;
    this.license = license;
    this.icon = icon;
    this.creativeCommonsIconUrl = creativeCommonsIconUrl;
    this.modified = modified;
    this.creatorId = creatorId;
    this.creatorName = creatorName;
    this.modifierId = modifierId;
    this.modifierName = modifierName;
    this.tags = tags;
    this.viewPath = viewPath;
    this.editPath = editPath;
    this.downloadLink = downloadLink;
    this.path = path;
    this.viewable = viewable;
  }

  public Long getId() {
    return id;
  }
  
  public String getIcon() {
    return icon;
  }
  
  public String getTitle() {
    return title;
  }
  
  public String getDescription() {
    return description;
  }
  
  public String getLicense() {
    return license;
  }
  
  public String getCreativeCommonsIconUrl() {
    return creativeCommonsIconUrl;
  }
  
  public Date getModified() {
    return modified;
  }
  
  public Long getCreatorId() {
    return creatorId;
  }

  public String getCreatorName() {
    return creatorName;
  }
  
  public Long getModifierId() {
    return modifierId;
  }
  
  public String getModifierName() {
    return modifierName;
  }
  
  public List<String> getTags() {
    return tags;
  }
  
  public String getEditPath() {
    return editPath;
  }
  
  public String getViewPath() {
    return viewPath;
  }
  
  public String getPath() {
    return path;
  }
  
  public Boolean getViewable() {
    return viewable;
  }
  
  public String getDownloadLink() {
    return downloadLink;
  }
  
  private Long id;
  private String title;
  private String description;
  private String license;
  private String icon;
  private String creativeCommonsIconUrl;
  private Date modified;
  private Long modifierId;
  private String modifierName;
  private Long creatorId;
  private String creatorName;
  private List<String> tags;
  private String editPath;
  private String viewPath;
  private String downloadLink;
  private String path;
  private Boolean viewable;
}