package fi.foyt.fni.test.ui.base;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ForgeUploadTestsBase extends AbstractUITest {

  @Test
  public void testTitle() {
    loginInternal(getWebDriver(), "user@foyt.fi", "pass");
    testTitle(getWebDriver(), "/forge/upload", "Forge - Import From My Computer");
  }

  @Test
  public void testLoginRedirect() throws Exception {
    testLoginRequired(getWebDriver(), "/forge/upload");
  }

  @Test
  public void testNotFound() throws Exception {
    loginInternal(getWebDriver(), "user@foyt.fi", "pass");
    testNotFound(getWebDriver(), "/forge/upload?parentFolderId=12345");
  }

  @Test
  public void testBreadcrumbs() {
    loginInternal(getWebDriver(), "user@foyt.fi", "pass");
    
    getWebDriver().get(getAppUrl() + "/forge/upload");
    List<WebElement> breadcrumps = getWebDriver().findElements(By.cssSelector(".view-header-navigation .view-header-navigation-item a"));
    assertEquals(2, breadcrumps.size());
    assertEquals(getAppUrl() + "/", breadcrumps.get(0).getAttribute("href"));
    assertEquals(getAppUrl() + "/forge/", breadcrumps.get(1).getAttribute("href"));
    
    getWebDriver().get(getAppUrl() + "/forge/upload?parentFolderId=1");
    breadcrumps = getWebDriver().findElements(By.cssSelector(".view-header-navigation .view-header-navigation-item a"));
    assertEquals(3, breadcrumps.size());
    assertEquals(getAppUrl() + "/", breadcrumps.get(0).getAttribute("href"));
    assertEquals(getAppUrl() + "/forge/", breadcrumps.get(1).getAttribute("href"));
    assertEquals(getAppUrl() + "/forge/folders/2/folder", breadcrumps.get(2).getAttribute("href"));
    
    getWebDriver().get(getAppUrl() + "/forge/upload?parentFolderId=2");
    breadcrumps = getWebDriver().findElements(By.cssSelector(".view-header-navigation .view-header-navigation-item a"));
    assertEquals(4, breadcrumps.size());
    assertEquals(getAppUrl() + "/", breadcrumps.get(0).getAttribute("href"));
    assertEquals(getAppUrl() + "/forge/", breadcrumps.get(1).getAttribute("href"));
    assertEquals(getAppUrl() + "/forge/folders/2/folder", breadcrumps.get(2).getAttribute("href"));
    assertEquals(getAppUrl() + "/forge/folders/2/folder/subfolder", breadcrumps.get(3).getAttribute("href"));
  }
}
