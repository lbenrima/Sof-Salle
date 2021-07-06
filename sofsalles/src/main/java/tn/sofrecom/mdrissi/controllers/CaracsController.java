package tn.sofrecom.mdrissi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tn.sofrecom.mdrissi.entities.Importancelevel;
import tn.sofrecom.mdrissi.entities.Typereservation;
import tn.sofrecom.mdrissi.entities.Visibilitytype;
import tn.sofrecom.mdrissi.repositories.ImportanceLevelRepository;
import tn.sofrecom.mdrissi.repositories.TypeReservationRepository;
import tn.sofrecom.mdrissi.repositories.VisibilityRepository;

@RestController
@RequestMapping("/v1/caracs")
public class CaracsController {
	
	@Autowired 
	VisibilityRepository vRepo;
	
	@Autowired
	TypeReservationRepository typeRepo;
	
	@Autowired
	ImportanceLevelRepository imLRepo;	
	
	@GetMapping(value="/imlevel/all")
	public List<Importancelevel> getAllL(){
		
		return imLRepo.findAll();
	}
	
	@GetMapping(value="/vis/all")
	public List<Visibilitytype> getAllV(){
		
		return vRepo.findAll();
	}
	
	@GetMapping(value="/type/all")
	public List<Typereservation> getAllT(){
		
		return typeRepo.findAll();
	}
	

}
