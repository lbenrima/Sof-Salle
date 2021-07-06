package tn.sofrecom.mdrissi.services.room;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.sofrecom.mdrissi.entities.Reservation;
import tn.sofrecom.mdrissi.entities.Room;
import tn.sofrecom.mdrissi.repositories.RoomRepository;

@Service
public class RoomServiceImpl implements RoomService {

	@Autowired
	RoomRepository roomRepo;

	public List<Room> getAllRooms() {
		// return roomRepo.findAll()
		return roomRepo.findByIdroomNotNull();
	}
	

	public String addRoom(Room room) {
		String result = "";
		Room savedRoom = roomRepo.save(room);
		if (savedRoom != null) {
			result = "Successfully added";
		} else {
			result = "Failure to add, please try again ! ";
		}

		return result;
	}

	public String updateRoom(int id, Room room) {
		String result = "";

		Room r = roomRepo.findOne(id);
		if (r != null) {
			room.setIdroom(id);
			roomRepo.save(room);

			result = "{\"data\": \"Successfully updated\"}";
		} else {
			result = "{\"data\": \"Failure to update, please try again !\"}";
		}

		return result;
	}

	public String deleteRoom(int id) {

		String result = "";
		Room r = roomRepo.findOne(id);
		if (r != null) {
			roomRepo.delete(id);
			result = "{\"data\": \"Successfully deleted\"}";
		} else {
			result = "{\"data\": \"Failure to delete, please try again !\"}";
		}
		return result;
	}

	@Override
	public List<Room> getAvailableRooms(Reservation res) {

		if (res.getAcceptConflict()) {
			System.out.println("accpet Confilcts");
			return roomRepo.findByIdroomNotNull();
		}
		List<Room> room = new ArrayList<>();
		if (res.getSimple()) { // simple
			room = roomRepo.getAvailableRooms(Arrays.asList(res.getStartdate()), Arrays.asList(res.getEnddate()),
					new java.sql.Time(res.getStarttime().getTime()), new java.sql.Time(res.getEndtime().getTime()));
		} else if (res.getDaily()) { // daily
			LocalDate startDay = res.getStartdate().toInstant().atZone(ZoneId.of("Africa/Tunis")).toLocalDate();
			LocalDate endDay = res.getEnddate().toInstant().atZone(ZoneId.of("Africa/Tunis")).toLocalDate();
			List<Date> days = Stream.iterate(startDay, d -> d.plusDays(1))
					.limit(ChronoUnit.DAYS.between(startDay, endDay.plusDays(1)))
					.map(ld -> Date.from(ld.atStartOfDay(ZoneId.of("Africa/Tunis")).toInstant()))
					.collect(Collectors.toList());
			room = roomRepo.getAvailableRooms(days, days, new java.sql.Time(res.getStarttime().getTime()),
					new java.sql.Time(res.getEndtime().getTime()));

		} else if (res.getWeekly()) { // weekly

			Calendar c = Calendar.getInstance();
			c.setTime(res.getStartdate());
			List<Date> resrDate = new ArrayList<>();
			int reservationDay = c.get(Calendar.DAY_OF_WEEK);
			res.getDaysList().forEach(d -> {
				int addedDays = 0;
				if (d.getNumberd() + 1 < reservationDay) {
					c.setTime(res.getStartdate());
					addedDays = 7 - reservationDay + d.getNumberd() + 1;
					c.add(Calendar.DATE, addedDays);
					resrDate.add(c.getTime());
				} else {
					c.setTime(res.getStartdate());
					addedDays = d.getNumberd() + 1 - reservationDay;
					c.add(Calendar.DATE, addedDays);
					resrDate.add(c.getTime());
				}
			});
			List<Date> days = new ArrayList<>();
			LocalDate endDay = res.getEnddate().toInstant().atZone(ZoneId.of("Africa/Tunis")).toLocalDate();
			resrDate.forEach(d -> {
				LocalDate startDay = d.toInstant().atZone(ZoneId.of("Africa/Tunis")).toLocalDate();
				while (startDay.isBefore(endDay.plusDays(1))) {
					days.add(Date.from(startDay.atStartOfDay(ZoneId.of("Africa/Tunis")).toInstant()));
					startDay = startDay.plusDays(7);
				}
			});
			room = roomRepo.getAvailableRooms(days, days, new java.sql.Time(res.getStarttime().getTime()),
					new java.sql.Time(res.getEndtime().getTime()));

		} else if (res.getMonthly()) {
			List<Date> resrDate = new ArrayList<>();
			LocalDate endDay = res.getEnddate().toInstant().atZone(ZoneId.of("Africa/Tunis")).toLocalDate();
			LocalDate startDay = res.getStartdate().toInstant().atZone(ZoneId.of("Africa/Tunis")).toLocalDate();
			if (res.isDayofmonth()) {// monthly day of the month
				while (startDay.isBefore(endDay.plusDays(1))) {
					resrDate.add(Date.from(startDay.atStartOfDay(ZoneId.of("Africa/Tunis")).toInstant()));
					startDay = startDay.plusMonths(1);
				}
				room = roomRepo.getAvailableRooms(resrDate, resrDate, new java.sql.Time(res.getStarttime().getTime()),
						new java.sql.Time(res.getEndtime().getTime()));

			} else {
				Calendar cal = Calendar.getInstance();
				cal.setTime(res.getStartdate());
				int week = cal.get(Calendar.WEEK_OF_MONTH);
				int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
				while (startDay.isBefore(endDay.plusDays(1))) {
					resrDate.add(Date.from(startDay.atStartOfDay(ZoneId.of("Africa/Tunis")).toInstant()));
					startDay = startDay.plusMonths(1);
					startDay = startDay.with(TemporalAdjusters.dayOfWeekInMonth(week, DayOfWeek.of(dayOfWeek)));
				}
				room = roomRepo.getAvailableRooms(resrDate, resrDate, new java.sql.Time(res.getStarttime().getTime()),
						new java.sql.Time(res.getEndtime().getTime()));

			}
		} else { // yearly to do
			room = roomRepo.getAvailableRooms(Arrays.asList(res.getStartdate()), Arrays.asList(res.getEnddate()),
					new java.sql.Time(res.getStarttime().getTime()), new java.sql.Time(res.getEndtime().getTime()));

		}

		return room;
	}

}
