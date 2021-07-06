package tn.sofrecom.mdrissi.repositories;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import tn.sofrecom.mdrissi.entities.Reservation;

public class ReservationRepositoryImpl implements ReservationRepositoryCustom {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<java.sql.Date> getConflictDays(List<Date> days, Date startTime, Date endTime, int room) {
		List<java.sql.Date> newDays = days.stream().map(d-> new java.sql.Date(d.getTime())).collect(Collectors.toList());

		String query = "select reservationchild.startdate  from reservation inner "
				+ "join reservationchild on (reservation.idreservation = reservationchild.idreservation)"
				+ " where reservation.room = ?1 and reservationchild.startdate in ?2 "
				+ "and (reservationchild.starttime = ?3 or (reservationchild.starttime > ?3 "
				+ "and reservationchild.starttime < ?4 ) or "
				+ "(reservationchild.starttime < ?3 and reservationchild.endtime > ?4) or "
				+ "(reservationchild.endtime > ?3 and reservationchild.endtime <= ?4 )) "
				+"union  "
				+ "select startdate from reservation where reservation.simple=?5 and room= ?1 "
				+ " and startdate in ?2 and ( starttime= ?3 or (starttime > ?3 and starttime < ?4)"
				+ " or (starttime < ?3 and endtime > ?4) or (endtime > ?3 and endtime <= ?4)) ";
		Query q = em.createNativeQuery(query);
		java.sql.Time stime = new java.sql.Time(startTime.getTime());
		java.sql.Time etime = new java.sql.Time(endTime.getTime());
		System.out.println(stime.toString());
		q.setParameter(1, room);
		q.setParameter(2, newDays);
		q.setParameter(3, stime);
		q.setParameter(4, etime);
		q.setParameter(5, 1);
		System.out.println(q.toString());
		@SuppressWarnings("unchecked")
		List <java.sql.Date> conflictDays=(List<java.sql.Date>) q.getResultList();
		
		return conflictDays;
	}
public List<Reservation> getReservations(Date startDay, Date endDay){
	List<Reservation> res = new ArrayList<>(); 
	java.sql.Date sqlStartDay = new java.sql.Date(startDay.getTime());
	java.sql.Date sqlEndDay = new java.sql.Date(endDay.getTime());
//	String query = "select reservation.*, reservationchild.id , reservationchild.idreservation as chIdRes, reservationchild.startdate as res_std, reservationchild.enddate as resEnddate, reservationchild.starttime as resStTime, "
//			+ "reservationchild.endtime as rchEndTime, room.idroom, room.name, room.idblock, room.idbuilding, room.idfloor,"
//			+ " room.capacity, room.adresse, room.telephone, room.videoproj, room.pontteleph"
//			+ " , room.visio  from reservationchild left join reservation ON ( reservationchild.idreservation" + 
//			"= reservation.idreservation and reservation.enddate >= ?1) inner join room ON (room.idroom = reservation.room) "
//			+ "inner join typereservation ON (reservation.typeres = typereservation.id) inner join visibilitytype ON "
//			+ "(reservation.visibilitytyperes = visibilitytype.id) "
//			+ "  where reservationchild.startdate >= ?1 and reservationchild.enddate <= ?2";
	//String query = "select * from reservationchild where reservationchild.startdate >= ?1 and reservationchild.enddate <= ?2";
	String query = "select reservation.*, room.*, user.*, participant.idparticipant"
			+ ", participant.reservationid, participant.intern, participant.mail as partEm, participant.name as partName, "
			+ "participant.obligatoire from reservation inner join reservationchild "
			+ "ON (reservation.idreservation= reservationchild.idreservation and "
			+ "reservationchild.startdate >= ?1 and reservationchild.enddate <= ?2) "
			+ "inner join room ON (room.idroom = reservation.room) inner join user ON ( "
			+ "reservation.reservedby= user.id) "
			+ "inner join participant ON ( participant.reservationid = reservation.idreservation )"
			+ " union select reservation.*, room.*, user.* , participant.idparticipant,"
			+ "  participant.reservationid, participant.intern, participant.mail as partEm, participant.name as partName, "
			+ "participant.obligatoire from reservation "
			+ "inner join room ON (room.idroom = reservation.room) inner join user ON ( "
			+ " reservation.reservedby= user.id) inner join participant ON (participant.reservationid = reservation.idreservation)"
			+ " where reservation.simple= ?3 and "
			+ " reservation.enddate >= ?1" ;
	Query q = em.createNativeQuery(query,"CurrentReservations");
	q.setParameter(1, sqlStartDay);
	q.setParameter(2,sqlEndDay);
	q.setParameter(3, 1);
	@SuppressWarnings("unchecked")
	List<Object[]> tuples = q.getResultList();
	for (Object[] r : tuples) {
		res.add((Reservation)r[0]);
	}
	return res;
}
}
