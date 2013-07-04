package fi.foyt.fni.persistence.model.store;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn (name="id")
public class FileProduct extends Product {

	public FileProductFile getFile() {
		return file;
	}
	
	public void setFile(FileProductFile file) {
		this.file = file;
	}
	
	@ManyToOne
	private FileProductFile file;
}
