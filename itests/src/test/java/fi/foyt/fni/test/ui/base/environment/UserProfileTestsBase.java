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
  ),
  @DefineSqlSet (id = "event-basic", 
    before = { "illusion-basic-setup.sql" },
    after = { "illusion-basic-teardown.sql" }
  ),
  @DefineSqlSet(id = "event", before = { "illusion-event-setup.sql" }, after = { "illusion-event-teardown.sql"}),  
  @DefineSqlSet (id = "event-organizer", 
    before = {"illusion-event-open-organizer-setup.sql"},
    after = {"illusion-event-open-organizer-teardown.sql"}
  )
})
public class UserProfileTestsBase extends AbstractUITest {

  @Test
  @SqlSets ("basic")
  public void testTitle() {
    testTitle("/profile/1", "User Profile");
  }
  
  @Test
  @SqlSets ({"basic", "event-basic", "event", "event-organizer"})
  public void testWithOrganizedEvents() {
    testTitle("/profile/4", "User Profile");
    assertSelectorCount(".event", 1);
    assertSelectorTextIgnoreCase(".event h4 a", "Open Event");
  }
  
  @Test
  @SqlSets ("basic")
  public void testNotFound() throws Exception {
    testNotFound("/profile/~");
    testNotFound("/profile/12345");
    testNotFound("/profile/-1");
    testNotFound("/profile/");
    testNotFound("/profile/asd");
  }

}
