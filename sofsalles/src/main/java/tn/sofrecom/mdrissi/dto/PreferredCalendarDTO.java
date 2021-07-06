package tn.sofrecom.mdrissi.dto;

import java.util.List;

public class PreferredCalendarDTO {

	private long preferredCalenderId;
	private List<RoomDTO> rooms;

	public List<RoomDTO> getRooms() {
		return rooms;
	}

	public void setRooms(List<RoomDTO> rooms) {
		this.rooms = rooms;
	}

	public long getPreferredCalenderId() {
		return preferredCalenderId;
	}

	public void setPreferredCalenderId(long preferredCalenderId) {
		this.preferredCalenderId = preferredCalenderId;
	}
}
