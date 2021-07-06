package tn.sofrecom.mdrissi.services.reservation;

import java.util.Date;
import java.util.List;

import tn.sofrecom.mdrissi.dto.ReservationDTO;
import tn.sofrecom.mdrissi.entities.Room;

public interface ReservationDtoService {

	 List<ReservationDTO> getByStartEndDate(Date start, Date end); 
	List<ReservationDTO> getActiveReservationOfUser(String email, Date startDay,Date endDay);
	 List<ReservationDTO> getByStartEndDatePerRoom(Room room, Date start, Date end);
	 List<ReservationDTO> getByStartEndDateOfRooms(List<String> rooms, Date start, Date end);

}
