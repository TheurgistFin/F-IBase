package fi.foyt.fni.test.ui.chrome.forge;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.chrome.ChromeDriver;

import fi.foyt.fni.test.ui.base.forge.ForgeFolderTestsBase;

public class ForgeFolderTestsIT extends ForgeFolderTestsBase {

  @Before
  public void setUp() {
    setWebDriver(new ChromeDriver());
  }

  @After
  public void tearDown() {
    getWebDriver().quit();
  }
   
}