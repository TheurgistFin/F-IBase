package fi.foyt.fni.test.ui.local.illusion;

import org.junit.Before;
import fi.foyt.fni.test.ui.base.illusion.IllusionEventMaterialsTestsBase;

public class IllusionEventMaterialsTestsIT extends IllusionEventMaterialsTestsBase {

  @Before
  public void setUp() {
    setWebDriver(createLocalDriver());
  }

}