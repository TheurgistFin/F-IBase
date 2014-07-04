package fi.foyt.fni.persistence.model.illusion;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import fi.foyt.fni.persistence.model.users.User;

@Entity
public class IllusionGroupMember {

  public Long getId() {
    return id;
  }
  
  public String getCharacterName() {
    return characterName;
  }
  
  public void setCharacterName(String characterName) {
    this.characterName = characterName;
  }
  
  public IllusionGroupMemberRole getRole() {
    return role;
  }
  
  public void setRole(IllusionGroupMemberRole role) {
    this.role = role;
  }
  
  public User getUser() {
    return user;
  }
  
  public void setUser(User user) {
    this.user = user;
  }
  
  public IllusionGroup getGroup() {
    return group;
  }
  
  public void setGroup(IllusionGroup group) {
    this.group = group;
  }
  
  @Id
  @GeneratedValue (strategy=GenerationType.IDENTITY)
  private Long id;
  
  private String characterName;

  @ManyToOne
  private User user;
  
  @ManyToOne
  private IllusionGroup group;
  
  @NotNull
  @Enumerated (EnumType.STRING)
  private IllusionGroupMemberRole role;
}