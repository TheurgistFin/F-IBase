package fi.foyt.fni.test.ui.chrome.store;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.chrome.ChromeDriver;

import fi.foyt.fni.test.ui.base.store.StoreListTestsBase;

public class StoreListTestsIT extends StoreListTestsBase {

  @Before
  public void setUp() {
    setWebDriver(new ChromeDriver());
  }

  @After
  public void tearDown() {
    getWebDriver().quit();
  }
   
}