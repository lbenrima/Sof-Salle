package tn.sofrecom.mdrissi.dto;

import java.util.List;

import tn.sofrecom.mdrissi.entities.Grparticipants;
import tn.sofrecom.mdrissi.entities.Reservation;
import tn.sofrecom.mdrissi.entities.User;
import tn.sofrecom.mdrissi.services.collaborateurs.Collaborateur;

public class NewReservationDTO {
	
	Reservation reservation;
	User user;
	WeeklyRes weeklyRes;
	private List<Collaborateur> collabs;
	private List<Grparticipants> grps;
	private String mails;
	
	public NewReservationDTO() {
		
	}

	public List<Collaborateur> getCollabs() {
		return collabs;
	}


	public Reservation getReservation() {
		return reservation;
	}


	public User getUser() {
		return user;
	}


	public WeeklyRes getWeeklyRes() {
		return weeklyRes;
	}


	public void setCollabs(List<Collaborateur> collabs) {
		this.collabs = collabs;
	}


	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	public void setWeeklyRes(WeeklyRes weeklyRes) {
		this.weeklyRes = weeklyRes;
	}

	public String getMails() {
		return mails;
	}

	public void setMails(String mails) {
		this.mails = mails;
	}

	public List<Grparticipants> getGrps() {
		return grps;
	}

	public void setGrps(List<Grparticipants> grps) {
		this.grps = grps;
	}
	
	
}
