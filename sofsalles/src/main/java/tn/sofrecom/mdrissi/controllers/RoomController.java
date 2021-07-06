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

import tn.sofrecom.mdrissi.dto.RoomDTO;
import tn.sofrecom.mdrissi.entities.Block;
import tn.sofrecom.mdrissi.entities.Building;
import tn.sofrecom.mdrissi.entities.Floor;
import tn.sofrecom.mdrissi.entities.Room;
import tn.sofrecom.mdrissi.repositories.BlockRepository;
import tn.sofrecom.mdrissi.repositories.BuildingRepository;
import tn.sofrecom.mdrissi.repositories.FloorRepository;
import tn.sofrecom.mdrissi.services.room.RoomDtoService;
import tn.sofrecom.mdrissi.services.room.RoomService;

@RestController
@RequestMapping(value = "/v1/room")
public class RoomController {
	
	@Autowired
	RoomService roomService;
	
	@Autowired
	RoomDtoService roomDtoService;
	
	@Autowired
	BuildingRepository buildingRepo;
	
	@Autowired
	FloorRepository floorRepo;
	
	@Autowired
	BlockRepository blockRepo;
	
	@PostMapping(value = "/create")
	public String createRoom(@RequestBody Room room) {
		
		String result = "";
		result = roomService.addRoom(room);
		
		return result;	
	}

	@GetMapping(value="/allResources")
	public List<RoomDTO> getResources(){
	
		return roomDtoService.getAll();
	}
	
	@GetMapping(value="/allrooms")
	public List<Room> getRooms(){
		List<Room> result = roomService.getAllRooms();
		return result;
	}
	
	@GetMapping(value="/buildings")
	public List<Building> getBuildings(){
		List<Building> result = buildingRepo.findAll();
		return result;
	}
	
	@GetMapping(value="/floors")
	public List<Floor> getFloors(){
		List<Floor> result = floorRepo.findAll();
		return result;
	}
	
	@GetMapping(value="/blocks")
	public List<Block> getBlocks(){
		List<Block> result = blockRepo.findAll();
		return result;
	}

	@DeleteMapping(value="/delete/{id}")
	public String deleteRoom(@PathVariable Integer id){
		String result = "";
		result = roomService.deleteRoom(id);
		return result;
	}
	
	@PutMapping(value="/update/{id}")
	public String updateRoom(@PathVariable Integer id, @RequestBody Room room){
		String result = "";
		result = roomService.updateRoom(id, room);
		return result;
	}
	
	
}
