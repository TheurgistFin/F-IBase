package fi.foyt.fni.test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

public abstract class AbstractTest {

  private static final long TEST_START_TIME = System.currentTimeMillis();

  @Rule
  public TestName testName = new TestName();

  @Before
  public void sqlSetup() throws Exception {
    Method method = getClass().getMethod(testName.getMethodName(), new Class<?>[] {});
    SqlBefore sqlBefore = method.getAnnotation(SqlBefore.class);
    if (sqlBefore != null) {
      for (String sqlFile : sqlBefore.value()) {
        executeSqlFile(sqlFile);
      }
    }
    
    SqlSets sqlSets = method.getAnnotation(SqlSets.class);
    if (sqlSets != null) {
      for (String sqlSetId : sqlSets.value()) {
        SqlSet sqlSet = getSqlSet(method, sqlSetId);
        if (sqlSet == null) {
          throw new RuntimeException("Could not find sqlset " + sqlSetId);
        }
        
        if (sqlSet.getBefore() != null) {
          for (String sqlFile : sqlSet.getBefore()) {
            executeSqlFile(sqlFile);
          }
        }
      }
    }
  }

  @After
  public void sqlTearDown() throws Exception {
    Method method = getClass().getMethod(testName.getMethodName(), new Class<?>[] {});
    SqlAfter sqlAfter = method.getAnnotation(SqlAfter.class);
    if (sqlAfter != null) {
      for (String sqlFile : sqlAfter.value()) {
        executeSqlFile(sqlFile);
      }
    }
    
    SqlSets sqlSets = method.getAnnotation(SqlSets.class);
    if (sqlSets != null) {
      for (String sqlSetId : sqlSets.value()) {
        SqlSet sqlSet = getSqlSet(method, sqlSetId);
        if (sqlSet == null) {
          throw new RuntimeException("Could not find sqlset " + sqlSetId);
        }
        
        if (sqlSet.getAfter() != null) {
          for (String sqlFile : sqlSet.getAfter()) {
            executeSqlFile(sqlFile);
          }
        }
      }
    }
  }
  
  private SqlSet getSqlSet(Method method, String id) {
    DefineSqlSets defineSqlSets = method.getDeclaringClass().getAnnotation(DefineSqlSets.class);
    if (defineSqlSets != null) {
      for (DefineSqlSet defineSqlSet : defineSqlSets.value()) {
        if (defineSqlSet.id().equals(id)) {
          return new SqlSet(defineSqlSet.before(), defineSqlSet.after());
        }
      }
    }
    
    return null;
  }

  protected String getAppUrl() {
    return getAppUrl(false);
  }

  protected String getAppUrl(boolean secure) {
    return (secure ? "https://" : "http://") + getHost() + ':' + (secure ? getPortHttps() : getPortHttp()) + '/' + getCtxPath();
  }

  protected String getHost() {
    return System.getProperty("it.host");
  }

  protected int getPortHttp() {
    return Integer.parseInt(System.getProperty("it.port.http"));
  }

  protected int getPortHttps() {
    return Integer.parseInt(System.getProperty("it.port.https"));
  }

  protected int getSmtpPort() {
    return Integer.parseInt(System.getProperty("it.smtp.port"));
  }

  public String getProjectVersion() {
    return System.getProperty("it.project.version");
  }

  public static long getTestStartTime() {
    return TEST_START_TIME;
  }

  protected String getCtxPath() {
    return System.getProperty("it.ctx");
  }

  protected String getGoogleApiKey() {
    return System.getProperty("it.google.apiKey");
  }

  protected String getGoogleApiSecret() {
    return System.getProperty("it.google.apiSecret");
  }

  protected String getGoogleUsername() {
    return System.getProperty("it.google.username");
  }

  protected String getGooglePassword() {
    return System.getProperty("it.google.password");
  }

  protected String getFacebookApiKey() {
    return System.getProperty("it.facebook.apiKey");
  }

  protected String getFacebookApiSecret() {
    return System.getProperty("it.facebook.apiSecret");
  }

  protected String getFacebookUsername() {
    return System.getProperty("it.facebook.username");
  }

  protected String getFacebookPassword() {
    return System.getProperty("it.facebook.password");
  }

  protected String getDropboxApiKey() {
    return System.getProperty("it.dropbox.apiKey");
  }

  protected String getDropboxApiSecret() {
    return System.getProperty("it.dropbox.apiSecret");
  }

  protected String getDropboxUsername() {
    return System.getProperty("it.dropbox.username");
  }

  protected String getDropboxPassword() {
    return System.getProperty("it.dropbox.password");
  }

  protected String getSauceUsername() {
    return System.getProperty("it.sauce.username");
  }

  protected String getSauceAccessKey() {
    return System.getProperty("it.sauce.accessKey");
  }

  protected String getSauceHost() {
    return System.getProperty("it.sauce.host");
  }

  protected String getSaucePort() {
    return System.getProperty("it.sauce.port");
  }

  protected Connection getConnection() throws Exception {
    String username = System.getProperty("it.jdbc.username");
    String password = System.getProperty("it.jdbc.password");
    String url = System.getProperty("it.jdbc.url");
    Class.forName(System.getProperty("it.jdbc.driver")).newInstance();

    return DriverManager.getConnection(url, username, password);
  }
  
  protected void executeSqlFile(String file) throws IOException, Exception {
    ClassLoader classLoader = getClass().getClassLoader();
    InputStream sqlStream = classLoader.getResourceAsStream(file);
    try {
      String statements = IOUtils.toString(sqlStream);
      if (StringUtils.isNotBlank(statements)) {
        // Tokenization regex from https://github.com/otavanopisto/muikku/blob/master/muikku-core-plugins/src/test/java/fi/muikku/plugins/workspace/test/ui/SeleniumTestBase.java
        for (String statement : statements.split(";(?=([^\']*\'[^\']*\')*[^\']*$)")) {
          String sql = StringUtils.trim(statement);
          if (StringUtils.isNotBlank(sql)) {
            executeSql(sql);
          }
        }
      }
    } finally {
      sqlStream.close();
    }
  }

  protected void executeSql(String sql, Object... params) throws Exception {
    Connection connection = getConnection();
    try {
      connection.setAutoCommit(true);
      PreparedStatement statement = connection.prepareStatement(sql);
      try {
        for (int i = 0, l = params.length; i < l; i++) {
          statement.setObject(i + 1, params[i]);
        }

        statement.execute();
      } finally {
        statement.close();
      }
    } finally {
      connection.close();
    }
  }

  protected void createUser(Long userId, String firstName, String lastName, String email, String password, String locale, String profileImageSource, String role)
      throws Exception {
    createUser(userId, firstName, lastName, email, password, locale, profileImageSource, role, true);
  }

  protected void createUser(Long userId, String firstName, String lastName, String email, String password, String role) throws Exception {
    createUser(userId, firstName, lastName, email, password, "en", "GRAVATAR", role, true);
  }

  protected void createUser(Long userId, String firstName, String lastName, String email, String password, String locale, String profileImageSource,
      String role, boolean verified) throws Exception {
    executeSql("insert into " + "  User (id, archived, firstName, lastName, locale, profileImageSource, registrationDate, role) " + "values "
        + "  (?, ?, ?, ?, ?, ?, ?, ?)", userId, false, firstName, lastName, locale, profileImageSource, new Date(), role);

    executeSql("insert into " + "  UserEmail (id, email, primaryEmail, user_id) " + "values " + "  (?, ?, ?, ?)", userId, email, true, userId);

    executeSql("insert into " + "  InternalAuth (id, password, verified, user_id) " + "values " + "  (?, ?, ?, ?)", userId, DigestUtils.md5Hex(password),
        verified, userId);
  }

  protected void deleteUser(Long userId) throws Exception {
    executeSql("delete from DropboxFile where id in (select id from Material where creator_id = ?)", userId);
    executeSql("delete from Material where creator_id = ? and type = 'DROPBOX_FILE'", userId);
    executeSql("delete from GoogleDocument where id in (select id from Material where creator_id = ?)", userId);
    executeSql("delete from Material where creator_id = ? and type = 'GOOGLE_DOCUMENT'", userId);
    executeSql("delete from DropboxFolder where id in (select id from Material where creator_id = ?)", userId);
    executeSql("delete from DropboxRootFolder where id in (select id from Material where creator_id = ?)", userId);
    executeSql("delete from Folder where id in (select id from Material where creator_id = ?)", userId);
    executeSql("delete from Material where creator_id = ? and type = 'FOLDER'", userId);
    executeSql("delete from Material where creator_id = ?", userId);
    executeSql("delete from Address where user_id = ?", userId);
    executeSql("delete from UserToken where userIdentifier_id in (select id from UserIdentifier where user_id = ?)", userId);
    executeSql("delete from UserIdentifier where user_id = ?", userId);
    executeSql("delete from InternalAuth where user_id = ?", userId);
    executeSql("delete from UserEmail where user_id = ?", userId);
    executeSql("delete from User where id = ?", userId);
  }

  protected void createOAuthSettings() throws Exception {
    executeSql("insert into SystemSetting (id, settingKey, value) values ((select max(id) + 1 from SystemSetting), ?, ?)", "FACEBOOK_APIKEY",
        getFacebookApiKey());
    executeSql("insert into SystemSetting (id, settingKey, value) values ((select max(id) + 1 from SystemSetting), ?, ?)", "FACEBOOK_APISECRET",
        getFacebookApiSecret());
    executeSql("insert into SystemSetting (id, settingKey, value) values ((select max(id) + 1 from SystemSetting), ?, ?)", "FACEBOOK_CALLBACKURL",
        getAppUrl(true) + "/login/?return=1&loginMethod=FACEBOOK");
    executeSql("insert into SystemSetting (id, settingKey, value) values ((select max(id) + 1 from SystemSetting), ?, ?)", "GOOGLE_APIKEY", getGoogleApiKey());
    executeSql("insert into SystemSetting (id, settingKey, value) values ((select max(id) + 1 from SystemSetting), ?, ?)", "GOOGLE_APISECRET",
        getGoogleApiSecret());
    executeSql("insert into SystemSetting (id, settingKey, value) values ((select max(id) + 1 from SystemSetting), ?, ?)", "GOOGLE_CALLBACKURL",
        getAppUrl(true) + "/login/?return=1&loginMethod=GOOGLE");
    executeSql("insert into SystemSetting (id, settingKey, value) values ((select max(id) + 1 from SystemSetting), ?, ?)", "DROPBOX_ROOT", "sandbox");
    executeSql("insert into SystemSetting (id, settingKey, value) values ((select max(id) + 1 from SystemSetting), ?, ?)", "DROPBOX_APIKEY", getDropboxApiKey());
    executeSql("insert into SystemSetting (id, settingKey, value) values ((select max(id) + 1 from SystemSetting), ?, ?)", "DROPBOX_APISECRET",
        getDropboxApiSecret());
    executeSql("insert into SystemSetting (id, settingKey, value) values ((select max(id) + 1 from SystemSetting), ?, ?)", "DROPBOX_CALLBACKURL",
        getAppUrl(true) + "/login/?return=1&loginMethod=DROPBOX");
  }

  protected void purgeOAuthSettings() throws Exception {
    executeSql("delete from SystemSetting where settingKey in ('FACEBOOK_APIKEY', 'FACEBOOK_APISECRET', 'FACEBOOK_CALLBACKURL', 'GOOGLE_APIKEY', 'GOOGLE_APISECRET', 'GOOGLE_CALLBACKURL', 'DROPBOX_ROOT', 'DROPBOX_APIKEY', 'DROPBOX_APISECRET', 'DROPBOX_CALLBACKURL')");
    deleteOAuthUsers();
  }

  protected void deleteOAuthUsers() throws Exception {
    List<Long> userIds = new ArrayList<>();

    Connection connection = getConnection();
    try {
      connection.setAutoCommit(true);
      PreparedStatement statement = connection.prepareStatement("select user_id from UserIdentifier where authSource in ('GOOGLE', 'FACEBOOK')");
      try {
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
          Long userId = resultSet.getLong(1);
          userIds.add(userId);
        }
      } finally {
        statement.close();
      }
    } finally {
      connection.close();
    }

    for (Long userId : userIds) {
      deleteUser(userId);
    }
  }

  protected GreenMail startSmtpServer() {
    GreenMail greenMail = new GreenMail(new ServerSetup(getSmtpPort(), "localhost", ServerSetup.PROTOCOL_SMTP));
    greenMail.start();
    return greenMail;
  }

  private class SqlSet {

    public SqlSet(String[] before, String[] after) {
      this.before = before;
      this.after = after;
    }
    
    public String[] getBefore() {
      return before;
    }
    
    public String[] getAfter() {
      return after;
    }
    
    private String[] before;
    private String[] after;
  }
}
