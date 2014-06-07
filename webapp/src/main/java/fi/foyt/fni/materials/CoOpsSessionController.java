package fi.foyt.fni.materials;

import java.util.UUID;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.foyt.fni.persistence.dao.materials.CoOpsSessionDAO;
import fi.foyt.fni.persistence.model.materials.CoOpsSession;
import fi.foyt.fni.persistence.model.materials.Material;
import fi.foyt.fni.persistence.model.users.User;

@Dependent
@Stateless
public class CoOpsSessionController {

  @Inject
  private CoOpsSessionDAO coOpsSessionDAO;
  
  public CoOpsSession createSession(Material material, User user, String algorithm, Long joinRevision) {
    String sessionId = UUID.randomUUID().toString();
    CoOpsSession session = coOpsSessionDAO.create(material, user, sessionId, algorithm, joinRevision);
    return session;
  }
  
  public CoOpsSession findSessionBySessionId(String sessionId) {
    return coOpsSessionDAO.findBySessionId(sessionId);
  }

}