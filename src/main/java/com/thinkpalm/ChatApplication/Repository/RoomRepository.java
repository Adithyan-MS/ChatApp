package com.thinkpalm.ChatApplication.Repository;

import com.thinkpalm.ChatApplication.Model.RoomModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<RoomModel,Integer> {
    RoomModel findByName(String name);
}
