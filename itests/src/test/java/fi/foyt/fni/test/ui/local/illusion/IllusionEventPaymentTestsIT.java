package fi.foyt.fni.test.ui.local.illusion;

import org.junit.Before;

import fi.foyt.fni.test.ui.base.illusion.IllusionEventPaymentTestsBase;

public class IllusionEventPaymentTestsIT extends IllusionEventPaymentTestsBase {

  @Before
  public void setUp() {
    setWebDriver(createLocalDriver());
  }

}