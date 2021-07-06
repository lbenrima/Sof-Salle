package tn.sofrecom.mdrissi.services.room;

import java.util.List;

import tn.sofrecom.mdrissi.entities.Reservation;
import tn.sofrecom.mdrissi.entities.Room;

public interface RoomService {
	
	public List<Room> getAllRooms();
	
	public String addRoom(Room room);
	
	public String updateRoom(int id, Room room);

	public String deleteRoom(int id);
	List<Room> getAvailableRooms(Reservation res);

}
