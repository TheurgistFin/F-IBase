package fi.foyt.fni.test.ui.local.environment;

import org.junit.Before;
import fi.foyt.fni.test.ui.base.environment.LogoutTestsBase;

public class LogoutTestsIT extends LogoutTestsBase {

  @Before
  public void setUp() {
    setWebDriver(createLocalDriver());
  }

}