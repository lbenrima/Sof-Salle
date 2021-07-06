package tn.sofrecom.mdrissi.repositories;

import java.util.Date;
import java.util.List;

import tn.sofrecom.mdrissi.entities.Reservation;

public interface ReservationRepositoryCustom {

	List<java.sql.Date> getConflictDays(List<Date> days, Date startTime, Date endTime, int room);
 List<Reservation> getReservations(Date startDay, Date endDay);
}
