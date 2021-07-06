package tn.sofrecom.mdrissi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.sofrecom.mdrissi.entities.Members;

@Repository
public interface MembersRepository extends JpaRepository<Members,Integer> {

}
