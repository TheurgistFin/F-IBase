package fi.foyt.fni.view.illusion;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.annotation.Parameter;
import org.ocpsoft.rewrite.annotation.RequestAction;
import org.ocpsoft.rewrite.faces.annotation.Deferred;

import de.neuland.jade4j.exceptions.JadeException;
import fi.foyt.fni.illusion.IllusionEventPage;
import fi.foyt.fni.illusion.IllusionTemplateModelBuilderFactory.IllusionTemplateModelBuilder;
import fi.foyt.fni.jade.JadeController;
import fi.foyt.fni.materials.IllusionEventDocumentController;
import fi.foyt.fni.materials.MaterialController;
import fi.foyt.fni.persistence.model.illusion.IllusionEvent;
import fi.foyt.fni.persistence.model.illusion.IllusionEventJoinMode;
import fi.foyt.fni.persistence.model.illusion.IllusionEventParticipant;
import fi.foyt.fni.persistence.model.illusion.IllusionEventParticipantRole;
import fi.foyt.fni.persistence.model.materials.IllusionEventDocument;
import fi.foyt.fni.persistence.model.materials.IllusionEventDocumentType;
import fi.foyt.fni.persistence.model.materials.IllusionEventFolder;
import fi.foyt.fni.security.SecurityContext;
import fi.foyt.fni.session.SessionController;
import fi.foyt.fni.utils.data.FileData;
import fi.foyt.fni.utils.faces.FacesUtils;

@RequestScoped
@Named
@Stateful
@Join (path = "/illusion/event/{urlName}", to = "/illusion/event.jsf")
public class IllusionEventBackingBean extends AbstractIllusionEventBackingBean {

  @Parameter
  private String urlName;
  
  @Parameter
  private String ref;

  @Parameter
  private String ignoreMessages;

  @Inject
  private Logger logger;

  @Inject
  private IllusionEventDocumentController illusionEventDocumentController;

  @Inject
  private MaterialController materialController;

  @Inject
  private IllusionEventNavigationController illusionEventNavigationController;

  @Inject
  private JadeController jadeController;

  @Inject
  private SessionController sessionController;
  
  @Override
  public String init(IllusionEvent illusionEvent, IllusionEventParticipant participant) {
    illusionEventNavigationController.setSelectedPage(IllusionEventPage.Static.INDEX);
    illusionEventNavigationController.setEventUrlName(getUrlName());
    
    if (participant != null) {
      memberRole = participant.getRole();
      switch (memberRole) {
        case BOT:
          return "/error/access-denied.jsf";
        case BANNED:
        case PENDING_APPROVAL:
        case WAITING_PAYMENT:
        case INVITED:
        case ORGANIZER:
        case PARTICIPANT:
        break;
      }
    }

    IllusionEventFolder folder = illusionEvent.getFolder();
    String indexText = null;
    
    IllusionEventDocument indexDocument = illusionEventDocumentController.findByFolderAndDocumentType(folder, IllusionEventDocumentType.INDEX);
    if (indexDocument != null) {
      try {
        FileData indexData = materialController.getMaterialData(null, null, indexDocument);
        if (indexData != null) {
          indexText = new String(indexData.getData(), "UTF-8");
        }
      } catch (IOException | GeneralSecurityException e) {
        logger.log(Level.WARNING, "Could not retreive event index text", e);
      }
    }

    IllusionEventJoinMode joinMode = illusionEvent.getJoinMode();
    boolean hasSignUpFee = illusionEvent.getSignUpFee() != null;
    String signUpFee = null;
    if (hasSignUpFee) {
      Currency currency = illusionEvent.getSignUpFeeCurrency();
      NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
      currencyFormatter.setCurrency(currency);
      signUpFee = currencyFormatter.format(illusionEvent.getSignUpFee());
    }
    
    IllusionTemplateModelBuilder templateModelBuilder = createDefaultTemplateModelBuilder(illusionEvent, participant, IllusionEventPage.Static.INDEX)
      .put("indexText", indexText)
      .put("joinMode", joinMode.toString())
      .put("hasSignUpFee", hasSignUpFee)
      .put("signUpFee", signUpFee)
      .put("memberRole", memberRole)
      .put("ref", ref)
      .addLocales(sessionController.getLocale(), "illusion.event.joinInfoFree", "illusion.event.joinInfoPrice", 
          "illusion.event.joinButton", "illusion.event.requestToJoinButton",  "illusion.event.shareOnFacebook", 
          "illusion.event.shareOnTwitter", "illusion.event.shareOnGooglePlus");
    
    try {
      Map<String, Object> templateModel = templateModelBuilder.build();
      headHtml = jadeController.renderTemplate(getJadeConfiguration(), illusionEvent.getUrlName() + "/index-head", templateModel);
      contentsHtml = jadeController.renderTemplate(getJadeConfiguration(), illusionEvent.getUrlName() + "/index-contents", templateModel);
    } catch (JadeException | IOException e) {
      logger.log(Level.SEVERE, "Could not parse jade template", e);
      return "/error/internal-error.jsf";
    }

    return null;
  }
  
  @RequestAction
  @Deferred
  public void showWarnings() {
    if (!"true".equals(getIgnoreMessages())) {
      if (memberRole != null) {
        switch (memberRole) {
          case BANNED:
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, FacesUtils.getLocalizedValue("illusion.event.bannedMessage"));
          break;
          case PENDING_APPROVAL:
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, FacesUtils.getLocalizedValue("illusion.event.pendingApprovalMessage"));
          break;
          case WAITING_PAYMENT:
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, FacesUtils.getLocalizedValue("illusion.event.waitingPaymentMessage"));
          break;
          default:
          break;
        }
      }
    }
  }
  
  @Override
  public String getUrlName() {
    return urlName;
  }

  public void setUrlName(@SecurityContext String urlName) {
    this.urlName = urlName;
  }
  
  public String getRef() {
    return ref;
  }
  
  public void setRef(String ref) {
    this.ref = ref;
  }
  
  public String getIgnoreMessages() {
    return ignoreMessages;
  }
  
  public void setIgnoreMessages(String ignoreMessages) {
    this.ignoreMessages = ignoreMessages;
  }
  
  public String getHeadHtml() {
    return headHtml;
  }
  
  public String getContentsHtml() {
    return contentsHtml;
  }
  
  private IllusionEventParticipantRole memberRole;
  private String headHtml;
  private String contentsHtml;
}
