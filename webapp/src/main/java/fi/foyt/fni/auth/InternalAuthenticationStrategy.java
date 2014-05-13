package fi.foyt.fni.auth;

import java.util.Locale;
import java.util.Map;

import javax.enterprise.context.RequestScoped;

import org.apache.commons.codec.digest.DigestUtils;

import fi.foyt.fni.persistence.model.users.UserToken;

@RequestScoped
public class InternalAuthenticationStrategy extends AbstractInternalAuthenticationStrategy {

  @Override
  public boolean getSupportLogin() {
    return true;
  }
  
  @Override
  public UserToken accessToken(Locale locale, Map<String, String[]> parameters) throws MultipleEmailAccountsException,
  		EmailDoesNotMatchLoggedUserException, IdentityBelongsToAnotherUserException, InvalidCredentialsException, UserNotConfirmedException {

  	String username = getParameter(parameters, "username");
    String password = DigestUtils.md5Hex(getParameter(parameters, "password"));
    
    return handleLogin(locale, username, password);
  }
  
}
