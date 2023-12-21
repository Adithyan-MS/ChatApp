package com.thinkpalm.ChatApplication.Repository;

import com.thinkpalm.ChatApplication.Model.MessageRoomModel;
import com.thinkpalm.ChatApplication.Model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface MessageRoomRepository extends JpaRepository<MessageRoomModel,Integer> {
    @Query(value = "SELECT m.id, m.content, m.sender_id,u.name as sender_name, m.parent_message_id, m1.content as parent_message_content,u1.name as parent_message_sender, m.like_count, m.created_at,m.modified_at\n" +
            "            FROM chatdb.message AS m\n" +
            "            INNER JOIN chatdb.message_room AS mr ON m.id = mr.message_id\n" +
            "            JOIN chatdb.user AS u ON m.sender_id = u.id\n" +
            "            left JOIN chatdb.message as m1 on m1.id = m.parent_message_id\n" +
            "            left Join chatdb.user as u1 on u1.id = m1.sender_id\n" +
            "            LEFT JOIN chatdb.deleted_message AS dm ON m.id = dm.message_id AND dm.deleted_by = ?2\n" +
            "            WHERE mr.room_id = ?1 AND dm.message_id IS NULL\n" +
            "            ORDER BY m.created_at",nativeQuery = true)
    List<Map<String, Object>> getAllRoomChatMessages(Integer roomId, Integer userId);

    @Query(value = "SELECT m.id, m.content, m.sender_id,u.name as sender_name, m.parent_message_id, m1.content as parent_message_content,u1.name as parent_message_sender, m.like_count, m.created_at,m.modified_at\n" +
            "            FROM chatdb.message AS m\n" +
            "            INNER JOIN chatdb.message_room AS mr ON m.id = mr.message_id\n" +
            "            JOIN chatdb.user AS u ON m.sender_id = u.id\n" +
            "            left JOIN chatdb.message as m1 on m1.id = m.parent_message_id\n" +
            "            left Join chatdb.user as u1 on u1.id = m1.sender_id\n" +
            "            LEFT JOIN chatdb.deleted_message AS dm ON m.id = dm.message_id AND dm.deleted_by = ?2\n" +
            "            WHERE mr.room_id = ?1 AND dm.message_id IS NULL\n" +
            "            AND m.content LIKE CONCAT('%',?3,'%')\n" +
            "            ORDER BY m.created_at",nativeQuery = true)
    List<Map<String, Object>> searchUserChatMessages(Integer id, Integer id1, String searchContent);

    @Query(value = "SELECT u.id as id, u.name as name, u.profile_pic as profile_pic, 'user' as type\n" +
            "\t\t\tFROM chatdb.user as u  where name LIKE CONCAT('%',?1,'%') and id != ?2\n" +
            "            UNION\n" +
            "\t\t\tSELECT r.id as id, r.name as name, r.room_pic as profile_pic, 'room' as type\n" +
            "            FROM chatdb.room as r where name LIKE CONCAT('%',?1,'%');",nativeQuery = true)
    List<Map<String, Object>> searchAllChats(String searchContent, Integer id);
}
