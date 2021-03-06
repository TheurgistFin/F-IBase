package fi.foyt.fni.persistence.dao.illusion;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.foyt.fni.persistence.dao.GenericDAO;
import fi.foyt.fni.persistence.model.illusion.IllusionEventParticipant;
import fi.foyt.fni.persistence.model.illusion.IllusionEventParticipantImage;
import fi.foyt.fni.persistence.model.illusion.IllusionEventParticipantImage_;

public class IllusionEventParticipantImageDAO extends GenericDAO<IllusionEventParticipantImage> {

	private static final long serialVersionUID = 1L;

	public IllusionEventParticipantImage create(IllusionEventParticipant participant, String contentType, byte[] data, Date modified) {
		IllusionEventParticipantImage illusionEventParticipantImage = new IllusionEventParticipantImage();

    illusionEventParticipantImage.setParticipant(participant);
    illusionEventParticipantImage.setData(data);
    illusionEventParticipantImage.setContentType(contentType);
    illusionEventParticipantImage.setModified(modified);
    
		return persist(illusionEventParticipantImage);
	}

  public IllusionEventParticipantImage findByParticipant(IllusionEventParticipant participant) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<IllusionEventParticipantImage> criteria = criteriaBuilder.createQuery(IllusionEventParticipantImage.class);
    Root<IllusionEventParticipantImage> root = criteria.from(IllusionEventParticipantImage.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(IllusionEventParticipantImage_.participant), participant)
    );

    return getSingleResult(entityManager.createQuery(criteria));
  }

  public IllusionEventParticipantImage updateData(IllusionEventParticipantImage image, byte[] data) {
    image.setData(data);
    return persist(image);
  }

  public IllusionEventParticipantImage updateContentType(IllusionEventParticipantImage image, String contentType) {
    image.setContentType(contentType);
    return persist(image);
  }

  public IllusionEventParticipantImage updateModified(IllusionEventParticipantImage image, Date modified) {
    image.setModified(modified);
    return persist(image);
  }

}
