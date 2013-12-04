package fi.foyt.fni.auth;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

import fi.foyt.fni.persistence.model.auth.AuthSource;
import fi.foyt.fni.persistence.model.system.SystemSettingKey;
import fi.foyt.fni.persistence.model.users.UserToken;
import fi.foyt.fni.system.SystemSettingsController;
import fi.foyt.fni.utils.auth.OAuthUtils;

@Dependent
public class UbuntuOneAuthenticationStrategy extends OAuthAuthenticationStrategy {

	@Inject
	private SystemSettingsController systemSettingsController;
	
	@Override
	protected String getApiKey() {
		return systemSettingsController.getSetting(SystemSettingKey.UBUNTUONE_APIKEY);
	}

	@Override
	protected String getApiSecret() {
		return systemSettingsController.getSetting(SystemSettingKey.UBUNTUONE_APISECRET);
	}

	@Override
	protected String getCallbackUrl() {
		return systemSettingsController.getSetting(SystemSettingKey.UBUNTUONE_CALLBACKURL);
	}

	@Override
	protected String[] getRequiredScopes() {
		return null;
	}
  
  @Override
  public boolean getSupportLogin() {
    return false;
  }

  protected java.lang.Class<? extends org.scribe.builder.api.Api> getApiClass() {
    return UbuntuOneApi.class;
  };
  
  @Override
  protected String getVerifier(Map<String, String[]> parameters) {
    return getParameter(parameters, "oauth_verifier");
  }
  
  @Override
  protected UserToken handleLogin(Locale locale, OAuthService service, Token accessToken, String[] grantedScopes) throws MultipleEmailAccountsException,
  		EmailDoesNotMatchLoggedUserException, IdentityBelongsToAnotherUserException, ExternalLoginFailedException {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      
      UbuntuOneUserInfo userInfoObject = objectMapper.readValue(OAuthUtils.doGetRequest(service, accessToken, "https://one.ubuntu.com/api/account/").getBody(), UbuntuOneUserInfo.class);
      String firstName = userInfoObject.getFirstName();
      String lastName = userInfoObject.getLastName();
      String email = userInfoObject.getEmail();
      String uid = userInfoObject.getId();
      String nickname = userInfoObject.getNickname();
      return loginUser(AuthSource.UBUNTU_ONE, email, accessToken.getToken(), accessToken.getSecret(), null, uid, null, firstName, lastName, nickname, locale, null);
    } catch (IOException e) {
    	throw new ExternalLoginFailedException();
    }
  }
  
  public static class UbuntuOneApi extends DefaultApi10a {

    @Override
    public String getRequestTokenEndpoint() {
      return "https://one.ubuntu.com/oauth/request/";
    }

    @Override
    public String getAccessTokenEndpoint() {
      return "https://one.ubuntu.com/oauth/access/";
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
      return String.format(AUTHORIZATION_URL, requestToken.getToken(), description);
    }
   
    private static final String AUTHORIZATION_URL = "https://one.ubuntu.com/oauth/authorize/?oauth_token=%s&description=%s";
    private static final String description = "Forge+%26+Illusion";
  }
  
  @SuppressWarnings ("unused")
  @JsonIgnoreProperties (ignoreUnknown = true)
  private static class UbuntuOneUserInfo {
    
    public String getId() {
      return id;
    }
    
    public void setId(String id) {
      this.id = id;
    }
    
    public String getFirstName() {
      return firstName;
    }
    
    public void setFirstName(String firstName) {
      this.firstName = firstName;
    }
    
    public String getLastName() {
      return lastName;
    }
    
    public void setLastName(String lastName) {
      this.lastName = lastName;
    }
    
    public String getEmail() {
      return email;
    }
    
    public void setEmail(String email) {
      this.email = email;
    }
    
    public String getNickname() {
      return nickname;
    }
    
    public void setNickname(String nickname) {
      this.nickname = nickname;
    }
    
    private String id;
    
    @JsonProperty ("first_name")
    private String firstName;
    
    @JsonProperty ("last_name")
    private String lastName;
    
    private String email; 
  
    private String nickname;
  }
}