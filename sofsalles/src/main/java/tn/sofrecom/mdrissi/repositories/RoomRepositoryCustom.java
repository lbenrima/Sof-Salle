package tn.sofrecom.mdrissi.repositories;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import tn.sofrecom.mdrissi.entities.Room;

public interface RoomRepositoryCustom {
	List<Room> getAvailableRooms(List<Date> startDay, List<Date> endDay, Time startTime, Time endTime);

}
