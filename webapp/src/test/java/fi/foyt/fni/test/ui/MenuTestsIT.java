package fi.foyt.fni.test.ui;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class MenuTestsIT extends AbstractUITest {
 

  @Test
  public void testItems() {
    ChromeDriver driver = new ChromeDriver();
    try {
      driver.get(getAppUrl() + "/");

      WebElement logoLink = driver.findElement(By.cssSelector(".index-menu>a:first-child"));
      WebElement forgeMenuLink = driver.findElement(By.cssSelector(".index-menu .menu-navigation-container>a:nth-child(1)"));
      WebElement illusionMenuLink = driver.findElement(By.cssSelector(".index-menu .menu-navigation-container>a:nth-child(2)"));
      WebElement gameLibraryMenuLink = driver.findElement(By.cssSelector(".index-menu .menu-navigation-container>a:nth-child(3)"));
      WebElement forumMenuLink = driver.findElement(By.cssSelector(".index-menu .menu-navigation-container>a:nth-child(4)"));

      assertEquals("Forge", forgeMenuLink.getText());
      assertEquals("Illusion", illusionMenuLink.getText());
      assertEquals("Game Library", gameLibraryMenuLink.getText());
      assertEquals("Forum", forumMenuLink.getText());

      assertEquals(getAppUrl() + "/", stripLinkJSessionId(logoLink.getAttribute("href")));
      assertEquals(getAppUrl() + "/forge", stripLinkJSessionId(forgeMenuLink.getAttribute("href")));
      assertEquals(getAppUrl() + "/illusion/", stripLinkJSessionId(illusionMenuLink.getAttribute("href")));
      assertEquals(getAppUrl() + "/gamelibrary", stripLinkJSessionId(gameLibraryMenuLink.getAttribute("href")));
      assertEquals(getAppUrl() + "/forum/", stripLinkJSessionId(forumMenuLink.getAttribute("href")));
    } finally {
      driver.close();
    }
  }

  @Test
  public void testAbout() throws Exception {
    ChromeDriver driver = new ChromeDriver();
    try {
      driver.get(getAppUrl());
      
      WebElement aboutMenuLink = driver.findElement(By.cssSelector(".index-menu .menu-navigation-container>div.menu-about-container>a"));
      WebElement aboutMenuList = driver.findElement(By.cssSelector(".menu-about-list"));

      assertEquals("About", aboutMenuLink.getText());
      
      // Menu list should be hidden by default
      assertEquals(false, aboutMenuList.isDisplayed());
      
      // Check about menu
      aboutMenuLink.click();

      assertEquals(true, aboutMenuList.isDisplayed());
      
      WebElement aboutMenuVision = driver.findElement(By.cssSelector(".menu-about-list>ul:nth-child(1)>a"));
      WebElement aboutMenuInformation = driver.findElement(By.cssSelector(".menu-about-list>ul:nth-child(2)>a"));
      WebElement aboutMenuForum = driver.findElement(By.cssSelector(".menu-about-list>ul:nth-child(3)>a"));
      WebElement aboutMenuDistribution = driver.findElement(By.cssSelector(".menu-about-list>ul:nth-child(4)>a"));
      WebElement aboutMenuGameplay = driver.findElement(By.cssSelector(".menu-about-list>ul:nth-child(5)>a"));
      WebElement aboutMenuHistory = driver.findElement(By.cssSelector(".menu-about-list>ul:nth-child(6)>a"));
      WebElement aboutMenuCookies = driver.findElement(By.cssSelector(".menu-about-list>ul:nth-child(7)>a"));
      WebElement aboutMenuOpenSource = driver.findElement(By.cssSelector(".menu-about-list>ul:nth-child(8)>a"));
      WebElement aboutMenuContact = driver.findElement(By.cssSelector(".menu-about-list>ul:nth-child(9)>a"));
      WebElement aboutMenuAcknowledgements = driver.findElement(By.cssSelector(".menu-about-list>ul:nth-child(10)>a"));
    
      assertEquals("Our Vision", aboutMenuVision.getText());
      assertEquals(getAppUrl() + "/about#vision", stripLinkJSessionId(aboutMenuVision.getAttribute("href")));
      
      assertEquals("Information", aboutMenuInformation.getText());
      assertEquals(getAppUrl() + "/about#information", stripLinkJSessionId(aboutMenuInformation.getAttribute("href")));
      
      assertEquals("Community participation and forum", aboutMenuForum.getText());
      assertEquals(getAppUrl() + "/about#forum", stripLinkJSessionId(aboutMenuForum.getAttribute("href")));
      
      assertEquals("Distribution", aboutMenuDistribution.getText());
      assertEquals(getAppUrl() + "/about#distribution", stripLinkJSessionId(aboutMenuDistribution.getAttribute("href")));
      
      assertEquals("Gameplay", aboutMenuGameplay.getText());
      assertEquals(getAppUrl() + "/about#gameplay", stripLinkJSessionId(aboutMenuGameplay.getAttribute("href")));
      
      assertEquals("History", aboutMenuHistory.getText());
      assertEquals(getAppUrl() + "/about#history", stripLinkJSessionId(aboutMenuHistory.getAttribute("href")));
      
      assertEquals("Use of cookies", aboutMenuCookies.getText());
      assertEquals(getAppUrl() + "/about#cookies", stripLinkJSessionId(aboutMenuCookies.getAttribute("href")));
      
      assertEquals("Open Source", aboutMenuOpenSource.getText());
      assertEquals(getAppUrl() + "/about#opensource", stripLinkJSessionId(aboutMenuOpenSource.getAttribute("href")));
      
      assertEquals("Contacting us", aboutMenuContact.getText());
      assertEquals(getAppUrl() + "/about#contact", stripLinkJSessionId(aboutMenuContact.getAttribute("href")));
      
      assertEquals("Acknowledgements", aboutMenuAcknowledgements.getText());
      assertEquals(getAppUrl() + "/about#acknowledgements", stripLinkJSessionId(aboutMenuAcknowledgements.getAttribute("href")));

      // Click somewhere else and the menu list should disappear
      driver.findElement(By.cssSelector(".index-banner")).click();
      assertEquals(false, aboutMenuList.isDisplayed());

      // Click link again and the menu list should reappear
      aboutMenuLink.click();
      assertEquals(true, aboutMenuList.isDisplayed());
      
      // ... and stay visible after another click
      aboutMenuLink.click();
      assertEquals(true, aboutMenuList.isDisplayed());
    } finally {
      driver.close();
    }
  }
  
  @Test
  public void testLocaleMenu() {
    ChromeDriver driver = new ChromeDriver();
    try {
      driver.get(getAppUrl());
      
      WebElement localeMenuLink = driver.findElement(By.cssSelector(".index-menu .menu-tools-container .menu-tools-locale"));
      WebElement localeMenuList = driver.findElement(By.cssSelector(".menu-tools-locale-list"));
  
      assertEquals("EN", localeMenuLink.getText());
      
      // Menu list should be hidden by default
      assertEquals(false, localeMenuList.isDisplayed());
      
      // Click menu should make the list appear
      localeMenuLink.click();
      assertEquals(true, localeMenuList.isDisplayed());
      
      WebElement fiItem = driver.findElement(By.cssSelector(".menu-tools-locale-list>ul:nth-child(1)>a"));
      WebElement enItem = driver.findElement(By.cssSelector(".menu-tools-locale-list>ul:nth-child(2)>a"));
      
      assertEquals("Suomi", fiItem.getText());
      assertEquals("English", enItem.getText());
  
      // Click somewhere else and the menu list should disappear
      driver.findElement(By.cssSelector(".index-banner")).click();
      assertEquals(false, localeMenuList.isDisplayed());
  
      // Click link again and the menu list should reappear
      localeMenuLink.click();
      assertEquals(true, localeMenuList.isDisplayed());
      
      // ... and stay visible after another click
      localeMenuLink.click();
      assertEquals(true, localeMenuList.isDisplayed());
    } finally {
      driver.close();
    }
  }
  
}
