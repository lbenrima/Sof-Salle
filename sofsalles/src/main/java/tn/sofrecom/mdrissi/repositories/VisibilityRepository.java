package tn.sofrecom.mdrissi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.sofrecom.mdrissi.entities.Visibilitytype;

@Repository
public interface VisibilityRepository extends JpaRepository<Visibilitytype,Integer> {

}
