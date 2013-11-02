package fi.foyt.fni;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.apache.commons.lang3.StringUtils;

import fi.foyt.fni.system.ErrorUtils;

@WebFilter(urlPatterns = "*")
public class ErrorMailerFilter implements Filter {
  
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
	    chain.doFilter(request, response);
		} catch (Throwable t) {
      String recipient = System.getProperty("fni-error-email");
      if (StringUtils.isNotBlank(recipient)) {
		    ErrorUtils.mailError(recipient, request, response, t);
      }
		  throw t;
		}
	}
  public void init(FilterConfig fConfig) throws ServletException {
    
	}

	@Override
	public void destroy() {
	}
	
}
