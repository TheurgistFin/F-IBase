package fi.foyt.fni.persistence.dao.gamelibrary;

import fi.foyt.fni.persistence.dao.GenericDAO;
import fi.foyt.fni.persistence.model.gamelibrary.PublicationFile;

public class PublicationFileDAO extends GenericDAO<PublicationFile> {
  
	private static final long serialVersionUID = 1L;

	public PublicationFile create(byte[] content, String contentType) {
		PublicationFile publicationFile = new PublicationFile();
		publicationFile.setContent(content);
		publicationFile.setContentType(contentType);
		return persist(publicationFile);
	}

	public PublicationFile updateContentType(PublicationFile publicationFile, String contentType) {
		publicationFile.setContentType(contentType);
		getEntityManager().persist(publicationFile);
		return publicationFile;
	}

	public PublicationFile updateContent(PublicationFile publicationFile, byte[] content) {
		publicationFile.setContent(content);
		getEntityManager().persist(publicationFile);
		return publicationFile;
	}
	
}
