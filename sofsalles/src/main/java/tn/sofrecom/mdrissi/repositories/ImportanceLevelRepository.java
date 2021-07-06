package tn.sofrecom.mdrissi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.sofrecom.mdrissi.entities.Importancelevel;

@Repository
public interface ImportanceLevelRepository extends JpaRepository<Importancelevel,Integer>{

}
