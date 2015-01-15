package fi.foyt.fni.test.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.joda.time.base.AbstractInstant;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Before;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.ObjectMapperConfig;
import com.jayway.restassured.config.RestAssuredConfig;
import com.jayway.restassured.mapper.factory.Jackson2ObjectMapperFactory;
import com.jayway.restassured.specification.RequestSpecification;

import fi.foyt.fni.test.AbstractTest;

public class AbstractRestTest extends AbstractTest {

  @Before
  public void setupRestAssured() {
    RestAssured.baseURI = getAppUrl() + "/rest";
    RestAssured.port = getPortHttps();

    RestAssured.config = RestAssuredConfig.config().objectMapperConfig(
      ObjectMapperConfig.objectMapperConfig().jackson2ObjectMapperFactory(new Jackson2ObjectMapperFactory() {
        @Override
        public ObjectMapper create(@SuppressWarnings("rawtypes") Class cls, String charset) {
          ObjectMapper objectMapper = new ObjectMapper();
          objectMapper.registerModule(new JodaModule());
          objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
          return objectMapper;
        }
      })
    );
  }

  protected RequestSpecification givenPlain(String accessToken) {
    return given().header("Authorization", "Bearer " + accessToken);
  }

  protected RequestSpecification givenJson() {
    return given().contentType("application/json");
  }

  protected RequestSpecification givenJson(String accessToken) {
    return given().header("Authorization", "Bearer " + accessToken).contentType("application/json");
  }

  protected String createServiceToken() throws OAuthSystemException, OAuthProblemException {
    return createServiceToken("client-id", "client-secret", getAppUrl(true) + "/fake-redirect");
  }

  protected String createServiceToken(String clientId, String clientSecet, String redirectURI) throws OAuthSystemException, OAuthProblemException {
    String tokenEndpoint = getAppUrl(true) + "/oauth2/token";

    OAuthClientRequest request = OAuthClientRequest.tokenLocation(tokenEndpoint).setGrantType(GrantType.CLIENT_CREDENTIALS).setClientId(clientId)
        .setClientSecret(clientSecet).setRedirectURI(redirectURI).buildQueryMessage();

    OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

    OAuthJSONAccessTokenResponse response = oAuthClient.accessToken(request);

    assertNotNull(response);
    assertNotNull(response.getAccessToken());

    return response.getAccessToken();
  }

  protected static Matcher<AbstractInstant> sameInstant(final AbstractInstant instant) {
    return new BaseMatcher<AbstractInstant>(){

      @Override
      public void describeTo(Description description) {
        description.appendText("same instant: ").appendValue(instant.toString());
      }

      @Override
      public boolean matches(Object item) {
        if (item == null && instant == null) {
          return true;
        }
        
        if (item instanceof String) {
          item = ISODateTimeFormat.dateTimeParser().parseDateTime((String) item);
        }
        
        if (!(item instanceof AbstractInstant)) {
          return false;
        }
        
        if (item == null || instant == null) {
          return false;
        }
        
        return ((AbstractInstant) item).getMillis() == instant.getMillis();
      }
      
    };
  }
}
