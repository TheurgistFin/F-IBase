package fi.foyt.fni.persistence.dao.users;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.foyt.fni.persistence.model.users.UserSetting_;
import fi.foyt.fni.persistence.dao.DAO;
import fi.foyt.fni.persistence.dao.GenericDAO;
import fi.foyt.fni.persistence.model.users.User;
import fi.foyt.fni.persistence.model.users.UserSetting;
import fi.foyt.fni.persistence.model.users.UserSettingKey;

@DAO
public class UserSettingDAO extends GenericDAO<UserSetting> {

	private static final long serialVersionUID = 1L;

	public UserSetting create(User user, UserSettingKey userSettingKey, String value) {
    EntityManager entityManager = getEntityManager();
    UserSetting userSetting = new UserSetting();
    userSetting.setValue(value);
    userSetting.setUserSettingKey(userSettingKey);
    userSetting.setUser(user);
    entityManager.persist(userSetting);
    return userSetting;
  }

  public UserSetting findByUserAndUserSettingKey(User user, UserSettingKey userSettingKey) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<UserSetting> criteria = criteriaBuilder.createQuery(UserSetting.class);
    Root<UserSetting> root = criteria.from(UserSetting.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(UserSetting_.user), user),
            criteriaBuilder.equal(root.get(UserSetting_.userSettingKey), userSettingKey)
        )
    );

    return getSingleResult(entityManager.createQuery(criteria));
  }

  public UserSetting updateValue(UserSetting userSetting, String value) {
    EntityManager entityManager = getEntityManager();
    userSetting.setValue(value);

    userSetting = entityManager.merge(userSetting);
    return userSetting;
  }

}
