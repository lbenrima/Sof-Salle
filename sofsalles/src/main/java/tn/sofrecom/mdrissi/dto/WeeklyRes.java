package tn.sofrecom.mdrissi.dto;

import java.util.List;

import tn.sofrecom.mdrissi.entities.Reservation;

public class WeeklyRes {
	
	List<DayDTO> selected;
	Reservation reservation;
	
	public WeeklyRes() {
		
	}
	
	public List<DayDTO> getSelected() {
		return selected;
	}
	public void setSelected(List<DayDTO> selected) {
		this.selected = selected;
	}
	public Reservation getReservation() {
		return reservation;
	}
	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}
	public WeeklyRes(List<DayDTO> selected, Reservation reservation) {
		super();
		this.selected = selected;
		this.reservation = reservation;
	}
	
}
