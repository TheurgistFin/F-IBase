package fi.foyt.fni.view.illusion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.annotation.Parameter;

import fi.foyt.fni.auth.OAuthController;
import fi.foyt.fni.illusion.IllusionEventController;
import fi.foyt.fni.illusion.IllusionEventPage;
import fi.foyt.fni.persistence.model.illusion.Genre;
import fi.foyt.fni.persistence.model.illusion.IllusionEvent;
import fi.foyt.fni.persistence.model.illusion.IllusionEventGenre;
import fi.foyt.fni.persistence.model.illusion.IllusionEventJoinMode;
import fi.foyt.fni.persistence.model.illusion.IllusionEventParticipant;
import fi.foyt.fni.persistence.model.illusion.IllusionEventParticipantRole;
import fi.foyt.fni.persistence.model.illusion.IllusionEventType;
import fi.foyt.fni.persistence.model.oauth.OAuthClient;
import fi.foyt.fni.persistence.model.users.Permission;
import fi.foyt.fni.security.LoggedIn;
import fi.foyt.fni.security.Secure;
import fi.foyt.fni.security.SecurityContext;
import fi.foyt.fni.system.SystemSettingsController;
import fi.foyt.fni.utils.faces.FacesUtils;
import fi.foyt.fni.utils.servlet.RequestUtils;

@RequestScoped
@Named
@Stateful
@Join(path = "/illusion/event/{urlName}/settings", to = "/illusion/event-settings.jsf")
@LoggedIn
@Secure(value = Permission.ILLUSION_EVENT_MANAGE)
@SecurityContext(context = "@urlName")
public class IllusionEventSettingsBackingBean extends AbstractIllusionEventBackingBean {

  @Parameter
  private String urlName;

  @Inject
  private IllusionEventController illusionEventController;

  @Inject
  private OAuthController oAuthController;

  @Inject
  private IllusionEventNavigationController illusionEventNavigationController;

  @Inject
  private SystemSettingsController systemSettingsController;

  @Override
  public String init(IllusionEvent illusionEvent, IllusionEventParticipant participant) {
    if ((participant == null) || (participant.getRole() != IllusionEventParticipantRole.ORGANIZER)) {
      return "/error/access-denied.jsf";
    }

    illusionEventNavigationController.setSelectedPage(IllusionEventPage.Static.SETTINGS);
    illusionEventNavigationController.setEventUrlName(getUrlName());

    name = illusionEvent.getName();
    description = illusionEvent.getDescription();
    location = illusionEvent.getLocation();
    joinMode = illusionEvent.getJoinMode();
    startDate = formatDate(illusionEvent.getStartDate());
    startTime = formatTime(illusionEvent.getStartTime());
    endDate = formatDate(illusionEvent.getEndDate());
    endTime = formatTime(illusionEvent.getEndTime());
    domain = illusionEvent.getDomain();
    ageLimit = illusionEvent.getAgeLimit();
    imageUrl = illusionEvent.getImageUrl();
    beginnerFriendly = illusionEvent.getBeginnerFriendly();
    signUpStartDate = formatTime(illusionEvent.getSignUpStartDate());
    signUpEndDate = formatTime(illusionEvent.getSignUpEndDate());
    typeId = illusionEvent.getType() != null ? illusionEvent.getType().getId() : null;
    genreIds = new ArrayList<Long>();
    
    List<IllusionEventGenre> eventGenres = illusionEventController.listIllusionEventGenres(illusionEvent);
    for (IllusionEventGenre eventGenre : eventGenres) {
      genreIds.add(eventGenre.getGenre().getId());
    }
    
    List<IllusionEventType> eventTypes = illusionEventController.listTypes();
    typeSelectItems = new ArrayList<>(eventTypes.size());
    for (IllusionEventType eventType : eventTypes) {
      typeSelectItems.add(new SelectItem(eventType.getId(), eventType.getName()));
    }

    genres = illusionEventController.listGenres();
    
    return null;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public IllusionEventJoinMode getJoinMode() {
    return joinMode;
  }

  public void setJoinMode(IllusionEventJoinMode joinMode) {
    this.joinMode = joinMode;
  }

  @Override
  public String getUrlName() {
    return urlName;
  }

  public void setUrlName(@SecurityContext String urlName) {
    this.urlName = urlName;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public Integer getAgeLimit() {
    return ageLimit;
  }

  public void setAgeLimit(Integer ageLimit) {
    this.ageLimit = ageLimit;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public Boolean getBeginnerFriendly() {
    return beginnerFriendly;
  }

  public void setBeginnerFriendly(Boolean beginnerFriendly) {
    this.beginnerFriendly = beginnerFriendly;
  }
  
  public String getSignUpStartDate() {
    return signUpStartDate;
  }
  
  public void setSignUpStartDate(String signUpStartDate) {
    this.signUpStartDate = signUpStartDate;
  }
  
  public String getSignUpEndDate() {
    return signUpEndDate;
  }
  
  public void setSignUpEndDate(String signUpEndDate) {
    this.signUpEndDate = signUpEndDate;
  }

  public Long getTypeId() {
    return typeId;
  }

  public void setTypeId(Long typeId) {
    this.typeId = typeId;
  }

  public List<Long> getGenreIds() {
    return genreIds;
  }

  public void setGenreIds(List<Long> genreIds) {
    this.genreIds = genreIds;
  }

  public List<Genre> getGenres() {
    return genres;
  }

  public List<SelectItem> getTypeSelectItems() {
    return typeSelectItems;
  }

  private String createUrlName(String name) {
    int maxLength = 20;
    int padding = 0;
    do {
      String urlName = RequestUtils.createUrlName(name, maxLength);
      if (padding > 0) {
        urlName = urlName.concat(StringUtils.repeat('_', padding));
      }

      IllusionEvent illusionEvent = illusionEventController.findIllusionEventByUrlName(urlName);
      if (illusionEvent == null) {
        return urlName;
      }

      if (maxLength < name.length()) {
        maxLength++;
      } else {
        padding++;
      }
    } while (true);
  }

  public String save() throws Exception {
    IllusionEvent illusionEvent = illusionEventController.findIllusionEventByUrlName(getUrlName());
    if (!illusionEvent.getName().equals(getName())) {
      String urlName = createUrlName(getName());
      illusionEventController.updateIllusionEventName(illusionEvent, getName());
      illusionEventController.updateIllusionEventUrlName(illusionEvent, urlName);
    }

    illusionEventController.updateIllusionEventDescription(illusionEvent, getDescription());
    illusionEventController.updateIllusionEventJoinMode(illusionEvent, getJoinMode());
    illusionEventController.updateIllusionEventStartDate(illusionEvent, parseDate(getStartDate()));
    illusionEventController.updateIllusionEventStartTime(illusionEvent, parseDate(getStartTime()));
    illusionEventController.updateIllusionEventEndDate(illusionEvent, parseDate(getEndDate()));
    illusionEventController.updateIllusionEventEndTime(illusionEvent, parseDate(getEndTime()));
    illusionEventController.updateIllusionEventLocation(illusionEvent, getLocation());
    illusionEventController.updateIllusionEventType(illusionEvent, illusionEventController.findTypeById(getTypeId()));
    illusionEventController.updateIllusionEventSignUpTimes(illusionEvent, parseDate(getSignUpStartDate()), parseDate(getSignUpEndDate()));
    illusionEventController.updateIllusionEventAgeLimit(illusionEvent, getAgeLimit());
    illusionEventController.updateIllusionEventBeginnerFriendly(illusionEvent, getBeginnerFriendly());
    illusionEventController.updateIllusionEventImageUrl(illusionEvent, getImageUrl());
    
    String domain = getDomain();
    if (StringUtils.isNotBlank(domain)) {
      // RegEx from
      // http://stackoverflow.com/questions/10306690/domain-name-validation-with-regex
      if (!domain
          .matches("^(([a-zA-Z]{1})|([a-zA-Z]{1}[a-zA-Z]{1})|([a-zA-Z]{1}[0-9]{1})|([0-9]{1}[a-zA-Z]{1})|([a-zA-Z0-9][a-zA-Z0-9-_]{1,61}[a-zA-Z0-9]))\\.([a-zA-Z]{2,6}|[a-zA-Z0-9-]{2,30}\\.[a-zA-Z]{2,3})$")) {
        FacesUtils.addMessage(javax.faces.application.FacesMessage.SEVERITY_WARN, FacesUtils.getLocalizedValue("illusion.eventSettings.customDomainInvalid"));
        return null;
      } else {
        if (illusionEvent.getOAuthClient() == null) {
          String clientId = UUID.randomUUID().toString();
          String clientSecret = UUID.randomUUID().toString();

          String redirectUrl = new StringBuilder(systemSettingsController.getHostUrl(domain, false, true)).append(
              "/login/?return=1&loginMethod=ILLUSION_INTERNAL").toString();

          OAuthClient oAuthClient = oAuthController.createClient(illusionEvent.getName(), clientId, clientSecret, redirectUrl);
          illusionEventController.updateEventOAuthClient(illusionEvent, oAuthClient);
        }

        illusionEventController.updateEventDomain(illusionEvent, domain);
      }
    }
    
    List<Genre> genres = new ArrayList<>();
    
    for (Long genreId : genreIds) {
      Genre genre = illusionEventController.findGenreById(genreId);
      genres.add(genre);
    }
    
    illusionEventController.updateEventGenres(illusionEvent, genres);

    return "/illusion/event-settings.jsf?faces-redirect=true&urlName=" + illusionEvent.getUrlName();
  }

  private String formatDate(Date time) {
    if (time == null) {
      return null;
    }

    DateTimeFormatter formatter = ISODateTimeFormat.date();
    return formatter.print(time.getTime());
  }

  private String formatTime(Date time) {
    if (time == null) {
      return null;
    }

    DateTimeFormatter formatter = ISODateTimeFormat.dateTimeNoMillis();
    return formatter.print(time.getTime());
  }

  private Date parseDate(String iso) {
    if (StringUtils.isBlank(iso)) {
      return null;
    }

    DateTimeFormatter parser = ISODateTimeFormat.dateTimeParser();
    return parser.parseDateTime(iso).toDate();
  }

  private String name;
  private String description;
  private String location;
  private IllusionEventJoinMode joinMode;
  private String startDate;
  private String startTime;
  private String endDate;
  private String endTime;
  private String domain;
  private Integer ageLimit;
  private String imageUrl;
  private Boolean beginnerFriendly;
  private String signUpStartDate;
  private String signUpEndDate;
  private List<Genre> genres;
  private Long typeId;
  private List<Long> genreIds;
  private List<SelectItem> typeSelectItems;
}
