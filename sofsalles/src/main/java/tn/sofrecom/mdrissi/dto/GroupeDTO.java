package tn.sofrecom.mdrissi.dto;

import java.util.List;

import org.springframework.stereotype.Component;

import tn.sofrecom.mdrissi.entities.Grparticipants;
import tn.sofrecom.mdrissi.entities.User;
import tn.sofrecom.mdrissi.services.collaborateurs.Collaborateur;

@Component
public class GroupeDTO {

	private User user;

	private Grparticipants grp;
	private List<Collaborateur> collabs;
	
	public GroupeDTO() {
		
	}

	public GroupeDTO(Grparticipants grp, List<Collaborateur> collabs) {
		super();
		this.grp = grp;
		this.collabs = collabs;
	}

	public Grparticipants getGrp() {
		return grp;
	}

	public void setGrp(Grparticipants grp) {
		this.grp = grp;
	}

	public List<Collaborateur> getCollabs() {
		return collabs;
	}

	public void setCollabs(List<Collaborateur> collabs) {
		this.collabs = collabs;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
