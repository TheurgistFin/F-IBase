package fi.foyt.fni.test.ui.base;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import fi.foyt.fni.test.DefineSqlSet;
import fi.foyt.fni.test.DefineSqlSets;
import fi.foyt.fni.test.SqlSets;

@DefineSqlSets({
  @DefineSqlSet (id = "illusion-basic", before = { "basic-users-setup.sql","illusion-basic-setup.sql"}, after = {"illusion-basic-teardown.sql","basic-users-teardown.sql"}),
})
public class IllusionCreateEventTestsBase extends AbstractUITest {

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
    acceptCookieDirective();

    loginInternal("admin@foyt.fi", "pass");
    navigate("/illusion/createevent");
    typeSelectorInputValue("input[data-alt-field='.actual-start-date']", "10/20/2030");
    clickSelector(".illusion-create-event-save");
    assertSelectorPresent(".illusion-create-event-name:invalid");
  }
  
  @Test
  @SqlSets ("illusion-basic")
  public void testCreateEvent() throws Exception {
    acceptCookieDirective();
    loginInternal("admin@foyt.fi", "pass");

    String name = "name";
    String urlName = "name";
    String description = "description";

    navigate("/illusion/createevent");
    
    findElementBySelector(".illusion-create-event-name").sendKeys(name);
    findElementBySelector(".illusion-create-event-description").sendKeys(description);
    typeSelectorInputValue("input[data-alt-field='.actual-start-date']", "10/20/2030");
    typeSelectorInputValue("input[data-alt-field='.actual-end-date']", "10/20/2030");
    
    waitSelectorToBeClickable(".illusion-create-event-save");
    findElementBySelector(".illusion-create-event-save").click();

    waitForUrlMatches(".*/illusion/event/" + urlName);
    testTitle("Illusion - name");
    
    assertSelectorTextIgnoreCase(".view-header-description-title", name);
    assertSelectorTextIgnoreCase(".view-header-description-text", description);
    assertSelectorPresent(".illusion-event-navigation-admin-menu");
    
    waitForNotification();
    assertNotificationStartsWith("warning", "Event is not published");
    
    deleteIllusionEventByUrl(urlName);
    deleteIllusionFolderByUser("admin@foyt.fi");
  }
  
  @Test
  @SqlSets ("illusion-basic")
  public void testCreateEventWithStartDate() throws Exception {
    acceptCookieDirective();

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
    
    waitSelectorToBeClickable(".illusion-create-event-save");
    findElementBySelector(".illusion-create-event-save").click();

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
    acceptCookieDirective();
    
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
    
    waitSelectorToBeClickable(".illusion-create-event-save");
    findElementBySelector(".illusion-create-event-save").click();

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
    acceptCookieDirective();

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
    waitSelectorToBeClickable(".illusion-create-event-save");
    findElementBySelector(".illusion-create-event-save").click();
    waitForUrlMatches(".*/illusion/event/" + urlName);
    navigate("/illusion/event/" + urlName + "/settings");
    assertSelectorValue(".illusion-event-settings-location", location);
    
    deleteIllusionEventByUrl(urlName);
    deleteIllusionFolderByUser("admin@foyt.fi");
  }
  
  @Test
  @SqlSets ("illusion-basic")
  public void testCreateEventSignUpDates() throws Exception {
    acceptCookieDirective();
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
    waitSelectorToBeClickable(".illusion-create-event-save");
    findElementBySelector(".illusion-create-event-save").click();
    waitForUrlMatches(".*/illusion/event/" + name);
    navigate("/illusion/event/" + name + "/settings");
    assertSelectorValue(".sign-up-start-date", signUpStartDate);
    assertSelectorValue(".sign-up-end-date", signUpEndDate);
    
    deleteIllusionEventByUrl(name);
    deleteIllusionFolderByUser("admin@foyt.fi");
  }
  
  @Test
  @SqlSets ("illusion-basic")
  public void testCreateEventImageUrl() throws Exception {
    acceptCookieDirective();
    loginInternal("admin@foyt.fi", "pass");

    String name = "signupdates";
    String startDate = "10/20/2030";
    String imageUrl = "http://www.url.to/image.png";
      
    navigate("/illusion/createevent");
    typeSelectorInputValue(".illusion-create-event-name", name);
    typeSelectorInputValue("input[data-alt-field='.actual-start-date']", startDate);
    typeSelectorInputValue("input[data-alt-field='.actual-end-date']", startDate);
    typeSelectorInputValue(".illusion-create-event-image-url", imageUrl);
    waitSelectorToBeClickable(".illusion-create-event-save");
    findElementBySelector(".illusion-create-event-save").click();
    waitForUrlMatches(".*/illusion/event/" + name);
    navigate("/illusion/event/" + name + "/settings");
    assertSelectorValue(".illusion-event-settings-image-url", imageUrl);
    
    deleteIllusionEventByUrl(name);
    deleteIllusionFolderByUser("admin@foyt.fi");
  }

  @Test
  @SqlSets ("illusion-basic")
  public void testCreateEventBeginnerFriendly() throws Exception {
    acceptCookieDirective();
    loginInternal("admin@foyt.fi", "pass");

    String name = "signupdates";
    String startDate = "10/20/2030";
      
    navigate("/illusion/createevent");
    typeSelectorInputValue(".illusion-create-event-name", name);
    typeSelectorInputValue("input[data-alt-field='.actual-start-date']", startDate);
    typeSelectorInputValue("input[data-alt-field='.actual-end-date']", startDate);
    clickSelector(".illusion-create-event-beginner-friendly");
    waitSelectorToBeClickable(".illusion-create-event-save");
    findElementBySelector(".illusion-create-event-save").click();
    waitForUrlMatches(".*/illusion/event/" + name);
    navigate("/illusion/event/" + name + "/settings");
    assertSelectorPresent(".illusion-event-settings-beginner-friendly:checked");
    deleteIllusionEventByUrl(name);
    deleteIllusionFolderByUser("admin@foyt.fi");
  }

  @Test
  @SqlSets ("illusion-basic")
  public void testCreateEventAgeLimit() throws Exception {
    acceptCookieDirective();
    loginInternal("admin@foyt.fi", "pass");

    String name = "signupdates";
    String startDate = "10/20/2030";
    String ageLimit = "16";
      
    navigate("/illusion/createevent");
    typeSelectorInputValue(".illusion-create-event-name", name);
    typeSelectorInputValue("input[data-alt-field='.actual-start-date']", startDate);
    typeSelectorInputValue("input[data-alt-field='.actual-end-date']", startDate);
    typeSelectorInputValue(".illusion-create-event-age-limit", ageLimit);
    waitSelectorToBeClickable(".illusion-create-event-save");
    findElementBySelector(".illusion-create-event-save").click();
    waitForUrlMatches(".*/illusion/event/" + name);
    navigate("/illusion/event/" + name + "/settings");
    assertSelectorValue(".illusion-event-settings-age-limit", ageLimit);
    
    deleteIllusionEventByUrl(name);
    deleteIllusionFolderByUser("admin@foyt.fi");
  }

  @Test
  @SqlSets ("illusion-basic")
  public void testCreateEventType() throws Exception {
    acceptCookieDirective();
    loginInternal("admin@foyt.fi", "pass");

    String name = "signupdates";
    String startDate = "10/20/2030";
      
    navigate("/illusion/createevent");
    typeSelectorInputValue(".illusion-create-event-name", name);
    typeSelectorInputValue("input[data-alt-field='.actual-start-date']", startDate);
    typeSelectorInputValue("input[data-alt-field='.actual-end-date']", startDate);
    selectSelectBoxByValue(".illusion-create-event-type", "2");
    waitSelectorToBeClickable(".illusion-create-event-save");
    findElementBySelector(".illusion-create-event-save").click();
    waitForUrlMatches(".*/illusion/event/" + name);
    navigate("/illusion/event/" + name + "/settings");
    assertSelectBoxValue(".illusion-event-settings-type", "2");

    deleteIllusionEventByUrl(name);
    deleteIllusionFolderByUser("admin@foyt.fi");
  }

  @Test
  @SqlSets ("illusion-basic")
  public void testCreateEventGenres() throws Exception {
    acceptCookieDirective();
    loginInternal("admin@foyt.fi", "pass");

    String name = "signupdates";
    String startDate = "10/20/2030";
      
    navigate("/illusion/createevent");
    typeSelectorInputValue(".illusion-create-event-name", name);
    typeSelectorInputValue("input[data-alt-field='.actual-start-date']", startDate);
    typeSelectorInputValue("input[data-alt-field='.actual-end-date']", startDate);
    clickSelector(".illusion-create-event-genre input[value='1']");
    clickSelector(".illusion-create-event-genre input[value='3']");
    waitSelectorToBeClickable(".illusion-create-event-save");
    findElementBySelector(".illusion-create-event-save").click();
    waitForUrlMatches(".*/illusion/event/" + name);
    navigate("/illusion/event/" + name + "/settings");
    assertSelectorPresent(".illusion-event-settings-genre input[value='1']:checked");
    assertSelectorNotPresent(".illusion-event-settings-genre input[value='2']:checked");
    assertSelectorPresent(".illusion-event-settings-genre input[value='3']:checked");
    assertSelectorNotPresent(".illusion-event-settings-genre input[value='4']:checked");
    
    deleteIllusionEventByUrl(name);
    deleteIllusionFolderByUser("admin@foyt.fi");
  }
}
