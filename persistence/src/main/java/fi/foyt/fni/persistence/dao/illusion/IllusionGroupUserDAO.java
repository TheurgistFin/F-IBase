package fi.foyt.fni.persistence.dao.illusion;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.foyt.fni.persistence.dao.DAO;
import fi.foyt.fni.persistence.dao.GenericDAO;
import fi.foyt.fni.persistence.model.illusion.IllusionGroup;
import fi.foyt.fni.persistence.model.illusion.IllusionGroupUser;
import fi.foyt.fni.persistence.model.illusion.IllusionGroupUserRole;
import fi.foyt.fni.persistence.model.illusion.IllusionGroupUser_;
import fi.foyt.fni.persistence.model.users.User;

@DAO
public class IllusionGroupUserDAO extends GenericDAO<IllusionGroupUser> {

	private static final long serialVersionUID = 1L;

	public IllusionGroupUser create(User user, IllusionGroup group, String nickname, IllusionGroupUserRole role) {
		IllusionGroupUser illusionSpaceUser = new IllusionGroupUser();

    illusionSpaceUser.setGroup(group);
    illusionSpaceUser.setNickname(nickname);
    illusionSpaceUser.setRole(role);
    illusionSpaceUser.setUser(user);

		return persist(illusionSpaceUser);
	}

  public IllusionGroupUser findByGroupAndUser(IllusionGroup group, User user) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<IllusionGroupUser> criteria = criteriaBuilder.createQuery(IllusionGroupUser.class);
    Root<IllusionGroupUser> root = criteria.from(IllusionGroupUser.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(root.get(IllusionGroupUser_.group), group),
        criteriaBuilder.equal(root.get(IllusionGroupUser_.user), user)
      )
    );

    return getSingleResult(entityManager.createQuery(criteria));
  }
  
	public List<IllusionGroupUser> listByGroup(IllusionGroup group) {
		EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<IllusionGroupUser> criteria = criteriaBuilder.createQuery(IllusionGroupUser.class);
    Root<IllusionGroupUser> root = criteria.from(IllusionGroupUser.class);
    criteria.select(root);
    criteria.where(
  		criteriaBuilder.equal(root.get(IllusionGroupUser_.group), group)
    );

    return entityManager.createQuery(criteria).getResultList();
	}

	public IllusionGroupUser updateRole(IllusionGroupUser illusionGroupUser, IllusionGroupUserRole role) {
		illusionGroupUser.setRole(role);
		return persist(illusionGroupUser);
	}

  public IllusionGroupUser updateNickname(IllusionGroupUser illusionGroupUser, String nickname) {
    illusionGroupUser.setNickname(nickname);
    return persist(illusionGroupUser);
  }

}
