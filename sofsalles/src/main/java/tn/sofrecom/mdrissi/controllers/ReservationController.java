package tn.sofrecom.mdrissi.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tn.sofrecom.mdrissi.dto.DayDTO;
import tn.sofrecom.mdrissi.dto.Days;
import tn.sofrecom.mdrissi.dto.NewReservationDTO;
import tn.sofrecom.mdrissi.dto.ReservationDTO;
import tn.sofrecom.mdrissi.entities.Reservation;
import tn.sofrecom.mdrissi.entities.Room;
import tn.sofrecom.mdrissi.repositories.ReservationRepository;
import tn.sofrecom.mdrissi.services.reservation.ReservationDtoService;
import tn.sofrecom.mdrissi.services.reservation.ReservationService;
import tn.sofrecom.mdrissi.services.user.UserService;

@RestController
@RequestMapping(value = "/v1/reservation")
public class ReservationController {

	@Autowired
	private ReservationService reservationService;

	@Autowired
	private ReservationRepository reservationRepo;

	@Autowired
	private ReservationDtoService resDtoService;

	@Autowired
	private UserService userService;

	@PostMapping(value = "/create")
	public String createSimpleReservation(@RequestBody NewReservationDTO newRes) {

		newRes.getReservation().setReservedby(userService.findUserByEmail(newRes.getUser().getMail()));

		Reservation res = reservationService.addSimpleReservation(newRes);

		if (res == null) {
			return "Conflits !";
		}
		return "Success reservation !!";

	}

	@PostMapping(value = "/createDailyReservation")
	public ResponseEntity<List<java.sql.Date>> createDailyReservation(@RequestBody NewReservationDTO newRes) {

		newRes.getReservation().setReservedby(userService.findUserByEmail(newRes.getUser().getMail()));
		List<java.sql.Date> conflictDays = reservationService.addDailyReservation(newRes);

		return new ResponseEntity<List<java.sql.Date>>(conflictDays, HttpStatus.OK);

	}

	@PostMapping("/createWeeklyReservation")
	public ResponseEntity<List<java.sql.Date>> createWeeklyReservation(@RequestBody NewReservationDTO newRes) {

		newRes.getWeeklyRes().getReservation().setReservedby(userService.findUserByEmail(newRes.getUser().getMail()));
		List<java.sql.Date> conflictDays = reservationService.addWeeklyReservation(newRes);

		return new ResponseEntity<List<java.sql.Date>>(conflictDays, HttpStatus.OK);

	}

	@PostMapping("/createMonthlyReservation")
	public ResponseEntity<List<java.sql.Date>> createMonthlyReservation(@RequestBody NewReservationDTO newRes) {

		newRes.getReservation().setReservedby(userService.findUserByEmail(newRes.getUser().getMail()));
		List<java.sql.Date> conflictDays = reservationService.addMonthlyReservation(newRes);

		return new ResponseEntity<List<java.sql.Date>>(conflictDays, HttpStatus.OK);

	}

	@PostMapping("/createYearlyReservation")
	public String createYearlyReservation(@RequestBody NewReservationDTO newRes) {

		newRes.getReservation().setReservedby(userService.findUserByEmail(newRes.getUser().getMail()));
		Reservation res = reservationService.addYearlyReservation(newRes);

		if (res == null) {

			return "Conflits !";

		}
		return "Succès reservation !!";
	}

	@PostMapping(value = "/listofroomnotconflit")
	public List<Room> getListeOfRoomNotConflit(@RequestBody Reservation reservation) {

		return reservationService.getListeRoomNotConflit(reservation);

	}

	/* update reservations */

	@PutMapping(value = "/updateSimple/{id}")
	public Reservation updateSimpleReservation(@PathVariable Integer id, @RequestBody Reservation reservation) {

		if (id != null) {
			Reservation p = reservationRepo.findOne(id);
			if (p != null) {
				reservation.setIdreservation(id);
				reservationRepo.save(reservation);
			}
		}

		return reservation;
	}

	/* delete reservations */

	@DeleteMapping(value = "/delete/{id}")
	public String deleteSimpleReservation(@PathVariable Integer id) {
		if (id != null) {
			Reservation p = reservationRepo.findOne(id);
			if (p != null) {
				Reservation res = reservationService.deleteSimpleReservation(p);
				System.out.println("UidMail = : " + p.getUidmail());
				reservationRepo.delete(id);
				return "{\"data\": \"Succès\"}";
			} else
				return "{\"data\": \"échec\"}";
		} else
			return "{\"data\": \"échec\"}";
	}
	
	@DeleteMapping(value="/deleteOccurance")
	public ResponseEntity<String> deleteOccranceReservation(@RequestParam Long id){
		boolean result = reservationService.deleteReservationOccurance(id);
		if(result) {
			return new ResponseEntity<String>( "{\"data\": \"Succès\"}", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("{\"data\": \"échec\"}", HttpStatus.NOT_FOUND);
		}
	}

	/* return Lists events/reservatiosn/days */

	@GetMapping(value = "/all")
	public List<Reservation> getAll() {

		reservationService.checkAndSet();

		return reservationService.getAll();
	}

	@GetMapping(value = "/allEvents")
	public List<ReservationDTO> getEvents(@RequestParam String start, @RequestParam String end) {
		// return resDtoService.getAll();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = new Date();
		Date endDate = new Date();
		try {
			startDate = formatter.parse(start);
			endDate = formatter.parse(end);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return resDtoService.getByStartEndDate(startDate, endDate);

	}

	@GetMapping(value = "/allEventsByRoom")
	public List<ReservationDTO> getEventsPerRoom(@RequestParam List<String> listroom, @RequestParam String start,
			@RequestParam String end) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = new Date();
		Date endDate = new Date();
		try {
			startDate = formatter.parse(start);
			endDate = formatter.parse(end);
		} catch (ParseException e) {
			e.printStackTrace();

		}
		List<ReservationDTO> res = resDtoService.getByStartEndDateOfRooms(listroom, startDate, endDate);
		return res;
	}

	@GetMapping(value = "/days")
	public List<DayDTO> getDays() {

		Days d = new Days();
		return d.getDaysLst();
	}

	@GetMapping(value = "/res/{id}")
	public Reservation getReservationById(@PathVariable Integer id) {
		if (reservationService.getReservation(id) != null) {
			Reservation res = reservationService.getReservation(id);

			return res;
		} else
			return null;
	}

	@GetMapping(value = "/res")
	public ResponseEntity<List<ReservationDTO>> getUserReservation(@RequestParam String start, @RequestParam String end,
			@RequestParam String email) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = new Date();
		Date endDate = new Date();
		try {
			startDate = formatter.parse(start);
			endDate = formatter.parse(end);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<ReservationDTO> res = resDtoService.getActiveReservationOfUser(email, startDate, endDate);
		return new ResponseEntity<List<ReservationDTO>>(res, HttpStatus.OK);
	}
}
