package fi.foyt.fni.view.forge;

import java.util.List;

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
import fi.foyt.fni.persistence.model.materials.Document;
import fi.foyt.fni.persistence.model.materials.Material;

@Join(path = "/forge/public/materials/{path}", to = "/forge/public-material.jsf")
@RequestScoped
@Named
@Stateful
public class ForgePublicMaterialBackingBean extends AbstractForgePublicViewBackingBean {

  @Parameter
  @Matches ("[a-zA-Z0-9_/.\\-:,]{1,}")
  private String path;
  
  @Inject
  private MaterialController materialController;
  
  @Inject
  private MaterialPermissionController materialPermissionController;
  
  @Inject
  private NavigationController navigationController;
  
  @RequestAction
  public String init() {
    String completePath = String.format("/materials/%s", getPath());
    Material material = materialController.findMaterialByCompletePath(completePath);
    if (material == null) {
      return navigationController.notFound();
    }
    
    if (!materialPermissionController.isPublic(null, material)) {
      return navigationController.accessDenied();
    }
    
    if (material instanceof Document) {
      contentType = "text/html";
      html = ((Document) material).getData();
    } else {
      switch (material.getType()) {
        case IMAGE:
        case VECTOR_IMAGE:
          imageUrl = String.format("/materials/%s", material.getPath());
        break;
        default:
          embedUrl = String.format("/materials/%s", material.getPath());
        break;
      }
    }
    
    downloadLink = String.format("/materials/%s?download=true", material.getPath());
    title = material.getTitle();
    allTags = toTagBeans(materialController.listPublicMaterialTagsWithCounts(TAG_COUNT));
    
    return null;
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
  
  public String getContentType() {
    return contentType; 
  }
  
  public String getHtml() {
    return html;
  }

  public String getEmbedUrl() {
    return embedUrl;
  }
  
  public String getImageUrl() {
    return imageUrl;
  }

  public String getDownloadLink() {
    return downloadLink;
  }
  
  public List<PublicTagBean> getAllTags() {
    return allTags;
  }

  private String title;
  private String contentType;
  private String html;
  private String imageUrl;
  private String embedUrl;
  private String downloadLink;
  private List<PublicTagBean> allTags;
}
