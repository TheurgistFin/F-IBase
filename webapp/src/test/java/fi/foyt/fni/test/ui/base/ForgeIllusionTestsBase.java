package fi.foyt.fni.test.ui.base;

import org.junit.Test;

import fi.foyt.fni.test.DefineSqlSet;
import fi.foyt.fni.test.DefineSqlSets;
import fi.foyt.fni.test.SqlSets;

@DefineSqlSets ({
  @DefineSqlSet (id = "illusion-open-materials", 
    before = {"illusion-basic-setup.sql", "illusion-event-open-setup.sql", "illusion-event-open-participant-setup.sql"}, 
    after = {"illusion-event-open-participant-teardown.sql", "illusion-event-open-teardown.sql", "illusion-basic-teardown.sql"}
  )
})
public class ForgeIllusionTestsBase extends AbstractUITest {

  @Test
  @SqlSets ("illusion-open-materials")
  public void testOpenShareDialog() {
    loginInternal(getWebDriver(), "user@foyt.fi", "pass");
    navigate("/forge/folders/2/illusion/openevent");
    waitForSelectorVisible(".forge-material[data-material-id=\"20001\"] .forge-material-icon");
    clickSelector(".forge-material[data-material-id=\"20001\"] .forge-material-icon");
    waitSelectorToBeClickable(".forge-material[data-material-id=\"20001\"] .forge-material-action-share a");
    clickSelector(".forge-material[data-material-id=\"20001\"] .forge-material-action-share a");
    waitForSelectorVisible(".forge-share-material-dialog");
    assertSelectorPresent(".forge-share-material-dialog");
  }

}
