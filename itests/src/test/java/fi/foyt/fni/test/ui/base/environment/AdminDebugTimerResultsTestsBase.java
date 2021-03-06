package fi.foyt.fni.test.ui.base.environment;

import org.junit.Test;

import fi.foyt.fni.test.DefineSqlSet;
import fi.foyt.fni.test.DefineSqlSets;
import fi.foyt.fni.test.SqlSets;
import fi.foyt.fni.test.ui.base.AbstractUITest;

@DefineSqlSets ({
  @DefineSqlSet (id = "basic-users", before = "basic-users-setup.sql", after = "basic-users-teardown.sql")
})
public class AdminDebugTimerResultsTestsBase extends AbstractUITest {

  @Test
  @SqlSets ({"basic-users"})
  public void testTitle() throws Exception {
    loginInternal("admin@foyt.fi", "pass");
    testTitle("/admin/debug-timer-results", "Debug Timer Results");
  }

  @Test
  @SqlSets ({"basic-users"})
  public void testLoginRedirect() throws Exception {
    testLoginRequired("/admin/debug-timer-results");
  }

  @Test
  @SqlSets ({"basic-users"})
  public void testUnauthorized() throws Exception {
    loginInternal("user@foyt.fi", "pass");
    testAccessDenied("/admin/debug-timer-results");
  }

}
