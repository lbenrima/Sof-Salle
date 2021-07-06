package tn.sofrecom.mdrissi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tn.sofrecom.mdrissi.services.collaborateurs.Collaborateur;
import tn.sofrecom.mdrissi.services.collaborateurs.CollaborateursService;

@RestController
@RequestMapping(value = "/v1/liste-collabs")
public class CollaborateursController {
	
	@Autowired
	CollaborateursService collabService;
	
	@GetMapping(value="/all")
	public List<Collaborateur> getAll(){
		List<Collaborateur> liste = collabService.getAll();
		return liste;
	}

}
