package tn.sofrecom.mdrissi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.sofrecom.mdrissi.entities.Floor;


@Repository
public interface FloorRepository extends JpaRepository<Floor,Integer>{

}
