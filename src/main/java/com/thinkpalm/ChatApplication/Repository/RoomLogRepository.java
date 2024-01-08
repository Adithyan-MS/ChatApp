package com.thinkpalm.ChatApplication.Repository;

import com.thinkpalm.ChatApplication.Model.RoomLogModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomLogRepository extends JpaRepository<RoomLogModel,Integer> {
}
