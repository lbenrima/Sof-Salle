package tn.sofrecom.mdrissi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tn.sofrecom.mdrissi.dto.GroupeDTO;
import tn.sofrecom.mdrissi.entities.Grparticipants;
import tn.sofrecom.mdrissi.entities.Members;
import tn.sofrecom.mdrissi.entities.User;
import tn.sofrecom.mdrissi.repositories.GrParticipantsRepository;
import tn.sofrecom.mdrissi.repositories.MembersRepository;
import tn.sofrecom.mdrissi.repositories.UserRepository;
import tn.sofrecom.mdrissi.services.user.UserService;

@RestController
@RequestMapping(value = "/v1/group")
public class GroupController {
	
	@Autowired
	private GrParticipantsRepository grp;
	
	@Autowired
	private MembersRepository mem;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepo;

@PostMapping(value = "/create")
public Grparticipants createGroup(@RequestBody GroupeDTO grpDTO) {
	
	grpDTO.getGrp().setIduser(userService.findUser(grpDTO.getUser()));
	Grparticipants gpar = grp.save(grpDTO.getGrp());
	
	for(int i=0; i<grpDTO.getCollabs().size(); i++) {
		Members member = new Members();
		member.setIdgroup(gpar);
		member.setMail(grpDTO.getCollabs().get(i).getMail());
		member.setName(grpDTO.getCollabs().get(i).getFirstName() + " " + grpDTO.getCollabs().get(i).getLastName());
		mem.save(member);
	}
	return gpar;
	
}
	
@PutMapping(value = "/update/{id}")
public Grparticipants updateGroup(@PathVariable Integer id, @RequestBody Grparticipants grpP) {
	
	if(id!=null) {
		Grparticipants gr = grp.findOne(id);
		if(gr != null) {
			grpP.setId(id);
			grp.save(grpP);
		}	
	}
	
	return grpP;
}

@DeleteMapping(value="/delete/{id}")
public void deleteGroup(@PathVariable Integer id){
	if(id!=null) {
		Grparticipants gp = grp.findOne(id);
		if(gp !=null) {
			grp.delete(id);	
		}
	}	
}
//@GetMapping(value="/all/{mail}/:.+")
@GetMapping(value="/all/{mail:.+}")
public List<Grparticipants> getAll(@PathVariable String mail){
	System.out.println(mail);
	User user = userRepo.findDistinctByMail(mail);
	//System.out.println(user.toString());
	return grp.findByIduser(user);
}

}
