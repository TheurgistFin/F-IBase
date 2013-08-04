package fi.foyt.fni.persistence.model.gamelibrary;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.hibernate.validator.constraints.NotEmpty;

import fi.foyt.fni.persistence.model.forum.ForumTopic;
import fi.foyt.fni.persistence.model.users.User;

@Entity
@Inheritance (strategy=InheritanceType.JOINED)
public class Product {

  public Long getId() {
    return id;
  }
  
  public Boolean getPublished() {
		return published;
	}
  
  public void setPublished(Boolean published) {
		this.published = published;
	}

  public String getName() {
		return name;
	}
  
  public void setName(String name) {
		this.name = name;
	}
  
  public String getUrlName() {
		return urlName;
	}
  
  public void setUrlName(String urlName) {
		this.urlName = urlName;
	}
  
  public String getDescription() {
		return description;
	}
  
  public void setDescription(String description) {
		this.description = description;
	}
  
  public Double getPrice() {
		return price;
	}
  
  public void setPrice(Double price) {
		this.price = price;
	}
  
  public ProductImage getDefaultImage() {
		return defaultImage;
	}
  
  public void setDefaultImage(ProductImage defaultImage) {
		this.defaultImage = defaultImage;
	}

  public Date getModified() {
    return modified;
  }

  public void setModified(Date modified) {
    this.modified = modified;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public User getCreator() {
    return creator;
  }

  public void setCreator(User creator) {
    this.creator = creator;
  }

  public User getModifier() {
    return modifier;
  }

  public void setModifier(User modifier) {
    this.modifier = modifier;
  }
  
  public Boolean getRequiresDelivery() {
		return requiresDelivery;
	}
  
  public void setRequiresDelivery(Boolean requiresDelivery) {
		this.requiresDelivery = requiresDelivery;
	}
  
  public Boolean getPurchasable() {
		return purchasable;
	}
  
  public void setPurchasable(Boolean purchasable) {
		this.purchasable = purchasable;
	}
  
  public Double getWeight() {
		return weight;
	}
  
  public void setWeight(Double weight) {
		this.weight = weight;
	}
  
  public Integer getWidth() {
		return width;
	}
  
  public void setWidth(Integer width) {
		this.width = width;
	}
  
  public Integer getHeight() {
		return height;
	}
  
  public void setHeight(Integer height) {
		this.height = height;
	}
  
  public Integer getDepth() {
		return depth;
	}
  
  public void setDepth(Integer depth) {
		this.depth = depth;
	}
  
  public ForumTopic getForumTopic() {
		return forumTopic;
	}
  
  public void setForumTopic(ForumTopic forumTopic) {
		this.forumTopic = forumTopic;
	}
  
  @Transient
  public String getDescriptionPlain() {
    return StringEscapeUtils.unescapeHtml4(getDescription().replaceAll("\\<.*?>",""));
  }
  
  @Transient
  public String getDescriptionPlainAbbr() {
  	return StringUtils.abbreviate(getDescriptionPlain(), 255);
  }
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @NotNull
  @Column (nullable = false, columnDefinition = "BIT")
  private Boolean published;

  @NotNull
  @NotEmpty
  @Column(nullable = false)
  private String name;
  
  @NotNull
  @NotEmpty
  @Column(nullable = false)
  private String urlName;
  
  @Lob
  private String description;

  @NotNull
  @Column(nullable = false)
  private Double price;
  
  @ManyToOne
  private ProductImage defaultImage;
  
  @NotNull
  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date modified;

  @NotNull
  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date created;

  @ManyToOne
  private User creator;

  @ManyToOne
  private User modifier;
  
  @NotNull
  @Column (nullable = false, columnDefinition = "BIT")
  private Boolean requiresDelivery;
  
  @NotNull
  @Column (nullable = false, columnDefinition = "BIT")
  private Boolean purchasable;
  
  private Double weight;
  
  private Integer width;
  
  private Integer height;
  
  private Integer depth;
  
  @ManyToOne
  private ForumTopic forumTopic;
}
