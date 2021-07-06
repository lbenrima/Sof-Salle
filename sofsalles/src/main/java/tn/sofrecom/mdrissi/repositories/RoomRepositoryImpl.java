package tn.sofrecom.mdrissi.repositories;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;



import tn.sofrecom.mdrissi.entities.Room;

public class RoomRepositoryImpl implements RoomRepositoryCustom {
	@PersistenceContext
	private EntityManager em;

	/*Get all available rooms without conflicts*/
	@Override
	public List<Room> getAvailableRooms(List<Date> startDay, List<Date> endDay, Time startTime, Time endTime) {
		if (startDay.isEmpty()) {
			return null;
		}
		List<java.sql.Date> sqlStartDays = startDay.stream().map(d-> new java.sql.Date(d.getTime())).collect(Collectors.toList());
		List<java.sql.Date> sqlEndDays = endDay.stream().map(d-> new java.sql.Date(d.getTime())).collect(Collectors.toList());
		String sqlQuery = "select r.*, floor.idfloor as idfloor_floor, floor.numfloor,"
				+ " floor.description, building.idbuiding, building.namebuilding, building.description"
				+ " as building_desc, block.idblock as blockId, block.nameblock, block.description "
				+ "as blockDescr " + "from room r left join floor ON r.idfloor = floor.idfloor"
				+ " left join building ON r.idbuilding = building.idbuiding"
				+ " left join block ON r.idblock = block.idblock where r.idroom not in ("
				+ " select  room from reservation where startdate in ( ?1 ) and enddate in ( ?4 )"
				+ " and ( starttime = ?2 or (?3 > starttime and starttime > ?2)   or ( endtime > ?2 and endtime <= ?3)"
				+ " or ( starttime < ?2 and endtime > ?3 ) " + " )  " + "union"
				+ " select  room from reservation, reservationchild"
				+ " where reservation.idreservation = reservationchild.idreservation"
				+ " and reservationchild.startdate in ( ?1 ) and reservationchild.enddate in ( ?4 )"
				+ " and ( reservationchild.starttime = ?2 or (reservationchild.starttime >  ?2 and reservationchild.starttime < ?3) or"
				+ " (reservationchild.endtime > ?2 and reservationchild.endtime <= ?3 ) "
				+ "or ( reservationchild.starttime < ?2 and reservationchild.endtime > ?3 ) " + " )) order by r.idroom";
		// Query q = em.createNativeQuery(sqlQuery,Room.class);
		
		Query q = em.createNativeQuery(sqlQuery,"availableRooms");
		q.setParameter(1, sqlStartDays);
		q.setParameter(2, startTime);
		q.setParameter(3, endTime);
		q.setParameter(4, sqlEndDays);
		List<Room> rooms = new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<Object[]> tuples = q.getResultList();
		for(Object[] t : tuples) {
			Room r = (Room) t[0];
			rooms.add(r);
		};
		
		
		// String query = "select r from Room as r join fetch r.idbuilding join fetch"
		// + " r.idfloor join fetch r.idblock"
		// + " where r.idroom not in (" + " select distinct room from Reservation where
		// "
		// + "startdate in ( ?1 ) and enddate in ( ?4 ) " + " and ( starttime between ?2
		// and ?3 or "
		// + "endtime between ?2 and ?3 ))";
		//
		// String secondQuery = "select r from Room as r join fetch r.idbuilding join
		// fetch"
		// + " r.idfloor join fetch r.idblock" + " where r.idroom not in ("
		// + " select distinct room from Reservation as rz where rz.idreservation in ( "
		// + " select idreservation from Reservationchild as rzch where"
		// + " rzch.startdate in ( ?1 )" + " and rzch.enddate in ( ?4 )"
		// + " and (rzch.starttime between ?2 and ?3 "
		// + " or rzch.endtime between ?2 and ?3 ) ))";

		return rooms;
	}

}
