package fi.foyt.fni.persistence.dao.store;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.foyt.fni.persistence.dao.DAO;
import fi.foyt.fni.persistence.dao.GenericDAO;
import fi.foyt.fni.persistence.model.store.Product;
import fi.foyt.fni.persistence.model.store.ShoppingCart;
import fi.foyt.fni.persistence.model.store.ShoppingCartItem;
import fi.foyt.fni.persistence.model.store.ShoppingCartItem_;

@RequestScoped
@DAO
public class ShoppingCartItemDAO extends GenericDAO<ShoppingCartItem> {
  
	public ShoppingCartItem create(ShoppingCart cart, Product product, Integer count) {
		ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
		shoppingCartItem.setCart(cart);
		shoppingCartItem.setCount(count);
		shoppingCartItem.setProduct(product);
		getEntityManager().persist(shoppingCartItem);
		
		return shoppingCartItem;
	}

	public List<ShoppingCartItem> listByCart(ShoppingCart cart) {
		EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ShoppingCartItem> criteria = criteriaBuilder.createQuery(ShoppingCartItem.class);
    Root<ShoppingCartItem> root = criteria.from(ShoppingCartItem.class);
    criteria.select(root);
    criteria.where(
  		criteriaBuilder.equal(root.get(ShoppingCartItem_.cart), cart)
    );
    
    return entityManager.createQuery(criteria).getResultList();
	}

	public List<ShoppingCartItem> listByCartAndProduct(ShoppingCart cart, Product product) {
		EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ShoppingCartItem> criteria = criteriaBuilder.createQuery(ShoppingCartItem.class);
    Root<ShoppingCartItem> root = criteria.from(ShoppingCartItem.class);
    criteria.select(root);
    criteria.where(
  		criteriaBuilder.equal(root.get(ShoppingCartItem_.cart), cart)
    );
    
    return entityManager.createQuery(criteria).getResultList();
	}

	public ShoppingCartItem updateCount(ShoppingCartItem shoppingCartItem, Integer count) {
		shoppingCartItem.setCount(count);
		getEntityManager().persist(shoppingCartItem);
		return shoppingCartItem;
	}
	
}
