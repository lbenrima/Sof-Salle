package tn.sofrecom.mdrissi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.sofrecom.mdrissi.entities.Reservation;
import tn.sofrecom.mdrissi.entities.Selectedday;

@Repository
public interface SelectedDayRepository extends JpaRepository<Selectedday,Integer>{
	
 List<Selectedday> findByIdreservation(Reservation res);
}
