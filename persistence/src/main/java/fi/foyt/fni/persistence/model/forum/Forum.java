package fi.foyt.fni.persistence.model.forum;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

@Entity
@Cacheable (true)
@Cache (usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Indexed
public class Forum {

  public Long getId() {
    return id;
  }
  
  public String getUrlName() {
    return urlName;
  }
  
  public void setUrlName(String urlName) {
    this.urlName = urlName;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getDescription() {
    return description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  public ForumCategory getCategory() {
    return category;
  }
  
  public void setCategory(ForumCategory category) {
    this.category = category;
  }
  
  public Boolean getAllowTopicCreation() {
		return allowTopicCreation;
	}
  
  public void setAllowTopicCreation(Boolean allowTopicCreation) {
		this.allowTopicCreation = allowTopicCreation;
	}
  
  @Id
  @DocumentId
  @GeneratedValue (strategy=GenerationType.IDENTITY)
  private Long id;
  
  @Column (nullable=false, unique = true)
  private String urlName;
  
  @Column (nullable=false)
  private String name;
  
  @Column
  @Field
  @Lob
  private String description;
  
  @ManyToOne
  private ForumCategory category;
  
  @Column (nullable=false, columnDefinition = "BIT")
  private Boolean allowTopicCreation;
}
