package fi.foyt.fni.jsf;

import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import fi.foyt.fni.utils.faces.FacesUtils;

public class PostRedirectMessagesSupport implements PhaseListener {

	private static final long serialVersionUID = -4074833216740931951L;

	@Override
	public void afterPhase(PhaseEvent event) {
		
	}

	@Override
	public void beforePhase(PhaseEvent event) {
		if (PhaseId.RENDER_RESPONSE.equals(event.getPhaseId())) {
			FacesContext facesContext = event.getFacesContext();
			
			if (!facesContext.getResponseComplete()) {

				Map<String, Object> sessionMap = facesContext.getExternalContext().getSessionMap();
				@SuppressWarnings("unchecked")
				List<FacesMessage> messages = (List<FacesMessage>) sessionMap.get(FacesUtils.POST_REDIRECT_SESSION_KEY);
				if (messages != null) {
					for (FacesMessage message : messages) {
						facesContext.addMessage(null, message);
					}
					
					sessionMap.remove(FacesUtils.POST_REDIRECT_SESSION_KEY);
				}
			}
		}
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}

}
