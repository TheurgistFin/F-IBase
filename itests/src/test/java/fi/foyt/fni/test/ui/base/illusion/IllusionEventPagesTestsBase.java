package fi.foyt.fni.test.ui.base.illusion;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import fi.foyt.fni.test.DefineSqlSet;
import fi.foyt.fni.test.DefineSqlSets;
import fi.foyt.fni.test.SqlSets;
import fi.foyt.fni.test.ui.base.AbstractIllusionUITest;

@DefineSqlSets ({
  @DefineSqlSet(id = "basic-users", before = "basic-users-setup.sql", after = "basic-users-teardown.sql"),
  @DefineSqlSet(id = "illusion-basic", before = "illusion-basic-setup.sql", after = "illusion-basic-teardown.sql"),
  @DefineSqlSet(id = "event", before = { "illusion-event-open-setup.sql" }, after = { "illusion-event-open-teardown.sql"}),  
  @DefineSqlSet(id = "event-participant", before = {"illusion-event-open-participant-setup.sql" }, after = {"illusion-event-open-participant-teardown.sql"}),
  @DefineSqlSet(id = "event-unpublished", before = { "illusion-event-open-unpublished-setup.sql" }, after = { "illusion-event-open-unpublished-teardown.sql"}),
  @DefineSqlSet(id = "event-organizer", before = {"illusion-event-open-organizer-setup.sql" }, after = {"illusion-event-open-organizer-teardown.sql"}),
  @DefineSqlSet(id = "event-invited", before = {"illusion-event-open-invited-setup.sql" }, after = {"illusion-event-open-invited-teardown.sql"}),
  @DefineSqlSet(id = "event-page", before = {"illusion-event-open-page-setup.sql"}, after = {"illusion-event-open-page-teardown.sql"}),
  @DefineSqlSet(id = "event-page-participants", before = {"illusion-event-open-page-participants-setup.sql" }, after = {"illusion-event-open-page-participants-teardown.sql"}),
  @DefineSqlSet(id = "event-custom", before = { "illusion-event-open-custom-setup.sql" }, after = {"illusion-event-open-custom-teardown.sql"})
})
public class IllusionEventPagesTestsBase extends AbstractIllusionUITest {
  
  @Test
  @SqlSets ({"basic-users", "illusion-basic", "event", "event-page", "event-page-participants"})
  public void testLoginRequired() throws Exception {
    testLoginRequired("/illusion/event/openevent/pages/testpage");
  }
  
  @Test
  @SqlSets ({"basic-users", "illusion-basic", "event", "event-page", "event-page-participants"})
  public void testNotFound() throws Exception {
    loginInternal("user@foyt.fi", "pass");
    testNotFound("/illusion/event/openevent/pages/testpage/");
    testNotFound("/illusion/event/openevent/pages/nothing");
    testNotFound("/illusion/event/openevent/pages/");
    testNotFound("/illusion/event/openevent/pages");
    testNotFound("/illusion/event/noevent/pages/testpage");
    testNotFound("/illusion/event/noevent//pages/testpage");
    testNotFound("/illusion/event/noevent/*/pages/testpage");
    testNotFound("/illusion/event/1/pages/testpage");
    testNotFound("/illusion/event///pages/testpage");
    testNotFound("/illusion/event//*/pages/testpage");
    testNotFound("/illusion/event/~/pages/testpage");
  }
  
  @Test
  @SqlSets ({"basic-users", "illusion-basic", "event", "event-participant", "event-page", "event-page-participants"})
  public void testPageTitle() throws Exception {
    loginInternal("user@foyt.fi", "pass");
    testTitle("/illusion/event/openevent/pages/testpage", "Open Event - Test Page");
  }
  
  @Test
  @SqlSets ({"basic-users", "illusion-basic", "event", "event-invited", "event-page", "event-page-participants"})
  public void testLoggedInInvited() throws Exception {
    loginInternal("librarian@foyt.fi", "pass");
    testTitle("/illusion/event/openevent/pages/testpage", "Open Event - Test Page");
  }
  
  @Test
  @SqlSets ({"basic-users", "illusion-basic", "event", "event-participant", "event-page", "event-page-participants"})
  public void testPageText() throws Exception {
    loginInternal("user@foyt.fi", "pass");
    navigate("/illusion/event/openevent/pages/testpage");
    assertSelectorTextIgnoreCase(".illusion-event-page-content p", "Page contents");
  }
  
  @Test
  @SqlSets ({"basic-users", "illusion-basic", "event", "event-organizer", "event-page", "event-page-participants"})
  public void testCreatePage() throws Exception {
    loginInternal("admin@foyt.fi", "pass");
    navigate("/illusion/event/openevent/manage-pages");
    testTitle("Open Event - Manage Pages");
    clickSelector(".illusion-event-manage-pages-new-page");
    waitForUrlMatches(".*/edit-page.*");
    assertUrlMatches(".*/illusion/event/openevent/edit-page.*");
    navigate("/");
    executeSql("update MaterialRevision set checksum = ? where id in (select id from DocumentRevision where document_id in (select id from Material where parentFolder_id = ? and urlName = ?))", "DELETE", 20000, "new_page");
    executeSql("delete from DocumentRevision where document_id in (select id from Material where parentFolder_id = ? and urlName = ?)", 20000, "new_page");
    executeSql("delete from MaterialRevision where checksum = ?", "DELETE");
    executeSql("delete from IllusionEventDocument where id in (select id from Material where parentFolder_id = ? and urlName = ?)", 20000, "new_page");
    executeSql("delete from CoopsSession where material_id in (select id from Material where parentFolder_id = ? and urlName = ?)", 20000, "new_page");
    executeSql("delete from Document where id in (select id from Material where parentFolder_id = ? and urlName = ?)", 20000, "new_page");
    executeSql("delete from Material where parentFolder_id = ? and urlName = ?", 20000, "new_page");
  }
  
  @Test
  @SqlSets ({"basic-users", "illusion-basic", "event", "event-organizer", "event-page", "event-page-participants"})
  public void testPagePermaLink() throws Exception {
    loginInternal("admin@foyt.fi", "pass");
    navigate("/illusion/event/openevent/manage-pages");
    testTitle("Open Event - Manage Pages");
    clickSelector(".illusion-event-manage-pages-new-page");
    waitForUrlMatches(".*/edit-page.*");
    String pageId = null;
    
    Pattern pattern = Pattern.compile("(.*pageId=)([0-9]{1,})(.*)");
    Matcher matcher = pattern.matcher(getWebDriver().getCurrentUrl());
    if (matcher.matches()) {
      pageId = matcher.group(2);
    }
    
    waitForSelectorText(".illusion-edit-page-editor-status", "Loaded", true);
    clearSelectorInput(".illusion-edit-page-title");
    typeSelectorInputValue(".illusion-edit-page-title", "changed");
    clickSelector(".illusion-edit-page-editor-status");
    waitForSelectorText(".illusion-edit-page-editor-status", "Saved", true);
    executeSql("update IllusionEventSetting set value = '{\"" + pageId + "\":{\"visibility\":\"VISIBLE\"}}' where id = 1");
    
    testTitle("/illusion/event/openevent/pages/new_page", "Open Event - changed");

    executeSql("delete from PermaLink where material_id = (select id from Material where parentFolder_id = ? and urlName = ?)", 20000, "changed");
    executeSql("delete from MaterialRevisionSetting where materialRevision_id in (select id from DocumentRevision where document_id in (select id from Material where parentFolder_id = ? and urlName = ?))", 20000, "changed");
    executeSql("update MaterialRevision set checksum = ? where id in (select id from DocumentRevision where document_id in (select id from Material where parentFolder_id = ? and urlName = ?))", "DELETE", 20000, "changed");
    executeSql("delete from DocumentRevision where document_id in (select id from Material where parentFolder_id = ? and urlName = ?)", 20000, "changed");
    executeSql("delete from MaterialRevision where checksum = ?", "DELETE");
    executeSql("delete from CoOpsSession where material_id in (select id from Material where parentFolder_id = ? and urlName = ?)", 20000, "changed");
    executeSql("delete from IllusionEventDocument where id in (select id from Material where parentFolder_id = ? and urlName = ?)", 20000, "changed");
    executeSql("delete from Document where id in (select id from Material where parentFolder_id = ? and urlName = ?)", 20000, "changed");
    executeSql("delete from Material where parentFolder_id = ? and urlName = ?", 20000, "changed");
  }
  
  @Test
  @SqlSets ({"basic-users", "illusion-basic", "event", "event-page"})
  public void testHiddenNotLoggedIn() throws UnsupportedEncodingException {
    testAccessDenied("/illusion/event/openevent/pages/testpage");
  }
  
  @Test
  @SqlSets ({"basic-users", "illusion-basic", "event", "event-page"})
  public void testHiddenLoggedIn() throws UnsupportedEncodingException {
    loginInternal("user@foyt.fi", "pass");
    testAccessDenied("/illusion/event/openevent/pages/testpage");
  }
  
  @Test
  @SqlSets ({"basic-users", "illusion-basic", "event", "event-participant", "event-page"})
  public void testHiddenLoggedParticipant() throws UnsupportedEncodingException {
    loginInternal("user@foyt.fi", "pass");
    testAccessDenied("/illusion/event/openevent/pages/testpage");
  }
  
  @Test
  @SqlSets ({"basic-users", "illusion-basic", "event", "event-participant", "event-page", "event-page-participants"})
  public void testVisibleForParticipantsNotLoggedIn() throws UnsupportedEncodingException {
    testLoginRequired("/illusion/event/openevent/pages/testpage");
  }
  
  @Test
  @SqlSets ({"basic-users", "illusion-basic", "event", "event-participant", "event-page", "event-page-participants"})
  public void testVisibleForParticipantsLoggedIn() throws UnsupportedEncodingException {
    loginInternal("admin@foyt.fi", "pass");
    testAccessDenied("/illusion/event/openevent/pages/testpage");
  }
  
  @Test
  @SqlSets ({"basic-users", "illusion-basic", "event", "event-organizer", "event-page", "event-page-participants", "event-custom"})
  public void testCustomDomain() {
    getWebDriver().get(getCustomEventUrl());
    loginCustomEvent("admin@foyt.fi", "pass");
    getWebDriver().get(getCustomEventUrl() + "/pages/testpage");
    testTitle("Open Event - Test Page");
  }
  
  @Test
  @SqlSets ({"basic-users", "illusion-basic", "event", "event-organizer", "event-page", "event-page-participants", "event-custom"})
  public void testCustomDomainLoginRedirect() {
    getWebDriver().get(getCustomEventUrl() + "/pages/testpage");
    waitForUrlMatches(".*/login.*");
    loginCustomEvent("admin@foyt.fi", "pass");
    testTitle("Open Event - Test Page");
  }
  
  @Test
  @SqlSets ({"basic-users", "illusion-basic", "event", "event-organizer", "event-page", "event-page-participants", "event-custom"})
  public void testCustomDomainMenuItems() {
    getWebDriver().get(getCustomEventUrl());
    loginCustomEvent("admin@foyt.fi", "pass");
    getWebDriver().get(getCustomEventUrl() + "/pages/testpage");
    testTitle("Open Event - Test Page");
    assertMenuItems();
  }
  
  @Test
  @SqlSets ({"basic-users", "illusion-basic", "event", "event-organizer", "event-page", "event-page-participants", "event-custom"})
  public void testCustomDomainNavigationLinks() {
    getWebDriver().get(getCustomEventUrl());
    loginCustomEvent("admin@foyt.fi", "pass");
    getWebDriver().get(getCustomEventUrl() + "/pages/testpage");
    testTitle("Open Event - Test Page");

    assertEquals(getAppUrl() + "/", findElementBySelector(".view-header-navigation .view-header-navigation-item:nth-child(1)").getAttribute("href"));
    assertEquals(getAppUrl() + "/illusion", findElementBySelector(".view-header-navigation .view-header-navigation-item:nth-child(3)").getAttribute("href"));
    assertEquals(getCustomEventUrl() + "/", findElementBySelector(".view-header-navigation .view-header-navigation-item:nth-child(5)").getAttribute("href"));
    assertEquals(getCustomEventUrl() + "/pages/testpage", findElementBySelector(".view-header-navigation .view-header-navigation-item:nth-child(7)").getAttribute("href"));
  }
  
  @Test
  @SqlSets ({"basic-users", "illusion-basic", "event", "event-organizer", "event-page", "event-page-participants", "event-unpublished"})
  public void testUnpublishedAccessDenied() throws UnsupportedEncodingException {
    loginInternal("user@foyt.fi", "pass");
    testAccessDenied("/illusion/event/openevent/pages/testpage");
    logout();
    loginInternal("admin@foyt.fi", "pass");
    navigate("/illusion/event/openevent/pages/testpage");
    testTitle("Open Event - Test Page");
  }
  
}
