package tn.sofrecom.mdrissi.services.reservation;

import java.util.Date;
import java.util.List;

import tn.sofrecom.mdrissi.dto.NewReservationDTO;
import tn.sofrecom.mdrissi.dto.WeeklyRes;
import tn.sofrecom.mdrissi.entities.Reservation;
import tn.sofrecom.mdrissi.entities.Reservationchild;
import tn.sofrecom.mdrissi.entities.Room;

public interface ReservationService {

	public Reservation addSimpleReservation(NewReservationDTO newRes);

	public List<java.sql.Date> addDailyReservation(NewReservationDTO newRes);

	public List<java.sql.Date> addMonthlyReservation(NewReservationDTO newRes);

	public Reservation addYearlyReservation(NewReservationDTO newRes);

	public List<java.sql.Date> addWeeklyReservation(NewReservationDTO newRes);

	public Reservation deleteSimpleReservation(Reservation deletRes);
	public boolean deleteReservationOccurance(Long idChild);

	public void checkAndSet();

	public List<Reservation> getAll();

	List<Reservationchild> listSimpleChildren(Reservation res);

	List<Reservationchild> listDailyChildren(Reservation res);

	List<Reservationchild> listWeeklyChildren(WeeklyRes weeklyRes);

	List<Reservationchild> listMonthlyChildren(Reservation res);

	List<Reservationchild> listYearlyChildren(Reservation res);

	public Reservation getReservation(Integer id);

	List<Room> getListeRoomNotConflit(Reservation res);

	List<Reservation> getActiveReservation(Date start, Date end);
	
	List<Reservation> getReservationOfRooms(List<String> rooms, Date start, Date end );

	List<Reservationchild> getActiveReservationChild(Date start, Date end);

	List<Reservationchild> getActiveReservationChildOfUser(String mail, Date start, Date end);

	List<Reservation> getActiveReservationOfUser(String mail, Date startDay, Date endDay);

	List<Reservation> getActiveReservationPerRoom(Room room, Date start, Date end);

	List<Reservationchild> getActiveReservationChildPerRoom(Room room, Date start, Date end);
}
