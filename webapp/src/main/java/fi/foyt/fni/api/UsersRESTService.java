package fi.foyt.fni.api;

import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.math.NumberUtils;

import fi.foyt.fni.auth.AuthenticationController;
import fi.foyt.fni.materials.MaterialController;
import fi.foyt.fni.persistence.dao.auth.InternalAuthDAO;
import fi.foyt.fni.persistence.dao.auth.UserIdentifierDAO;
import fi.foyt.fni.persistence.dao.materials.ImageDAO;
import fi.foyt.fni.persistence.dao.users.UserDAO;
import fi.foyt.fni.persistence.dao.users.UserEmailDAO;
import fi.foyt.fni.persistence.dao.users.UserSettingDAO;
import fi.foyt.fni.persistence.dao.users.UserSettingKeyDAO;
import fi.foyt.fni.persistence.model.materials.Image;
import fi.foyt.fni.persistence.model.materials.ImageSize;
import fi.foyt.fni.persistence.model.materials.MaterialThumbnail;
import fi.foyt.fni.persistence.model.users.User;
import fi.foyt.fni.persistence.model.users.UserRole;
import fi.foyt.fni.session.SessionController;
import fi.foyt.fni.system.SystemSettingsController;
import fi.foyt.fni.users.UserController;

@Path("/users")
@RequestScoped
@Stateful
@Produces ("application/json")
public class UsersRESTService extends RESTService {
	
	@Inject
	private Logger logger;

	@Inject
	private UserController userController;

	@Inject
	private SessionController sessionController;
	
	@Inject
	private SystemSettingsController systemSettingsController;

	@Inject
	private MaterialController materialController;
	
	@Inject
	private AuthenticationController authenticationController;
	
	@Inject
	private UserEmailDAO userEmailDAO;

	@Inject
	private InternalAuthDAO internalAuthDAO;

	@Inject
	private UserIdentifierDAO userIdentifierDAO;
	
	@Inject
	private UserDAO userDAO;
	
	@Inject
	private ImageDAO imageDAO;

	@Inject
	private UserSettingKeyDAO userSettingKeyDAO;

	@Inject
	private UserSettingDAO userSettingDAO;
	
	@PUT
	@POST
	@Path ("/{USERID}/changeLocale")
	public Response changeLocale(
			@PathParam ("USERID") String userIdParam,
			@FormParam ("locale") String localeParam,
			@Context HttpHeaders httpHeaders) {
		
		Locale browserLocale = getBrowserLocale(httpHeaders);
    
		User user = null;
		Locale locale = LocaleUtils.toLocale(localeParam);
		User loggedUser = getLoggedUser(httpHeaders);

		if ("ANONYMOUS".equals(userIdParam)) {
			if (loggedUser == null) {
  		  sessionController.setLocale(locale);
			} else {
  		  return Response.status(Response.Status.FORBIDDEN).entity(ApiMessages.getText(browserLocale, "error.generic.permissionDenied")).build();
			}
		} else {
  		if ("SELF".equals(userIdParam)) {
    		user = loggedUser;
  		} else {
  		  return Response.status(Response.Status.FORBIDDEN).entity(ApiMessages.getText(browserLocale, "error.generic.permissionDenied")).build();
  		}
  		
  		userDAO.updateLocale(user, locale);
  		sessionController.setLocale(locale);
		}
		
		return Response.ok(new ApiResult<>(null)).build();
	}
	
	@GET
	@Path ("/{USERID}/profileImage/{SIZE}")
	public Response profileImage(
			@PathParam ("USERID") String userIdParam, 
			@PathParam ("SIZE") String sizeParam,
			@Context HttpHeaders httpHeaders) {
		
		boolean currentUser = "SELF".equals(userIdParam);
		Locale browserLocale = getBrowserLocale(httpHeaders);
    
		User user = null;
		
		if (currentUser) {
  		User loggedUser = getLoggedUser(httpHeaders);
  		if (!hasRole(loggedUser, UserRole.GUEST)) {
  		  return Response.status(Response.Status.FORBIDDEN).entity(ApiMessages.getText(browserLocale, "error.generic.permissionDenied")).build();
  		}
  		user = loggedUser;
		} else {
			Long userId =  NumberUtils.createLong(userIdParam);
			user = userDAO.findById(userId);
		}
		
		ImageSize size = sizeParam == null || sizeParam.equals("ORIGINAL") ? ImageSize.ORIGINAL : ImageSize.valueOf('_' + sizeParam);
    
    if (user == null) {
      return Response.status(Response.Status.NOT_FOUND).entity(ApiMessages.getText(browserLocale, "error.users.profile.userNotFound")).build();
    }
    
    Image profileImage = user.getProfileImage();
    if (profileImage == null) {
    	return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    try {
    	MaterialThumbnail imageThumbnail = materialController.getImageThumbnail(profileImage, size);
    	return createBinaryResponse(imageThumbnail.getContent(), imageThumbnail.getContentType());
    } catch (IOException e) {
    	return Response.noContent().build();
    }
	}

}
