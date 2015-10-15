package fi.foyt.fni.test.ui.sauce.forge;

import java.net.MalformedURLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.junit.SauceOnDemandTestWatcher;

import fi.foyt.fni.test.SqlSets;
import fi.foyt.fni.test.ui.base.forge.ForgeDocumentTestsBase;
import fi.foyt.fni.test.ui.sauce.SauceLabsUtils;

@RunWith (Parameterized.class)
public class ForgeDocumentTestsIT extends ForgeDocumentTestsBase {

  public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication(getSauceUsername(), getSauceAccessKey());

  @Rule
  public SauceOnDemandTestWatcher resultReportingTestWatcher = new SauceOnDemandTestWatcher(this, authentication);

  @Parameterized.Parameters
  public static List<String[]> browsers() throws Exception {
    return SauceLabsUtils.getSauceBrowsers();
  }

  public ForgeDocumentTestsIT(String browser, String version, String platform) {
    this.browser = browser;
    this.version = version;
    this.platform = platform;
  }
  
  @Before
  public void setUp() throws MalformedURLException {
    setWebDriver(createSauceWebDriver(browser, version, platform));
  }
  
  @After
  public void tearDown() {
    getWebDriver().quit();
  }
  
  @Override
  @SqlSets ({"basic-materials-users"})
  public void testMayEdit() {
    if ("microsoftedge".equals(browser)) {
      // FIXME: Edge driver does not support frame switchTo
      return;
    }
    
    super.testMayEdit();
  }
  
  @Override
  @SqlSets ({"basic-materials-users"})
  public void testWithHyphen() {
    if ("microsoftedge".equals(browser)) {
      // FIXME: Edge driver does not support frame switchTo
      return;
    }
    
    super.testWithHyphen();
  }
  
  @Override
  @SqlSets ({"basic-materials-users"})
  public void textCreateSharedFolder() throws Exception {
    if ("microsoftedge".equals(browser)) {
      // FIXME: Edge driver does not support frame switchTo
      return;
    }
    
    super.textCreateSharedFolder();
  }
  
  @Override
  @SqlSets ({"basic-materials-users"})
  public void testMayView() {
    if ("microsoftedge".equals(browser)) {
      // FIXME: Edge driver does not support frame switchTo
      return;
    }
    
    super.testMayView();
  }
  
  private String platform;
  private String browser;
  private String version;  

}