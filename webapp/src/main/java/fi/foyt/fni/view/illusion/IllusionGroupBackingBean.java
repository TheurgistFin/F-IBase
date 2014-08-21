package fi.foyt.fni.view.illusion;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.annotation.Parameter;

import fi.foyt.fni.materials.IllusionGroupDocumentController;
import fi.foyt.fni.materials.MaterialController;
import fi.foyt.fni.persistence.model.illusion.IllusionGroup;
import fi.foyt.fni.persistence.model.illusion.IllusionGroupMember;
import fi.foyt.fni.persistence.model.materials.IllusionGroupDocument;
import fi.foyt.fni.persistence.model.materials.IllusionGroupDocumentType;
import fi.foyt.fni.persistence.model.materials.IllusionGroupFolder;
import fi.foyt.fni.persistence.model.users.User;
import fi.foyt.fni.security.LoggedIn;
import fi.foyt.fni.security.SecurityContext;
import fi.foyt.fni.session.SessionController;
import fi.foyt.fni.utils.data.FileData;

@RequestScoped
@Named
@Stateful
@Join (path = "/illusion/group/{urlName}", to = "/illusion/group.jsf")
@LoggedIn
public class IllusionGroupBackingBean extends AbstractIllusionGroupBackingBean {

  @Parameter
  private String urlName;

  @Inject
  private Logger logger;

  @Inject
  private SessionController sessionController;

  @Inject
  private IllusionGroupDocumentController illusionGroupDocumentController;

  @Inject
  private MaterialController materialController;
  
  @Override
  public String init(IllusionGroup illusionGroup, IllusionGroupMember member) {
    if (member == null) {
      return "/illusion/intro.jsf?faces-redirect=true&urlName=" + getUrlName();
    }
    
    switch (member.getRole()) {
      case BANNED:
      case BOT:
        return "/error/access-denied.jsf";
      case PENDING_APPROVAL:
      case WAITING_PAYMENT:
      case INVITED:
        return "/illusion/intro.jsf?faces-redirect=true&urlName=" + getUrlName();
      case GAMEMASTER:
      case PLAYER:
      break;
    }
    
    IllusionGroupFolder folder = illusionGroup.getFolder();
    User loggedUser = sessionController.getLoggedUser();
    
    IllusionGroupDocument indexDocument = illusionGroupDocumentController.findByFolderAndDocumentType(folder, IllusionGroupDocumentType.INDEX);
    if (indexDocument != null) {
      try {
        FileData indexData = materialController.getMaterialData(null, loggedUser, indexDocument);
        if (indexData != null) {
          indexText = new String(indexData.getData(), "UTF-8");
        }
      } catch (IOException | GeneralSecurityException e) {
        logger.log(Level.WARNING, "Could not retreive group index text", e);
      }
    }
    
    return null;
  }
  
  @Override
  public String getUrlName() {
    return urlName;
  }

  public void setUrlName(@SecurityContext String urlName) {
    this.urlName = urlName;
  }
  
  public String getIndexText() {
    return indexText;
  }
  
  private String indexText;
}
