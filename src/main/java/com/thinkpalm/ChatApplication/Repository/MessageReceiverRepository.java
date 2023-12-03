package com.thinkpalm.ChatApplication.Repository;

import com.thinkpalm.ChatApplication.Model.MessageReceiverModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface MessageReceiverRepository extends JpaRepository<MessageReceiverModel,Integer> {

    //old when there was no deleted_message table-------------->
//    @Query(value = "SELECT m.id,m.content,m.sender_id,m.parent_message_id,m.like_count,m.created_at\n" +
//            "FROM message AS m\n" +
//            "JOIN message_receiver AS mr ON m.id = mr.message_id\n" +
//            "WHERE\n" +
//            "    (m.sender_id = ?1 AND mr.receiver_id = ?2)\n" +
//            "    OR\n" +
//            "    (m.sender_id = ?2 AND mr.receiver_id = ?1)\n" +
//            "ORDER BY m.created_at",nativeQuery = true)
//    List<Map<String, Object>> getAllUserChatMessages(Integer currentUserId,Integer otherUserId);


    //new
    @Query(value = "SELECT m.id, m.content, m.sender_id, u.name as sender_name, m.parent_message_id, m.like_count, m.created_at, m.modified_at\n" +
            "FROM message AS m\n" +
            "JOIN message_receiver AS mr ON m.id = mr.message_id\n" +
            "JOIN user AS u ON m.sender_id = u.id\n" +
            "LEFT JOIN deleted_message AS dm ON m.id = dm.message_id AND dm.deleted_by = ?1\n" +
            "WHERE \n" +
            "    ((m.sender_id = ?1 AND mr.receiver_id = ?2) OR (m.sender_id = ?2 AND mr.receiver_id = ?1))\n" +
            "    AND dm.message_id IS NULL\n" +
            "ORDER BY m.created_at",nativeQuery = true)
    List<Map<String, Object>> getAllUserChatMessages(Integer currentUserId,Integer otherUserId);
}
