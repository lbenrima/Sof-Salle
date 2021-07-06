package tn.sofrecom.mdrissi.services.childreservation;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.sofrecom.mdrissi.dto.DayDTO;
import tn.sofrecom.mdrissi.dto.WeeklyRes;
import tn.sofrecom.mdrissi.entities.Reservation;
import tn.sofrecom.mdrissi.entities.Reservationchild;
import tn.sofrecom.mdrissi.repositories.ReservationRepository;
import tn.sofrecom.mdrissi.repositories.ReservationchildRepository;

@Service
public class ChildReservationServiceImpl implements ChildReservationService {

	@Autowired
	ReservationchildRepository childRepo;

	@Autowired
	private ReservationRepository resRepo;


	public List<java.sql.Date> saveDailyReservation(Reservation res) {
		List<Date> days = new ArrayList<>();

		LocalDate startDay = res.getStartdate().toInstant().atZone(ZoneId.of("Africa/Tunis")).toLocalDate();
		if (res.getEnddate() != null) {
			LocalDate endDay = res.getEnddate().toInstant().atZone(ZoneId.of("Africa/Tunis")).toLocalDate();
			days = Stream.iterate(startDay, d -> d.plusDays(1))
					.limit(ChronoUnit.DAYS.between(startDay, endDay.plusDays(1)))
					.map(ld -> Date.from(ld.atStartOfDay(ZoneId.of("Africa/Tunis")).toInstant()))
					.collect(Collectors.toList());
		} else if (res.getReccurenceNumber() != 0) {
			days = Stream.iterate(startDay, d -> d.plusDays(1)).limit(res.getReccurenceNumber())
					.map(ld -> Date.from(ld.atStartOfDay(ZoneId.of("Africa/Tunis")).toInstant()))
					.collect(Collectors.toList());
		} else {
			days = Stream.iterate(startDay, d -> d.plusDays(1))
					.limit(ChronoUnit.DAYS.between(startDay, startDay.plusYears(1)))
					.map(ld -> Date.from(ld.atStartOfDay(ZoneId.of("Africa/Tunis")).toInstant()))
					.collect(Collectors.toList());
		}
		List<java.sql.Date> newDays = days.stream().map(d-> new java.sql.Date(d.getTime())).collect(Collectors.toList());
		List<java.sql.Date> conflictDays = resRepo.getConflictDays(days, res.getStarttime(), res.getEndtime(),
				res.getRoom().getIdroom());
		System.out.println("List changed "+newDays.removeAll(conflictDays));

		List<java.sql.Date> tempDays = new ArrayList<>();
		tempDays.addAll(newDays);
		newDays.forEach(d->{
			conflictDays.forEach(cd ->{
				if (d.toString().equals(cd.toString())) {					
					tempDays.remove(d);				
				}
			});
			
		});
		tempDays.forEach(day -> {
			System.out.println("saved day " + day.toString());
			Reservationchild reservationChild = new Reservationchild();
			reservationChild.setIdreservation(res);
			reservationChild.setEndtime(res.getEndtime());
			reservationChild.setStarttime(res.getStarttime());
			reservationChild.setStartdate(day);
			reservationChild.setEnddate(day);
			childRepo.save(reservationChild);
		});
		if (!conflictDays.isEmpty()) {
			conflictDays.forEach(day -> System.out.println("not saved days " + day.toString()));
		}
		res.setReccurenceNumber(days.size());
		return conflictDays;
	}

	public List<java.sql.Date> saveWeeklyReservation(Reservation res) {
		List<Date> days = new ArrayList<>();

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
		if (res.getEnddate() != null) {
			System.out.println("save weeklyRes enddate !null");
			List<Date> lDays = new ArrayList<>();
			LocalDate endDay = res.getEnddate().toInstant().atZone(ZoneId.of("Africa/Tunis")).toLocalDate();
			resrDate.forEach(d -> {
				LocalDate stDay = d.toInstant().atZone(ZoneId.of("Africa/Tunis")).toLocalDate();
				while (stDay.isBefore(endDay.plusDays(1))) {
					lDays.add(Date.from(stDay.atStartOfDay(ZoneId.of("Africa/Tunis")).toInstant()));
					stDay = stDay.plusDays(7);
				}
			});

			days.addAll(lDays);
			days.forEach(d-> System.out.println("actual days "+ d.toString()));
			
		} else if (res.getReccurenceNumber() != 0) {
			List<Date> lDays = new ArrayList<>();
			resrDate.forEach(d -> {
				LocalDate stDay = d.toInstant().atZone(ZoneId.of("Africa/Tunis")).toLocalDate();
				while (stDay.isBefore(stDay.plusWeeks(res.getReccurenceNumber()))) {
					lDays.add(Date.from(stDay.atStartOfDay(ZoneId.of("Africa/Tunis")).toInstant()));
					stDay = stDay.plusDays(7);
				}
			});
			days.addAll(lDays);
		} else {
			List<Date> lDays = new ArrayList<>();
			resrDate.forEach(d -> {
				LocalDate stDay = d.toInstant().atZone(ZoneId.of("Africa/Tunis")).toLocalDate();
				while (stDay.isBefore(stDay.plusYears(1))) {
					lDays.add(Date.from(stDay.atStartOfDay(ZoneId.of("Africa/Tunis")).toInstant()));
					stDay = stDay.plusDays(7);
				}
			});
			days.addAll(lDays);
		}

		List<java.sql.Date> conflictDays = resRepo.getConflictDays(days, res.getStarttime(), res.getEndtime(),
				res.getRoom().getIdroom());
		if(!conflictDays.isEmpty()) {
			conflictDays.forEach(d-> System.out.println("conflict day"+ d.toString()));
		}
		days.forEach(d-> System.out.println("actual days "+ d.toString()));
		List<java.sql.Date> newDays = days.stream().map(d-> new java.sql.Date(d.getTime())).collect(Collectors.toList());
		newDays.removeAll(conflictDays);
		List<java.sql.Date> tempDays = new ArrayList<>();
		tempDays.addAll(newDays);
		newDays.forEach(d->{
			conflictDays.forEach(cd ->{
				if (d.toString().equals(cd.toString())) {					
					tempDays.remove(d);				
				}
			});
			
		});
		tempDays.forEach(day -> {
			System.out.println("saved day " + day.toString());
			Reservationchild reservationChild = new Reservationchild();
			reservationChild.setIdreservation(res);
			reservationChild.setEndtime(res.getEndtime());
			reservationChild.setStarttime(res.getStarttime());
			reservationChild.setStartdate(day);
			reservationChild.setEnddate(day);
			childRepo.save(reservationChild);
		});
		if (!conflictDays.isEmpty()) {
			conflictDays.forEach(day -> System.out.println("not saved days " + day.toString()));
		}
		res.setReccurenceNumber(days.size());
		return conflictDays;
	}

	public void saveweeklyReservation(WeeklyRes weeklyRes) {

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
			reschild.setIdreservation(res);
			Date date = Date.from(liste.get(i).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
			reschild.setStartdate(date);
			reschild.setEnddate(date);
			reschild.setStarttime(res.getStarttime());
			reschild.setEndtime(res.getEndtime());
			childRepo.save(reschild);

		}

	}
	public List<java.sql.Date> saveMonthlyReservation(Reservation res) {
		List<Date> days = new ArrayList<>();
		LocalDate endDay = res.getEnddate().toInstant().atZone(ZoneId.of("Africa/Tunis")).toLocalDate();
		LocalDate startDay = res.getStartdate().toInstant().atZone(ZoneId.of("Africa/Tunis")).toLocalDate();
		if (res.isDayofmonth()) {// monthly day of the month
			while (startDay.isBefore(endDay.plusDays(1))) {
				days.add(Date.from(startDay.atStartOfDay(ZoneId.of("Africa/Tunis")).toInstant()));
				startDay = startDay.plusMonths(1);
			}
			
		} else {
			System.out.println("save Monthly child day of the week");
			Calendar cal = Calendar.getInstance();
			cal.setTime(res.getStartdate());
			int week = cal.get(Calendar.WEEK_OF_MONTH);
			int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
			while (startDay.isBefore(endDay.plusDays(1))) {
				days.add(Date.from(startDay.atStartOfDay(ZoneId.of("Africa/Tunis")).toInstant()));
				startDay = startDay.plusMonths(1);
				startDay = startDay.with(TemporalAdjusters.dayOfWeekInMonth(week, DayOfWeek.of(dayOfWeek)));
			}
	}
		List<java.sql.Date> conflictDays = resRepo.getConflictDays(days, res.getStarttime(), res.getEndtime(),
				res.getRoom().getIdroom());
		List<java.sql.Date> newDays = days.stream().map(d-> new java.sql.Date(d.getTime())).collect(Collectors.toList());
		newDays.removeAll(conflictDays);
		List<java.sql.Date> tempDays = new ArrayList<>();
		tempDays.addAll(newDays);
		newDays.forEach(d->{
			conflictDays.forEach(cd ->{
				if (d.toString().equals(cd.toString())) {					
					tempDays.remove(d);				
				}
			});
			
		});
		tempDays.forEach(day -> {
			System.out.println("saved day " + day.toString());
			Reservationchild reservationChild = new Reservationchild();
			reservationChild.setIdreservation(res);
			reservationChild.setEndtime(res.getEndtime());
			reservationChild.setStarttime(res.getStarttime());
			reservationChild.setStartdate(day);
			reservationChild.setEnddate(day);
			childRepo.save(reservationChild);
		});
		if (!conflictDays.isEmpty()) {
			conflictDays.forEach(day -> System.out.println("not saved days " + day.toString()));
		}
		res.setReccurenceNumber(days.size());
		return conflictDays;
	}
	public void savemonthlyReservation(Reservation res) {

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

		else if (res.getEnddate() != null) {
			// creating list of repeating monthly event when end date is specified

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
			reschild.setIdreservation(res);
			Date date = Date.from(liste.get(i).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
			reschild.setStartdate(date);
			reschild.setEnddate(date);
			reschild.setStarttime(res.getStarttime());
			reschild.setEndtime(res.getEndtime());
			childRepo.save(reschild);

		}

	}

	public void saveyearlyReservation(Reservation res) {

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
			reschild.setIdreservation(res);
			Date date = Date.from(liste.get(i).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
			reschild.setStartdate(date);
			reschild.setEnddate(date);
			reschild.setStarttime(res.getStarttime());
			reschild.setEndtime(res.getEndtime());
			childRepo.save(reschild);

		}
	}

	@Override
	public List<Reservationchild> getReservationsOfRooms(List<String> rooms, Date start, Date end) {
		java.sql.Date startDate = new java.sql.Date(start.getTime());
		java.sql.Date endDate = new java.sql.Date(end.getTime());
		return childRepo.findDistinctByIdreservationRoomNameInAndStartdateLessThanEqualAndStartdateGreaterThanEqual(rooms, endDate, startDate);
	}

}
