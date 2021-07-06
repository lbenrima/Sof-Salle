package tn.sofrecom.mdrissi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tn.sofrecom.mdrissi.entities.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room,Integer>, RoomRepositoryCustom{
	
	@EntityGraph(attributePaths = {"idbuilding","idfloor","idblock"})
	List<Room> findByIdroom(Integer idRoom);
	
	@EntityGraph(attributePaths = {"idbuilding","idfloor","idblock"})
	List<Room> findByIdroomNotNull();
	@Query("select r FROM Room r WHERE r.name = :name")	
	Room findByName(@Param("name") String name);
	
	List<Room> findByNameIn(List<String> rooms);
	
}
