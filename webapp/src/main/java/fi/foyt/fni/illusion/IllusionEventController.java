package fi.foyt.fni.illusion;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import fi.foyt.fni.materials.MaterialController;
import fi.foyt.fni.persistence.dao.illusion.GenreDAO;
import fi.foyt.fni.persistence.dao.illusion.IllusionEventDAO;
import fi.foyt.fni.persistence.dao.illusion.IllusionEventGenreDAO;
import fi.foyt.fni.persistence.dao.illusion.IllusionEventParticipantDAO;
import fi.foyt.fni.persistence.dao.illusion.IllusionEventParticipantImageDAO;
import fi.foyt.fni.persistence.dao.illusion.IllusionEventTypeDAO;
import fi.foyt.fni.persistence.dao.materials.IllusionEventFolderDAO;
import fi.foyt.fni.persistence.dao.materials.IllusionFolderDAO;
import fi.foyt.fni.persistence.model.illusion.Genre;
import fi.foyt.fni.persistence.model.illusion.IllusionEvent;
import fi.foyt.fni.persistence.model.illusion.IllusionEventGenre;
import fi.foyt.fni.persistence.model.illusion.IllusionEventJoinMode;
import fi.foyt.fni.persistence.model.illusion.IllusionEventParticipant;
import fi.foyt.fni.persistence.model.illusion.IllusionEventParticipantImage;
import fi.foyt.fni.persistence.model.illusion.IllusionEventParticipantRole;
import fi.foyt.fni.persistence.model.illusion.IllusionEventType;
import fi.foyt.fni.persistence.model.materials.IllusionEventFolder;
import fi.foyt.fni.persistence.model.materials.IllusionFolder;
import fi.foyt.fni.persistence.model.materials.MaterialPublicity;
import fi.foyt.fni.persistence.model.oauth.OAuthClient;
import fi.foyt.fni.persistence.model.users.User;

@Dependent
@Stateless
public class IllusionEventController {
  
  private static final String ILLUSION_FOLDER_TITLE = "Illusion";
  
  @Inject
  private IllusionEventDAO illusionEventDAO;

  @Inject
  private IllusionEventGenreDAO illusionEventGenreDAO;

  @Inject
  private IllusionEventParticipantDAO illusionEventParticipantDAO;

  @Inject
  private IllusionEventParticipantImageDAO illusionEventParticipantImageDAO;
  
  @Inject
  private IllusionFolderDAO illusionFolderDAO;

  @Inject
  private IllusionEventFolderDAO illusionEventFolderDAO;

  @Inject
  private IllusionEventTypeDAO illusionEventTypeDAO;
  
  @Inject
  private GenreDAO genreDAO;
  
  @Inject
  private MaterialController materialController;
  
  @Inject
  private Event<IllusionParticipantAddedEvent> illusionParticipantAddedEvent;

  @Inject
  private Event<IllusionParticipantRoleChangeEvent> roleChangeEvent;
  
  /* IllusionEvent */

  public IllusionEvent createIllusionEvent(String urlName, String location, String name, String description, String xmppRoom, IllusionEventFolder folder, IllusionEventJoinMode joinMode, Date created, Double signUpFee, Currency signUpFeeCurrency, Date startDate, Date startTime, Date endDate, Date endTime, Integer ageLimit, Boolean beginnerFriendly, String imageUrl, IllusionEventType type, Date signUpStartDate, Date signUpEndDate) {
    return illusionEventDAO.create(urlName, name, location, description, xmppRoom, folder, joinMode, created, signUpFee, signUpFeeCurrency, startDate, startTime, endDate, endTime, null, ageLimit, beginnerFriendly, imageUrl, type, signUpStartDate, signUpEndDate);
  }

  public IllusionEvent findIllusionEventById(Long id) {
    return illusionEventDAO.findById(id);
  }

  public IllusionEvent findIllusionEventByUrlName(String urlName) {
    return illusionEventDAO.findByUrlName(urlName);
  }

  public List<IllusionEvent> listIllusionEventsByUserAndRole(User user, IllusionEventParticipantRole role) {
    return illusionEventParticipantDAO.listIllusionEventsByUserAndRole(user, role);
  }

  public List<IllusionEvent> listNextIllusionEvents(int maxResults) {
    Date now = new Date();
    return illusionEventDAO.listByStartDateGEOrEndDateGESortByStartDateAndStartTime(now, now, 0, maxResults);
  }

  public List<IllusionEvent> listPastIllusionEvents(int maxResults) {
    Date now = new Date();
    return illusionEventDAO.listByStartDateLTAndEndDateLTSortByEndDateEndTimeStartDateStartTime(now, now, 0, maxResults);
  }

  public List<IllusionEvent> listIllusionEventsWithDomain() {
    return illusionEventDAO.listByDomainNotNull();
  }

  public IllusionEvent updateIllusionEventName(IllusionEvent illusionEvent, String name) {
    return illusionEventDAO.updateName(illusionEvent, name);
  }

  public IllusionEvent updateIllusionEventUrlName(IllusionEvent illusionEvent, String urlName) {
    return illusionEventDAO.updateUrlName(illusionEvent, urlName);
  }

  public IllusionEvent updateIllusionEventDescription(IllusionEvent illusionEvent, String description) {
    return illusionEventDAO.updateDescription(illusionEvent, description);
  }

  public IllusionEvent updateIllusionEventJoinMode(IllusionEvent illusionEvent, IllusionEventJoinMode joinMode) {
    return illusionEventDAO.updateJoinMode(illusionEvent, joinMode);
  }

  public IllusionEvent updateIllusionEventStartDate(IllusionEvent illusionEvent, Date startDate) {
    return illusionEventDAO.updateStartDate(illusionEvent, startDate);
  }

  public IllusionEvent updateIllusionEventStartTime(IllusionEvent illusionEvent, Date startTime) {
    return illusionEventDAO.updateStartTime(illusionEvent, startTime);
  }

  public IllusionEvent updateIllusionEventEndDate(IllusionEvent illusionEvent, Date endDate) {
    return illusionEventDAO.updateEndDate(illusionEvent, endDate);
  }

  public IllusionEvent updateIllusionEventEndTime(IllusionEvent illusionEvent, Date endTime) {
    return illusionEventDAO.updateEndTime(illusionEvent, endTime);
  }

  public IllusionEvent updateIllusionEventLocation(IllusionEvent illusionEvent, String location) {
    return illusionEventDAO.updateLocation(illusionEvent, location);
  }
  
  public IllusionEvent updateEventOAuthClient(IllusionEvent illusionEvent, OAuthClient oAuthClient) {
    return illusionEventDAO.updateOAuthClient(illusionEvent, oAuthClient);
  }

  public IllusionEvent updateEventDomain(IllusionEvent illusionEvent, String domain) {
    return illusionEventDAO.updateDomain(illusionEvent, domain);
  }

  public IllusionEvent updateIllusionEventType(IllusionEvent illusionEvent, IllusionEventType type) {
    return illusionEventDAO.updateType(illusionEvent, type);
  }

  public IllusionEvent updateIllusionEventSignUpTimes(IllusionEvent illusionEvent, Date signUpStartDate, Date signUpEndDate) {
    return illusionEventDAO.updateSignUpEndDate(illusionEventDAO.updateSignUpStartDate(illusionEvent, signUpStartDate), signUpEndDate);
  }
  
  public IllusionEvent updateIllusionEventAgeLimit(IllusionEvent illusionEvent, Integer ageLimit) {
    return illusionEventDAO.updateAgeLimit(illusionEvent, ageLimit);
  }
  
  public IllusionEvent updateIllusionEventBeginnerFriendly(IllusionEvent illusionEvent, Boolean beginnerFriendly) {
    return illusionEventDAO.updateBeginnerFriendly(illusionEvent, beginnerFriendly);
  }
  
  public IllusionEvent updateIllusionEventImageUrl(IllusionEvent illusionEvent, String imageUrl) {
    return illusionEventDAO.updateImageUrl(illusionEvent, imageUrl);
  }
  
  
  public IllusionEvent updateEventGenres(IllusionEvent event, List<Genre> genres) {
    List<IllusionEventGenre> existingGenres = illusionEventGenreDAO.listByEvent(event);
    List<Genre> addGenres = new ArrayList<>(genres);
    
    Map<Long, IllusionEventGenre> existingGenreMap = new HashMap<>();
    for (IllusionEventGenre existingGender : existingGenres) {
      existingGenreMap.put(existingGender.getGenre().getId(), existingGender);
    }
    
    for (int i = addGenres.size() - 1; i >= 0; i--) {
      Genre addGenre = addGenres.get(i);
      
      if (existingGenreMap.containsKey(addGenre.getId())) {
        addGenres.remove(i);
      } 
      
      existingGenreMap.remove(addGenre.getId());
    }
    
    for (IllusionEventGenre removeGenre : existingGenreMap.values()) {
      illusionEventGenreDAO.delete(removeGenre);
    }
    
    for (Genre genre : addGenres) {
      illusionEventGenreDAO.create(event, genre);
    }
    
    return event;
  }
  
  /* IllusionEventParticipant */
  
  public IllusionEventParticipant createIllusionEventParticipant(User user, IllusionEvent event, String characterName, IllusionEventParticipantRole role) {
    IllusionEventParticipant member = illusionEventParticipantDAO.create(user, event, characterName, role);
    illusionParticipantAddedEvent.fire(new IllusionParticipantAddedEvent(member.getId()));
    
    return member;
  }

  public IllusionEventParticipant findIllusionEventParticipantById(Long id) {
    return illusionEventParticipantDAO.findById(id);
  }
  
  public IllusionEventParticipant findIllusionEventParticipantByEventAndUser(IllusionEvent event, User user) {
    return illusionEventParticipantDAO.findByEventAndUser(event, user);
  }
  
  public List<IllusionEventParticipant> listIllusionEventParticipantsByEvent(IllusionEvent event) {
    return illusionEventParticipantDAO.listByEvent(event);
  }
  
  public List<IllusionEventParticipant> listIllusionEventParticipantsByEventAndRole(IllusionEvent event, IllusionEventParticipantRole role) {
    return illusionEventParticipantDAO.listByEventAndRole(event, role);
  }

  public Long countIllusionEventParticipantsByEventAndRole(IllusionEvent event, IllusionEventParticipantRole role) {
    return illusionEventParticipantDAO.countByEventAndRole(event, role);
  }
  
  public IllusionEventParticipant updateIllusionEventParticipantCharacterName(IllusionEventParticipant participant, String characterName) {
    return illusionEventParticipantDAO.updateCharacterName(participant, characterName);
  }

  public IllusionEventParticipant updateIllusionEventParticipantRole(IllusionEventParticipant participant, IllusionEventParticipantRole role) {
    IllusionEventParticipantRole oldRole = participant.getRole();
    illusionEventParticipantDAO.updateRole(participant, role);
    
    if (oldRole != role) {
      roleChangeEvent.fire(new IllusionParticipantRoleChangeEvent(participant.getId(), oldRole, role));
    }
    
    return participant;
  }

  /* IllusionEventParticipantImage */

  public IllusionEventParticipantImage createIllusionEeventParticipantImage(IllusionEventParticipant participant, String contentType, byte[] data, Date modified) {
    return illusionEventParticipantImageDAO.create(participant, contentType, data, modified);
  }

  public IllusionEventParticipantImage findIllusionEventParticipantImageByParticipant(IllusionEventParticipant participant) {
    return illusionEventParticipantImageDAO.findByParticipant(participant);
  }
  
  public IllusionEventParticipantImage updateIllusionEventParticipantImage(IllusionEventParticipantImage image, String contentType, byte[] data, Date modified) {
    return illusionEventParticipantImageDAO.updateModified(illusionEventParticipantImageDAO.updateContentType(illusionEventParticipantImageDAO.updateData(image, data), contentType), modified);
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
  
  /* IllusionEventFolder */
  
  public IllusionEventFolder createIllusionEventFolder(User creator, IllusionFolder illusionFolder, String urlName, String title) {
    return illusionEventFolderDAO.create(creator, illusionFolder, urlName, title, MaterialPublicity.PRIVATE);
  }

  public IllusionEvent findIllusionEventByFolder(IllusionEventFolder folder) {
    return illusionEventDAO.findByFolder(folder);
  }

  public IllusionEvent findIllusionEventByDomain(String domain) {
    return illusionEventDAO.findByDomain(domain);
  }

  public IllusionEvent findIllusionEventByOAuthClient(OAuthClient oAuthClient) {
    return illusionEventDAO.findByOAuthClient(oAuthClient);
  }
  
  /* Types */
  
  public IllusionEventType findTypeById(Long id) {
    return illusionEventTypeDAO.findById(id);
  }

  public List<IllusionEventType> listTypes() {
    return illusionEventTypeDAO.listAll();
  }
  
  /* Genres */

  public Genre findGenreById(Long genreId) {
    return genreDAO.findById(genreId);
  }
  
  public List<Genre> listGenres() {
    return genreDAO.listAll();
  }

  public List<IllusionEventGenre> listIllusionEventGenres(IllusionEvent illusionEvent) {
    return illusionEventGenreDAO.listByEvent(illusionEvent);
  }

}
