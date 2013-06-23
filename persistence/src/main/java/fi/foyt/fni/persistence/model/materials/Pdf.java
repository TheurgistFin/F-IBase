package fi.foyt.fni.persistence.model.materials;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Indexed;

@Entity
@Cacheable (true)
@Cache (usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@PrimaryKeyJoinColumn (name="id")
@Indexed
public class Pdf extends Binary {

  public Pdf() {
    setType(MaterialType.PDF);
    setContentType("application/pdf");
  }
  
}