package tn.sofrecom.mdrissi.services.user;

import java.util.List;

import tn.sofrecom.mdrissi.entities.User;

public interface UserService {
	
	public void checkAndSet();
	
	List<User> findAllUsers();

    User findUser(User user);

    User save(User user);
    User findUserByEmail(String email);

}
