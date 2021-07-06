package tn.sofrecom.mdrissi.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tn.sofrecom.mdrissi.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User,Integer>{

//	@Query("select u from User u where u.mail = ?1")
//	User findByMail(String mail);
	@EntityGraph(attributePaths = {"rooms.idbuilding","rooms.idfloor","rooms.idblock"})
	User findDistinctByMail(String mail);
	
	}
