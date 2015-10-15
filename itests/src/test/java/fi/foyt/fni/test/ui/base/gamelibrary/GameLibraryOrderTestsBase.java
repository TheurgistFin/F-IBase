package fi.foyt.fni.test.ui.base.gamelibrary;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

import fi.foyt.fni.test.DefineSqlSet;
import fi.foyt.fni.test.DefineSqlSets;
import fi.foyt.fni.test.SqlSets;
import fi.foyt.fni.test.ui.base.AbstractUITest;

@DefineSqlSets({
  @DefineSqlSet (id = "basic-gamelibrary", before = { "basic-users-setup.sql","basic-forum-setup.sql","basic-gamelibrary-setup.sql"}, after={"basic-gamelibrary-teardown.sql", "basic-forum-teardown.sql","basic-users-teardown.sql"}),
})
public class GameLibraryOrderTestsBase extends AbstractUITest {

  @Test
  @SqlSets ("basic-gamelibrary")
  public void testAccessKey() throws Exception {
    navigate("/gamelibrary/orders/1?key=bogus-access-key", true);
    waitTitle("Forge & Illusion - Game Library");
    testOrderDetails(getWebDriver());
  }

  @Test
  @SqlSets ("basic-gamelibrary")
  public void testInvalidAccessKey() throws Exception {
    testAccessDenied("/gamelibrary/orders/1?key=invalid-access-key", true);
  }

  @Test
  @SqlSets ("basic-gamelibrary")
  public void testNotFound() throws Exception {
    testNotFound(getWebDriver(), "/gamelibrary/orders/~", true);
    testNotFound(getWebDriver(), "/gamelibrary/orders/-1", true);
    testNotFound(getWebDriver(), "/gamelibrary/orders/", true);
    testNotFound(getWebDriver(), "/gamelibrary/orders/asd", true);
  }

  @Test
  @SqlSets ("basic-gamelibrary")
  public void testAccessDenied() throws Exception {
    loginInternal("guest@foyt.fi", "pass");
    testAccessDenied("/gamelibrary/orders/1", true);
  }

  @Test
  @SqlSets ("basic-gamelibrary")
  public void testUser() throws Exception {
    loginInternal("user@foyt.fi", "pass");
    navigate("/gamelibrary/orders/1", true);
    waitTitle("Forge & Illusion - Game Library");
    testOrderDetails(getWebDriver());
  }

  @Test
  @SqlSets ("basic-gamelibrary")
  public void testLibrarian() throws Exception {
    loginInternal("librarian@foyt.fi", "pass");
    navigate("/gamelibrary/orders/1", true);
    waitTitle("Forge & Illusion - Game Library");
    testOrderDetails(getWebDriver());
  }

  @Test
  @SqlSets ("basic-gamelibrary")
  public void testAdmin() throws Exception {
    loginInternal("admin@foyt.fi", "pass");
    navigate("/gamelibrary/orders/1", true);
    waitTitle("Forge & Illusion - Game Library");
    testOrderDetails(getWebDriver());
  }

  private void testOrderDetails(RemoteWebDriver driver) {
    String firstName = "Bogus";
    String lastName = "Person";
    String company = "Bogus Company";
    String email = "bogus.order@foyt.fi";
    String mobile = "+123-456-789-0123";
    String phone = "+098-765-432-1098";
    String addressPostalOffice = "Bogus City";
    String addressStreet = "12 Bogus Street";
    String addressPostalCode = "12345";
    String notes = "This is a test order";

    assertEquals("Status: Paid, Waiting for Delivery", getWebDriver().findElement(By.cssSelector(".gamelibrary-order-status")).getText());
    assertEquals(company, getWebDriver().findElement(By.cssSelector(".gamelibrary-order-customer-company")).getText());
    assertEquals(firstName + " " + lastName, getWebDriver().findElement(By.cssSelector(".gamelibrary-order-customer-name")).getText());
    assertEquals(email, getWebDriver().findElement(By.cssSelector(".gamelibrary-order-customer-email")).getText());
    assertEquals(mobile, getWebDriver().findElement(By.cssSelector(".gamelibrary-order-customer-mobile")).getText());
    assertEquals(phone, getWebDriver().findElement(By.cssSelector(".gamelibrary-order-customer-phone")).getText());

    assertEquals(addressStreet, getWebDriver().findElement(By.cssSelector(".gamelibrary-order-delivery-address-street")).getText());
    assertEquals(addressPostalCode + " " + addressPostalOffice, getWebDriver().findElement(By.cssSelector(".gamelibrary-order-delivery-address-postal-code")).getText());
    assertEquals("Antarctica", getWebDriver().findElement(By.cssSelector(".gamelibrary-order-delivery-address-country")).getText());

    assertEquals(notes, getWebDriver().findElement(By.cssSelector(".gamelibrary-order-notes p")).getText());

    assertSelectorCount(".gamelibrary-order-item", 1);
    
    assertEquals("10 X TEST BOOK #1", findElementsBySelector(".gamelibrary-order-item div:nth-child(1)").get(0).getText());
    assertEquals("EUR10.00", findElementsBySelector(".gamelibrary-order-item div:nth-child(2)").get(0).getText());
    assertEquals("EUR100.00", findElementsBySelector(".gamelibrary-order-item div:nth-child(3)").get(0).getText());
    assertSelectorTextIgnoreCase(".gamelibrary-order-total div", "EUR100.00");
    assertSelectorTextIgnoreCase(".gamelibrary-order-tax-label label", "TAX (0% - NOT VAT REGISTERED)");
    assertSelectorTextIgnoreCase(".gamelibrary-order-tax-amount div", "EUR0.00");
  }

}
