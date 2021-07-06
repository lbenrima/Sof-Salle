package tn.sofrecom.mdrissi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tn.sofrecom.mdrissi.entities.Room;
import tn.sofrecom.mdrissi.entities.User;
import tn.sofrecom.mdrissi.services.user.UserService;

@RestController
@RequestMapping(value = "/v1")
public class PreferredCalendarController {

	@Autowired 
	private UserService userService;
	@GetMapping("/prefCalendar")
	ResponseEntity<List<Room>> getPreferedCalendarRooms(@RequestParam String mail){
		User user = userService.findUserByEmail(mail);
		return new ResponseEntity<List<Room>>(user.getRooms(), HttpStatus.OK);
	}
	@PostMapping("/prefCalendar")
	ResponseEntity<List<Room>> addPreferedCalendarRooms(@RequestBody User user ){
		User upUser = userService.findUserByEmail(user.getMail());
		upUser.setRooms(user.getRooms());
		User newUse = userService.save(upUser);
		return new ResponseEntity<List<Room>>(newUse.getRooms(), HttpStatus.OK);
	}
}
