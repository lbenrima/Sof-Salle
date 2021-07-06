package tn.sofrecom.mdrissi.repositories;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.sofrecom.mdrissi.entities.Reservation;
import tn.sofrecom.mdrissi.entities.Reservationchild;
import tn.sofrecom.mdrissi.entities.Room;

@Repository
public interface ReservationchildRepository extends JpaRepository<Reservationchild, Long> {

	List<Reservationchild> findByIdreservation(Reservation res);

	List<Reservationchild> findByIdreservationAndStartdateLessThanEqualAndStartdateGreaterThanEqual(Reservation res,
			Date start, Date end);

	@EntityGraph(attributePaths = { "idreservation", "idreservation.room", "idreservation.reservedby",
			"idreservation.typeres", "idreservation.visibilitytyperes", "idreservation.participantList" })
	List<Reservationchild> findDistinctByStartdateLessThanEqualAndStartdateGreaterThanEqual(Date start, Date end);
	
	@EntityGraph(attributePaths = { "idreservation", "idreservation.room", "idreservation.reservedby",
			"idreservation.typeres", "idreservation.visibilitytyperes", "idreservation.participantList" })
	List<Reservationchild> findDistinctByIdreservationRoomAndStartdateLessThanEqualAndStartdateGreaterThanEqual(Room room, Date start, Date end);
	
	@EntityGraph(attributePaths = { "idreservation", "idreservation.room", "idreservation.reservedby",
			"idreservation.typeres", "idreservation.visibilitytyperes", "idreservation.participantList" })
	List<Reservationchild> findDistinctByIdreservationRoomNameInAndStartdateLessThanEqualAndStartdateGreaterThanEqual(List<String> rooms, Date start, Date end);
	
	@EntityGraph(attributePaths = { "idreservation", "idreservation.room", "idreservation.reservedby",
			"idreservation.typeres", "idreservation.visibilitytyperes", "idreservation.participantList" })
	List<Reservationchild> findDistinctByIdreservationReservedbyMailAndStartdateLessThanEqualAndStartdateGreaterThanEqual(String mail, Date start, Date end);
}
