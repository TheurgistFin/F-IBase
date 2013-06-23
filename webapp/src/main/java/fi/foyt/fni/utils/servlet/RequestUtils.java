package fi.foyt.fni.utils.servlet;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class RequestUtils {

  public static String requestParamsAsUrlString(HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
    StringBuilder result = new StringBuilder();
    
    Enumeration<String> parameterNames = httpServletRequest.getParameterNames();
    
    while (parameterNames.hasMoreElements()) {
      String parameterName = parameterNames.nextElement();
      String[] parameterValues = httpServletRequest.getParameterValues(parameterName);
      for (int i = 0, l = parameterValues.length; i < l; i++) {
        result.append(URLEncoder.encode(parameterName, "UTF-8"));
        result.append('=');
        result.append(URLEncoder.encode(parameterValues[i], "UTF-8"));
        
        if (i < (parameterValues.length - 1)) {
          result.append('&');
        }
      }
      
      if (parameterNames.hasMoreElements())
        result.append('&');
    }
    
    return result.toString();
  }
  
  public static String stripCtxPath(String contextPath, String uri) {
    if (!StringUtils.isBlank(contextPath) && uri.startsWith(contextPath))
      return uri.substring(contextPath.length());
    return uri;
  }
  
  public static String stripPrecedingSlash(String url) {
  	if (url.startsWith("/")) {
      return url.substring(1);
    }
    
    return url;
  }
  
  public static String stripTrailingSlash(String url) {
    if (url.endsWith("/")) {
      return url.substring(0, url.length() - 1);
    }
    
    return url;
  }
  
  public static String extractToNextSlash(String uri) {
    int nextSlash = uri.indexOf('/');
    if (nextSlash > -1) {
      return uri.substring(0, nextSlash);
    }
    
    return uri;
  }
  
  public static String createUrlName(String text) {
    String urlName = StringUtils.normalizeSpace((text.length() > 20 ? text.substring(0, 20) : text).toLowerCase());
    if (StringUtils.isBlank(urlName))
      return null;
    
    return StringUtils.stripAccents(urlName.replaceAll(" ", "_"));
  }
}
