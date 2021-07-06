package tn.sofrecom.mdrissi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.sofrecom.mdrissi.entities.Participant;
import tn.sofrecom.mdrissi.entities.Reservation;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant,Integer>{

	List<Participant> findByInternTrueAndReservationid(Reservation res);
	
	List<Participant> findByInternFalseAndReservationid(Reservation res);
	
}
