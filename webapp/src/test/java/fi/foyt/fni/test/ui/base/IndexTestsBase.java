package fi.foyt.fni.test.ui.base;

import static org.junit.Assert.assertEquals;

import java.net.URLEncoder;
import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class IndexTestsBase extends AbstractUITest {

  @Test
  public void testTitle() throws Exception {
    getWebDriver().get(getAppUrl() + "/");
    assertEquals("Forge & Illusion", getWebDriver().getTitle());
  }

  @Test
  public void testUnicodeGarbage() {
    testNotFound(getWebDriver(), "/å®‰è£…è¯´æ˜Ž.txt");
  }

  @Test
  public void testTexts() {
    getWebDriver().get(getAppUrl() + "/");

    assertEquals("Forge & Illusion is an open platform built for roleplaying and roleplayers.", getWebDriver().findElement(By.cssSelector("p.index-description-text")).getText());
    assertEquals("LATEST GAME LIBRARY PUBLICATIONS", getWebDriver().findElement(By.cssSelector(".index-publications-panel>h3>a")).getText());
    assertEquals("LATEST FORUM TOPICS", getWebDriver().findElement(By.cssSelector(".index-forum-panel>h3>a")).getText());
    assertEquals("NEWS", getWebDriver().findElement(By.cssSelector(".index-blog-panel>h3>a")).getText());

    assertEquals("More >>", getWebDriver().findElement(By.cssSelector(".index-gamelibrary-more")).getText());
    assertEquals("More >>", getWebDriver().findElement(By.cssSelector("a.index-forum-more")).getText());
    assertEquals("More >>", getWebDriver().findElement(By.cssSelector("a.index-blog-more")).getText());
  }

  @Test
  public void testPublicationTags() throws Exception {
    getWebDriver().get(getAppUrl());
    List<WebElement> tagLinks = getWebDriver().findElements(By.cssSelector(".index-publication-tag a"));
    for (WebElement tagLink : tagLinks) {
      String tag = tagLink.getText().toLowerCase();
      assertEquals(getAppUrl() + "/gamelibrary/tags/" + URLEncoder.encode(tag, "UTF-8").replaceAll("\\+", "%20"), tagLink.getAttribute("href"));
    }
  }
}