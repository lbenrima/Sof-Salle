package tn.sofrecom.mdrissi.services.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.sofrecom.mdrissi.entities.User;
import tn.sofrecom.mdrissi.repositories.UserRepository;
import tn.sofrecom.mdrissi.services.collaborateurs.ListCollaborateurs;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepo;

	@Autowired
	ListCollaborateurs collabs;

	public void checkAndSet() {

		// ListCollaborateurs collabs = new ListCollaborateurs();
		collabs.setUsersCollabs();

		List<User> listUsers = userRepo.findAll();
		for (int i = 0; i < listUsers.size(); i++) {
			if (!collabs.check(listUsers.get(i))) {
				listUsers.get(i).setStateuser(false);
			}
		}

	}

	public List<User> findAllUsers() {
		return userRepo.findAll();
	}

	public User findUser(User user) {
	
		User userResult = userRepo.findDistinctByMail(user.getMail());

		if (userResult == null) {
			user.setStateuser(true);
			user.setProfileuser(false);
			return this.save(user);
		}
		return userResult;

	}

	public User save(User user) {
		return userRepo.save(user);
	}

	@Override
	public User findUserByEmail(String email) {
		return userRepo.findDistinctByMail(email);
	}

}
