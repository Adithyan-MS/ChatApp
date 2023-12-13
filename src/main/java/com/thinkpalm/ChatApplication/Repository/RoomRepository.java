package com.thinkpalm.ChatApplication.Repository;

import com.thinkpalm.ChatApplication.Model.RoomModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface RoomRepository extends JpaRepository<RoomModel,Integer> {
    RoomModel findByName(String name);
    @Query(value = "SELECT * FROM chatdb.room where room.name = ?1",nativeQuery = true)
    List<RoomModel> existByRoomName(String name);
}
