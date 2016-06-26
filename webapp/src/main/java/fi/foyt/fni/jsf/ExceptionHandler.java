package fi.foyt.fni.jsf;

import java.io.FileNotFoundException;
import java.util.Iterator;

import javax.ejb.EJBException;
import javax.el.ELException;
import javax.enterprise.inject.CreationException;
import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.servlet.http.HttpServletResponse;

import org.ocpsoft.rewrite.exception.RewriteException;

public class ExceptionHandler extends ExceptionHandlerWrapper {
  
  private final javax.faces.context.ExceptionHandler wrapped;

  public ExceptionHandler(final javax.faces.context.ExceptionHandler wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  public javax.faces.context.ExceptionHandler getWrapped() {
    return this.wrapped;
  }

  @Override
  public void handle() throws FacesException {
    for (final Iterator<ExceptionQueuedEvent> queuedEventIterator = getUnhandledExceptionQueuedEvents().iterator(); queuedEventIterator.hasNext();) {
      ExceptionQueuedEvent queuedEvent = queuedEventIterator.next();
      ExceptionQueuedEventContext queuedEventContext = queuedEvent.getContext();

      Throwable exception = queuedEventContext.getException();
      while (isWrappedException(exception) && exception.getCause() != null) {
        exception = exception.getCause();
      }

      FacesContext facesContext = FacesContext.getCurrentInstance();
      ExternalContext externalContext = facesContext.getExternalContext();

      try {
        if (exception instanceof FileNotFoundException) {
          externalContext.setResponseStatus(HttpServletResponse.SC_NOT_FOUND);
          renderView("/error/not-found.jsf");
        } else {
          externalContext.setResponseStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
          renderView("/error/internal-error.jsf");
        }
      } finally {
        queuedEventIterator.remove();
      }
    }

    getWrapped().handle();
  }

  private boolean isWrappedException(Throwable exception) {
    return 
       exception instanceof FacesException || 
       exception instanceof EJBException || 
       exception instanceof ELException || 
       exception instanceof CreationException || 
       exception instanceof IllegalStateException ||
       exception instanceof RewriteException;
  }

  private void renderView(String viewId) {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    NavigationHandler navigationHandler = facesContext.getApplication().getNavigationHandler();
    navigationHandler.handleNavigation(facesContext, null, viewId);
    facesContext.renderResponse();
  }
}