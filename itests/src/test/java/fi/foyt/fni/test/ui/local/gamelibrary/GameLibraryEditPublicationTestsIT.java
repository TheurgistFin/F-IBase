package fi.foyt.fni.test.ui.local.gamelibrary;

import org.junit.Before;
import fi.foyt.fni.test.ui.base.gamelibrary.GameLibraryEditPublicationTestsBase;

public class GameLibraryEditPublicationTestsIT extends GameLibraryEditPublicationTestsBase {

  @Before
  public void setUp() {
    setWebDriver(createLocalDriver());
  }

}