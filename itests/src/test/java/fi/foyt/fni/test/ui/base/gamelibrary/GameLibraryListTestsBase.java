package fi.foyt.fni.test.ui.base.gamelibrary;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import fi.foyt.fni.test.DefineSqlSet;
import fi.foyt.fni.test.DefineSqlSets;
import fi.foyt.fni.test.SqlSets;
import fi.foyt.fni.test.ui.base.AbstractUITest;

@DefineSqlSets({
  @DefineSqlSet (id = "basic-gamelibrary", before = { "basic-users-setup.sql","basic-forum-setup.sql","basic-gamelibrary-setup.sql"}, after={"basic-gamelibrary-teardown.sql", "basic-forum-teardown.sql","basic-users-teardown.sql"}),
})
public class GameLibraryListTestsBase extends AbstractUITest {

  private static final String UML_ID = "3";
  private static final boolean UML_PURCHASABLE = true;
  private static final String UML_LICENSE = "http://creativecommons.org/licenses/by-nc/3.0";
  private static final String UML_PAGES = "15";
  private static final String UML_PRICE = "EUR7.50";
  private static final String UML_DESC = "Beowulf pohti zuluja ja ångström-yksikköä katsellessaan Q-stone- ja CMX-yhtyeitä videolta.";
  private static final String[] UML_TAGS = new String[] { "törkylempijävongahdus" };
  private static final String UML_TITLE = UML_DESC;
  private static final String UML_PATH = "/gamelibrary/pangram_fi";
  private static final String[] UML_AUTHOR_NAMES = new String[] { "Test Librarian" };
  private static final Long[] UML_AUTHOR_IDS = new Long[] { 3l };
  private static final String UML_COMMENT_URL = "immutable/pangram_fi";
  private static final int UML_COMMENTS = 0;

  private static final String BYSARUS_ID = "2";
  private static final String BYSARUS_PATH = "/gamelibrary/with-special.characters";
  private static final boolean BYSARUS_PURCHASABLE = false;
  private static final String BYSARUS_LICENSE = "http://www.example.com/custom/license";
  private static final String BYSARUS_PAGES = "200";
  private static final String BYSARUS_PRICE = null;
  private static final String BYSARUS_DESC = "Эх, чужак, общий съём цен шляп (юфть) – вдрызг";
  private static final String[] BYSARUS_TAGS = new String[] { "test", "with whitespace" };
  private static final String BYSARUS_TITLE = "Эх, чужак, общий съём цен шляп (юфть) – вдрызг";
  private static final String[] BYSARUS_AUTHOR_NAMES = new String[] { "Test Guest", "Test User" };
  private static final Long[] BYSARUS_AUTHOR_IDS = new Long[] { 1l, 2l };
  private static final String BYSARUS_COMMENT_URL = "immutable/with-special.characters";
  private static final int BYSARUS_COMMENTS = 1;

  private static final String SIMPLE_ID = "1";
  private static final String SIMPLE_PATH = "/gamelibrary/testbook_1";
  private static final boolean SIMPLE_PURCHASABLE = true;
  private static final String SIMPLE_LICENSE = "http://creativecommons.org/licenses/by-sa/3.0";
  private static final String SIMPLE_PAGES = "100";
  private static final String SIMPLE_PRICE = "EUR10.00";
  private static final String SIMPLE_DESC = "Fat hag dwarves quickly zap jinx mob";
  private static final String[] SIMPLE_TAGS = new String[] { "test" };
  private static final String SIMPLE_TITLE = SIMPLE_DESC;
  private static final String[] SIMPLE_AUTHOR_NAMES = null;
  private static final Long[] SIMPLE_AUTHOR_IDS = null;
  private static final String SIMPLE_COMMENT_URL = "immutable/testbook_1";
  private static final int SIMPLE_COMMENTS = 2;

  @Test
  @SqlSets ("basic-gamelibrary")
  public void testTagNotFound() {
    testNotFound("/gamelibrary/tags/bogus");
    testNotFound("/gamelibrary/tags/~");
    testNotFound("/gamelibrary/tags/12345");
    testNotFound("/gamelibrary/tags/-1");
    testNotFound("/gamelibrary/tags/");
  }

  @Test
  @SqlSets ("basic-gamelibrary")
  public void testPublicationNotFound() {
    testNotFound("/gamelibrary/bogus");
    testNotFound("/gamelibrary/~");
    testNotFound("/gamelibrary/12345");
    testNotFound("/gamelibrary/-1");
  }

  @Test
  @SqlSets ("basic-gamelibrary")
  public void testMiniCartTexts() {
    navigate("/gamelibrary/");
    assertSelectorText(".mini-shopping-cart-title", "SHOPPING CART", true, true);
    assertEquals(getAppUrl() + "/gamelibrary/cart/", getWebDriver().findElement(By.cssSelector(".mini-shopping-cart-view")).getAttribute("href"));
    assertSelectorText(".mini-shopping-cart-empty","Shopping cart is empty", true, true);
    assertSelectorText(".mini-shopping-cart-summary label","Total", true, true);
    assertSelectorText(".mini-shopping-cart-summary span","EUR0.00", true, true);
  }

  @Test
  @SqlSets ("basic-gamelibrary")
  public void testMostRecentList() {
    navigate("/gamelibrary/");
    
    assertSelectorCount(".gamelibrary-publication", 3);

    testPublicationDetails(".gamelibrary-publication[data-index='0']", UML_ID, UML_PATH, UML_TITLE, UML_TAGS, UML_DESC, UML_PRICE, UML_PAGES,
        UML_AUTHOR_NAMES, UML_AUTHOR_IDS, UML_LICENSE, UML_PURCHASABLE, UML_COMMENT_URL, UML_COMMENTS);

    testPublicationDetails(".gamelibrary-publication[data-index='1']", BYSARUS_ID, BYSARUS_PATH, BYSARUS_TITLE, BYSARUS_TAGS, BYSARUS_DESC, BYSARUS_PRICE,
        BYSARUS_PAGES, BYSARUS_AUTHOR_NAMES, BYSARUS_AUTHOR_IDS, BYSARUS_LICENSE, BYSARUS_PURCHASABLE, BYSARUS_COMMENT_URL, BYSARUS_COMMENTS);

    testPublicationDetails(".gamelibrary-publication[data-index='2']", SIMPLE_ID, SIMPLE_PATH, SIMPLE_TITLE, SIMPLE_TAGS, SIMPLE_DESC, SIMPLE_PRICE,
        SIMPLE_PAGES, SIMPLE_AUTHOR_NAMES, SIMPLE_AUTHOR_IDS, SIMPLE_LICENSE, SIMPLE_PURCHASABLE, SIMPLE_COMMENT_URL, SIMPLE_COMMENTS);
  }
  
  @Test
  @SqlSets ("basic-gamelibrary")
  public void testProposeLinkNotLogged() throws UnsupportedEncodingException {
    navigate("/gamelibrary/");
    waitTitle("Forge & Illusion - Game Library");
    waitForSelectorPresent(".gamelibrary-propose-game-link");
    assertSelectorText(".gamelibrary-propose-game-link", "Login to propose a game to the Library", true, true);
    String redirectUrl = URLEncoder.encode((getCtxPath() != null ? '/' + getCtxPath() : "") + "/gamelibrary/proposegame/", "UTF-8");
    assertEquals(getAppUrl() + "/login/?redirectUrl=" + redirectUrl, getWebDriver().findElement(By.cssSelector(".gamelibrary-propose-game-link")).getAttribute("href"));
  }
  
  @Test
  @SqlSets ("basic-gamelibrary")
  public void testProposeLinkLogged() {
    loginInternal("user@foyt.fi", "pass");
    navigate("/gamelibrary/");
    waitTitle("Forge & Illusion - Game Library");
    waitForSelectorPresent(".gamelibrary-propose-game-link");
    assertSelectorText(".gamelibrary-propose-game-link", "Propose a game to the Library", true, true);
    assertEquals(getAppUrl() + "/gamelibrary/proposegame/", getWebDriver().findElement(By.cssSelector(".gamelibrary-propose-game-link")).getAttribute("href"));
  }

  @Test
  @SqlSets ("basic-gamelibrary")
  public void testTagList() {
    acceptCookieDirective();
    getWebDriver().get(getAppUrl() + "/gamelibrary/tags/test");

    testPublicationDetails(".gamelibrary-publication[data-index='0']", SIMPLE_ID, SIMPLE_PATH, SIMPLE_TITLE, SIMPLE_TAGS, SIMPLE_DESC, SIMPLE_PRICE,
        SIMPLE_PAGES, SIMPLE_AUTHOR_NAMES, SIMPLE_AUTHOR_IDS, SIMPLE_LICENSE, SIMPLE_PURCHASABLE, SIMPLE_COMMENT_URL, SIMPLE_COMMENTS);

    testPublicationDetails(".gamelibrary-publication[data-index='1']", BYSARUS_ID, BYSARUS_PATH, BYSARUS_TITLE, BYSARUS_TAGS, BYSARUS_DESC, BYSARUS_PRICE,
        BYSARUS_PAGES, BYSARUS_AUTHOR_NAMES, BYSARUS_AUTHOR_IDS, BYSARUS_LICENSE, BYSARUS_PURCHASABLE, BYSARUS_COMMENT_URL, BYSARUS_COMMENTS);
  }

  @Test
  @SqlSets ("basic-gamelibrary")
  public void testPublicationDetailsSimple() {
    acceptCookieDirective();
    getWebDriver().get(getAppUrl() + SIMPLE_PATH);
    testPublicationDetails(".gamelibrary-publication", SIMPLE_ID, SIMPLE_PATH, SIMPLE_TITLE, SIMPLE_TAGS, SIMPLE_DESC, SIMPLE_PRICE, SIMPLE_PAGES, SIMPLE_AUTHOR_NAMES,
        SIMPLE_AUTHOR_IDS, SIMPLE_LICENSE, SIMPLE_PURCHASABLE, SIMPLE_COMMENT_URL, SIMPLE_COMMENTS);
  }

  @Test
  @SqlSets ("basic-gamelibrary")
  public void testPublicationDetailsBySaAndRus() {
    acceptCookieDirective();
    getWebDriver().get(getAppUrl() + BYSARUS_PATH);
    testPublicationDetails(".gamelibrary-publication", BYSARUS_ID, BYSARUS_PATH, BYSARUS_TITLE, BYSARUS_TAGS, BYSARUS_DESC, BYSARUS_PRICE, BYSARUS_PAGES, BYSARUS_AUTHOR_NAMES,
        BYSARUS_AUTHOR_IDS, BYSARUS_LICENSE, BYSARUS_PURCHASABLE, BYSARUS_COMMENT_URL, BYSARUS_COMMENTS);
  }

  @Test
  @SqlSets ("basic-gamelibrary")
  public void testPublicationDetailsUmlaut() {
    acceptCookieDirective();
    getWebDriver().get(getAppUrl() + UML_PATH);
    testPublicationDetails(".gamelibrary-publication", UML_ID, UML_PATH, UML_TITLE, UML_TAGS, UML_DESC, UML_PRICE, UML_PAGES, UML_AUTHOR_NAMES, UML_AUTHOR_IDS, UML_LICENSE,
        UML_PURCHASABLE, UML_COMMENT_URL, UML_COMMENTS);
  }

  private void testPublicationDetails(String publicationSelector, String publicationId, String path, String title, String[] tags, String description, String price,
      String numberOfPages, String[] authorNames, Long[] authorIds, String license, boolean purchasable, String commentUrl, int comments) {
//    CreativeCommonsLicense creativeCommonsLicense = CreativeCommonsUtils.parseLicenseUrl(license);

    assertSelectorText(String.format("%s h3 a", publicationSelector), title, true, true);
    assertEquals(getAppUrl() + path, getWebDriver().findElement(By.cssSelector(publicationSelector + " h3 a")).getAttribute("href"));

    for (int i = 0, l = tags.length; i < l; i++) {
      String tag = tags[i];
      assertSelectorText(String.format("%s .gamelibrary-publication-tags a:nth-child(%d)", publicationSelector, i + 1), tag, true, true);
    }
    
    assertSelectorText(String.format("%s .gamelibrary-publication-description", publicationSelector), description, true, true);

    if (price != null) {
      assertSelectorText(String.format("%s .gamelibrary-publication-detail-price span", publicationSelector), price, true, true);
    }

    assertSelectorText(String.format("%s .gamelibrary-publication-detail-number-of-pages span", publicationSelector), numberOfPages, true, true);

    if (authorIds == null || authorNames == null) {
      assertSelectorNotPresent(String.format("%s .gamelibrary-publication-author", publicationSelector));
    } else {
      List<WebElement> authorLinks = getWebDriver().findElements(By.cssSelector(publicationSelector + " .gamelibrary-publication-author"));
      assertEquals(authorIds.length, authorNames.length);
      assertEquals(authorIds.length, authorLinks.size());

      for (int i = 0, l = authorIds.length; i < l; i++) {
        String authorName = authorNames[i];
        Long authorId = authorIds[i];
        WebElement authorLink = authorLinks.get(i);
        assertEquals(authorLink.getText().toUpperCase(), authorName.toUpperCase());
        assertEquals(authorLink.getAttribute("href"), getAppUrl() + "/profile/" + authorId);
      }
    }

    assertEquals(getAppUrl() + "/gamelibrary/publicationFiles/" + publicationId, getWebDriver().findElement(By.cssSelector(publicationSelector + " .gamelibrary-publication-action-download-pdf"))
        .getAttribute("href"));

    if (purchasable) {
      assertSelectorPresent(String.format("%s .gamelibrary-publication-action-add-to-cart", publicationSelector));
    } else {
      assertSelectorNotPresent(String.format("%s .gamelibrary-publication-action-add-to-cart", publicationSelector));
    }

//  FIXME: cc license tests
//    if (creativeCommonsLicense != null) {
//      assertEquals(1, getWebDriver().findElements(By.cssSelector(publicationSelector + " .gamelibrary-publication-creative-commans-license-container")).size());
//      assertEquals(creativeCommonsLicense.getUrl(),
//          getWebDriver().findElement(By.cssSelector(publicationSelector + " .gamelibrary-publication-creative-commans-license-container a")).getAttribute("href"));
//      assertEquals(creativeCommonsLicense.getIconUrl(), getWebDriver().findElement(By.cssSelector(publicationSelector + " .gamelibrary-publication-creative-commans-license-container img"))
//          .getAttribute("src"));
//    } else {
//      assertEquals(license, getWebDriver().findElement(By.cssSelector(publicationSelector + " .gamelibrary-publication-detail-license a")).getAttribute("href"));
//    }

    assertSelectorText(String.format("%s .gamelibrary-publication-comments", publicationSelector), String.format("COMMENTS (%d)", comments), true, true);
    assertEquals(getAppUrl() + "/forum/" + commentUrl, getWebDriver().findElement(By.cssSelector(publicationSelector + " .gamelibrary-publication-comments")).getAttribute("href"));
    
    if (!"microsoftedge".equals(getBrowser())) {
      // FIXME: These tests do not work on ms edge
      assertShareButtonsHidden(publicationSelector);
      scrollWaitAndClick(publicationSelector + " .gamelibrary-publication-share-button label");
      assertShareButtonsVisible(publicationSelector);
      scrollWaitAndClick(publicationSelector + " .gamelibrary-publication-share-button label");
      assertShareButtonsHidden(publicationSelector);
      scrollWaitAndClick(publicationSelector + " .gamelibrary-publication-share-button label");
      assertShareButtonsVisible(publicationSelector);
      scrollWaitAndClick(publicationSelector + " .gamelibrary-publication-detail-number-of-pages");
      assertShareButtonsHidden(publicationSelector);
    }
  }

  private void assertShareButtonsHidden(String publicationSelector) {
    new WebDriverWait(getWebDriver(), 10).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(publicationSelector + " .gamelibrary-publication-share-button .entypo-twitter")));
    assertSelectorNotVisible(String.format("%s .gamelibrary-publication-share-button .entypo-twitter", publicationSelector));
    assertSelectorNotVisible(String.format("%s .gamelibrary-publication-share-button .entypo-facebook", publicationSelector));
    assertSelectorNotVisible(String.format("%s .gamelibrary-publication-share-button .entypo-gplus", publicationSelector));
  }

  private void assertShareButtonsVisible(String publicationSelector) {
    waitForSelectorVisible(String.format("%s .gamelibrary-publication-share-button .entypo-twitter", publicationSelector));
    assertSelectorVisible(String.format("%s .gamelibrary-publication-share-button .entypo-twitter", publicationSelector));
    assertSelectorVisible(String.format("%s .gamelibrary-publication-share-button .entypo-facebook", publicationSelector));
    assertSelectorVisible(String.format("%s .gamelibrary-publication-share-button .entypo-gplus", publicationSelector));
  }

}
