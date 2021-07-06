package tn.sofrecom.mdrissi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tn.sofrecom.mdrissi.entities.Grparticipants;
import tn.sofrecom.mdrissi.entities.User;

@Repository
public interface GrParticipantsRepository extends JpaRepository<Grparticipants,Integer> {
	
	@Query("select u from Grparticipants u where u.iduser = ?1")
	List<Grparticipants> findByIduser(User user);

}
