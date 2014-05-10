package fi.foyt.fni.test.ui.base;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Rule;
import org.junit.rules.TestName;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.saucelabs.common.SauceOnDemandSessionIdProvider;

public class AbstractUITest extends fi.foyt.fni.test.ui.AbstractUITest implements SauceOnDemandSessionIdProvider {

  @Rule 
  public TestName testName = new TestName();

  @Override
  public String getSessionId() {
    return sessionId;
  }
  
  protected void setWebDriver(RemoteWebDriver webDriver) {
    this.webDriver = webDriver;
    this.sessionId = webDriver.getSessionId().toString();
  }
  
  protected RemoteWebDriver getWebDriver() {
    return webDriver;
  }
  
  protected RemoteWebDriver createSauceWebDriver(String browser, String version, String platform) throws MalformedURLException {
    DesiredCapabilities capabilities = new DesiredCapabilities();
    capabilities.setCapability(CapabilityType.BROWSER_NAME, browser);
    capabilities.setCapability(CapabilityType.VERSION, version);
    capabilities.setCapability(CapabilityType.PLATFORM, platform);
    capabilities.setCapability("name", getClass().getSimpleName() + ':' + testName.getMethodName());
    return new RemoteWebDriver(new URL(String.format("http://%s:%s@%s:%s/wd/hub", getSauceUsername(), getSauceAccessKey(), getSauceHost(), getSaucePort())), capabilities);
  }
  
  private String sessionId;
  
  private RemoteWebDriver webDriver;
}
