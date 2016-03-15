package fi.foyt.fni.rest.material.model;

import java.util.Date;
import java.util.List;

import fi.foyt.fni.persistence.model.materials.MaterialPublicity;
import fi.foyt.fni.persistence.model.materials.MaterialType;

public class Material {
  
  public Material() {
  }
  
  public Material(Long id, MaterialType type, String urlName, String path, String title, String description, MaterialPublicity publicity, Long languageId, Date modified, Date created,
      Long creatorId, Long modifierId, Long parentFolderId, String license, List<String> tags) {
    super();
    this.id = id;
    this.type = type;
    this.path = path;
    this.urlName = urlName;
    this.title = title;
    this.description = description;
    this.publicity = publicity;
    this.languageId = languageId;
    this.modified = modified;
    this.created = created;
    this.creatorId = creatorId;
    this.modifierId = modifierId;
    this.parentFolderId = parentFolderId;
    this.license = license;
    this.tags = tags;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public MaterialType getType() {
    return type;
  }

  public void setType(MaterialType type) {
    this.type = type;
  }

  public String getUrlName() {
    return urlName;
  }

  public void setUrlName(String urlName) {
    this.urlName = urlName;
  }
  
  public String getPath() {
    return path;
  }
  
  public void setPath(String path) {
    this.path = path;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
  
  public String getDescription() {
    return description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }

  public MaterialPublicity getPublicity() {
    return publicity;
  }

  public void setPublicity(MaterialPublicity publicity) {
    this.publicity = publicity;
  }

  public Long getLanguageId() {
    return languageId;
  }

  public void setLanguageId(Long languageId) {
    this.languageId = languageId;
  }

  public Date getModified() {
    return modified;
  }

  public void setModified(Date modified) {
    this.modified = modified;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public Long getCreatorId() {
    return creatorId;
  }

  public void setCreatorId(Long creatorId) {
    this.creatorId = creatorId;
  }

  public Long getModifierId() {
    return modifierId;
  }

  public void setModifierId(Long modifierId) {
    this.modifierId = modifierId;
  }

  public Long getParentFolderId() {
    return parentFolderId;
  }

  public void setParentFolderId(Long parentFolderId) {
    this.parentFolderId = parentFolderId;
  }
  
  public String getLicense() {
    return license;
  }
  
  public void setLicense(String license) {
    this.license = license;
  }
  
  public List<String> getTags() {
    return tags;
  }
  
  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  private Long id;
  private MaterialType type;
  private String urlName;
  private String path;
  private String title;
  private String description;
  private MaterialPublicity publicity;
  private Long languageId;
  private Date modified;
  private Date created;
  private Long creatorId;
  private Long modifierId;
  private Long parentFolderId;
  private String license;
  private List<String> tags; 
}
