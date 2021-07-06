package tn.sofrecom.mdrissi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.sofrecom.mdrissi.entities.Building;

@Repository
public interface BuildingRepository extends JpaRepository<Building,Integer>{

}
