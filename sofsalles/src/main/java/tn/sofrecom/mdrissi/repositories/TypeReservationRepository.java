package tn.sofrecom.mdrissi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.sofrecom.mdrissi.entities.Typereservation;


@Repository
public interface TypeReservationRepository extends JpaRepository<Typereservation,Integer>{
}
