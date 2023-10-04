package com.thinkpalm.ChatApplication.Repository;

import com.thinkpalm.ChatApplication.Model.MessageRoomModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface MessageRoomRepository extends JpaRepository<MessageRoomModel,Integer> {

    @Query(value = "select m.id,m.content,m.sender_id,m.parent_message_id,m.like_count,m.is_starred,m.created_at from message as m inner join message_room as mr on m.id = mr.message_id where mr.room_id=? order by m.created_at",nativeQuery = true)
    List<Map<String, Object>> getAllRoomChatMessages(Integer roomId);
}
