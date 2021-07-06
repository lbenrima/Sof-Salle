package tn.sofrecom.mdrissi.services.childreservation;

import java.sql.Date;
import java.util.List;

import tn.sofrecom.mdrissi.dto.WeeklyRes;
import tn.sofrecom.mdrissi.entities.Reservation;
import tn.sofrecom.mdrissi.entities.Reservationchild;

public interface ChildReservationService {

	public List<Date> saveDailyReservation(Reservation res);

	public List<Date> saveWeeklyReservation(Reservation res);
	public void saveweeklyReservation(WeeklyRes res);

	public void savemonthlyReservation(Reservation res);
	public List<Date> saveMonthlyReservation(Reservation res);

	public void saveyearlyReservation(Reservation res);
	List<Reservationchild> getReservationsOfRooms(List<String> rooms, java.util.Date start, java.util.Date end );

}
