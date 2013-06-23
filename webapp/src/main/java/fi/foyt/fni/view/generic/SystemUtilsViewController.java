package fi.foyt.fni.view.generic;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.servlet.http.HttpSession;

import org.hibernate.search.MassIndexer;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;

import fi.foyt.fni.persistence.model.users.User;
import fi.foyt.fni.persistence.model.users.UserRole;
import fi.foyt.fni.session.SessionController;
import fi.foyt.fni.view.AbstractViewController;
import fi.foyt.fni.view.ViewControllerContext;

@RequestScoped
@Stateful
public class SystemUtilsViewController extends AbstractViewController {
	
	private static final String REINDEX_ENTITY_CLASSES = "__reindexEntityClasses__";

	@Inject
  private SessionController sessionController;

	@PersistenceContext
  private EntityManager entityManager;
	
	@Override
	public boolean checkPermissions(ViewControllerContext context) {
		User loggedUser = sessionController.getLoggedUser();
    if (loggedUser == null) {
    	return false;
    }
    
  	return loggedUser.getRole() == UserRole.ADMINISTRATOR;
	}

	@Override
	public void execute(ViewControllerContext context) {
		Action action = Action.valueOf(context.getStringParameter("action"));
		switch (action) {
		  case ENTITY_REINDEX:
		  	entityReindexAction(context);
		  break;
		}
	}

	private void entityReindexAction(ViewControllerContext context) {
		HttpSession session = context.getRequest().getSession();
		@SuppressWarnings("unchecked") List<Class<?>> entityClasses = (List<Class<?>>) session.getAttribute("__reindexEntityClasses__");
		if (entityClasses == null) {
			entityClasses = listIndexedEntityClasses();
			session.setAttribute(REINDEX_ENTITY_CLASSES, entityClasses);
		} 
		
		if (entityClasses.size() > 0) {
		  Class<?> entityClass = entityClasses.get(0);
		  entityClasses.remove(0);
		
		  try {
        reindexEntity(entityClass);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
		
		  session.setAttribute(REINDEX_ENTITY_CLASSES, entityClasses);
		  context.getRequest().setAttribute("remainingClasses", entityClasses);
		  context.setIncludeJSP("/jsp/generic/systemutils/entityreindex.jsp");
		} else {
			session.removeAttribute(REINDEX_ENTITY_CLASSES);
			context.setRedirect(context.getBasePath() + "/", false);
		}
	}
	
	private void reindexEntity(Class<?> entity) throws InterruptedException {
		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
				
    MassIndexer massIndexer = fullTextEntityManager.createIndexer(entity);
    
    massIndexer.batchSizeToLoadObjects(10);
    massIndexer.threadsForSubsequentFetching(1);
    massIndexer.threadsToLoadObjects(1);

    massIndexer.startAndWait();
	}

	private List<Class<?>> listIndexedEntityClasses() {
		List<Class<?>> result = new ArrayList<Class<?>>();
		
		Metamodel metamodel = entityManager.getMetamodel();
		Set<EntityType<?>> entityTypes = metamodel.getEntities();
		for (EntityType<?> entityType : entityTypes) {
			if (isIndexed(entityType.getJavaType())) {
				result.add(entityType.getJavaType());
			}
		}
		
		return result;
	}
	
	private boolean isIndexed(Class<?> entityClass) {
    if (entityClass.isAnnotationPresent(Indexed.class)) {
      return true;
    }

    if (entityClass.equals(Object.class))
      return false;

    return isIndexed(entityClass.getSuperclass());
  }
	
	private enum Action {
		ENTITY_REINDEX
	}
}