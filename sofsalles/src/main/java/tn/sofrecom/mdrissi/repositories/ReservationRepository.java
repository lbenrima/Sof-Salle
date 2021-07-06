package tn.sofrecom.mdrissi.repositories;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.sofrecom.mdrissi.entities.Reservation;
import tn.sofrecom.mdrissi.entities.Room;
import tn.sofrecom.mdrissi.entities.User;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Integer>, ReservationRepositoryCustom{
	
	List<Reservation> findBySimpleTrue();
	
	List<Reservation> findByDailyTrue();
	
	List<Reservation> findByMonthlyTrue();
	
	List<Reservation> findByWeeklyTrue();
	
	List<Reservation> findByYearlyTrue();
	@EntityGraph(attributePaths = {"room","reservedby","typeres","visibilitytyperes","participantList"} )
	List<Reservation> findDistinctByEnddateGreaterThanEqualAndStateresTrue(Date start);
	
	@EntityGraph(attributePaths = {"room","reservedby","typeres","visibilitytyperes","participantList"} )
	List<Reservation> findDistinctByReservedbyAndEnddateGreaterThanEqualAndStateresTrue(User user, Date start);
	
	@EntityGraph(attributePaths = {"room","reservedby","typeres","visibilitytyperes","participantList"} )
	List<Reservation> findDistinctByRoomAndEnddateGreaterThanEqualAndEnddateLessThanEqualAndStateresTrueAndSimpleTrue(Room room, Date start, Date end);
	
	@EntityGraph(attributePaths = {"room","reservedby","typeres","visibilitytyperes","participantList"} )
	List<Reservation> findDistinctByRoomNameInAndEnddateGreaterThanEqualAndEnddateLessThanEqualAndStateresTrueAndSimpleTrue(List<String> rooms, Date start, Date end);
	
	@EntityGraph(attributePaths = {"room","reservedby","typeres","visibilitytyperes","participantList"} )
	List<Reservation> findDistinctByEnddateGreaterThanEqualAndEnddateLessThanEqualAndStateresTrueAndSimpleTrue(Date start, Date end);
	
	@EntityGraph(attributePaths = {"room","reservedby","typeres","visibilitytyperes","participantList"} )
	List<Reservation> findDistinctByReservedbyMailAndEnddateGreaterThanEqualAndEnddateLessThanEqualAndStateresTrueAndSimpleTrue(String mail, Date start, Date end);

}
