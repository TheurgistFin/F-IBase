package fi.foyt.fni.test.ui.local.environment;

import org.junit.Before;
import fi.foyt.fni.test.ui.base.environment.UserProfileTestsBase;

public class UserProfileTestsIT extends UserProfileTestsBase {

  @Before
  public void setUp() {
    setWebDriver(createLocalDriver());
  }

}