package tn.sofrecom.mdrissi.services.room;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.sofrecom.mdrissi.dto.RoomDTO;
import tn.sofrecom.mdrissi.entities.Room;
import tn.sofrecom.mdrissi.repositories.RoomRepository;

@Service
public class RoomDtoServiceImpl implements RoomDtoService{

	@Autowired
	RoomRepository roomRepo;
	
	private List<RoomDTO> listRooms;
	
	@Override
	public List<RoomDTO> getAll() {
		
		listRooms = new ArrayList<RoomDTO>();
		List<Room> listeMR = roomRepo.findAll();
		
		for(int i=0; i<listeMR.size(); i++) { 

			RoomDTO newRoom = new RoomDTO();
			newRoom.setId(listeMR.get(i).getIdroom());
			newRoom.setTitle(listeMR.get(i).getName());
			newRoom.setBuilding(listeMR.get(i).getIdbuilding().getNamebuilding());
			newRoom.setFloor(listeMR.get(i).getIdfloor().getNumfloor());
			newRoom.setCapacity(listeMR.get(i).getCapacity());

			listRooms.add(newRoom);
		}
		
		return listRooms;
	}
	

}
