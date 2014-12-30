package fi.foyt.fni.test.ui.base;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import fi.foyt.fni.test.DefineSqlSet;
import fi.foyt.fni.test.DefineSqlSets;
import fi.foyt.fni.test.SqlSets;

@DefineSqlSets ({
  @DefineSqlSet (id = "illusion-open-materials-hidden", 
    before = {"basic-users-setup.sql","illusion-basic-setup.sql", "illusion-event-open-setup.sql", "illusion-event-open-participant-setup.sql"}, 
    after = {"illusion-event-open-participant-teardown.sql", "illusion-event-open-teardown.sql", "illusion-basic-teardown.sql","basic-users-teardown.sql"}
  ),
  @DefineSqlSet (id = "illusion-open-materials-participant", 
    before = {"basic-users-setup.sql","illusion-basic-setup.sql", "illusion-event-open-setup.sql", "illusion-event-open-participant-setup.sql", "illusion-event-open-materials-to-participants-setup.sql"}, 
    after = {"illusion-event-open-materials-to-participants-teardown.sql", "illusion-event-open-participant-teardown.sql", "illusion-event-open-teardown.sql", "illusion-basic-teardown.sql","basic-users-teardown.sql"}
  ),
  @DefineSqlSet(id = "illusion-open-materials-organizer", 
    before = {"basic-users-setup.sql","illusion-basic-setup.sql", "illusion-event-open-setup.sql", "illusion-event-open-organizer-setup.sql", "illusion-event-open-materials-to-participants-setup.sql"},
    after = {"illusion-event-open-materials-to-participants-teardown.sql", "illusion-event-open-organizer-teardown.sql", "illusion-event-open-teardown.sql", "illusion-basic-teardown.sql","basic-users-teardown.sql"}
  ),
  @DefineSqlSet(id = "illusion-open-materials-organizer-custom", 
    before = {"basic-users-setup.sql","illusion-basic-setup.sql", "illusion-event-open-setup.sql", "illusion-event-open-organizer-setup.sql", "illusion-event-open-materials-to-participants-setup.sql", "illusion-event-open-custom-setup.sql"},
    after = {"illusion-event-open-custom-teardown.sql", "illusion-event-open-materials-to-participants-teardown.sql", "illusion-event-open-organizer-teardown.sql", "illusion-event-open-teardown.sql", "illusion-basic-teardown.sql","basic-users-teardown.sql"}
  )
})
public class IllusionEventMaterialsTestsBase extends AbstractIllusionUITest {
  
  @Test
  @SqlSets ("illusion-open-materials-participant")
  public void testLoginRequired() throws Exception {
    testLoginRequired("/illusion/event/openevent/materials");
  }
  
  @Test
  @SqlSets ("illusion-open-materials-participant")
  public void testNotFound() throws Exception {
    loginInternal("user@foyt.fi", "pass");
    testNotFound("/illusion/event/openevent/materials/");
    testNotFound("/illusion/event/noevent/materials");
    testNotFound("/illusion/event/noevent//materials");
    testNotFound("/illusion/event/noevent/*/materials");
    testNotFound("/illusion/event/1/materials");
    testNotFound("/illusion/event///materials");
    testNotFound("/illusion/event//*/materials");
    testNotFound("/illusion/event/~/materials");
  }
  
  @Test
  @SqlSets ("illusion-open-materials-participant")
  public void testLoggedInParticipant() throws Exception {
    loginInternal("user@foyt.fi", "pass");
    testTitle("/illusion/event/openevent/materials", "Illusion - Open Event");
    assertSelectorCount(".illusion-event-navigation>a", 2);
    assertSelectorCount(".illusion-event-navigation-item-active", 1);
    assertSelectorNotPresent(".illusion-event-navigation-admin-menu");
    assertSelectorTextIgnoreCase(".illusion-event-navigation-item-active", "materials");
  }
  
  @Test
  @SqlSets ("illusion-open-materials-organizer")
  public void testLoggedInOrganizer() throws Exception {
    loginInternal("admin@foyt.fi", "pass");
    testTitle("/illusion/event/openevent/materials", "Illusion - Open Event");
    assertSelectorCount(".illusion-event-navigation>a", 2);
    assertSelectorCount(".illusion-event-navigation-item-active", 1);
    assertSelectorPresent(".illusion-event-navigation-admin-menu");
    assertSelectorTextIgnoreCase(".illusion-event-navigation-item-active", "materials");
  }
  
  @Test
  @SqlSets ("illusion-open-materials-hidden")
  public void testHiddenNotLoggedIn() throws Exception {
    testLoginRequired("/illusion/event/openevent/materials");
  }
  
  @Test
  @SqlSets ("illusion-open-materials-hidden")
  public void testHiddenLoggedIn() throws Exception {
    loginInternal("user@foyt.fi", "pass");
    testAccessDenied("/illusion/event/openevent/materials");
  }
  
  @Test
  @SqlSets ({"illusion-open-materials-organizer-custom"})
  public void testCustomDomain() {
    getWebDriver().get(getCustomEventUrl());
    loginCustomEvent("admin@foyt.fi", "pass");
    getWebDriver().get(getCustomEventUrl() + "/materials");
    testTitle("Illusion - Open Event");
  }
  
  @Test
  @SqlSets ({"illusion-open-materials-organizer-custom"})
  public void testCustomDomainLoginRedirect() {
    getWebDriver().get(getCustomEventUrl() + "/materials");
    waitForUrlMatches(".*/login.*");
    loginCustomEvent("admin@foyt.fi", "pass");
    testTitle("Illusion - Open Event");
  }
  
  @Test
  @SqlSets ({"illusion-open-materials-organizer-custom"})
  public void testCustomDomainMenuItems() {
    getWebDriver().get(getCustomEventUrl());
    loginCustomEvent("admin@foyt.fi", "pass");
    getWebDriver().get(getCustomEventUrl() + "/materials");
    testTitle("Illusion - Open Event");

    WebElement logoLink = getWebDriver().findElement(By.cssSelector(".index-menu>a:first-child"));
    WebElement forgeMenuLink = getWebDriver().findElement(By.cssSelector(".index-menu .menu-navigation-container>a:nth-child(1)"));
    WebElement illusionMenuLink = getWebDriver().findElement(By.cssSelector(".index-menu .menu-navigation-container>a:nth-child(2)"));
    WebElement gameLibraryMenuLink = getWebDriver().findElement(By.cssSelector(".index-menu .menu-navigation-container>a:nth-child(3)"));
    WebElement forumMenuLink = getWebDriver().findElement(By.cssSelector(".index-menu .menu-navigation-container>a:nth-child(4)"));

    assertEquals("Forge", forgeMenuLink.getText());
    assertEquals("Illusion", illusionMenuLink.getText());
    assertEquals("Game Library", gameLibraryMenuLink.getText());
    assertEquals("Forum", forumMenuLink.getText());

    assertEquals(getAppUrl() + "/", stripLinkJSessionId(logoLink.getAttribute("href")));
    assertEquals(getAppUrl() + "/forge", stripLinkJSessionId(forgeMenuLink.getAttribute("href")));
    assertEquals(getAppUrl() + "/illusion", stripLinkJSessionId(illusionMenuLink.getAttribute("href")));
    assertEquals(getAppUrl() + "/gamelibrary", stripLinkJSessionId(gameLibraryMenuLink.getAttribute("href")));
    assertEquals(getAppUrl() + "/forum", stripLinkJSessionId(forumMenuLink.getAttribute("href")));
  }
  
  @Test
  @SqlSets ({"illusion-open-materials-organizer-custom"})
  public void testCustomDomainNavigationLinks() {
    getWebDriver().get(getCustomEventUrl());
    loginCustomEvent("admin@foyt.fi", "pass");
    getWebDriver().get(getCustomEventUrl() + "/materials");
    testTitle("Illusion - Open Event");

    assertEquals(getAppUrl(), findElementBySelector(".view-header-navigation .view-header-navigation-item:nth-child(1)").getAttribute("href"));
    assertEquals(getAppUrl() + "/illusion", findElementBySelector(".view-header-navigation .view-header-navigation-item:nth-child(3)").getAttribute("href"));
    assertEquals(getCustomEventUrl(), findElementBySelector(".view-header-navigation .view-header-navigation-item:nth-child(5)").getAttribute("href"));
    assertEquals(getCustomEventUrl() + "/materials", findElementBySelector(".view-header-navigation .view-header-navigation-item:nth-child(7)").getAttribute("href"));
  }
}
