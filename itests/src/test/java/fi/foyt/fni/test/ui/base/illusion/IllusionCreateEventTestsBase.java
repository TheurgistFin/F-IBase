package fi.foyt.fni.test.ui.base.illusion;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import fi.foyt.fni.larpkalenteri.Event;
import fi.foyt.fni.test.DefineSqlSet;
import fi.foyt.fni.test.DefineSqlSets;
import fi.foyt.fni.test.SqlSets;
import fi.foyt.fni.test.ui.base.AbstractIllusionUITest;

@DefineSqlSets({
  @DefineSqlSet (id = "illusion-basic", before = { "basic-users-setup.sql","illusion-basic-setup.sql"}, after = {"illusion-basic-teardown.sql","basic-users-teardown.sql"}),
})
public class IllusionCreateEventTestsBase extends AbstractIllusionUITest {

  @Rule
  public WireMockRule wireMockRule = new WireMockRule(9080);
  
  @Test 
  public void testLoginRedirect() throws UnsupportedEncodingException {
    testLoginRequired("/illusion/createevent");
  }

  @Test
  @SqlSets ("illusion-basic")
  public void testTitleAdmin() {
    loginInternal("admin@foyt.fi", "pass");
    testTitle("/illusion/createevent", "Create Event");
  }

  @Test
  @SqlSets ("illusion-basic")
  public void testTitleUser() {
    loginInternal("user@foyt.fi", "pass");
    testTitle("/illusion/createevent", "Create Event");
  }
  
  @Test
  @SqlSets ("illusion-basic")
  public void testNameRequired() {    
    loginInternal("admin@foyt.fi", "pass");
    navigate("/illusion/createevent");
    typeSelectorInputValue("input[data-alt-field='.actual-start-date']", "10/20/2030");
    scrollIntoView(".illusion-create-event-save");
    clickSelector(".illusion-create-event-save");
    assertSelectorPresent(".illusion-create-event-name:invalid");
  }
  
  @Test
  @SqlSets ("illusion-basic")
  public void testCreateEvent() throws Exception {
    loginInternal("admin@foyt.fi", "pass");

    String name = "name";
    String urlName = "name";
    String description = "description";

    navigate("/illusion/createevent");
    
    findElementBySelector(".illusion-create-event-name").sendKeys(name);
    findElementBySelector(".illusion-create-event-description").sendKeys(description);
    typeSelectorInputValue("input[data-alt-field='.actual-start-date']", "10/20/2030");
    typeSelectorInputValue("input[data-alt-field='.actual-end-date']", "10/20/2030");
    scrollWaitAndClick(".illusion-create-event-save");

    waitForUrlMatches(".*/illusion/event/" + urlName);
    testTitle("Illusion - name");
    
    waitAndAssertSelectorText(".view-header-description-title", name, true, true);
    waitAndAssertSelectorText(".view-header-description-text", description, true, true);
    assertSelectorPresent(".illusion-event-navigation-admin-menu");
    
    waitForNotification();
    assertNotificationStartsWith("warning", "Event is not published");
    
    deleteIllusionEventByUrl(urlName);
    deleteIllusionFolderByUser("admin@foyt.fi");
  }
  
  @Test
  @SqlSets ("illusion-basic")
  public void testCreateEventWithStartDate() throws Exception {
    loginInternal("admin@foyt.fi", "pass");

    String name = "withstart";
    String urlName = "withstart";
    String description = "withstart";
    String startDate = "10/20/2030";
    
    navigate("/illusion/createevent");
    
    typeSelectorInputValue(".illusion-create-event-name", name);
    typeSelectorInputValue(".illusion-create-event-description", description);
    typeSelectorInputValue("input[data-alt-field='.actual-start-date']", startDate);
    typeSelectorInputValue("input[data-alt-field='.actual-end-date']", startDate);
    scrollWaitAndClick(".illusion-create-event-save");

    waitForUrlMatches(".*/illusion/event/" + urlName);
    testTitle("Illusion - " + name);
    
    assertSelectorTextIgnoreCase(".view-header-description-title", name);
    assertSelectorTextIgnoreCase(".view-header-description-text", description);
    assertSelectorPresent(".illusion-event-navigation-admin-menu");
    
    navigate("/illusion/event/" + urlName + "/settings");
    assertSelectorValue("input[data-alt-field='.actual-start-date']", startDate);
    
    deleteIllusionEventByUrl(urlName);
    deleteIllusionFolderByUser("admin@foyt.fi");
  }
  
  @Test
  @SqlSets ("illusion-basic")
  public void testCreateEventWithTimesAndDates() throws Exception {
    loginInternal("admin@foyt.fi", "pass");

    String name = "timesanddates";
    String urlName = "timesanddates";
    String description = "timesanddates";
    String startDate = "10/20/2030";
    String startTime = "12:00";
    String endDate = "11/21/2031";
    String endTime = "10:30";
    
    navigate("/illusion/createevent");
    typeSelectorInputValue(".illusion-create-event-name", name);
    typeSelectorInputValue(".illusion-create-event-description", description);
    typeSelectorInputValue("input[data-alt-field='.actual-start-date']", startDate);
    typeSelectorInputValue("input[data-alt-field='.actual-start-time']", startTime);
    typeSelectorInputValue("input[data-alt-field='.actual-end-date']", endDate);
    typeSelectorInputValue("input[data-alt-field='.actual-end-time']", endTime);
    scrollWaitAndClick(".illusion-create-event-save");

    waitForUrlMatches(".*/illusion/event/" + urlName);
    testTitle("Illusion - " + name);
    
    assertSelectorTextIgnoreCase(".view-header-description-title", name);
    assertSelectorTextIgnoreCase(".view-header-description-text", description);
    assertSelectorPresent(".illusion-event-navigation-admin-menu");
    
    navigate("/illusion/event/" + urlName + "/settings");
    assertSelectorValue("input[data-alt-field='.actual-start-date']", startDate);
    assertSelectorValue("input[data-alt-field='.actual-start-time']", startTime);
    assertSelectorValue("input[data-alt-field='.actual-end-date']", endDate);
    assertSelectorValue("input[data-alt-field='.actual-end-time']", endTime);

    deleteIllusionEventByUrl(urlName);
    deleteIllusionFolderByUser("admin@foyt.fi");
  }
  
  @Test
  @SqlSets ("illusion-basic")
  public void testCreateEventWithLocation() throws Exception {
    loginInternal("admin@foyt.fi", "pass");

    String name = "timesanddates";
    String urlName = "timesanddates";
    String description = "timesanddates";
    String location = "location";
    String startDate = "10/20/2030";
    
    navigate("/illusion/createevent");
    typeSelectorInputValue(".illusion-create-event-name", name);
    typeSelectorInputValue(".illusion-create-event-description", description);
    typeSelectorInputValue("input[data-alt-field='.actual-start-date']", startDate);
    typeSelectorInputValue("input[data-alt-field='.actual-end-date']", startDate);
    typeSelectorInputValue(".illusion-create-event-location", location);
    scrollWaitAndClick(".illusion-create-event-save");
    
    waitForUrlMatches(".*/illusion/event/" + urlName);
    navigate("/illusion/event/" + urlName + "/settings");
    waitForSelectorPresent(".illusion-event-settings-location");
    assertSelectorValue(".illusion-event-settings-location", location);
    
    deleteIllusionEventByUrl(urlName);
    deleteIllusionFolderByUser("admin@foyt.fi");
  }
  
  @Test
  @SqlSets ("illusion-basic")
  public void testCreateEventSignUpDates() throws Exception {
    loginInternal("admin@foyt.fi", "pass");

    String name = "signupdates";
    String startDate = "10/20/2030";
    String signUpStartDate = "10/05/2030";
    String signUpEndDate = "10/10/2030";
      
    navigate("/illusion/createevent");
    typeSelectorInputValue(".illusion-create-event-name", name);
    typeSelectorInputValue("input[data-alt-field='.actual-start-date']", startDate);
    typeSelectorInputValue("input[data-alt-field='.actual-end-date']", startDate);
    typeSelectorInputValue(".sign-up-start-date", signUpStartDate);
    typeSelectorInputValue(".sign-up-end-date", signUpEndDate);
    scrollWaitAndClick(".illusion-create-event-save");

    waitForUrlMatches(".*/illusion/event/" + name);
    navigate("/illusion/event/" + name + "/settings");
    
    waitForSelectorPresent(".sign-up-start-date");
    
    assertSelectorValue(".sign-up-start-date", signUpStartDate);
    assertSelectorValue(".sign-up-end-date", signUpEndDate);
    
    deleteIllusionEventByUrl(name);
    deleteIllusionFolderByUser("admin@foyt.fi");
  }
  
  @Test
  @SqlSets ("illusion-basic")
  public void testCreateEventImageUrl() throws Exception {
    loginInternal("admin@foyt.fi", "pass");

    String name = "signupdates";
    String startDate = "10/20/2030";
    String imageUrl = "http://www.url.to/image.png";
      
    navigate("/illusion/createevent");
    typeSelectorInputValue(".illusion-create-event-name", name);
    typeSelectorInputValue("input[data-alt-field='.actual-start-date']", startDate);
    typeSelectorInputValue("input[data-alt-field='.actual-end-date']", startDate);
    typeSelectorInputValue(".illusion-create-event-image-url", imageUrl);
    scrollWaitAndClick(".illusion-create-event-save");

    waitForUrlMatches(".*/illusion/event/" + name);
    navigate("/illusion/event/" + name + "/settings");

    waitPresent(".illusion-event-settings-image-url");
    assertSelectorValue(".illusion-event-settings-image-url", imageUrl);
    
    deleteIllusionEventByUrl(name);
    deleteIllusionFolderByUser("admin@foyt.fi");
  }

  @Test
  @SqlSets ("illusion-basic")
  public void testCreateEventBeginnerFriendly() throws Exception {
    loginInternal("admin@foyt.fi", "pass");

    String name = "signupdates";
    String startDate = "10/20/2030";
      
    navigate("/illusion/createevent");
    typeSelectorInputValue(".illusion-create-event-name", name);
    typeSelectorInputValue("input[data-alt-field='.actual-start-date']", startDate);
    typeSelectorInputValue("input[data-alt-field='.actual-end-date']", startDate);
    scrollWaitAndClick(".illusion-create-event-beginner-friendly");
    scrollWaitAndClick(".illusion-create-event-save");
    waitForUrlMatches(".*/illusion/event/" + name);
    navigate("/illusion/event/" + name + "/settings");
    waitTitle("Event Settings");
    assertSelectorPresent(".illusion-event-settings-beginner-friendly:checked");
    deleteIllusionEventByUrl(name);
    deleteIllusionFolderByUser("admin@foyt.fi");
  }

  @Test
  @SqlSets ("illusion-basic")
  public void testCreateEventAgeLimit() throws Exception {
    loginInternal("admin@foyt.fi", "pass");

    String name = "signupdates";
    String startDate = "10/20/2030";
    String ageLimit = "16";
      
    navigate("/illusion/createevent");
    typeSelectorInputValue(".illusion-create-event-name", name);
    typeSelectorInputValue("input[data-alt-field='.actual-start-date']", startDate);
    typeSelectorInputValue("input[data-alt-field='.actual-end-date']", startDate);
    typeSelectorInputValue(".illusion-create-event-age-limit", ageLimit);
    scrollWaitAndClick(".illusion-create-event-save");

    waitForUrlMatches(".*/illusion/event/" + name);
    navigate("/illusion/event/" + name + "/settings");
    waitForSelectorPresent(".illusion-event-settings-age-limit");
    assertSelectorValue(".illusion-event-settings-age-limit", ageLimit);
    
    deleteIllusionEventByUrl(name);
    deleteIllusionFolderByUser("admin@foyt.fi");
  }

  @Test
  @SqlSets ("illusion-basic")
  public void testCreateEventType() throws Exception {
    loginInternal("admin@foyt.fi", "pass");

    String name = "signupdates";
    String startDate = "10/20/2030";
      
    navigate("/illusion/createevent");
    typeSelectorInputValue(".illusion-create-event-name", name);
    typeSelectorInputValue("input[data-alt-field='.actual-start-date']", startDate);
    typeSelectorInputValue("input[data-alt-field='.actual-end-date']", startDate);
    selectSelectBoxByValue(".illusion-create-event-type", "2");
    scrollWaitAndClick(".illusion-create-event-save");

    waitForUrlMatches(".*/illusion/event/" + name);
    navigate("/illusion/event/" + name + "/settings");
    waitPresent(".illusion-event-settings-type");
    assertSelectBoxValue(".illusion-event-settings-type", "2");

    deleteIllusionEventByUrl(name);
    deleteIllusionFolderByUser("admin@foyt.fi");
  }

  @Test
  @SqlSets ("illusion-basic")
  public void testCreateEventGenres() throws Exception {
    loginInternal("admin@foyt.fi", "pass");

    String name = "signupdates";
    String startDate = "10/20/2030";
      
    navigate("/illusion/createevent");
    waitAndSendKeys(".illusion-create-event-name", name);
    waitAndSendKeys("input[data-alt-field='.actual-start-date']", startDate);
    waitAndSendKeys("input[data-alt-field='.actual-end-date']", startDate);
    scrollWaitAndClick(".illusion-create-event-genre input[value='1']");
    scrollWaitAndClick(".illusion-create-event-genre input[value='3']");
    scrollWaitAndClick(".illusion-create-event-save");
    waitForUrlMatches(".*/illusion/event/" + name);
    navigate("/illusion/event/" + name + "/settings");
    
    waitForSelectorPresent(".illusion-event-settings-genres");
    assertSelectorPresent(".illusion-event-settings-genre input[value='1']:checked");
    assertSelectorNotPresent(".illusion-event-settings-genre input[value='2']:checked");
    assertSelectorPresent(".illusion-event-settings-genre input[value='3']:checked");
    assertSelectorNotPresent(".illusion-event-settings-genre input[value='4']:checked");
    
    deleteIllusionEventByUrl(name);
    deleteIllusionFolderByUser("admin@foyt.fi");
  }
  
  @Test
  @SqlSets ("illusion-basic")
  public void testEventLarpKalenteriCreate() throws Exception {
    stubLarpKalenteriAccessToken();
    stubLarpKalenteriTypes();
    stubLarpKalenteriGenres();
    
    loginInternal("admin@foyt.fi", "pass");

    String name = "name";
    String urlName = "name";
    String description = "description";
    
    String requestBody = (new com.fasterxml.jackson.databind.ObjectMapper()
      .registerModule(new JavaTimeModule())
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .setSerializationInclusion(Include.NON_NULL))
      .writeValueAsString(new Event(null, 
        name, 
        "2", 
        null, // getDate(2030, 10, 20), FIXME: These fail on SauceLabs to incorrect timezone  
        null, // getDate(2030, 10, 20), FIXME: These fail on SauceLabs to incorrect timezone  
        null, 
        null, 
        null, 
        8l, 
        "", 
        "", 
        new ArrayList<String>(), 
        "", 
        null, 
        false, 
        null, 
        description, 
        "Test Admin", 
        "admin@foyt.fi", 
        "http://test.forgeandillusion.net:8080/illusion/event/" + urlName, 
        null, 
        Event.Status.PENDING, 
        null, 
        false, 
        false, 
        false, 
        null));
    
    String responseBody = (new com.fasterxml.jackson.databind.ObjectMapper()
      .registerModule(new JavaTimeModule())
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .setSerializationInclusion(Include.NON_NULL))
      .writeValueAsString(new Event(234l, 
        name, 
        "2", 
        getDate(2030, 10, 20), 
        getDate(2030, 10, 20), 
        null, 
        null, 
        null, 
        8l, 
        "", 
        "", 
        new ArrayList<String>(), 
        "", 
        null, 
        false, 
        null, 
        description, 
        "Test Admin", 
        "admin@foyt.fi", 
        "http://test.forgeandillusion.net:8080/illusion/event/" + urlName, 
        null, 
        Event.Status.PENDING, 
        null, 
        false, 
        false, 
        false, 
        1l));

    stubFor(post(urlEqualTo("/rest/events/"))
      .willReturn(aResponse()
        .withStatus(200)
        .withHeader("Content-Type", "application/json")
        .withBody(responseBody)));
    
    navigate("/illusion/createevent");
    
    waitAndSendKeys(".illusion-create-event-name", name);
    waitAndSendKeys(".illusion-create-event-description", description);
    typeSelectorInputValue("input[data-alt-field='.actual-start-date']", "10/20/2030");
    typeSelectorInputValue("input[data-alt-field='.actual-end-date']", "10/20/2030");
    scrollWaitAndClick(".illusion-create-event-larp-kalenteri");
    scrollWaitAndClick(".illusion-create-event-save");

    waitForUrlMatches(".*/illusion/event/" + urlName);
    testTitle("Illusion - name");
    
    assertSelectorTextIgnoreCase(".view-header-description-title", name);
    assertSelectorTextIgnoreCase(".view-header-description-text", description);
    assertSelectorPresent(".illusion-event-navigation-admin-menu");
    
    waitForNotification();
    assertNotificationStartsWith("warning", "Event is not published");
    
    verify(1, postRequestedFor(urlEqualTo("/rest/events/"))
      .withRequestBody(equalToJson(requestBody, true, true))    
    );
    
    navigate(String.format("/illusion/event/%s/settings", urlName));
    assertSelectorPresent(".illusion-create-event-larp-kalenteri:checked");
    
    deleteIllusionEventByUrl(urlName);
    deleteIllusionFolderByUser("admin@foyt.fi"); 
  }
  
  @Test
  @SqlSets ("illusion-basic")
  public void testEventLarpKalenteriCreateGenres() throws Exception {
    stubLarpKalenteriAccessToken();
    stubLarpKalenteriTypes();
    stubLarpKalenteriGenres();
    
    loginInternal("admin@foyt.fi", "pass");

    String name = "name";
    String urlName = "name";
    String description = "description";
    
    String requestBody = (new com.fasterxml.jackson.databind.ObjectMapper()
      .registerModule(new JavaTimeModule())
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .setSerializationInclusion(Include.NON_NULL))
      .writeValueAsString(new Event(null, 
        name, 
        "2", 
        null, // getDate(2030, 10, 20), FIXME: These fail on SauceLabs to incorrect timezone  
        null, // getDate(2030, 10, 20), FIXME: These fail on SauceLabs to incorrect timezone  
        null, 
        null, 
        null, 
        8l, 
        "", 
        "", 
        Arrays.asList("fantasy", "cyberpunk"),
        "", 
        null, 
        false, 
        null, 
        description, 
        "Test Admin", 
        "admin@foyt.fi", 
        "http://test.forgeandillusion.net:8080/illusion/event/" + urlName, 
        null, 
        Event.Status.PENDING, 
        null, 
        false, 
        false, 
        false, 
        null));
    
    String responseBody = (new com.fasterxml.jackson.databind.ObjectMapper()
      .registerModule(new JavaTimeModule())
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .setSerializationInclusion(Include.NON_NULL))
      .writeValueAsString(new Event(234l, 
        name, 
        "2", 
        getDate(2030, 10, 20), 
        getDate(2030, 10, 20), 
        null, 
        null, 
        null, 
        8l, 
        "", 
        "", 
        Arrays.asList("fantasy", "cyberpunk"),
        "", 
        null, 
        false, 
        null, 
        description, 
        "Test Admin", 
        "admin@foyt.fi", 
        "http://test.forgeandillusion.net:8080/illusion/event/" + urlName, 
        null, 
        Event.Status.PENDING, 
        null, 
        false, 
        false, 
        false, 
        1l));

    stubFor(post(urlEqualTo("/rest/events/"))
      .willReturn(aResponse()
        .withStatus(200)
        .withHeader("Content-Type", "application/json")
        .withBody(responseBody)));
    
    navigate("/illusion/createevent");
    
    findElementBySelector(".illusion-create-event-name").sendKeys(name);
    findElementBySelector(".illusion-create-event-description").sendKeys(description);
    typeSelectorInputValue("input[data-alt-field='.actual-start-date']", "10/20/2030");
    typeSelectorInputValue("input[data-alt-field='.actual-end-date']", "10/20/2030");
    scrollWaitAndClick(".illusion-create-event-genre input[value='1']");
    scrollWaitAndClick(".illusion-create-event-genre input[value='3']");
    scrollWaitAndClick(".illusion-create-event-larp-kalenteri");
    scrollWaitAndClick(".illusion-create-event-save");

    waitForUrlMatches(".*/illusion/event/" + urlName);
    testTitle("Illusion - name");
    
    assertSelectorTextIgnoreCase(".view-header-description-title", name);
    assertSelectorTextIgnoreCase(".view-header-description-text", description);
    assertSelectorPresent(".illusion-event-navigation-admin-menu");
    
    waitForNotification();
    assertNotificationStartsWith("warning", "Event is not published");
    
    verify(1, postRequestedFor(urlEqualTo("/rest/events/"))
      .withRequestBody(equalToJson(requestBody, true, true))    
    );
    
    navigate(String.format("/illusion/event/%s/settings", urlName));
    assertSelectorPresent(".illusion-create-event-larp-kalenteri:checked");
    
    deleteIllusionEventByUrl(urlName);
    deleteIllusionFolderByUser("admin@foyt.fi"); 
  }

  
  @Test
  @SqlSets ("illusion-basic")
  public void testEventLarpKalenteriCreateLocation() throws Exception {
    stubLarpKalenteriAccessToken();
    stubLarpKalenteriTypes();
    stubLarpKalenteriGenres();
    
    loginInternal("admin@foyt.fi", "pass");

    String name = "name";
    String urlName = "name";
    String description = "description";
    
    String requestBody = (new com.fasterxml.jackson.databind.ObjectMapper()
      .registerModule(new JavaTimeModule())
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .setSerializationInclusion(Include.NON_NULL))
      .writeValueAsString(new Event(null, 
        name, 
        "2", 
        null, // getDate(2030, 10, 20), FIXME: These fail on SauceLabs to incorrect timezone  
        null, // getDate(2030, 10, 20), FIXME: These fail on SauceLabs to incorrect timezone  
        null, 
        null, 
        null, 
        2l, 
        "Otakaari 24, 02150 Espoo, Finland", 
        "", 
        new ArrayList<String>(),
        "", 
        null, 
        false, 
        null, 
        description, 
        "Test Admin", 
        "admin@foyt.fi", 
        "http://test.forgeandillusion.net:8080/illusion/event/" + urlName, 
        null, 
        Event.Status.PENDING, 
        null, 
        false, 
        false, 
        false, 
        null));
    
    String responseBody = (new com.fasterxml.jackson.databind.ObjectMapper()
      .registerModule(new JavaTimeModule())
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .setSerializationInclusion(Include.NON_NULL))
      .writeValueAsString(new Event(234l, 
        name, 
        "2", 
        getDate(2030, 10, 20), 
        getDate(2030, 10, 20), 
        null, 
        null, 
        null, 
        2l, 
        "Otakaari 24, 02150 Espoo, Finland", 
        "", 
        new ArrayList<String>(),
        "", 
        null, 
        false, 
        null, 
        description, 
        "Test Admin", 
        "admin@foyt.fi", 
        "http://test.forgeandillusion.net:8080/illusion/event/" + urlName, 
        null, 
        Event.Status.PENDING, 
        null, 
        false, 
        false, 
        false, 
        1l));

    stubFor(post(urlEqualTo("/rest/events/"))
      .willReturn(aResponse()
        .withStatus(200)
        .withHeader("Content-Type", "application/json")
        .withBody(responseBody)));
    
    navigate("/illusion/createevent");
    
    findElementBySelector(".illusion-create-event-name").sendKeys(name);
    findElementBySelector(".illusion-create-event-description").sendKeys(description);
    typeSelectorInputValue("input[data-alt-field='.actual-start-date']", "10/20/2030");
    typeSelectorInputValue("input[data-alt-field='.actual-end-date']", "10/20/2030");
    typeSelectorInputValue(".illusion-create-event-location", "Otakaari 24, 02150 Espoo, Finland");
    scrollWaitAndClick(".illusion-create-event-larp-kalenteri");
    
    waitForInputValueNotBlank(".location-lat");
    scrollWaitAndClick(".illusion-create-event-save");

    waitForUrlMatches(".*/illusion/event/" + urlName);
    testTitle("Illusion - name");
    
    assertSelectorTextIgnoreCase(".view-header-description-title", name);
    assertSelectorTextIgnoreCase(".view-header-description-text", description);
    assertSelectorPresent(".illusion-event-navigation-admin-menu");
    
    waitForNotification();
    assertNotificationStartsWith("warning", "Event is not published");
    
    verify(1, postRequestedFor(urlEqualTo("/rest/events/"))
      .withRequestBody(equalToJson(requestBody, true, true))    
    );
    
    navigate(String.format("/illusion/event/%s/settings", urlName));
    assertSelectorPresent(".illusion-create-event-larp-kalenteri:checked");
    
    deleteIllusionEventByUrl(urlName);
    deleteIllusionFolderByUser("admin@foyt.fi"); 
  }

}
