package fi.foyt.fni.persistence.model.illusion;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class IllusionEventParticipantImage {
	
	public Long getId() {
		return id;
	}
	
	public IllusionEventParticipant getParticipant() {
    return participant;
  }
	
	public void setParticipant(IllusionEventParticipant participant) {
    this.participant = participant;
  }
	
  public byte[] getData() {
    return data;
  }
  
  public void setData(byte[] data) {
    this.data = data;
  }
  
  public String getContentType() {
    return contentType;
  }
 
  public void setContentType(String contentType) {
    this.contentType = contentType;
  } 

  public Date getModified() {
		return modified;
	}
  
  public void setModified(Date modified) {
		this.modified = modified;
	}
 
  @Id
  @GeneratedValue (strategy=GenerationType.IDENTITY)
  private Long id;
 
  @ManyToOne
  private IllusionEventParticipant participant;
  
  @Lob
  private byte[] data;
  
  @NotNull
  @NotEmpty
  @Column (nullable = false)
  private String contentType; 
  
  @Column (nullable=false)
  @Temporal (TemporalType.TIMESTAMP)
  private Date modified;
}
