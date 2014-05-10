package fi.foyt.fni.test.ui.base;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.UUID;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class UsersResetPasswordTestsBase extends AbstractUITest {

  private static final Long USER_ID = 1024l;
  private static final String USER_EMAIL = "reset.test@foyt.fi";

  @Test
  public void testInvalidKeyTest() {
    getWebDriver().get(getAppUrl() + "/users/resetpassword/bogus");

    WebElement form = getWebDriver().findElement(By.cssSelector(".user-resetpassword-panel form"));
    String formId = form.getAttribute("id");
    getWebDriver().findElement(By.name(formId + ":password1")).sendKeys("qwe");
    getWebDriver().findElement(By.name(formId + ":password2")).sendKeys("qwe");
    getWebDriver().findElement(By.cssSelector("input[type='submit']")).click();

    assertEquals("Invalid Password Reset Key. Perhaps You Have Already Used This Reset Link.", getWebDriver().findElement(By.cssSelector(".jsf-messages-container li.error span")).getText());
  }

  @Test
  public void testCorrectKeyTest() throws Exception {
    createUser(USER_ID, "Reset", "Test", USER_EMAIL, "pass", "en_US", "GRAVATAR", "USER");
    try {
      loginInternal(getWebDriver(), USER_EMAIL, "pass");
      logout(getWebDriver());

      String key = UUID.randomUUID().toString();

      executeSql("insert into PasswordResetKey (id, created, value, user_id) values (?, ?, ?, ?)", USER_ID, new Date(), key, USER_ID);
      try {
        getWebDriver().get(getAppUrl() + "/users/resetpassword/" + key);
        WebElement form = getWebDriver().findElement(By.cssSelector(".user-resetpassword-panel form"));
        String formId = form.getAttribute("id");
        getWebDriver().findElement(By.name(formId + ":password1")).sendKeys("qwe");
        getWebDriver().findElement(By.name(formId + ":password2")).sendKeys("qwe");
        getWebDriver().findElement(By.cssSelector("input[type='submit']")).click();

        loginInternal(getWebDriver(), USER_EMAIL, "qwe");
      } finally {
        executeSql("delete from PasswordResetKey where id = ?", USER_ID);
      }

    } finally {
      deleteUser(USER_ID);
    }
  }

}
