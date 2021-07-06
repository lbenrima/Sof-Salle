package tn.sofrecom.mdrissi.services.collaborateurs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CollaborateursServiceImpl implements CollaborateursService {
	
	@Autowired
	ListCollaborateurs collabs;
	
	public List<Collaborateur> getAll() {

		collabs.setUsersCollabs();
		
		return collabs.getCollabs();
	}

	
	
}
