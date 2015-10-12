package fi.foyt.fni.test.ui.base.environment;

import org.junit.Test;

import fi.foyt.fni.test.DefineSqlSet;
import fi.foyt.fni.test.DefineSqlSets;
import fi.foyt.fni.test.SqlSets;
import fi.foyt.fni.test.ui.base.AbstractUITest;

@DefineSqlSets ({
  @DefineSqlSet (id = "basic", 
    before = {"basic-users-setup.sql"},
    after = {"basic-users-teardown.sql"}
  )
})
public class LogoutTestsBase extends AbstractUITest {

  @Test
  @SqlSets ("basic")
  public void testLogout() {
    loginInternal("user@foyt.fi", "pass");
    assertLoggedIn();
    logout();
    assertNotLoggedIn();
  }
}
