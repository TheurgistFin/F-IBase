package fi.foyt.fni.test.ui.local.forge;

import org.junit.Before;
import fi.foyt.fni.test.ui.base.forge.ForgeMaterialListTestsBase;

public class ForgeMaterialListTestsIT extends ForgeMaterialListTestsBase {

  @Before
  public void setUp() {
    setWebDriver(createLocalDriver());
  }

}