package fi.foyt.fni.test.ui.base;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AdminArchiveUserTestsBase extends AbstractUITest {
  
  private static final Long USER_ID = 1024l;
  
  @Before
  public void addTestData() throws Exception {
    createUser(USER_ID, "Test", "Archiving", "test.archiving@foyt.fi", "pass", "en_US", "GRAVATAR", "USER");
  }
  
  @After
  public void cleanTestData() throws Exception {
    deleteUser(USER_ID);
  }
  
  @Test
  public void testLoginRedirect() throws Exception {
    testLoginRequired(getWebDriver(), "/admin/archive-user/" + USER_ID);
  }
  
  @Test
  public void testNotFound() throws Exception {
    loginInternal(getWebDriver(), "admin@foyt.fi", "pass");
    testNotFound(getWebDriver(), "/admin/archive-user");
    testNotFound(getWebDriver(), "/admin/archive-user/");
    testNotFound(getWebDriver(), "/admin/archive-user/");
    testNotFound(getWebDriver(), "/admin/archive-user/-1");
    testNotFound(getWebDriver(), "/admin/archive-user/2048");
  }
  
  @Test
  public void testArchive() throws Exception {
    loginInternal(getWebDriver(), "admin@foyt.fi", "pass");
    getWebDriver().get(getAppUrl() + "/admin/archive-user/" + USER_ID);
    assertEquals("Forge & Illusion", getWebDriver().getTitle());
  }
  
  @Test
  public void testUnauthorized() throws Exception {
    loginInternal(getWebDriver(), "user@foyt.fi", "pass");
    testAccessDenied(getWebDriver(), "/admin/archive-user/" + USER_ID);
  }
  
}
