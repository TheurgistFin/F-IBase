package fi.foyt.fni.view.store;

import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.annotation.RequestAction;

import fi.foyt.fni.jsf.NavigationController;
import fi.foyt.fni.persistence.model.store.StoreProduct;
import fi.foyt.fni.persistence.model.users.Permission;
import fi.foyt.fni.security.LoggedIn;
import fi.foyt.fni.session.SessionController;
import fi.foyt.fni.store.StoreProductController;
import fi.foyt.fni.system.SystemSettingsController;
import fi.foyt.fni.utils.faces.FacesUtils;

@RequestScoped
@Named
@Stateful
@Join (path = "/store/manage", to = "/store/manage.jsf")
@LoggedIn
public class StoreManageBackingBean {
	
	@Inject
	private SessionController sessionController;

  @Inject
  private NavigationController navigationController;
  
  @Inject
  private StoreProductController storeProductController;

  @Inject
  private SystemSettingsController systemSettingsController;

  @RequestAction
  public String init() {
    if (!sessionController.hasLoggedUserPermission(Permission.STORE_MANAGE_PRODUCTS)) {
      return navigationController.accessDenied();
    }
    
    unpublishedProducts = storeProductController.listUnpublishedStoreProducts();
    publishedProducts = storeProductController.listPublishedStoreProducts();
    
    return null;
  }
  
  public List<StoreProduct> getPublishedProducts() {
    return publishedProducts;
  }
  
  public List<StoreProduct> getUnpublishedProducts() {
    return unpublishedProducts;
  }
  
  public String createStoreProduct() {
    StoreProduct storeProduct = storeProductController.createStoreProduct(
      sessionController.getLoggedUser(), 
      FacesUtils.getLocalizedValue("store.manage.untitledStoreProduct") ,
      systemSettingsController.getDefaultLanguage()
    );
    
    return String.format("/store/editstoreproduct.jsf?faces-redirect=true&amp;storeProductId=%d", storeProduct.getId());
  }
  
  private List<StoreProduct> unpublishedProducts;
  private List<StoreProduct> publishedProducts;
}
