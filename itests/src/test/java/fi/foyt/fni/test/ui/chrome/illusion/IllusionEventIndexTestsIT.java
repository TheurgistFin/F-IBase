package fi.foyt.fni.test.ui.chrome.illusion;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.chrome.ChromeDriver;

import fi.foyt.fni.test.ui.base.illusion.IllusionEventIndexTestsBase;

public class IllusionEventIndexTestsIT extends IllusionEventIndexTestsBase {

  @Before
  public void setUp() {
    setWebDriver(new ChromeDriver());
  }

  @After
  public void tearDown() {
    getWebDriver().quit();
  }
   
}