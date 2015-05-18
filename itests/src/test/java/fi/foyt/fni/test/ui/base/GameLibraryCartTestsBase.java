package fi.foyt.fni.test.ui.base;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import fi.foyt.fni.test.DefineSqlSet;
import fi.foyt.fni.test.DefineSqlSets;
import fi.foyt.fni.test.SqlSets;

@DefineSqlSets({
  @DefineSqlSet (id = "basic-gamelibrary", before = { "basic-users-setup.sql","basic-forum-setup.sql","basic-gamelibrary-setup.sql"}, after={"basic-gamelibrary-teardown.sql", "basic-forum-teardown.sql","basic-users-teardown.sql"}),
})
public class GameLibraryCartTestsBase extends AbstractUITest {

  @Test
  public void testEmptyCart() throws Exception {
    getWebDriver().get(getAppUrl(true) + "/gamelibrary/cart/");
    assertEquals("Shopping Cart is Empty", getWebDriver().findElement(By.cssSelector(".gamelibrary-cart-empty-message")).getText());
    assertEquals("true", getWebDriver().findElement(By.cssSelector(".gamelibrary-cart-submit")).getAttribute("disabled"));
  }

  @Test
  @SqlSets ("basic-gamelibrary")
  public void testSingleItemPurchase() throws Exception {
    String firstName = "Test";
    String lastName = "Orderer";
    String email = "test.orderer@foyt.fi";
    String mobile = "+111 23 567 4444";
    String addressStreet = "Test Orderer Street 18 F22";
    String addressPostalCode = "12345";
    String addressPostalOffice = "Town of Test";
    String notes = "This is an automated test order";

    getWebDriver().get(getAppUrl(true) + "/gamelibrary/");
    getWebDriver().manage().addCookie(new Cookie("cookiesDirective", "1", getHost(), "/", null));
    getWebDriver().get(getAppUrl(true) + "/gamelibrary/testbook_1");

    getWebDriver().findElement(By.cssSelector(".gamelibrary-publication-action-add-to-cart")).click();
    getWebDriver().get(getAppUrl(true) + "/gamelibrary/cart/");
    
    getWebDriver().findElement(By.id("cart-form:payerFirstName")).sendKeys(firstName);
    getWebDriver().findElement(By.id("cart-form:payerLastName")).sendKeys(lastName);
    getWebDriver().findElement(By.id("cart-form:payerEmail")).sendKeys(email);
    getWebDriver().findElement(By.id("cart-form:payerMobile")).sendKeys(mobile);
    getWebDriver().findElement(By.id("cart-form:payerStreetAddress")).sendKeys(addressStreet);
    getWebDriver().findElement(By.id("cart-form:payerPostalCode")).sendKeys(addressPostalCode);
    getWebDriver().findElement(By.id("cart-form:payerPostalOffice")).sendKeys(addressPostalOffice);
    getWebDriver().findElement(By.id("cart-form:notes")).sendKeys(notes);
    getWebDriver().findElement(By.cssSelector(".gamelibrary-cart-submit")).click();

    acceptPaytrailPayment(getWebDriver());

    new WebDriverWait(getWebDriver(), 60).until(ExpectedConditions.titleIs("Forge & Illusion - Game Library"));

    assertEquals("Status: Paid, Waiting for Delivery", getWebDriver().findElement(By.cssSelector(".gamelibrary-order-status")).getText());
    assertEquals(firstName + " " + lastName, getWebDriver().findElement(By.cssSelector(".gamelibrary-order-customer-name")).getText());
    assertEquals(email, getWebDriver().findElement(By.cssSelector(".gamelibrary-order-customer-email")).getText());
    assertEquals(mobile, getWebDriver().findElement(By.cssSelector(".gamelibrary-order-customer-mobile")).getText());

    assertEquals(addressStreet, getWebDriver().findElement(By.cssSelector(".gamelibrary-order-delivery-address-street")).getText());
    assertEquals(addressPostalCode + " " + addressPostalOffice, getWebDriver().findElement(By.cssSelector(".gamelibrary-order-delivery-address-postal-code")).getText());
    assertEquals("Finland", getWebDriver().findElement(By.cssSelector(".gamelibrary-order-delivery-address-country")).getText());

    assertEquals(notes, getWebDriver().findElement(By.cssSelector(".gamelibrary-order-notes p")).getText());

    assertSelectorCount(".gamelibrary-order-item", 1);
    
    assertEquals("1 X FAT HAG DWARVES QUICKLY ZAP JINX MOB", findElementsBySelector(".gamelibrary-order-item div:nth-child(1)").get(0).getText());
    assertEquals("EUR10.00", findElementsBySelector(".gamelibrary-order-item div:nth-child(2)").get(0).getText());
    assertEquals("EUR10.00", findElementsBySelector(".gamelibrary-order-item div:nth-child(3)").get(0).getText());
    assertSelectorTextIgnoreCase(".gamelibrary-order-total div", "EUR10.00");
    assertSelectorTextIgnoreCase(".gamelibrary-order-tax-label label", "TAX (0% - NOT VAT REGISTERED)");
    assertSelectorTextIgnoreCase(".gamelibrary-order-tax-amount div", "EUR0.00");
  }

  @Test
  @SqlSets ("basic-gamelibrary")
  public void testMultiItemPurchase() throws Exception {
    String firstName = "Ärri";
    String lastName = "Pörri";
    String email = "arri.porri@foyt.fi";
    String mobile = "+222 33 444 5555";
    String addressStreet = "Ärri Pörri Katu 18 F22";
    String addressPostalCode = "12345";
    String addressPostalOffice = "Mäkkylä";
    String notes = "Tämä on automaattinen testitilaus";

    getWebDriver().get(getAppUrl(true) + "/gamelibrary/");
    getWebDriver().manage().addCookie(new Cookie("cookiesDirective", "1", getHost(), "/", null));
    getWebDriver().get(getAppUrl(true) + "/gamelibrary/testbook_1");
    getWebDriver().findElement(By.cssSelector(".gamelibrary-publication-action-add-to-cart")).click();
    getWebDriver().get(getAppUrl(true) + "/gamelibrary/pangram_fi");
    getWebDriver().findElement(By.cssSelector(".gamelibrary-publication-action-add-to-cart")).click();
    getWebDriver().get(getAppUrl(true) + "/gamelibrary/cart/");
    
    findElementsBySelector(".gamelibrary-cart-item .gamelibrary-cart-action-inc-count").get(0).click();
    
    new WebDriverWait(getWebDriver(), 60)
      .until(ExpectedConditions.textToBePresentInElement(findElementsBySelector(".gamelibrary-cart-item div:first-child").get(0), "2"));
    
    getWebDriver().findElement(By.id("cart-form:payerFirstName")).sendKeys(firstName);
    getWebDriver().findElement(By.id("cart-form:payerLastName")).sendKeys(lastName);
    getWebDriver().findElement(By.id("cart-form:payerEmail")).sendKeys(email);
    getWebDriver().findElement(By.id("cart-form:payerMobile")).sendKeys(mobile);
    getWebDriver().findElement(By.id("cart-form:payerStreetAddress")).sendKeys(addressStreet);
    getWebDriver().findElement(By.id("cart-form:payerPostalCode")).sendKeys(addressPostalCode);
    getWebDriver().findElement(By.id("cart-form:payerPostalOffice")).sendKeys(addressPostalOffice);
    getWebDriver().findElement(By.id("cart-form:notes")).sendKeys(notes);
    getWebDriver().findElement(By.cssSelector(".gamelibrary-cart-submit")).click();

    acceptPaytrailPayment(getWebDriver());

    new WebDriverWait(getWebDriver(), 60).until(ExpectedConditions.titleIs("Forge & Illusion - Game Library"));

    assertEquals("Status: Paid, Waiting for Delivery", getWebDriver().findElement(By.cssSelector(".gamelibrary-order-status")).getText());
    assertEquals(firstName + " " + lastName, getWebDriver().findElement(By.cssSelector(".gamelibrary-order-customer-name")).getText());
    assertEquals(email, getWebDriver().findElement(By.cssSelector(".gamelibrary-order-customer-email")).getText());
    assertEquals(mobile, getWebDriver().findElement(By.cssSelector(".gamelibrary-order-customer-mobile")).getText());

    assertEquals(addressStreet, getWebDriver().findElement(By.cssSelector(".gamelibrary-order-delivery-address-street")).getText());
    assertEquals(addressPostalCode + " " + addressPostalOffice, getWebDriver().findElement(By.cssSelector(".gamelibrary-order-delivery-address-postal-code")).getText());
    assertEquals("Finland", getWebDriver().findElement(By.cssSelector(".gamelibrary-order-delivery-address-country")).getText());

    assertEquals(notes, getWebDriver().findElement(By.cssSelector(".gamelibrary-order-notes p")).getText());

    assertSelectorCount(".gamelibrary-order-item", 2);
    
    assertEquals("2 X FAT HAG DWARVES QUICKLY ZAP JINX MOB", findElementsBySelector(".gamelibrary-order-item div:nth-child(1)").get(0).getText());
    assertEquals("EUR10.00", findElementsBySelector(".gamelibrary-order-item div:nth-child(2)").get(0).getText());
    assertEquals("EUR20.00", findElementsBySelector(".gamelibrary-order-item div:nth-child(3)").get(0).getText());
    assertEquals("1 X BEOWULF POHTI ZULUJA JA ÅNGSTRÖM-YKSIKKÖÄ KATSELLESSAAN Q-STONE- JA CMX-YHTYEITÄ VIDEOLTA.", findElementsBySelector(".gamelibrary-order-item div:nth-child(1)").get(1).getText());
    assertEquals("EUR7.50", findElementsBySelector(".gamelibrary-order-item div:nth-child(2)").get(1).getText());
    assertEquals("EUR7.50", findElementsBySelector(".gamelibrary-order-item div:nth-child(3)").get(1).getText());

    assertSelectorTextIgnoreCase(".gamelibrary-order-total div", "EUR27.50");
    assertSelectorTextIgnoreCase(".gamelibrary-order-tax-label label", "TAX (0% - NOT VAT REGISTERED)");
    assertSelectorTextIgnoreCase(".gamelibrary-order-tax-amount div", "EUR0.00");
  }

  @Test
  @SqlSets ("basic-gamelibrary")
  public void testCartDelete() throws Exception {
    getWebDriver().get(getAppUrl(true) + "/gamelibrary/");
    getWebDriver().manage().addCookie(new Cookie("cookiesDirective", "1", getHost(), "/", null));
    getWebDriver().get(getAppUrl(true) + "/gamelibrary/testbook_1");
    getWebDriver().findElement(By.cssSelector(".gamelibrary-publication-action-add-to-cart")).click();
    getWebDriver().get(getAppUrl(true) + "/gamelibrary/pangram_fi");
    getWebDriver().findElement(By.cssSelector(".gamelibrary-publication-action-add-to-cart")).click();
    getWebDriver().get(getAppUrl(true) + "/gamelibrary/cart/");
    
    assertSelectorTextIgnoreCase(".gamelibrary-cart-summary-field-total-value", "EUR17.50");
    assertSelectorClickable(".gamelibrary-cart-submit");
    
    findElementsBySelector(".gamelibrary-cart-item .gamelibrary-cart-action-remove").get(0).click();
    assertSelectorTextIgnoreCase(".gamelibrary-cart-summary-field-total-value", "EUR7.50");
    assertSelectorClickable(".gamelibrary-cart-submit");

    findElementsBySelector(".gamelibrary-cart-item .gamelibrary-cart-action-remove").get(0).click();
    assertSelectorTextIgnoreCase(".gamelibrary-cart-summary-field-total-value", "EUR0.00");
    assertSelectorNotClickable(".gamelibrary-cart-submit");
  }

  @Test
  @SqlSets ("basic-gamelibrary")
  public void testCartLoggedIn() throws Exception {
    Long userId = 2048l;
    String firstName = "Cart";
    String lastName = "Tester";
    String email = "cart.tester@foyt.fi";
    String password = "pass";
    String mobile = "+222 33 444 5555";
    String telephone = "+123 45 678 909";
    String addressStreet = "Ärri Pörri Katu 18 F22";
    String addressPostalCode = "12345";
    String addressPostalOffice = "Mäkkylä";

    createUser(userId, firstName, lastName, email, password, "en_US", "GRAVATAR", "USER");
    try {
      try {
        getWebDriver().get(getAppUrl(true) + "/gamelibrary/");
        getWebDriver().manage().addCookie(new Cookie("cookiesDirective", "1", getHost(), "/", null));
        loginInternal(getWebDriver(), email, password);

        getWebDriver().get(getAppUrl(true) + "/gamelibrary/testbook_1");
        getWebDriver().findElement(By.cssSelector(".gamelibrary-publication-action-add-to-cart")).click();

        getWebDriver().get(getAppUrl(true) + "/gamelibrary/cart/");
        assertEquals(firstName, getWebDriver().findElement(By.id("cart-form:payerFirstName")).getAttribute("value"));
        assertEquals(lastName, getWebDriver().findElement(By.id("cart-form:payerLastName")).getAttribute("value"));
        assertEquals(email, getWebDriver().findElement(By.id("cart-form:payerEmail")).getAttribute("value"));

        assertEquals("", getWebDriver().findElement(By.id("cart-form:payerMobile")).getAttribute("value"));
        assertEquals("", getWebDriver().findElement(By.id("cart-form:payerTelephone")).getAttribute("value"));
        assertEquals("", getWebDriver().findElement(By.id("cart-form:payerStreetAddress")).getAttribute("value"));
        assertEquals("", getWebDriver().findElement(By.id("cart-form:payerPostalCode")).getAttribute("value"));
        assertEquals("", getWebDriver().findElement(By.id("cart-form:payerPostalOffice")).getAttribute("value"));

        getWebDriver().findElement(By.id("cart-form:payerMobile")).sendKeys(mobile);
        getWebDriver().findElement(By.id("cart-form:payerTelephone")).sendKeys(telephone);
        getWebDriver().findElement(By.id("cart-form:payerStreetAddress")).sendKeys(addressStreet);
        getWebDriver().findElement(By.id("cart-form:payerPostalCode")).sendKeys(addressPostalCode);
        getWebDriver().findElement(By.id("cart-form:payerPostalOffice")).sendKeys(addressPostalOffice);

        assertEquals(mobile, getWebDriver().findElement(By.id("cart-form:payerMobile")).getAttribute("value"));
        assertEquals(telephone, getWebDriver().findElement(By.id("cart-form:payerTelephone")).getAttribute("value"));
        assertEquals(addressStreet, getWebDriver().findElement(By.id("cart-form:payerStreetAddress")).getAttribute("value"));
        assertEquals(addressPostalCode, getWebDriver().findElement(By.id("cart-form:payerPostalCode")).getAttribute("value"));
        assertEquals(addressPostalOffice, getWebDriver().findElement(By.id("cart-form:payerPostalOffice")).getAttribute("value"));
        getWebDriver().findElement(By.cssSelector(".gamelibrary-cart-submit")).click();

        acceptPaytrailPayment(getWebDriver());
        logout(getWebDriver());
        loginInternal(getWebDriver(), email, password);

        getWebDriver().get(getAppUrl(true) + "/gamelibrary/cart/");
        assertEquals(firstName, getWebDriver().findElement(By.id("cart-form:payerFirstName")).getAttribute("value"));
        assertEquals(lastName, getWebDriver().findElement(By.id("cart-form:payerLastName")).getAttribute("value"));
        assertEquals(email, getWebDriver().findElement(By.id("cart-form:payerEmail")).getAttribute("value"));
        assertEquals(mobile, getWebDriver().findElement(By.id("cart-form:payerMobile")).getAttribute("value"));
        assertEquals(telephone, getWebDriver().findElement(By.id("cart-form:payerTelephone")).getAttribute("value"));
        assertEquals(addressStreet, getWebDriver().findElement(By.id("cart-form:payerStreetAddress")).getAttribute("value"));
        assertEquals(addressPostalCode, getWebDriver().findElement(By.id("cart-form:payerPostalCode")).getAttribute("value"));
        assertEquals(addressPostalOffice, getWebDriver().findElement(By.id("cart-form:payerPostalOffice")).getAttribute("value"));
      } finally {
        executeSql("delete from OrderItem where order_id in (select id from Order_ where customer_id = ?)", userId);
        executeSql("delete from Order_ where customer_id = ?", userId);
        executeSql("delete from ShoppingCartItem where cart_id in (select id from ShoppingCart where customer_id = ?)", userId);
        executeSql("delete from ShoppingCart where customer_id = ?", userId);
      }
    } finally {
      deleteUser(userId);
    }
  }

  private void acceptPaytrailPayment(RemoteWebDriver driver) {
    getWebDriver().findElement(By.cssSelector("form[action=\"https://kultaraha.op.fi/cgi-bin/krcgi\"] input[type=\"submit\"]")).click();

    waitForUrl(getWebDriver(), "https://kultaraha.op.fi/cgi-bin/krcgi");

    getWebDriver().findElement(By.name("id")).sendKeys("123456");
    getWebDriver().findElement(By.name("pw")).sendKeys("7890");
    getWebDriver().findElement(By.name("ktunn")).click();
    getWebDriver().findElement(By.name("avainluku")).sendKeys("1234");
    getWebDriver().findElement(By.name("avainl")).click();
    getWebDriver().findElement(By.id("Toiminto")).click();
  }

}
