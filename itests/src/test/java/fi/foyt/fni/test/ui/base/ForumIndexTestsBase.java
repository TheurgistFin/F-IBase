package fi.foyt.fni.test.ui.base;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.Test;

import fi.foyt.fni.test.DefineSqlSet;
import fi.foyt.fni.test.DefineSqlSets;
import fi.foyt.fni.test.SqlSets;

@DefineSqlSets({
  @DefineSqlSet (id = "basic-users", before = { "basic-users-setup.sql" }, after = { "basic-users-teardown.sql"  }),
  @DefineSqlSet (id = "forum-basic", before = { "basic-forum-setup.sql" }, after = { "basic-forum-teardown.sql" }),
  @DefineSqlSet (id = "illusion-basic", before = "illusion-basic-setup.sql", after = "illusion-basic-teardown.sql"),
  @DefineSqlSet (id = "event", before = { "illusion-event-open-setup.sql" }, after = { "illusion-event-open-teardown.sql" }),
  @DefineSqlSet (id = "event-forum", before = { "illusion-event-open-forum-setup.sql" }, after = {"illusion-event-open-forum-teardown.sql"}),
})
public class ForumIndexTestsBase extends AbstractUITest {

  @Test
  @SqlSets ({"basic-users", "forum-basic"})
  public void testTitle() {
    testTitle("/forum/", "Forum");
  }
  
  @Test
  @SqlSets ({"basic-users", "forum-basic", "illusion-basic", "event", "event-forum"})
  public void testVisibleCategories() throws SQLException, Exception {
    navigate("/forum/");
    assertSelectorCount(".forum", 4);
    assertEquals(5, countForums().intValue());
  }
  
  @Test
  @SqlSets ({"basic-users", "forum-basic"})
  public void testForumLink() {
    navigate("/forum/");
    assertSelectorTextIgnoreCase("*[data-forum-index=\"2\"] h3 a", "Five topic Forum");
    assertEquals(String.format("%s/forum/5_topic_forum", getAppUrl()), findElementBySelector("*[data-forum-index=\"2\"] h3 a").getAttribute("href"));
  }

  @Test
  @SqlSets ({"basic-users", "forum-basic"})
  public void testForumMoreLink() {
    navigate("/forum/");
    assertSelectorTextIgnoreCase("*[data-forum-index=\"2\"] .more-link", "More >>");
    assertEquals(String.format("%s/forum/5_topic_forum", getAppUrl()), findElementBySelector("*[data-forum-index=\"2\"] .more-link").getAttribute("href"));
  }
  
  @Test
  @SqlSets ({"basic-users", "forum-basic"})
  public void testTopicLink() {
    navigate("/forum/");
    assertSelectorTextIgnoreCase("*[data-forum-index=\"2\"] *[data-topic-index=\"0\"] h5 a", "Topic 5 of 5 topic forum");
    assertEquals(String.format("%s/forum/5_topic_forum/topic5of5", getAppUrl()), findElementBySelector("*[data-forum-index=\"2\"] *[data-topic-index=\"0\"] h5 a").getAttribute("href"));
  }
  
  @Test
  @SqlSets ({"basic-users", "forum-basic"})
  public void testAuthorLink() {
    navigate("/forum/");
    assertSelectorTextIgnoreCase("*[data-forum-index=\"2\"] *[data-topic-index=\"0\"] .topic-start-info a", "Test Guest");
    assertEquals(String.format("%s/profile/1", getAppUrl()), findElementBySelector("*[data-forum-index=\"2\"] *[data-topic-index=\"0\"] .topic-start-info a").getAttribute("href"));
  }

}
