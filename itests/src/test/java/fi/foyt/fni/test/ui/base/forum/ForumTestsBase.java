package fi.foyt.fni.test.ui.base.forum;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openqa.selenium.By;

import fi.foyt.fni.test.DefineSqlSet;
import fi.foyt.fni.test.DefineSqlSets;
import fi.foyt.fni.test.SqlSets;
import fi.foyt.fni.test.ui.base.AbstractUITest;

@DefineSqlSets({
  @DefineSqlSet (id = "basic-users", before = { "basic-users-setup.sql" }, after = { "basic-users-teardown.sql"  }),
  @DefineSqlSet (id = "forum-basic", before = { "basic-forum-setup.sql" }, after = { "basic-forum-teardown.sql" }),
  @DefineSqlSet (id = "illusion-basic", before = "illusion-basic-setup.sql", after = "illusion-basic-teardown.sql"),
  @DefineSqlSet (id = "event", before = { "illusion-event-open-setup.sql" }, after = { "illusion-event-open-teardown.sql" }),
  @DefineSqlSet (id = "event-forum", before = { "illusion-event-open-forum-setup.sql" }, after = {"illusion-event-open-forum-teardown.sql"}),
  @DefineSqlSet (id = "forum-with-special-characters", before = { "forum-with-special-characters-setup.sql"}, after={"forum-with-special-characters-teardown.sql" })
})
public class ForumTestsBase extends AbstractUITest {

  @Test
  @SqlSets ({"basic-users", "forum-basic"})
  public void testTexts() {
    getWebDriver().get(getAppUrl() + "/forum/1_topic_forum");
    assertEquals("SINGLE TOPIC FORUM", getWebDriver().findElement(By.cssSelector(".view-header-description-title")).getText());
  }

  @Test
  @SqlSets ({"basic-users", "forum-basic"})
  public void testAnonymous() {
    navigate("/forum/1_topic_forum");
    assertSelectorVisible(".forum-new-topic-login-link");
    assertSelectorTextIgnoreCase(".forum-new-topic-login-link", "LOGIN TO CREATE NEW TOPIC");
  }

  @Test
  @SqlSets ({"basic-users", "forum-basic"})
  public void testLoggedIn() {
    loginInternal("user@foyt.fi", "pass");
    navigate("/forum/1_topic_forum");
    assertSelectorVisible(".forum-view-new-topic-link");
  }

  @Test
  @SqlSets ({"basic-users", "forum-basic"})
  public void testNotFound() throws Exception {
    testNotFound(getWebDriver(), "/forum/qwe");
    testNotFound(getWebDriver(), "/forum/*");
  }

  @Test
  @SqlSets ({"basic-users", "forum-basic", "forum-with-special-characters"})
  public void testSpecialCharacter() throws Exception {
    navigate("/forum/with-special.characters");
    assertTitle("Forum");
  }

  @Test
  @SqlSets ({"basic-users", "forum-basic", "illusion-basic", "event", "event-forum"})
  public void testInvisible() throws Exception {
    testNotFound("/forum/illusion");
  }

  @Test
  @SqlSets ({"basic-users", "forum-basic"})
  public void testForumLink() {
    navigate("/forum/1_topic_forum");
    assertSelectorTextIgnoreCase("h3 a", "Single topic Forum");
    assertEquals(String.format("%s/forum/1_topic_forum", getAppUrl()), findElementBySelector("h3 a").getAttribute("href"));
  }
  
  @Test
  @SqlSets ({"basic-users", "forum-basic"})
  public void testTopicLink() {
    navigate("/forum/1_topic_forum");
    assertSelectorTextIgnoreCase("*[data-topic-index=\"0\"] h5 a", "Topic of single topic forum");
    assertEquals(String.format("%s/forum/1_topic_forum/single_topic", getAppUrl()), findElementBySelector("*[data-topic-index=\"0\"] h5 a").getAttribute("href"));
  }
  
  @Test
  @SqlSets ({"basic-users", "forum-basic"})
  public void testAuthorLink() {
    navigate("/forum/");
    assertSelectorTextIgnoreCase("*[data-topic-index=\"0\"] .topic-start-info a", "Test Guest");
    assertEquals(String.format("%s/profile/1", getAppUrl()), findElementBySelector("*[data-topic-index=\"0\"] .topic-start-info a").getAttribute("href"));
  }
}