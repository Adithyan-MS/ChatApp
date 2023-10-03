package com.thinkpalm.ChatApplication.Repository;

import com.thinkpalm.ChatApplication.Model.MessageRoomModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRoomRepository extends JpaRepository<MessageRoomModel,Integer> {
}
