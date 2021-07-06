package tn.sofrecom.mdrissi.services.reservation;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.sofrecom.mdrissi.dto.DayDTO;
import tn.sofrecom.mdrissi.dto.NewReservationDTO;
import tn.sofrecom.mdrissi.dto.WeeklyRes;
import tn.sofrecom.mdrissi.entities.Participant;
import tn.sofrecom.mdrissi.entities.Reservation;
import tn.sofrecom.mdrissi.entities.Reservationchild;
import tn.sofrecom.mdrissi.entities.Room;
import tn.sofrecom.mdrissi.entities.Selectedday;
import tn.sofrecom.mdrissi.mail.DailyMailService;
import tn.sofrecom.mdrissi.mail.MonthlyMailService;
import tn.sofrecom.mdrissi.mail.SendMail;
import tn.sofrecom.mdrissi.mail.SimpleMailService;
import tn.sofrecom.mdrissi.mail.WeeklyMailService;
import tn.sofrecom.mdrissi.mail.YearlyMailService;
import tn.sofrecom.mdrissi.repositories.ParticipantRepository;
import tn.sofrecom.mdrissi.repositories.ReservationRepository;
import tn.sofrecom.mdrissi.repositories.ReservationchildRepository;
import tn.sofrecom.mdrissi.repositories.SelectedDayRepository;
import tn.sofrecom.mdrissi.services.childreservation.ChildReservationService;
import tn.sofrecom.mdrissi.services.collaborateurs.Collaborateur;
import tn.sofrecom.mdrissi.services.room.RoomService;
import tn.sofrecom.mdrissi.services.user.UserService;

/**
 * @author m.drissi
 *
 */
@Service
public class ReservationServiceImpl implements ReservationService {

	@Autowired
	UserService userService;

	@Autowired
	SimpleMailService simpleMS;

	@Autowired
	DailyMailService dailyMS;

	@Autowired
	WeeklyMailService weeklyMS;

	@Autowired
	MonthlyMailService monthlyMS;

	@Autowired
	YearlyMailService yearlyMS;

	@Autowired
	SendMail mail;
@Autowired
SelectedDayRepository selectedDayRepository;
	@Autowired
	ReservationRepository reservationRepo;

	@Autowired
	ParticipantRepository participantRepo;

	@Autowired
	ChildReservationService childService;

	@Autowired
	ReservationchildRepository childRepo;

	@Autowired
	SelectedDayRepository sdayRepo;

	@Autowired
	RoomService roomservice;

	public List<String> getListFrom(String str) {
		if (str == null) {
			return new ArrayList<String>();
		}
		List<String> result = new ArrayList<String>();
		int i = 0;
		int length = str.length();
		while (i < length) {
			if (str.indexOf(',') == -1) {
				result.add(str);
				break;
			}
			;
			String retrievedStr = str.substring(i, str.indexOf(','));
			result.add(retrievedStr);
			str = str.substring(str.indexOf(',') + 1);
			length = str.length();
		}

		return result;
	}

	public void saveInvited(String invited, Reservation res) {

		if (getListFrom(invited) != null) {
			List<String> listInvited = getListFrom(invited);

			for (int i = 0; i < listInvited.size(); i++) {
				Participant pc = new Participant();
				String firstName = listInvited.get(i).substring(0, listInvited.get(i).indexOf('.'));
				String lastName = listInvited.get(i).substring(listInvited.get(i).indexOf('.') + 1,
						listInvited.get(i).indexOf('@'));
				String name = firstName + lastName.toUpperCase();

				pc.setMail(listInvited.get(i));
				pc.setName(name);
				pc.setIntern(false);// invité
				pc.setReservationid(res);

				participantRepo.save(pc);
			}

			// SendMail mail = new SendMail();
			try {
				mail.sendToInvited(invited, res);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}

	public Reservation addSimpleReservation(NewReservationDTO newRes) {

		Reservation res = newRes.getReservation();
		int randomNum = ThreadLocalRandom.current().nextInt(10000, 9999999);
		String uidmail = simpleMS.dateToUTC(new Date(), new Date()) + "-"
				+ simpleMS.dateToUTC(res.getStartdate(), res.getStarttime()) + "-" + res.getReservedby().getId() + "-"
				+ randomNum;
		res.setUidmail(uidmail);
		List<Collaborateur> participants = newRes.getCollabs();
		String invited = newRes.getMails();
		System.out.println(invited);

		if (invited != null) {
			List<String> listInvited = getListFrom(invited);
			for (int i = 0; i < listInvited.size(); i++) {
				System.out.println(listInvited.get(i) + "***************");
			}
		}
		res.setAcceptConflict(false);
		List<Room> rooms = roomservice.getAvailableRooms(res);
		if(!rooms.contains(res.getRoom())) {
			System.out.println("Conflict for simple reservation");
			return null;
		}
		Reservation savedRes = reservationRepo.save(res);
		savedRes.setReccurenceNumber(0);
		// childService.savedailyReservation(savedRes);
		// saveInvited(invited,res);
		if (invited != null) {
			List<String> listInvited = getListFrom(invited);
			savedRes.setNumberpart(listInvited.size());
			for (int i = 0; i < listInvited.size(); i++) {
				System.out.println(listInvited.get(i) + "***************");
				Participant pc = new Participant();
				String firstName = listInvited.get(i).substring(0, listInvited.get(i).indexOf('.'));
				String lastName = listInvited.get(i).substring(listInvited.get(i).indexOf('.') + 1,
						listInvited.get(i).indexOf('@'));
				String name = firstName + lastName.toUpperCase();

				pc.setMail(listInvited.get(i));
				pc.setName(name);
				pc.setIntern(false);// invité
				pc.setReservationid(res);
				pc.setObligatoire(true);
				participantRepo.save(pc);
			}
		}

		for (int i = 0; i < participants.size(); i++) {
			Participant pt = new Participant();
			pt.setReservationid(savedRes);
			pt.setMail(participants.get(i).getMail());
			String fName = participants.get(i).getFirstName();
			String first = fName.substring(0, 1).toUpperCase();
			fName = first + fName.substring(1);
			pt.setName(participants.get(i).getLastName().toUpperCase());
			pt.setIntern(true);// collaborateur de sofrecom
			pt.setObligatoire(participants.get(i).getObligatoire());
			participantRepo.save(pt);
		}

		List<Participant> parts = participantRepo.findByInternTrueAndReservationid(savedRes);

		List<String> listInvited = getListFrom(invited);
		for (int i = 0; i < listInvited.size(); i++) {
			System.out.println(listInvited.get(i) + "***************");
			Participant pc = new Participant();
			String firstName = listInvited.get(i).substring(0, listInvited.get(i).indexOf('.'));
			String lastName = listInvited.get(i).substring(listInvited.get(i).indexOf('.') + 1,
					listInvited.get(i).indexOf('@'));
			String name = firstName + " " + lastName.toUpperCase();

			pc.setMail(listInvited.get(i));
			pc.setName(name);
			pc.setIntern(false);// invité
			pc.setReservationid(res);
			pc.setObligatoire(true);
			parts.add(pc);
		}
		int nbr = 0;// savedRes.getNumberpart();
		savedRes.setNumberpart(parts.size() + nbr);
		System.out.println(nbr);
		System.out.println(parts.size());
		try {
			// SimpleMailService simpleMS = new SimpleMailService();
			System.out.println("aaaaaaaa");
			simpleMS.sendSimple(savedRes, parts, 1);
			System.out.println("aaaaaaaa");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return savedRes;

	}

	public List<java.sql.Date> addDailyReservation(NewReservationDTO newRes) {
		final int CREATE = 1;
		Reservation res = newRes.getReservation();
		int randomNum = ThreadLocalRandom.current().nextInt(10000, 9999999);
		String uidmail = simpleMS.dateToUTC(new Date(), new Date()) + "-"
				+ simpleMS.dateToUTC(res.getStartdate(), res.getStarttime()) + "-" + res.getReservedby().getId() + "-"
				+ randomNum;
		res.setUidmail(uidmail);
		List<Collaborateur> participants = newRes.getCollabs();
		String invited = newRes.getMails();
		System.out.println(invited);
		if (invited != null) {
			List<String> listInvited = getListFrom(invited);
			for (int i = 0; i < listInvited.size(); i++) {
				System.out.println(listInvited.get(i) + "***************");
			}
		}

		Reservation savedRes = reservationRepo.save(res);
		List<java.sql.Date> conflictDays = childService.saveDailyReservation(savedRes);
		StringBuffer description = new StringBuffer();
		if (!conflictDays.isEmpty()) {
			if (conflictDays.size() > 1) {
				description.append("La salle n'est pas reservé pour les jours ");
			} else {
				description.append("La salle n'est pas reservé pour le jour ");
			}

			description.append(conflictDays.stream().map(Date::toString).collect(Collectors.joining(", ")));

			description.append(" \n");
		}
		description.append(savedRes.getDescription());
		savedRes.setDescription(description.toString());
		// saveInvited(invited, res);
		if (invited != null) {
			List<String> listInvited = getListFrom(invited);
			savedRes.setNumberpart(listInvited.size());
			for (int i = 0; i < listInvited.size(); i++) {
				System.out.println(listInvited.get(i) + "***************");
				Participant pc = new Participant();
				String firstName = listInvited.get(i).substring(0, listInvited.get(i).indexOf('.'));
				String lastName = listInvited.get(i).substring(listInvited.get(i).indexOf('.') + 1,
						listInvited.get(i).indexOf('@'));
				String name = firstName + lastName.toUpperCase();

				pc.setMail(listInvited.get(i));
				pc.setName(name);
				pc.setIntern(false);// invité
				pc.setReservationid(res);
				pc.setObligatoire(true);
				participantRepo.save(pc);
			}
		}
		for (int i = 0; i < participants.size(); i++) {
			Participant pt = new Participant();
			pt.setReservationid(savedRes);
			pt.setMail(participants.get(i).getMail());
			String fName = participants.get(i).getFirstName();
			String first = fName.substring(0, 1).toUpperCase();
			fName = first + fName.substring(1);
			pt.setName(participants.get(i).getLastName().toUpperCase() + " " + fName);
			pt.setIntern(true);// collaborateur de sofrecom
			participantRepo.save(pt);
		}

		List<Participant> parts = participantRepo.findByInternTrueAndReservationid(savedRes);

		List<String> listInvited = getListFrom(invited);
		for (int i = 0; i < listInvited.size(); i++) {
			System.out.println(listInvited.get(i) + "***************");
			Participant pc = new Participant();
			String firstName = listInvited.get(i).substring(0, listInvited.get(i).indexOf('.'));
			String lastName = listInvited.get(i).substring(listInvited.get(i).indexOf('.') + 1,
					listInvited.get(i).indexOf('@'));
			String name = firstName + " " + lastName.toUpperCase();

			pc.setMail(listInvited.get(i));
			pc.setName(name);
			pc.setIntern(false);// invité
			pc.setReservationid(res);
			pc.setObligatoire(true);
			parts.add(pc);
		}
		int nbr = 0;// savedRes.getNumberpart();
		savedRes.setNumberpart(parts.size() + nbr);
		System.out.println(nbr);
		System.out.println(parts.size());

		if (savedRes.getEnddate() != null) {
			System.out.println("Reserved by end date :" + savedRes.getEnddate());
			try {
				// DailyMailService dailyMS = new DailyMailService();
				dailyMS.sendDailyWithEnddate(savedRes, parts, CREATE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (savedRes.getReccurenceNumber() != 0) {
			System.out.println("Reserved by ReccurenceNumber :" + savedRes.getReccurenceNumber());

			try {
				// DailyMailService dailyMS = new DailyMailService();
				dailyMS.sendDailyWithnumberRec(savedRes, parts);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (savedRes.getReccurenceNumber() == 0 && savedRes.getEnddate() == null) {
			System.out.println("Reserved by no end");
			// savedRes.setReccurenceNumber(lc.size());
			try {
				// DailyMailService dailyMS = new DailyMailService();
				dailyMS.sendDailyWithnumberRec(savedRes, parts);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return conflictDays;

	}

	public List<java.sql.Date> addWeeklyReservation(NewReservationDTO newRes) {

		WeeklyRes weeklyRes = newRes.getWeeklyRes();
		List<Collaborateur> participants = newRes.getCollabs();
		String invited = newRes.getMails();
		System.out.println(invited);

		if (invited != null) {
			List<String> listInvited = getListFrom(invited);
			for (int i = 0; i < listInvited.size(); i++) {
				System.out.println(getListFrom(invited).get(i) + "***************");
			}
		}

		List<DayDTO> sdays = new ArrayList<DayDTO>();
		sdays = weeklyRes.getSelected();
		Reservation res = weeklyRes.getReservation();
		int randomNum = ThreadLocalRandom.current().nextInt(10000, 9999999);
		String uidmail = simpleMS.dateToUTC(new Date(), new Date()) + "-"
				+ simpleMS.dateToUTC(res.getStartdate(), res.getStarttime()) + "-" + res.getReservedby().getId() + "-"
				+ randomNum;
		res.setUidmail(uidmail);

		/*
		 * List<Reservationchild> lc = this.listWeeklyChildren(weeklyRes);
		 * 
		 * boolean cf = false;
		 * 
		 * for (int i = 0; i < lc.size(); i++) { cf = this.checkConflicts(res,
		 * lc.get(i)); if (cf == true) { break; } }
		 * 
		 * if (cf == false) {
		 */
		Reservation savedRes = reservationRepo.save(res);
		for (int i = 0; i < sdays.size(); i++) {
			System.out.println(sdays.get(i));
			Selectedday day = new Selectedday();
			day.setIdreservation(savedRes);
			day.setName(sdays.get(i).getName());
			day.setNumberd(sdays.get(i).getNumberd());
			sdayRepo.save(day);
		}
		// childService.saveweeklyReservation(weeklyRes);
		List<java.sql.Date> conflictDays = childService.saveWeeklyReservation(weeklyRes.getReservation());
		StringBuffer description = new StringBuffer();
		if (!conflictDays.isEmpty()) {
			if (conflictDays.size() > 1) {
				description.append("La salle n'est pas reservé pour les jours ");
			} else {
				description.append("La salle n'est pas reservé pour le jour ");
			}
			description.append(conflictDays.stream().map(Date::toString).collect(Collectors.joining(", ")));

			description.append(" \n");
		}
		description.append(weeklyRes.getReservation().getDescription());
		weeklyRes.getReservation().setDescription(description.toString());

		// saveInvited(invited, res);
		// if (invited != null) {
		// savedRes.setNumberpart(getListFrom(invited).size());
		// }
		if (invited != null) {
			List<String> listInvited = getListFrom(invited);
			savedRes.setNumberpart(listInvited.size());
			for (int i = 0; i < listInvited.size(); i++) {
				System.out.println(listInvited.get(i) + "***************");
				Participant pc = new Participant();
				String firstName = listInvited.get(i).substring(0, listInvited.get(i).indexOf('.'));
				String lastName = listInvited.get(i).substring(listInvited.get(i).indexOf('.') + 1,
						listInvited.get(i).indexOf('@'));
				String name = firstName + lastName.toUpperCase();

				pc.setMail(listInvited.get(i));
				pc.setName(name);
				pc.setIntern(false);// invité
				pc.setReservationid(res);
				pc.setObligatoire(true);
				participantRepo.save(pc);
			}
		}

		for (int i = 0; i < participants.size(); i++) {
			Participant pt = new Participant();
			pt.setReservationid(savedRes);
			pt.setMail(participants.get(i).getMail());
			String fName = participants.get(i).getFirstName();
			String first = fName.substring(0, 1).toUpperCase();
			fName = first + fName.substring(1);
			pt.setName(participants.get(i).getLastName().toUpperCase() + " " + fName);
			pt.setIntern(true);// collaborateur de sofrecom
			participantRepo.save(pt);
		}

		List<Participant> parts = participantRepo.findByInternTrueAndReservationid(savedRes);

		List<String> listInvited = getListFrom(invited);
		for (int i = 0; i < listInvited.size(); i++) {
			System.out.println(listInvited.get(i) + "***************");
			Participant pc = new Participant();
			String firstName = listInvited.get(i).substring(0, listInvited.get(i).indexOf('.'));
			String lastName = listInvited.get(i).substring(listInvited.get(i).indexOf('.') + 1,
					listInvited.get(i).indexOf('@'));
			String name = firstName + " " + lastName.toUpperCase();

			pc.setMail(listInvited.get(i));
			pc.setName(name);
			pc.setIntern(false);// invité
			pc.setReservationid(res);
			pc.setObligatoire(true);
			parts.add(pc);
		}
		int nbr = 0;// savedRes.getNumberpart();
		savedRes.setNumberpart(parts.size() + nbr);
		System.out.println(nbr);
		System.out.println(parts.size());

		if (savedRes.getEnddate() != null) {
			System.out.println("End date =" + savedRes.getEnddate());
			try {
				// WeeklyMailService weeklyMS = new WeeklyMailService();
				weeklyMS.sendWeeklyWithEnddate(weeklyRes, parts, 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		else if (savedRes.getReccurenceNumber() != 0) {
			System.out.println("occuurencce number =" + savedRes.getReccurenceNumber());
			try {
				// WeeklyMailService weeklyMS = new WeeklyMailService();
				weeklyMS.sendWeeklyWithnumberRec(weeklyRes, parts);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		else {
			// weeklyRes.getReservation().setReccurenceNumber(lc.size());
			try {
				// WeeklyMailService weeklyMS = new WeeklyMailService();
				weeklyMS.sendWeeklyWithnumberRec(weeklyRes, parts);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return conflictDays;
		// } else
		// return null;
	}

	public List<java.sql.Date> addMonthlyReservation(NewReservationDTO newRes) {
		System.out.println("add monthly reservation ReservationServiceImpl");
		Reservation res = newRes.getReservation();
		int randomNum = ThreadLocalRandom.current().nextInt(10000, 9999999);
		String uidmail = simpleMS.dateToUTC(new Date(), new Date()) + "-"
				+ simpleMS.dateToUTC(res.getStartdate(), res.getStarttime()) + "-" + res.getReservedby().getId() + "-"
				+ randomNum;
		res.setUidmail(uidmail);
		List<Collaborateur> participants = newRes.getCollabs();
		String invited = newRes.getMails();
		System.out.println(invited);

		if (invited != null) {
			List<String> listInvited = getListFrom(invited);
			for (int i = 0; i < listInvited.size(); i++) {
				System.out.println(getListFrom(invited).get(i) + "***************");
			}
		}

		/*
		 * List<Reservationchild> lc = this.listMonthlyChildren(res);
		 * 
		 * boolean cf = false; for (int i = 0; i < lc.size(); i++) { cf =
		 * this.checkConflicts(res, lc.get(i)); if (cf == true) { break; } }
		 * 
		 * if (cf == false) {
		 */
		Reservation savedRes = reservationRepo.save(res);
		// childService.savemonthlyReservation(savedRes);
		List<java.sql.Date> conflictDays = childService.saveMonthlyReservation(savedRes);
		StringBuffer description = new StringBuffer();
		if (!conflictDays.isEmpty()) {
			if (conflictDays.size() > 1) {
				description.append("La salle n'est pas reservé pour les jours ");
			} else {
				description.append("La salle n'est pas reservé pour le jour ");
			}
			description.append(conflictDays.stream().map(Date::toString).collect(Collectors.joining(", ")));

			description.append(" \n");
		}
		description.append(savedRes.getDescription());
		savedRes.setDescription(description.toString());
		// saveInvited(invited, res);
		if (invited != null) {
			List<String> listInvited = getListFrom(invited);
			savedRes.setNumberpart(listInvited.size());
			for (int i = 0; i < listInvited.size(); i++) {
				System.out.println(listInvited.get(i) + "***************");
				Participant pc = new Participant();
				String firstName = listInvited.get(i).substring(0, listInvited.get(i).indexOf('.'));
				String lastName = listInvited.get(i).substring(listInvited.get(i).indexOf('.') + 1,
						listInvited.get(i).indexOf('@'));
				String name = firstName + lastName.toUpperCase();

				pc.setMail(listInvited.get(i));
				pc.setName(name);
				pc.setIntern(false);// invité
				pc.setReservationid(res);
				pc.setObligatoire(true);
				participantRepo.save(pc);
			}
		}

		for (int i = 0; i < participants.size(); i++) {
			Participant pt = new Participant();
			pt.setReservationid(savedRes);
			pt.setMail(participants.get(i).getMail());
			String fName = participants.get(i).getFirstName();
			String first = fName.substring(0, 1).toUpperCase();
			fName = first + fName.substring(1);
			pt.setName(participants.get(i).getLastName().toUpperCase() + " " + fName);
			pt.setIntern(true);// collaborateur de sofrecom
			participantRepo.save(pt);
		}

		List<Participant> parts = participantRepo.findByInternTrueAndReservationid(savedRes);

		List<String> listInvited = getListFrom(invited);
		for (int i = 0; i < listInvited.size(); i++) {
			System.out.println(listInvited.get(i) + "***************");
			Participant pc = new Participant();
			String firstName = listInvited.get(i).substring(0, listInvited.get(i).indexOf('.'));
			String lastName = listInvited.get(i).substring(listInvited.get(i).indexOf('.') + 1,
					listInvited.get(i).indexOf('@'));
			String name = firstName + " " + lastName.toUpperCase();

			pc.setMail(listInvited.get(i));
			pc.setName(name);
			pc.setIntern(false);// invité
			pc.setReservationid(res);
			pc.setObligatoire(true);
			parts.add(pc);
		}
		int nbr = 0;// savedRes.getNumberpart();
		savedRes.setNumberpart(parts.size() + nbr);
		System.out.println(nbr);
		System.out.println(parts.size());

		if (savedRes.getEnddate() != null) {
			System.out.println("***********1************");
			try {
				// MonthlyMailService monthlyMS = new MonthlyMailService();
				monthlyMS.sendMonthlyWithEnddate(savedRes, parts,1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		else if (savedRes.getReccurenceNumber() != 0) {
			System.out.println("***************2**************");
			try {
				// MonthlyMailService monthlyMS = new MonthlyMailService();
				monthlyMS.sendMonthlyWithnumberRec(savedRes, parts);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// savedRes.setReccurenceNumber(lc.size());
			System.out.println("***************3**************");
			try {
				// MonthlyMailService monthlyMS = new MonthlyMailService();
				monthlyMS.sendMonthlyWithnumberRec(savedRes, parts);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return conflictDays;
		// } else
		// return null;
	}

	public Reservation addYearlyReservation(NewReservationDTO newRes) {

		// TO DO

		/*
		 * Reservation res = newRes.getReservation(); int randomNum =
		 * ThreadLocalRandom.current().nextInt(10000, 9999999); String uidmail =
		 * simpleMS.dateToUTC(new Date(), new Date()) + "-" +
		 * simpleMS.dateToUTC(res.getStartdate(), res.getStarttime()) + "-" +
		 * res.getReservedby().getId() + "-" + randomNum; res.setUidmail(uidmail);
		 * List<Collaborateur> participants = newRes.getCollabs(); String invited =
		 * newRes.getMails(); System.out.println(invited);
		 * 
		 * if (invited != null) { List<String> listInvited = getListFrom(invited); for
		 * (int i = 0; i < listInvited.size(); i++) {
		 * System.out.println(getListFrom(invited).get(i) + "***************"); } }
		 * 
		 * List<Reservationchild> lc = this.listYearlyChildren(res);
		 * 
		 * boolean cf = false; for (int i = 0; i < lc.size(); i++) { cf =
		 * this.checkConflicts(res, lc.get(i)); if (cf == true) { break; } }
		 * 
		 * if (cf == false) {
		 * 
		 * Reservation savedRes = reservationRepo.save(res);
		 * childService.saveyearlyReservation(savedRes);
		 * 
		 * // saveInvited(invited, res);
		 * 
		 * // if (invited != null) { //
		 * savedRes.setNumberpart(getListFrom(invited).size()); // }
		 * 
		 * if (invited != null) { List<String> listInvited = getListFrom(invited);
		 * savedRes.setNumberpart(listInvited.size()); for (int i = 0; i <
		 * listInvited.size(); i++) { System.out.println(listInvited.get(i) +
		 * "***************"); Participant pc = new Participant(); String firstName =
		 * listInvited.get(i).substring(0, listInvited.get(i).indexOf('.')); String
		 * lastName = listInvited.get(i).substring(listInvited.get(i).indexOf('.') + 1,
		 * listInvited.get(i).indexOf('@')); String name = firstName +
		 * lastName.toUpperCase();
		 * 
		 * pc.setMail(listInvited.get(i)); pc.setName(name); pc.setIntern(false);//
		 * invité pc.setReservationid(res); pc.setObligatoire(true);
		 * participantRepo.save(pc); } }
		 * 
		 * for (int i = 0; i < participants.size(); i++) { Participant pt = new
		 * Participant(); pt.setReservationid(savedRes); ;
		 * pt.setMail(participants.get(i).getMail()); String fName =
		 * participants.get(i).getFirstName(); String first = fName.substring(0,
		 * 1).toUpperCase(); fName = first + fName.substring(1);
		 * pt.setName(participants.get(i).getLastName().toUpperCase() + " " + fName);
		 * pt.setIntern(true);// collaborateur de sofrecom participantRepo.save(pt); }
		 * 
		 * List<Participant> parts =
		 * participantRepo.findByInternTrueAndReservationid(savedRes);
		 * 
		 * List<String> listInvited = getListFrom(invited); for (int i = 0; i <
		 * listInvited.size(); i++) { System.out.println(listInvited.get(i) +
		 * "***************"); Participant pc = new Participant(); String firstName =
		 * listInvited.get(i).substring(0, listInvited.get(i).indexOf('.')); String
		 * lastName = listInvited.get(i).substring(listInvited.get(i).indexOf('.') + 1,
		 * listInvited.get(i).indexOf('@')); String name = firstName + " " +
		 * lastName.toUpperCase();
		 * 
		 * pc.setMail(listInvited.get(i)); pc.setName(name); pc.setIntern(false);//
		 * invité pc.setReservationid(res); pc.setObligatoire(true); parts.add(pc); }
		 * int nbr = 0;// savedRes.getNumberpart(); savedRes.setNumberpart(parts.size()
		 * + nbr); System.out.println(nbr); System.out.println(parts.size());
		 * 
		 * if (savedRes.getReccurenceNumber() == 0 && savedRes.getEnddate() != null) {
		 * System.out.println("End date =" + savedRes.getEnddate()); try { //
		 * YearlyMailService yearlyMS = new YearlyMailService();
		 * yearlyMS.sendYearlyWithEnddate(savedRes, parts); } catch (Exception e) {
		 * e.printStackTrace(); } }
		 * 
		 * if (savedRes.getReccurenceNumber() != 0 && savedRes.getEnddate() == null) {
		 * System.out.println("occuurencce number =" + savedRes.getReccurenceNumber());
		 * try { // YearlyMailService yearlyMS = new YearlyMailService();
		 * yearlyMS.sendYearlyWithnumberRec(savedRes, parts); } catch (Exception e) {
		 * e.printStackTrace(); } }
		 * 
		 * if (savedRes.getReccurenceNumber() == 0 && savedRes.getEnddate() == null) {
		 * savedRes.setReccurenceNumber(lc.size()); System.out.println("no End =" +
		 * savedRes.getReccurenceNumber()); try { // MonthlyMailService monthlyMS = new
		 * MonthlyMailService(); yearlyMS.sendYearlyWithnumberRec(savedRes, parts); }
		 * catch (Exception e) { e.printStackTrace(); } }
		 * 
		 * return savedRes; } else
		 */
		return null;
	}

	@Override
	public List<Reservationchild> listSimpleChildren(Reservation res) {
		List<Reservationchild> ret = new ArrayList<Reservationchild>();

		List<LocalDate> liste = new ArrayList<LocalDate>();

		int freq = res.getFrequency();
		System.out.println("freq : " + freq);

		Date start = res.getStartdate();
		SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");

		String datestart = sdfs.format(start); // 2017-08-02

		String startday = datestart.substring(datestart.lastIndexOf('-') + 1, datestart.length());
		String startmonth = datestart.substring(datestart.indexOf('-') + 1, datestart.lastIndexOf('-'));
		String startyear = datestart.substring(0, datestart.indexOf('-'));

		String starts = startyear + startmonth + startday;
		System.out.println(starts);

		LocalDate startdate = LocalDate.of(Integer.parseInt(startyear), Integer.parseInt(startmonth),
				Integer.parseInt(startday));
		System.out.println(startdate);
		// creating list of repeating daily event when end date is specified
		Date end = res.getEnddate();
		String dateend = sdfs.format(end);

		String endday = dateend.substring(dateend.lastIndexOf('-') + 1, dateend.length());
		String endmonth = dateend.substring(dateend.indexOf('-') + 1, dateend.lastIndexOf('-'));
		String endyear = dateend.substring(0, dateend.indexOf('-'));

		String ends = endyear + endmonth + endday;
		System.out.println(ends);

		LocalDate enddate = LocalDate.of(Integer.parseInt(endyear), Integer.parseInt(endmonth),
				Integer.parseInt(endday));
		System.out.println(enddate);

		LocalDate nextdate = startdate;
		while (nextdate.isBefore(enddate)) {
			liste.add(nextdate);
			nextdate = nextdate.plus(freq, ChronoUnit.DAYS);
		}

		for (int i = 0; i < liste.size(); i++) {
			Reservationchild reschild = new Reservationchild();
			reschild.setIdreservation(null);
			Date date = Date.from(liste.get(i).atStartOfDay().atZone(ZoneId.of("Africa/Tunis")).toInstant());
			reschild.setStartdate(date);
			reschild.setEnddate(date);
			reschild.setStarttime(res.getStarttime());
			reschild.setEndtime(res.getEndtime());
			ret.add(reschild);
		}
		return ret;
	}

	@Override
	public List<Reservationchild> listDailyChildren(Reservation res) {

		List<Reservationchild> ret = new ArrayList<Reservationchild>();

		List<LocalDate> liste = new ArrayList<LocalDate>();

		int freq = res.getFrequency();
		System.out.println("freq : " + freq);

		Date start = res.getStartdate();
		SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");

		String datestart = sdfs.format(start); // 2017-08-02

		String startday = datestart.substring(datestart.lastIndexOf('-') + 1, datestart.length());
		String startmonth = datestart.substring(datestart.indexOf('-') + 1, datestart.lastIndexOf('-'));
		String startyear = datestart.substring(0, datestart.indexOf('-'));

		String starts = startyear + startmonth + startday;
		System.out.println(starts);

		LocalDate startdate = LocalDate.of(Integer.parseInt(startyear), Integer.parseInt(startmonth),
				Integer.parseInt(startday));
		System.out.println(startdate);

		if (res.getReccurenceNumber() != 0) { // creating list of repeating daily event when number of recc is specified
			int nOr = res.getReccurenceNumber();
			System.out.println("nor : " + nOr);
			LocalDate nextdate = startdate;
			for (int i = 1; i <= nOr; i++) {
				liste.add(nextdate);
				nextdate = nextdate.plus(freq, ChronoUnit.DAYS);

			}
		} else if (res.getEnddate() != null) { // creating list of repeating daily event when end date is specified
			Date end = res.getEnddate();
			String dateend = sdfs.format(end);

			String endday = dateend.substring(dateend.lastIndexOf('-') + 1, dateend.length());
			String endmonth = dateend.substring(dateend.indexOf('-') + 1, dateend.lastIndexOf('-'));
			String endyear = dateend.substring(0, dateend.indexOf('-'));

			String ends = endyear + endmonth + endday;
			System.out.println(ends);

			LocalDate enddate = LocalDate.of(Integer.parseInt(endyear), Integer.parseInt(endmonth),
					Integer.parseInt(endday));
			System.out.println(enddate);

			LocalDate nextdate = startdate;
			while (nextdate.isBefore(enddate)) {
				liste.add(nextdate);
				nextdate = nextdate.plus(freq, ChronoUnit.DAYS);
			}
		} else {
			// creating list of repeating daily event when no end date/number of occ is
			// specified
			LocalDate limit = startdate.plusYears(1);
			LocalDate nextdate = startdate;
			while (nextdate.isBefore(limit)) {
				liste.add(nextdate);
				nextdate = nextdate.plus(freq, ChronoUnit.DAYS);
			}
		}

		for (int i = 0; i < liste.size(); i++) {
			Reservationchild reschild = new Reservationchild();
			reschild.setIdreservation(null);
			Date date = Date.from(liste.get(i).atStartOfDay().atZone(ZoneId.of("Africa/Tunis")).toInstant());
			reschild.setStartdate(date);
			reschild.setEnddate(date);
			reschild.setStarttime(res.getStarttime());
			reschild.setEndtime(res.getEndtime());
			ret.add(reschild);
		}
		return ret;
	}

	@Override
	public List<Reservationchild> listWeeklyChildren(WeeklyRes weeklyRes) {

		List<Reservationchild> ret = new ArrayList<Reservationchild>();
		Reservation res = weeklyRes.getReservation();
		List<DayDTO> sdays = weeklyRes.getSelected();

		List<LocalDate> liste = new ArrayList<LocalDate>();

		int freq = res.getFrequency();
		System.out.println("freq : " + freq);

		Date start = res.getStartdate();
		SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");

		String datestart = sdfs.format(start); // 2017-08-02

		String startday = datestart.substring(datestart.lastIndexOf('-') + 1, datestart.length());
		String startmonth = datestart.substring(datestart.indexOf('-') + 1, datestart.lastIndexOf('-'));
		String startyear = datestart.substring(0, datestart.indexOf('-'));

		String starts = startyear + startmonth + startday;
		System.out.println(starts);

		LocalDate startdate = LocalDate.of(Integer.parseInt(startyear), Integer.parseInt(startmonth),
				Integer.parseInt(startday));
		System.out.println(startdate);

		if (res.getReccurenceNumber() != 0) { // creating list of repeating weekly event when number of recc is
												// specified
			for (int i = 0; i < sdays.size(); i++) {
				int nOr = res.getReccurenceNumber();
				System.out.println("nor : " + nOr);
				LocalDate nextdate = startdate.with(DayOfWeek.of(sdays.get(i).getNumberd()));
				System.out.println(nextdate);
				for (int j = 1; j <= nOr; j++) {
					liste.add(nextdate);
					nextdate = nextdate.plus(freq, ChronoUnit.WEEKS);
				}
			}
		}

		else if (res.getEnddate() != null) { // creating list of repeating weekly event when end date is specified
			Date end = res.getEnddate();
			String dateend = sdfs.format(end);
			String endday = dateend.substring(dateend.lastIndexOf('-') + 1, dateend.length());
			String endmonth = dateend.substring(dateend.indexOf('-') + 1, dateend.lastIndexOf('-'));
			String endyear = dateend.substring(0, dateend.indexOf('-'));
			String ends = endyear + endmonth + endday;
			System.out.println(ends);
			LocalDate enddate = LocalDate.of(Integer.parseInt(endyear), Integer.parseInt(endmonth),
					Integer.parseInt(endday));
			System.out.println(enddate);

			for (int i = 0; i < sdays.size(); i++) {
				LocalDate nextdate = startdate.with(DayOfWeek.of(sdays.get(i).getNumberd()));
				System.out.println(nextdate);
				while (nextdate.isBefore(enddate)) {
					liste.add(nextdate);
					nextdate = nextdate.plus(freq, ChronoUnit.WEEKS);
				}
			}
		}

		else { // creating list of repeating weekly event when no end date/number of occ is
				// specified
			LocalDate limit = startdate.plusYears(1);
			for (int i = 0; i < sdays.size(); i++) {
				LocalDate nextdate = startdate.with(DayOfWeek.of(sdays.get(i).getNumberd()));
				System.out.println(nextdate);
				while (nextdate.isBefore(limit)) {
					liste.add(nextdate);
					nextdate = nextdate.plus(freq, ChronoUnit.WEEKS);
				}
			}
		}

		for (int i = 0; i < liste.size(); i++) {
			Reservationchild reschild = new Reservationchild();
			reschild.setIdreservation(null);
			Date date = Date.from(liste.get(i).atStartOfDay().atZone(ZoneId.of("Africa/Tunis")).toInstant());
			reschild.setStartdate(date);
			reschild.setEnddate(date);
			reschild.setStarttime(res.getStarttime());
			reschild.setEndtime(res.getEndtime());
			ret.add(reschild);

		}
		return ret;
	}

	@Override
	public List<Reservationchild> listMonthlyChildren(Reservation res) {

		List<Reservationchild> ret = new ArrayList<Reservationchild>();

		List<LocalDate> liste = new ArrayList<LocalDate>();

		int freq = res.getFrequency();
		System.out.println("freq : " + freq);
		boolean dayofmonth = res.isDayofmonth();
		System.out.println("dayofmonth : " + dayofmonth);
		boolean dayofweek = res.isDayofweek();
		System.out.println("dayofweek : " + dayofweek);

		Date start = res.getStartdate();
		SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");

		String datestart = sdfs.format(start);
		String startday = datestart.substring(datestart.lastIndexOf('-') + 1, datestart.length());
		String startmonth = datestart.substring(datestart.indexOf('-') + 1, datestart.lastIndexOf('-'));
		String startyear = datestart.substring(0, datestart.indexOf('-'));

		String starts = startyear + startmonth + startday;
		System.out.println(starts);

		LocalDate startdate = LocalDate.of(Integer.parseInt(startyear), Integer.parseInt(startmonth),
				Integer.parseInt(startday));
		System.out.println(startdate);

		if (res.getReccurenceNumber() != 0) { // creating list of repeating monthly event when number of recc is
												// specified

			int nOr = res.getReccurenceNumber();
			System.out.println("nor : " + nOr);
			LocalDate nextdate = startdate;

			if (dayofmonth == true) {
				int day = startdate.getDayOfMonth();
				for (int i = 1; i <= nOr; i++) {
					liste.add(nextdate);
					nextdate = nextdate.plus(freq, ChronoUnit.MONTHS);
					nextdate = nextdate.withDayOfMonth(day);
				}
			} else if (dayofweek == true) {
				DayOfWeek day = startdate.getDayOfWeek();
				for (int i = 1; i <= nOr; i++) {
					liste.add(nextdate);
					nextdate = nextdate.plus(freq, ChronoUnit.MONTHS);
					nextdate = nextdate.with(day);
				}
			}
		}

		else if (res.getEnddate() != null) { // creating list of repeating monthly event when end date is specified

			Date end = res.getEnddate();
			String dateend = sdfs.format(end);

			String endday = dateend.substring(dateend.lastIndexOf('-') + 1, dateend.length());
			String endmonth = dateend.substring(dateend.indexOf('-') + 1, dateend.lastIndexOf('-'));
			String endyear = dateend.substring(0, dateend.indexOf('-'));

			String ends = endyear + endmonth + endday;
			System.out.println(ends);

			LocalDate enddate = LocalDate.of(Integer.parseInt(endyear), Integer.parseInt(endmonth),
					Integer.parseInt(endday));
			System.out.println(enddate);

			if (dayofmonth) {

				int day = startdate.getDayOfMonth();
				LocalDate nextdate = startdate;
				while (nextdate.isBefore(enddate)) {
					liste.add(nextdate);
					nextdate = nextdate.plus(freq, ChronoUnit.MONTHS);
					nextdate = nextdate.withDayOfMonth(day);
				}

			} else if (dayofweek) {

				DayOfWeek day = startdate.getDayOfWeek();
				LocalDate nextdate = startdate;
				while (nextdate.isBefore(enddate)) {
					liste.add(nextdate);
					nextdate = nextdate.plus(freq, ChronoUnit.MONTHS);
					nextdate = nextdate.with(day);
				}
			}
		}

		else { // creating list of repeating monthly event when no end date/number of occ is
				// specified

			if (dayofmonth) {
				int day = startdate.getDayOfMonth();
				LocalDate limit = startdate.plusYears(1);
				LocalDate nextdate = startdate;
				while (nextdate.isBefore(limit)) {
					liste.add(nextdate);
					nextdate = nextdate.plus(freq, ChronoUnit.MONTHS);
					nextdate = nextdate.withDayOfMonth(day);
				}
			} else if (dayofweek) {
				DayOfWeek day = startdate.getDayOfWeek();
				LocalDate limit = startdate.plusYears(2);
				LocalDate nextdate = startdate;
				while (nextdate.isBefore(limit)) {
					liste.add(nextdate);
					nextdate = nextdate.plus(freq, ChronoUnit.MONTHS);
					nextdate = nextdate.with(day);
				}
			}

		}

		for (int i = 0; i < liste.size(); i++) {

			Reservationchild reschild = new Reservationchild();
			reschild.setIdreservation(null);
			Date date = Date.from(liste.get(i).atStartOfDay().atZone(ZoneId.of("Africa/Tunis")).toInstant());
			reschild.setStartdate(date);
			reschild.setEnddate(date);
			reschild.setStarttime(res.getStarttime());
			reschild.setEndtime(res.getEndtime());
			ret.add(reschild);

		}
		return ret;
	}

	@Override
	public List<Reservationchild> listYearlyChildren(Reservation res) {

		List<Reservationchild> ret = new ArrayList<Reservationchild>();

		List<LocalDate> liste = new ArrayList<LocalDate>();

		int freq = res.getFrequency();
		System.out.println("freq : " + freq);

		Date start = res.getStartdate();
		SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");

		String datestart = sdfs.format(start); // 2017-08-02

		String startday = datestart.substring(datestart.lastIndexOf('-') + 1, datestart.length());
		String startmonth = datestart.substring(datestart.indexOf('-') + 1, datestart.lastIndexOf('-'));
		String startyear = datestart.substring(0, datestart.indexOf('-'));

		String starts = startyear + startmonth + startday;
		System.out.println(starts);

		LocalDate startdate = LocalDate.of(Integer.parseInt(startyear), Integer.parseInt(startmonth),
				Integer.parseInt(startday));
		System.out.println(startdate);

		if (res.getReccurenceNumber() != 0) { // creating list of repeating yearly event when number of recc is
												// specified

			int nOr = res.getReccurenceNumber();
			System.out.println("nor : " + nOr);
			LocalDate nextdate = startdate;
			for (int i = 1; i <= nOr; i++) {
				liste.add(nextdate);
				nextdate = nextdate.plus(freq, ChronoUnit.YEARS);

			}
		}

		else if (res.getEnddate() != null) { // creating list of repeating yearly event when end date is specified

			Date end = res.getEnddate();
			String dateend = sdfs.format(end);

			String endday = dateend.substring(dateend.lastIndexOf('-') + 1, dateend.length());
			String endmonth = dateend.substring(dateend.indexOf('-') + 1, dateend.lastIndexOf('-'));
			String endyear = dateend.substring(0, dateend.indexOf('-'));

			String ends = endyear + endmonth + endday;
			System.out.println(ends);

			LocalDate enddate = LocalDate.of(Integer.parseInt(endyear), Integer.parseInt(endmonth),
					Integer.parseInt(endday));
			System.out.println(enddate);

			LocalDate nextdate = startdate;
			while (nextdate.isBefore(enddate)) {
				liste.add(nextdate);
				nextdate = nextdate.plus(freq, ChronoUnit.YEARS);
			}
		}

		else { // creating list of repeating yearly event when no end date/number of occ is
				// specified

			LocalDate limit = startdate.plusYears(1);
			LocalDate nextdate = startdate;
			while (nextdate.isBefore(limit)) {
				liste.add(nextdate);
				nextdate = nextdate.plus(freq, ChronoUnit.YEARS);
			}
		}

		for (int i = 0; i < liste.size(); i++) {

			Reservationchild reschild = new Reservationchild();
			reschild.setIdreservation(null);
			Date date = Date.from(liste.get(i).atStartOfDay().atZone(ZoneId.of("Africa/Tunis")).toInstant());
			reschild.setStartdate(date);
			reschild.setEnddate(date);
			reschild.setStarttime(res.getStarttime());
			reschild.setEndtime(res.getEndtime());
			ret.add(reschild);

		}
		return ret;

	}

	public void checkAndSet() {
		userService.checkAndSet();
		List<Reservation> listRes = (List<Reservation>) reservationRepo.findAll();
		System.out.println("ls esmpty ?" + listRes.size());
		for (int i = 0; i < listRes.size(); i++) {
			System.out.println("ls esmpty ?" + listRes.get(i).getReservedby().getStateuser());
			System.out.println("2 ?" + listRes.get(i).getReservedby().getStateuser());
			if (listRes.get(i).getReservedby().getStateuser() == false) {
				listRes.get(i).setStateres(false);
			}
			System.out.println("3 ?" + listRes.get(i).getStateres());
		}

	}

	public List<Reservation> getAll() {
		List<Reservation> allList = (List<Reservation>) reservationRepo.findAll();
		System.out.println("getAll Reservation Service ls esmpty ?" + allList.size());

		List<Reservation> activeReservations = new ArrayList<Reservation>();

		for (int i = 0; i < allList.size(); i++) {
			System.out.println("for reservation Service " + allList.get(i).getStateres());

			if (allList.get(i).getStateres() == true) {
				activeReservations.add(allList.get(i));
			}
		}
		return activeReservations;
	}

	public List<Reservation> getActiveReservation(Date start, Date end) {
		// List <ReservationDTO> actualReservations = new ArrayList<>();
		// List<Reservation> res = reservationRepo.getReservations(start, end);
		// return res;
		// res.forEach(r -> {
		// if(r.getSimple()) {
		// ReservationDTO resDto =
		// ReservationMapper.instance.ReservationToReservationDTO(r);
		// actualReservations.add(resDto);
		// }
		// });
		java.sql.Date startDate = new java.sql.Date(start.getTime());
		java.sql.Date endDate = new java.sql.Date(end.getTime());
		// return
		// reservationRepo.findDistinctByEnddateGreaterThanEqualAndStateresTrue(startDate);

		return reservationRepo.findDistinctByEnddateGreaterThanEqualAndEnddateLessThanEqualAndStateresTrueAndSimpleTrue(
				startDate, endDate);
	}

	public Reservation getReservation(Integer id) {
		Reservation res = reservationRepo.getOne(id);
		if (res != null) {
			return res;
		}
		return null;
	}

	@Override
	public Reservation deleteSimpleReservation(Reservation deletRes) {
		try {
			// SimpleMailService simpleMS = new SimpleMailService();
			List<Participant> participant = new ArrayList<>();
			participant.addAll(deletRes.getParticipantList());
			Participant p = new Participant();
			p.setName(deletRes.getReservedby().getName());
			p.setMail(deletRes.getReservedby().getMail());
			p.setObligatoire(true);
			p.setIntern(true);
			participant.add(p);
			deletRes.setStartdate(new java.util.Date(deletRes.getStartdate().getTime()));
			deletRes.setEnddate(new java.util.Date(deletRes.getEnddate().getTime()));
			deletRes.setStarttime(new java.util.Date(deletRes.getStarttime().getTime()));
			deletRes.setEndtime(new java.util.Date(deletRes.getEndtime().getTime()));
			System.out.println("delete réservation uidmail: " + deletRes.getUidmail());
			if (deletRes.getSimple()) {
				simpleMS.sendSimple(deletRes, participant, 0);
			}else if(deletRes.getDaily()) {
				dailyMS.sendDailyWithEnddate(deletRes, participant, 0);
			}else if(deletRes.getWeekly()) {
				List<Selectedday> selectedDays = selectedDayRepository.findByIdreservation(deletRes) ;
				List<DayDTO> selected = new ArrayList<>();
				selectedDays.forEach(day -> {
					selected.add(new DayDTO(day.getName(),day.getNumberd()));
				});
				WeeklyRes res = new WeeklyRes(selected, deletRes);
				weeklyMS.sendWeeklyWithEnddate(res, participant, 0);
			} else if(deletRes.getMonthly()) {
				monthlyMS.sendMonthlyWithEnddate(deletRes, participant, 0);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	@Override
	public List<Room> getListeRoomNotConflit(Reservation res) {
		return roomservice.getAvailableRooms(res);
	}

	@Override
	public List<Reservation> getActiveReservationPerRoom(Room room, Date start, Date end) {
		java.sql.Date startDate = new java.sql.Date(start.getTime());
		java.sql.Date endDate = new java.sql.Date(end.getTime());
		return reservationRepo
				.findDistinctByRoomAndEnddateGreaterThanEqualAndEnddateLessThanEqualAndStateresTrueAndSimpleTrue(room,
						startDate, endDate);
	}

	@Override
	public List<Reservation> getActiveReservationOfUser(String mail, Date startDay, Date endDay) {
		java.sql.Date startDate = new java.sql.Date(startDay.getTime());
		java.sql.Date endDate = new java.sql.Date(endDay.getTime());
		return reservationRepo
				.findDistinctByReservedbyMailAndEnddateGreaterThanEqualAndEnddateLessThanEqualAndStateresTrueAndSimpleTrue(
						mail, startDate, endDate);
	}

	@Override
	public List<Reservationchild> getActiveReservationChild(Date start, Date end) {
		java.sql.Date startDate = new java.sql.Date(start.getTime());
		java.sql.Date endDate = new java.sql.Date(end.getTime());
		return childRepo.findDistinctByStartdateLessThanEqualAndStartdateGreaterThanEqual(endDate, startDate);
	}

	@Override
	public List<Reservationchild> getActiveReservationChildOfUser(String mail, Date start, Date end) {
		java.sql.Date startDate = new java.sql.Date(start.getTime());
		java.sql.Date endDate = new java.sql.Date(end.getTime());
		return childRepo.findDistinctByIdreservationReservedbyMailAndStartdateLessThanEqualAndStartdateGreaterThanEqual(
				mail, endDate, startDate);
	}

	@Override
	public List<Reservationchild> getActiveReservationChildPerRoom(Room room, Date start, Date end) {
		java.sql.Date startDate = new java.sql.Date(start.getTime());
		java.sql.Date endDate = new java.sql.Date(end.getTime());

		return childRepo.findDistinctByIdreservationRoomAndStartdateLessThanEqualAndStartdateGreaterThanEqual(room,
				endDate, startDate);
	}

	@Override
	public List<Reservation> getReservationOfRooms(List<String> rooms, Date start, Date end) {
		java.sql.Date startDate = new java.sql.Date(start.getTime());
		java.sql.Date endDate = new java.sql.Date(end.getTime());
		return reservationRepo
				.findDistinctByRoomNameInAndEnddateGreaterThanEqualAndEnddateLessThanEqualAndStateresTrueAndSimpleTrue(
						rooms, startDate, endDate);
	}

	@Override
	public boolean deleteReservationOccurance(Long idChild) {
		Reservationchild resCh = childRepo.findOne(idChild);
		List<Participant> participant = new ArrayList<>();
		participant.addAll(resCh.getIdreservation().getParticipantList());
		Participant p = new Participant();
		p.setName(resCh.getIdreservation().getReservedby().getName());
		p.setMail(resCh.getIdreservation().getReservedby().getMail());
		resCh.setStartdate(new java.util.Date(resCh.getStartdate().getTime()));
		resCh.setEnddate(new java.util.Date(resCh.getEnddate().getTime()));
		resCh.setStarttime(new java.util.Date(resCh.getStarttime().getTime()));
		resCh.setEndtime(new java.util.Date(resCh.getEndtime().getTime()));
		p.setObligatoire(true);
		p.setIntern(true);
		participant.add(p);
		try {
			simpleMS.deleteOccurence(resCh, participant);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(resCh != null) {
			childRepo.delete(idChild);
			return true;
		}
		return false ;
	}

}
