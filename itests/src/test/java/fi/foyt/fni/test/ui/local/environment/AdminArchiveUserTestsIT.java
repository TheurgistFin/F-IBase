package fi.foyt.fni.test.ui.local.environment;

import org.junit.Before;
import fi.foyt.fni.test.ui.base.environment.AdminArchiveUserTestsBase;

public class AdminArchiveUserTestsIT extends AdminArchiveUserTestsBase {
  
  @Before
  public void setUp() {
    setWebDriver(createLocalDriver());
  }
  
}
