package fi.foyt.fni.illusion;

import java.io.IOException;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import fi.foyt.fni.materials.MaterialController;
import fi.foyt.fni.persistence.dao.illusion.IllusionGroupDAO;
import fi.foyt.fni.persistence.dao.illusion.IllusionEventParticipantDAO;
import fi.foyt.fni.persistence.dao.illusion.IllusionEventParticipantImageDAO;
import fi.foyt.fni.persistence.dao.illusion.IllusionEventParticipantSettingDAO;
import fi.foyt.fni.persistence.dao.illusion.IllusionGroupSettingDAO;
import fi.foyt.fni.persistence.dao.materials.IllusionFolderDAO;
import fi.foyt.fni.persistence.dao.materials.IllusionGroupFolderDAO;
import fi.foyt.fni.persistence.model.illusion.IllusionGroup;
import fi.foyt.fni.persistence.model.illusion.IllusionEventJoinMode;
import fi.foyt.fni.persistence.model.illusion.IllusionEventParticipant;
import fi.foyt.fni.persistence.model.illusion.IllusionEventParticipantImage;
import fi.foyt.fni.persistence.model.illusion.IllusionEventParticipantRole;
import fi.foyt.fni.persistence.model.illusion.IllusionEventParticipantSetting;
import fi.foyt.fni.persistence.model.illusion.IllusionGroupSetting;
import fi.foyt.fni.persistence.model.illusion.IllusionGroupSettingKey;
import fi.foyt.fni.persistence.model.materials.IllusionFolder;
import fi.foyt.fni.persistence.model.materials.IllusionGroupFolder;
import fi.foyt.fni.persistence.model.materials.MaterialPublicity;
import fi.foyt.fni.persistence.model.users.User;

@Dependent
@Stateless
public class IllusionGroupController {
  
  private static final String ILLUSION_FOLDER_TITLE = "Illusion";
  
  @Inject
  private Logger logger;

  @Inject
  private IllusionGroupDAO illusionGroupDAO;

  @Inject
  private IllusionEventParticipantDAO illusionEventParticipantDAO;

  @Inject
  private IllusionEventParticipantImageDAO illusionEventParticipantImageDAO;
  
  @Inject
  private IllusionGroupSettingDAO illusionGroupSettingDAO;

  @Inject
  private IllusionEventParticipantSettingDAO illusionEventParticipantSettingDAO;

  @Inject
  private IllusionFolderDAO illusionFolderDAO;

  @Inject
  private IllusionGroupFolderDAO illusionGroupFolderDAO;
  
  @Inject
  private MaterialController materialController;

  @Inject
  private Event<MemberAddedEvent> memberAddedEvent;

  @Inject
  private Event<MemberRoleChangeEvent> roleChangeEvent;
  
  /* IllusionGroup */

  public IllusionGroup createIllusionGroup(String urlName, String name, String description, String xmppRoom, IllusionGroupFolder folder, IllusionEventJoinMode joinMode, Date created, Double signUpFee, Currency signUpFeeCurrency) {
    return illusionGroupDAO.create(urlName, name, description, xmppRoom, folder, joinMode, created, signUpFee, signUpFeeCurrency);
  }

  public IllusionGroup findIllusionGroupById(Long id) {
    return illusionGroupDAO.findById(id);
  }

  public IllusionGroup findIllusionGroupByUrlName(String urlName) {
    return illusionGroupDAO.findByUrlName(urlName);
  }

  public List<IllusionGroup> listIllusionGroupsByUserAndRole(User user, IllusionEventParticipantRole role) {
    return illusionEventParticipantDAO.listIllusionGroupsByUserAndRole(user, role);
  }

  /* IllusionEventParticipant */
  
  public IllusionEventParticipant createIllusionGroupMember(User user, IllusionGroup group, String characterName, IllusionEventParticipantRole role) {
    IllusionEventParticipant member = illusionEventParticipantDAO.create(user, group, characterName, role);
    memberAddedEvent.fire(new MemberAddedEvent(member.getId()));
    
    return member;
  }

  public IllusionEventParticipant findIllusionGroupMemberById(Long memberId) {
    return illusionEventParticipantDAO.findById(memberId);
  }
  
  public IllusionEventParticipant findIllusionGroupMemberByUserAndGroup(IllusionGroup group, User user) {
    return illusionEventParticipantDAO.findByGroupAndUser(group, user);
  }
  
  public List<IllusionEventParticipant> listIllusionGroupMembersByGroup(IllusionGroup group) {
    return illusionEventParticipantDAO.listByGroup(group);
  }
  
  public List<IllusionEventParticipant> listIllusionGroupMembersByGroupAndRole(IllusionGroup group, IllusionEventParticipantRole role) {
    return illusionEventParticipantDAO.listByGroupAndRole(group, role);
  }

  public Long countIllusionGroupMembersByGroupAndRole(IllusionGroup group, IllusionEventParticipantRole role) {
    return illusionEventParticipantDAO.countByGroupAndRole(group, role);
  }
  
  public IllusionEventParticipant updateIllusionGroupMemberCharacterName(IllusionEventParticipant member, String characterName) {
    return illusionEventParticipantDAO.updateCharacterName(member, characterName);
  }

  public IllusionEventParticipant updateIllusionGroupMemberRole(IllusionEventParticipant member, IllusionEventParticipantRole role) {
    IllusionEventParticipantRole oldRole = member.getRole();
    IllusionEventParticipant groupMember = illusionEventParticipantDAO.updateRole(member, role);
    if (oldRole != role) {
      roleChangeEvent.fire(new MemberRoleChangeEvent(member.getId(), oldRole, role));
    }
    
    return groupMember;
  }

  /* IllusionEventParticipantImage */

  public IllusionEventParticipantImage createIllusionGroupMemberImage(IllusionEventParticipant member, String contentType, byte[] data, Date modified) {
    return illusionEventParticipantImageDAO.create(member, contentType, data, modified);
  }

  public IllusionEventParticipantImage findIllusionGroupMemberImageByMember(IllusionEventParticipant member) {
    return illusionEventParticipantImageDAO.findByMember(member);
  }
  
  public IllusionEventParticipantImage updateIllusionGroupMemberImage(IllusionEventParticipantImage image, String contentType, byte[] data, Date modified) {
    return illusionEventParticipantImageDAO.updateModified(illusionEventParticipantImageDAO.updateContentType(illusionEventParticipantImageDAO.updateData(image, data), contentType), modified);
  }
  
  /* Settings */
  
  public String getIllusionGroupSettingValue(IllusionEventParticipant member, IllusionGroupSettingKey key) {
    IllusionEventParticipantSetting userSetting = illusionEventParticipantSettingDAO.findByMemberAndKey(member, key);
    if ((userSetting != null) && StringUtils.isNotBlank(userSetting.getValue())) {
      return userSetting.getValue();
    }
    
    IllusionGroupSetting groupSetting = illusionGroupSettingDAO.findByUserAndKey(member.getGroup(), key);
    if (groupSetting != null) {
      return groupSetting.getValue();
    }
    
    return null;
  }
  
  public Object getIllusionGroupUserSetting(IllusionEventParticipant participant, IllusionGroupSettingKey key) {
    switch (key) {
      case DICE:
        return getIllusionGroupDiceSetting(participant);
    }
    
    return null;
  }
  
  private <T> T getIllusionGroupSetting(IllusionEventParticipant user, IllusionGroupSettingKey key, Class<T> clazz) {
    String value = getIllusionGroupSettingValue(user, key);
    if (StringUtils.isNotBlank(value)) {
      ObjectMapper objectMapper = new ObjectMapper();
      try {
        return objectMapper.readValue(value, clazz);
      } catch (IOException e) {
        logger.log(Level.SEVERE, "Could not parse IllusionGroupSetting " + key + " from user: " + user.getId());
      }
    }
    
    return null;
  }
  
  public List<String> getIllusionGroupDiceSetting(IllusionEventParticipant user) {
    @SuppressWarnings("unchecked")
    List<String> result = getIllusionGroupSetting(user, IllusionGroupSettingKey.DICE, List.class);
    if (result == null) {
      result = Collections.emptyList();
    }
    
    return result;
  }

  public IllusionEventParticipantSetting setIllusionGroupSettingValue(IllusionEventParticipant member, IllusionGroupSettingKey key, String value) {
    IllusionEventParticipantSetting setting = illusionEventParticipantSettingDAO.findByMemberAndKey(member, key);
    if (setting == null) {
      return illusionEventParticipantSettingDAO.create(member, key, value);
    } else {
      return illusionEventParticipantSettingDAO.updateValue(setting, value);
    }
  }

  public Map<IllusionGroupSettingKey, Object> getIllusionGroupUserSettings(IllusionEventParticipant participant) {
    Map<IllusionGroupSettingKey, Object> result = new HashMap<>();
    
    for (IllusionGroupSettingKey key : IllusionGroupSettingKey.values()) {
      switch (key) {
        case DICE:
          result.put(key, getIllusionGroupUserSetting(participant, key));
        break;
      }
    }
    
    return result;
  }

  /* IllusionFolder */
  
  public IllusionFolder findUserIllusionFolder(User user, boolean createMissing) {
    IllusionFolder illusionFolder = illusionFolderDAO.findByCreator(user);
    if (illusionFolder == null && createMissing) {
      String illusionUrlName = materialController.getUniqueMaterialUrlName(user, null, null,  ILLUSION_FOLDER_TITLE);
      illusionFolder = illusionFolderDAO.create(user, illusionUrlName, ILLUSION_FOLDER_TITLE, MaterialPublicity.PRIVATE);
    }
    
    return illusionFolder;
  }
  
  /* IllusionGroupFolder */
  
  public IllusionGroupFolder createIllusionGroupFolder(User creator, IllusionFolder illusionFolder, String urlName, String title) {
    return illusionGroupFolderDAO.create(creator, illusionFolder, urlName, title, MaterialPublicity.PRIVATE);
  }

}
