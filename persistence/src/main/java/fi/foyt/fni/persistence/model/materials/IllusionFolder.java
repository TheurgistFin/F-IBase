package fi.foyt.fni.persistence.model.materials;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.search.annotations.Indexed;

@Entity
@PrimaryKeyJoinColumn (name="id")
@Indexed
public class IllusionFolder extends Folder {
  
  public IllusionFolder() {
    setType(MaterialType.ILLUSION_FOLDER);
  }
  
}