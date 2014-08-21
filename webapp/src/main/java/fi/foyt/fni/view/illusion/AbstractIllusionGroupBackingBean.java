package fi.foyt.fni.view.illusion;

import javax.inject.Inject;

import org.ocpsoft.rewrite.annotation.RequestAction;

import fi.foyt.fni.illusion.IllusionGroupController;
import fi.foyt.fni.persistence.model.illusion.IllusionGroup;
import fi.foyt.fni.persistence.model.illusion.IllusionEventParticipant;
import fi.foyt.fni.persistence.model.illusion.IllusionEventParticipantRole;
import fi.foyt.fni.persistence.model.materials.IllusionGroupFolder;
import fi.foyt.fni.persistence.model.users.User;
import fi.foyt.fni.session.SessionController;

public abstract class AbstractIllusionGroupBackingBean {

  @Inject
  private IllusionGroupController illusionGroupController;

  @Inject
  private SessionController sessionController;
  
  @RequestAction
  public String basicInit() {
    IllusionGroup illusionGroup = illusionGroupController.findIllusionGroupByUrlName(getUrlName());
    if (illusionGroup == null) {
      return "/error/not-found.jsf";
    }
    
    IllusionEventParticipant member = null;
    
    if (sessionController.isLoggedIn()) {
      User loggedUser = sessionController.getLoggedUser();
  
      member = illusionGroupController.findIllusionGroupMemberByUserAndGroup(illusionGroup, loggedUser);
    }
    
    IllusionGroupFolder folder = illusionGroup.getFolder();
    
    id = illusionGroup.getId();
    name = illusionGroup.getName();
    description = illusionGroup.getDescription();
    illusionFolderPath = folder.getPath();
    mayManageGroup = member != null ? member.getRole() == IllusionEventParticipantRole.GAMEMASTER : false;
  
    return init(illusionGroup, member);
  }

  public abstract String init(IllusionGroup illusionGroup, IllusionEventParticipant participant);
  public abstract String getUrlName();
  
  public Long getId() {
    return id;
  }
  
  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
  
  public String getIllusionFolderPath() {
    return illusionFolderPath;
  }
  
  public boolean getMayManageGroup() {
    return mayManageGroup;
  }
  
  private Long id;
  private String name;
  private String description;
  private String illusionFolderPath;
  private boolean mayManageGroup;
}
