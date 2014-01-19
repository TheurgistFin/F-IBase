package fi.foyt.fni.view.illusion;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;

import fi.foyt.fni.chat.ChatCredentialsController;
import fi.foyt.fni.illusion.IllusionGroupController;
import fi.foyt.fni.persistence.model.illusion.IllusionGroup;
import fi.foyt.fni.persistence.model.system.SystemSettingKey;
import fi.foyt.fni.persistence.model.users.User;
import fi.foyt.fni.security.LoggedIn;
import fi.foyt.fni.session.SessionController;
import fi.foyt.fni.system.SystemSettingsController;

@RequestScoped
@Named
@Stateful
@URLMappings(mappings = { @URLMapping(id = "illusion-space", pattern = "/illusion/group/#{illusionGroupBackingBean.urlName}", viewId = "/illusion/group.jsf") })
public class IllusionGroupBackingBean {

  @Inject
  private SessionController sessionController;

  @Inject
  private SystemSettingsController systemSettingsController;

  @Inject
  private IllusionGroupController illusionGroupController;

  @Inject
  private ChatCredentialsController chatCredentialsController;

  @URLAction
  @LoggedIn
  public void load() throws GeneralSecurityException, IOException {
    IllusionGroup illusionGroup = illusionGroupController.findIllusionGroupByUrlName(getUrlName());
    if (illusionGroup == null) {
      throw new FileNotFoundException();
    }

    User loggedUser = sessionController.getLoggedUser();

    id = illusionGroup.getId();
    name = illusionGroup.getName();
    description = illusionGroup.getDescription();
    urlName = illusionGroup.getUrlName();
    xmppRoom = illusionGroup.getXmppRoom();

    userNickname = loggedUser.getId().toString(); //URLEncoder.encode((StringUtils.isNotBlank(loggedUser.getNickname()) ? loggedUser.getNickname() : loggedUser.getFullName()), "UTF-8");
    xmppUserJid = chatCredentialsController.getUserJidByUser(loggedUser);
    xmppPassword = chatCredentialsController.getPasswordByUser(loggedUser);
    xmppBoshService = systemSettingsController.getSetting(SystemSettingKey.CHAT_BOSH_SERVICE);
  }

  public String getUrlName() {
    return urlName;
  }

  public void setUrlName(String urlName) {
    this.urlName = urlName;
  }

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

  public String getXmppBoshService() {
    return xmppBoshService;
  }

  public void setXmppBoshService(String xmppBoshService) {
    this.xmppBoshService = xmppBoshService;
  }

  public String getXmppUserJid() {
    return xmppUserJid;
  }

  public void setXmppUserJid(String xmppUserJid) {
    this.xmppUserJid = xmppUserJid;
  }

  public String getXmppPassword() {
    return xmppPassword;
  }

  public void setXmppPassword(String xmppPassword) {
    this.xmppPassword = xmppPassword;
  }

  public String getXmppRoom() {
    return xmppRoom;
  }

  public void setXmppRoom(String xmppRoom) {
    this.xmppRoom = xmppRoom;
  }

  public String getUserNickname() {
    return userNickname;
  }

  public void setUserNickname(String userNickname) {
    this.userNickname = userNickname;
  }

  private Long id;
  private String urlName;
  private String name;
  private String description;
  private String xmppBoshService;
  private String xmppUserJid;
  private String xmppPassword;
  private String xmppRoom;
  private String userNickname;
}
