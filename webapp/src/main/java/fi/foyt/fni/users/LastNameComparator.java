package fi.foyt.fni.users;

import java.util.Comparator;

import fi.foyt.fni.persistence.model.users.User;

public class LastNameComparator implements Comparator<User> {

	@Override
  public int compare(User user, User user2) {
		return user.getLastName().compareTo(user2.getLastName());
  }

}
