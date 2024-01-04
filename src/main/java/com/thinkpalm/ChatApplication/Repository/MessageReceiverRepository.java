package com.thinkpalm.ChatApplication.Repository;

import com.thinkpalm.ChatApplication.Model.MessageReceiverModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface MessageReceiverRepository extends JpaRepository<MessageReceiverModel,Integer> {
// before geting parent_message_details
//    @Query(value = "SELECT m.id, m.content, m.sender_id, u.name as sender_name, m.parent_message_id, m.like_count, m.created_at, m.modified_at\n" +
//            "FROM message AS m\n" +
//            "JOIN message_receiver AS mr ON m.id = mr.message_id\n" +
//            "JOIN user AS u ON m.sender_id = u.id\n" +
//            "LEFT JOIN deleted_message AS dm ON m.id = dm.message_id AND dm.deleted_by = ?1\n" +
//            "WHERE \n" +
//            "    ((m.sender_id = ?1 AND mr.receiver_id = ?2) OR (m.sender_id = ?2 AND mr.receiver_id = ?1))\n" +
//            "    AND dm.message_id IS NULL\n" +
//            "ORDER BY m.created_at",nativeQuery = true)
//    List<Map<String, Object>> getAllUserChatMessages(Integer currentUserId,Integer otherUserId);


    //new
    @Query(value = "SELECT m.id, m.content, m.type, m.sender_id, u.name as sender_name,case when sm.id is null then 0 else 1 end as is_starred, m.parent_message_id,u1.name as parent_message_sender ,m1.content as parent_message_content,m1.type as parent_message_type, m.like_count, m.created_at, m.modified_at\n" +
            "            FROM chatdb.message AS m\n" +
            "\t\t\tJOIN chatdb.message_receiver AS mr ON m.id = mr.message_id\n" +
            "            JOIN chatdb.user AS u ON m.sender_id = u.id\n" +
            "            left join chatdb.message as m1 on m.parent_message_id = m1.id\n" +
            "            left JOIN chatdb.user AS u1 ON m1.sender_id = u1.id\n" +
            "            left join chatdb.starred_message as sm on sm.message_id = m.id and sm.user_id = ?1\n" +
            "            LEFT JOIN chatdb.deleted_message AS dm ON m.id = dm.message_id AND dm.deleted_by = ?1\n" +
            "\t\t\tWHERE \n" +
            "                ((m.sender_id = ?1 AND mr.receiver_id = ?2) OR (m.sender_id = ?2 AND mr.receiver_id = ?1))\n" +
            "                AND dm.message_id IS NULL\n" +
            "            ORDER BY m.created_at",nativeQuery = true)
    List<Map<String, Object>> getAllUserChatMessages(Integer currentUserId,Integer otherUserId);

    @Query(value = "SELECT m.id, m.content, m.type, m.sender_id, u.name as sender_name,case when sm.id is null then 0 else 1 end as is_starred, m.parent_message_id,u1.name as parent_message_sender ,m1.content as parent_message_content,m1.type as parent_message_type, m.like_count, m.created_at, m.modified_at\n" +
            "FROM chatdb.message AS m\n" +
            "JOIN chatdb.message_receiver AS mr ON m.id = mr.message_id\n" +
            "JOIN chatdb.user AS u ON m.sender_id = u.id\n" +
            "left join chatdb.message as m1 on m.parent_message_id = m1.id\n" +
            "left JOIN chatdb.user AS u1 ON m1.sender_id = u1.id\n" +
            "left join chatdb.starred_message as sm on sm.message_id = m.id and sm.user_id = ?1\n" +
            "LEFT JOIN chatdb.deleted_message AS dm ON m.id = dm.message_id AND dm.deleted_by = ?1\n" +
            "WHERE \n" +
            "((m.sender_id = ?1 AND mr.receiver_id = ?2) OR (m.sender_id = ?2 AND mr.receiver_id = ?1))\n" +
            "AND m.content LIKE CONCAT('%',?3,'%')\n"+
            "AND dm.message_id IS NULL\n" +
            "ORDER BY m.created_at;",nativeQuery = true)
    List<Map<String, Object>> searchUserChatMessages(Integer id, Integer id1, String searchContent);
}
