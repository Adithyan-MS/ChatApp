package com.thinkpalm.ChatApplication.Repository;

import com.thinkpalm.ChatApplication.Model.MessageRoomModel;
import com.thinkpalm.ChatApplication.Model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface MessageRoomRepository extends JpaRepository<MessageRoomModel,Integer> {
    @Query(value = "SELECT m.id, m.content, m.sender_id, m.parent_message_id, m.like_count, m.is_starred, m.created_at\n" +
            "FROM message AS m\n" +
            "INNER JOIN message_room AS mr ON m.id = mr.message_id\n" +
            "LEFT JOIN deleted_message AS dm ON m.id = dm.message_id AND dm.deleted_by = ?2\n" +
            "WHERE mr.room_id = ?1 AND dm.message_id IS NULL\n" +
            "ORDER BY m.created_at;",nativeQuery = true)
    List<Map<String, Object>> getAllRoomChatMessages(Integer roomId, Integer userId);
}
