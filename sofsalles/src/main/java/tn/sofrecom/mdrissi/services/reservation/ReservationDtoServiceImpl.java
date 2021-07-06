package tn.sofrecom.mdrissi.services.reservation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.sofrecom.mdrissi.dto.ReservationDTO;
import tn.sofrecom.mdrissi.entities.Reservation;
import tn.sofrecom.mdrissi.entities.Reservationchild;
import tn.sofrecom.mdrissi.entities.Room;
import tn.sofrecom.mdrissi.repositories.ParticipantRepository;
import tn.sofrecom.mdrissi.repositories.ReservationRepository;
import tn.sofrecom.mdrissi.repositories.ReservationchildRepository;
import tn.sofrecom.mdrissi.services.childreservation.ChildReservationService;
import tn.sofrecom.mdrissi.services.user.UserService;

@Service
public class ReservationDtoServiceImpl implements ReservationDtoService {

	@Autowired
	ReservationService reservationService;

	@Autowired
	ReservationRepository reservationRepository;
	@Autowired
	ReservationchildRepository childRepo;
    @Autowired
	private ChildReservationService childReservationService;
	@Autowired
	ParticipantRepository partRepo;
	@Autowired
	UserService userService;

	public List<ReservationDTO> getByStartEndDate(Date start, Date end) {
		List<ReservationDTO> listResDTO = new ArrayList<ReservationDTO>();
		List<Reservation> listeRes = reservationService.getActiveReservation(start, end);
		for (int i = 0; i < listeRes.size(); i++) {
			ReservationDTO newRes = new ReservationDTO();
			newRes.setAllDay(listeRes.get(i).isAllday());
			newRes.setColor(listeRes.get(i).getRoom().getColor());
			newRes.setResourceId(listeRes.get(i).getRoom().getIdroom());
			newRes.setId(listeRes.get(i).getIdreservation());

			int total = listeRes.get(i).getParticipantList().size() + 1;
			newRes.setTitle(listeRes.get(i).getRoom().getName() + "(" + total + "/"
					+ listeRes.get(i).getRoom().getCapacity() + ")");
			newRes.setStart(listeRes.get(i).getStartdate() + " " + listeRes.get(i).getStarttime());
			newRes.setEnd(listeRes.get(i).getEnddate() + "T" + listeRes.get(i).getEndtime());
			newRes.setStartDate(listeRes.get(i).getStartdate());
			newRes.setEndDate(listeRes.get(i).getEnddate());
			newRes.setStartTime(listeRes.get(i).getStarttime());
			newRes.setEndTime(listeRes.get(i).getEndtime());
			newRes.setDescription(listeRes.get(i).getTitle());
			newRes.setDaily(listeRes.get(i).getDaily());
			newRes.setSimple(listeRes.get(i).getSimple());
			newRes.setWeekly(listeRes.get(i).getWeekly());
			newRes.setMonthly(listeRes.get(i).getMonthly());
			newRes.setYearly(listeRes.get(i).getYearly());
			newRes.setType(listeRes.get(i).getVisibilitytyperes().getTypev());
			newRes.setUserId(listeRes.get(i).getReservedby().getId());
			newRes.setReservedBy(listeRes.get(i).getReservedby().getName());
			newRes.setMailReservedBy(listeRes.get(i).getReservedby().getMail());

			listResDTO.add(newRes);
		}
		List<Reservationchild> Reschilds = reservationService.getActiveReservationChild(start, end);
		for (int j = 0; j < Reschilds.size(); j++) {
			ReservationDTO newRes = new ReservationDTO();
			newRes.setAllDay(Reschilds.get(j).getIdreservation().getAllday());
			newRes.setColor(Reschilds.get(j).getIdreservation().getRoom().getColor());
			newRes.setResourceId(Reschilds.get(j).getIdreservation().getRoom().getIdroom());
			newRes.setId(Reschilds.get(j).getIdreservation().getIdreservation());
			newRes.setIdchild(Reschilds.get(j).getId());
			int total = Reschilds.get(j).getIdreservation().getParticipantList().size() +1;
			newRes.setTitle(Reschilds.get(j).getIdreservation().getRoom().getName() + "(" + total + "/"
					+ Reschilds.get(j).getIdreservation().getRoom().getCapacity() + ")");
			newRes.setStart(Reschilds.get(j).getStartdate() + "T" + Reschilds.get(j).getStarttime());
			newRes.setEnd(Reschilds.get(j).getEnddate() + "T" + Reschilds.get(j).getEndtime());
			newRes.setStartDate(Reschilds.get(j).getStartdate());
			newRes.setEndDate(Reschilds.get(j).getEnddate());
			newRes.setStartTime(Reschilds.get(j).getStarttime());
			newRes.setEndTime(Reschilds.get(j).getEndtime());
			newRes.setDescription(Reschilds.get(j).getIdreservation().getTitle());
			newRes.setReservedBy(Reschilds.get(j).getIdreservation().getReservedby().getName());
			newRes.setMailReservedBy(Reschilds.get(j).getIdreservation().getReservedby().getMail());
			newRes.setDaily(Reschilds.get(j).getIdreservation().getDaily());
			newRes.setSimple(Reschilds.get(j).getIdreservation().getSimple());
			newRes.setWeekly(Reschilds.get(j).getIdreservation().getWeekly());
			newRes.setMonthly(Reschilds.get(j).getIdreservation().getMonthly());
			newRes.setYearly(Reschilds.get(j).getIdreservation().getYearly());
			newRes.setType(Reschilds.get(j).getIdreservation().getVisibilitytyperes().getTypev());
			newRes.setUserId(Reschilds.get(j).getIdreservation().getReservedby().getId());
			listResDTO.add(newRes);
		}

		return listResDTO;
	}

	public List<ReservationDTO> getByStartEndDatePerRoom(Room room, Date start, Date end) {
		List<ReservationDTO> listResDTO = new ArrayList<ReservationDTO>();
		List<Reservation> listeRes = reservationService.getActiveReservationPerRoom(room, start, end);
//		java.sql.Date startDate = new java.sql.Date(start.getTime());
//		java.sql.Date endDate = new java.sql.Date(end.getTime());
//		List<Reservation> listeRes = reservationRepository.findDistinctByRoomAndEnddateGreaterThanEqualAndEnddateLessThanEqualAndStateresTrueAndSimpleTrue(room, startDate, endDate);
		for (int i = 0; i < listeRes.size(); i++) {
			ReservationDTO newRes = new ReservationDTO();
			newRes.setAllDay(listeRes.get(i).isAllday());
			newRes.setColor(listeRes.get(i).getRoom().getColor());
			newRes.setResourceId(listeRes.get(i).getRoom().getIdroom());
			newRes.setId(listeRes.get(i).getIdreservation());

			int total = listeRes.get(i).getParticipantList().size() + 1;
			newRes.setTitle(listeRes.get(i).getRoom().getName() + "(" + total + "/"
					+ listeRes.get(i).getRoom().getCapacity() + ")");
			newRes.setStart(listeRes.get(i).getStartdate() + " " + listeRes.get(i).getStarttime());
			newRes.setEnd(listeRes.get(i).getEnddate() + "T" + listeRes.get(i).getEndtime());
			newRes.setStartDate(listeRes.get(i).getStartdate());
			newRes.setEndDate(listeRes.get(i).getEnddate());
			newRes.setStartTime(listeRes.get(i).getStarttime());
			newRes.setEndTime(listeRes.get(i).getEndtime());
			newRes.setDescription(listeRes.get(i).getTitle());
			newRes.setDaily(listeRes.get(i).getDaily());
			newRes.setSimple(listeRes.get(i).getSimple());
			newRes.setWeekly(listeRes.get(i).getWeekly());
			newRes.setMonthly(listeRes.get(i).getMonthly());
			newRes.setYearly(listeRes.get(i).getYearly());
			newRes.setType(listeRes.get(i).getVisibilitytyperes().getTypev());
			newRes.setUserId(listeRes.get(i).getReservedby().getId());
			newRes.setReservedBy(listeRes.get(i).getReservedby().getName());
			newRes.setMailReservedBy(listeRes.get(i).getReservedby().getMail());

			listResDTO.add(newRes);
		}
		List<Reservationchild> Reschilds = reservationService.getActiveReservationChildPerRoom(room, start, end);
		//List<Reservationchild> Reschilds = childRepo.findDistinctByIdreservationRoomNameInAndStartdateLessThanEqualAndStartdateGreaterThanEqual(rooms, startDate, endDate); 
		for (int j = 0; j < Reschilds.size(); j++) {
			ReservationDTO newRes = new ReservationDTO();
			newRes.setAllDay(Reschilds.get(j).getIdreservation().getAllday());
			newRes.setColor(Reschilds.get(j).getIdreservation().getRoom().getColor());
			newRes.setResourceId(Reschilds.get(j).getIdreservation().getRoom().getIdroom());
			newRes.setId(Reschilds.get(j).getIdreservation().getIdreservation());
			newRes.setIdchild(Reschilds.get(j).getId());
			int total = Reschilds.get(j).getIdreservation().getParticipantList().size() + 1;
			newRes.setTitle(Reschilds.get(j).getIdreservation().getRoom().getName() + "(" + total + "/"
					+ Reschilds.get(j).getIdreservation().getRoom().getCapacity() + ")");
			newRes.setStart(Reschilds.get(j).getStartdate() + "T" + Reschilds.get(j).getStarttime());
			newRes.setEnd(Reschilds.get(j).getEnddate() + "T" + Reschilds.get(j).getEndtime());
			newRes.setStartDate(Reschilds.get(j).getStartdate());
			newRes.setEndDate(Reschilds.get(j).getEnddate());
			newRes.setStartTime(Reschilds.get(j).getStarttime());
			newRes.setEndTime(Reschilds.get(j).getEndtime());
			newRes.setDescription(Reschilds.get(j).getIdreservation().getTitle());
			newRes.setReservedBy(Reschilds.get(j).getIdreservation().getReservedby().getName());
			newRes.setMailReservedBy(Reschilds.get(j).getIdreservation().getReservedby().getMail());
			newRes.setDaily(Reschilds.get(j).getIdreservation().getDaily());
			newRes.setSimple(Reschilds.get(j).getIdreservation().getSimple());
			newRes.setWeekly(Reschilds.get(j).getIdreservation().getWeekly());
			newRes.setMonthly(Reschilds.get(j).getIdreservation().getMonthly());
			newRes.setYearly(Reschilds.get(j).getIdreservation().getYearly());
			newRes.setType(Reschilds.get(j).getIdreservation().getVisibilitytyperes().getTypev());
			newRes.setUserId(Reschilds.get(j).getIdreservation().getReservedby().getId());
			listResDTO.add(newRes);
		}
		return listResDTO;
	}

	@Override
	public List<ReservationDTO> getActiveReservationOfUser(String email, Date start, Date end) {

		List<ReservationDTO> listResDTO = new ArrayList<ReservationDTO>();
		List<Reservation> listeRes = reservationService.getActiveReservationOfUser(email, start, end);
		for (int i = 0; i < listeRes.size(); i++) {
			ReservationDTO newRes = new ReservationDTO();
			newRes.setAllDay(listeRes.get(i).isAllday());
			newRes.setColor(listeRes.get(i).getRoom().getColor());
			newRes.setResourceId(listeRes.get(i).getRoom().getIdroom());
			newRes.setId(listeRes.get(i).getIdreservation());

			int total = listeRes.get(i).getParticipantList().size() + 1;
			newRes.setTitle(listeRes.get(i).getRoom().getName() + "(" + total + "/"
					+ listeRes.get(i).getRoom().getCapacity() + ")");
			newRes.setStart(listeRes.get(i).getStartdate() + " " + listeRes.get(i).getStarttime());
			newRes.setEnd(listeRes.get(i).getEnddate() + "T" + listeRes.get(i).getEndtime());
			newRes.setStartDate(listeRes.get(i).getStartdate());
			newRes.setEndDate(listeRes.get(i).getEnddate());
			newRes.setStartTime(listeRes.get(i).getStarttime());
			newRes.setEndTime(listeRes.get(i).getEndtime());
			newRes.setDescription(listeRes.get(i).getTitle());
			newRes.setDaily(listeRes.get(i).getDaily());
			newRes.setSimple(listeRes.get(i).getSimple());
			newRes.setWeekly(listeRes.get(i).getWeekly());
			newRes.setMonthly(listeRes.get(i).getMonthly());
			newRes.setYearly(listeRes.get(i).getYearly());
			newRes.setType(listeRes.get(i).getVisibilitytyperes().getTypev());
			newRes.setUserId(listeRes.get(i).getReservedby().getId());
			newRes.setReservedBy(listeRes.get(i).getReservedby().getName());
			newRes.setMailReservedBy(listeRes.get(i).getReservedby().getMail());

			listResDTO.add(newRes);
		}
		List<Reservationchild> Reschilds = reservationService.getActiveReservationChildOfUser(email, start, end);
		for (int j = 0; j < Reschilds.size(); j++) {
			ReservationDTO newRes = new ReservationDTO();
			newRes.setAllDay(Reschilds.get(j).getIdreservation().getAllday());
			newRes.setColor(Reschilds.get(j).getIdreservation().getRoom().getColor());
			newRes.setResourceId(Reschilds.get(j).getIdreservation().getRoom().getIdroom());
			newRes.setId(Reschilds.get(j).getIdreservation().getIdreservation());
			newRes.setIdchild(Reschilds.get(j).getId());
			int total = Reschilds.get(j).getIdreservation().getParticipantList().size() + 1;
			newRes.setTitle(Reschilds.get(j).getIdreservation().getRoom().getName() + "(" + total + "/"
					+ Reschilds.get(j).getIdreservation().getRoom().getCapacity() + ")");
			newRes.setStart(Reschilds.get(j).getStartdate() + "T" + Reschilds.get(j).getStarttime());
			newRes.setEnd(Reschilds.get(j).getEnddate() + "T" + Reschilds.get(j).getEndtime());
			newRes.setStartDate(Reschilds.get(j).getStartdate());
			newRes.setEndDate(Reschilds.get(j).getEnddate());
			newRes.setStartTime(Reschilds.get(j).getStarttime());
			newRes.setEndTime(Reschilds.get(j).getEndtime());
			newRes.setDescription(Reschilds.get(j).getIdreservation().getTitle());
			newRes.setReservedBy(Reschilds.get(j).getIdreservation().getReservedby().getName());
			newRes.setMailReservedBy(Reschilds.get(j).getIdreservation().getReservedby().getMail());
			newRes.setDaily(Reschilds.get(j).getIdreservation().getDaily());
			newRes.setSimple(Reschilds.get(j).getIdreservation().getSimple());
			newRes.setWeekly(Reschilds.get(j).getIdreservation().getWeekly());
			newRes.setMonthly(Reschilds.get(j).getIdreservation().getMonthly());
			newRes.setYearly(Reschilds.get(j).getIdreservation().getYearly());
			newRes.setType(Reschilds.get(j).getIdreservation().getVisibilitytyperes().getTypev());
			newRes.setUserId(Reschilds.get(j).getIdreservation().getReservedby().getId());
			listResDTO.add(newRes);
		}
		return listResDTO;

	}

	@Override
	public List<ReservationDTO> getByStartEndDateOfRooms(List<String> rooms, Date start, Date end) {
		List<ReservationDTO> listResDTO = new ArrayList<ReservationDTO>();
		List<Reservation> listeRes = reservationService.getReservationOfRooms(rooms, start, end);
		for (int i = 0; i < listeRes.size(); i++) {
			ReservationDTO newRes = new ReservationDTO();
			newRes.setAllDay(listeRes.get(i).isAllday());
			newRes.setColor(listeRes.get(i).getRoom().getColor());
			newRes.setResourceId(listeRes.get(i).getRoom().getIdroom());
			newRes.setId(listeRes.get(i).getIdreservation());

			int total = listeRes.get(i).getParticipantList().size() + 1;
			newRes.setTitle(listeRes.get(i).getRoom().getName() + "(" + total + "/"
					+ listeRes.get(i).getRoom().getCapacity() + ")");
			newRes.setStart(listeRes.get(i).getStartdate() + " " + listeRes.get(i).getStarttime());
			newRes.setEnd(listeRes.get(i).getEnddate() + "T" + listeRes.get(i).getEndtime());
			newRes.setStartDate(listeRes.get(i).getStartdate());
			newRes.setEndDate(listeRes.get(i).getEnddate());
			newRes.setStartTime(listeRes.get(i).getStarttime());
			newRes.setEndTime(listeRes.get(i).getEndtime());
			newRes.setDescription(listeRes.get(i).getTitle());
			newRes.setDaily(listeRes.get(i).getDaily());
			newRes.setSimple(listeRes.get(i).getSimple());
			newRes.setWeekly(listeRes.get(i).getWeekly());
			newRes.setMonthly(listeRes.get(i).getMonthly());
			newRes.setYearly(listeRes.get(i).getYearly());
			newRes.setType(listeRes.get(i).getVisibilitytyperes().getTypev());
			newRes.setUserId(listeRes.get(i).getReservedby().getId());
			newRes.setReservedBy(listeRes.get(i).getReservedby().getName());
			newRes.setMailReservedBy(listeRes.get(i).getReservedby().getMail());

			listResDTO.add(newRes);
		}
		List<Reservationchild> Reschilds =  childReservationService.getReservationsOfRooms(rooms, start, end);
		for (int j = 0; j < Reschilds.size(); j++) {
			ReservationDTO newRes = new ReservationDTO();
			newRes.setAllDay(Reschilds.get(j).getIdreservation().getAllday());
			newRes.setColor(Reschilds.get(j).getIdreservation().getRoom().getColor());
			newRes.setResourceId(Reschilds.get(j).getIdreservation().getRoom().getIdroom());
			newRes.setId(Reschilds.get(j).getIdreservation().getIdreservation());
			newRes.setIdchild(Reschilds.get(j).getId());
			int total = Reschilds.get(j).getIdreservation().getParticipantList().size() +1 ;
			newRes.setTitle(Reschilds.get(j).getIdreservation().getRoom().getName() + "(" + total + "/"
					+ Reschilds.get(j).getIdreservation().getRoom().getCapacity() + ")");
			newRes.setStart(Reschilds.get(j).getStartdate() + "T" + Reschilds.get(j).getStarttime());
			newRes.setEnd(Reschilds.get(j).getEnddate() + "T" + Reschilds.get(j).getEndtime());
			newRes.setStartDate(Reschilds.get(j).getStartdate());
			newRes.setEndDate(Reschilds.get(j).getEnddate());
			newRes.setStartTime(Reschilds.get(j).getStarttime());
			newRes.setEndTime(Reschilds.get(j).getEndtime());
			newRes.setDescription(Reschilds.get(j).getIdreservation().getTitle());
			newRes.setReservedBy(Reschilds.get(j).getIdreservation().getReservedby().getName());
			newRes.setMailReservedBy(Reschilds.get(j).getIdreservation().getReservedby().getMail());
			newRes.setDaily(Reschilds.get(j).getIdreservation().getDaily());
			newRes.setSimple(Reschilds.get(j).getIdreservation().getSimple());
			newRes.setWeekly(Reschilds.get(j).getIdreservation().getWeekly());
			newRes.setMonthly(Reschilds.get(j).getIdreservation().getMonthly());
			newRes.setYearly(Reschilds.get(j).getIdreservation().getYearly());
			newRes.setType(Reschilds.get(j).getIdreservation().getVisibilitytyperes().getTypev());
			newRes.setUserId(Reschilds.get(j).getIdreservation().getReservedby().getId());
			listResDTO.add(newRes);
		}
		
		return listResDTO;
	}
}
