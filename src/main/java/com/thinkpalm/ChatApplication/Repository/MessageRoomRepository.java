package com.thinkpalm.ChatApplication.Repository;

import com.thinkpalm.ChatApplication.Model.MessageRoomModel;
import com.thinkpalm.ChatApplication.Model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface MessageRoomRepository extends JpaRepository<MessageRoomModel,Integer> {
    @Query(value = "SELECT DISTINCT\n" +
            "    m.id,\n" +
            "    m.content,\n" +
            "    m.type,\n" +
            "    p.is_active,\n" +
            "    m.sender_id,\n" +
            "    u.name AS sender_name,\n" +
            "    CASE WHEN sm.id IS NULL THEN 0 ELSE 1 END AS is_starred,\n" +
            "    m.parent_message_id,\n" +
            "    m1.content AS parent_message_content,\n" +
            "    m1.type AS parent_message_type,\n" +
            "    u1.name AS parent_message_sender,\n" +
            "    u1.id AS parent_message_sender_id,\n" +
            "    m.like_count,\n" +
            "    m.created_at,\n" +
            "    m.modified_at\n" +
            "FROM \n" +
            "    chatdb.message AS m\n" +
            "INNER JOIN \n" +
            "    chatdb.message_room AS mr ON m.id = mr.message_id\n" +
            "JOIN \n" +
            "    chatdb.user AS u ON m.sender_id = u.id\n" +
            "LEFT JOIN \n" +
            "    chatdb.message AS m1 ON m1.id = m.parent_message_id\n" +
            "LEFT JOIN \n" +
            "    chatdb.user AS u1 ON u1.id = m1.sender_id\n" +
            "LEFT JOIN \n" +
            "    chatdb.starred_message AS sm ON sm.message_id = m.id AND sm.user_id = ?2\n" +
            "LEFT JOIN \n" +
            "    chatdb.deleted_message AS dm ON m.id = dm.message_id AND dm.deleted_by = ?2\n" +
            "LEFT JOIN \n" +
            "    chatdb.participant AS p ON mr.room_id = p.room_id AND p.user_id = ?2\n" +
            "LEFT JOIN (\n" +
            "    SELECT\n" +
            "        r_join.user_id,\n" +
            "        r_join.room_id,\n" +
            "        r_join.timestamp AS join_timestamp,\n" +
            "        r_leave.timestamp AS leave_timestamp\n" +
            "    FROM\n" +
            "        chatdb.room_log r_join\n" +
            "    LEFT JOIN\n" +
            "        chatdb.room_log r_leave ON r_join.user_id = r_leave.user_id\n" +
            "                             AND r_join.room_id = r_leave.room_id\n" +
            "                             AND r_leave.action = 'leave'\n" +
            "                             AND r_leave.timestamp >= r_join.timestamp\n" +
            "    WHERE\n" +
            "        r_join.room_id = ?1\n" +
            ") AS aup ON p.user_id = aup.user_id AND p.room_id = aup.room_id\n" +
            "WHERE \n" +
            "    mr.room_id = ?1\n" +
            "    AND dm.message_id IS NULL\n" +
            "    AND (\n" +
            "        (aup.leave_timestamp IS NULL AND aup.join_timestamp <= m.created_at) OR\n" +
            "        (aup.leave_timestamp IS NOT NULL AND aup.join_timestamp <= m.created_at AND m.created_at <= aup.leave_timestamp)\n" +
            "    )\n" +
            "ORDER BY\n" +
            "    m.created_at ASC",nativeQuery = true)
    List<Map<String, Object>> getAllRoomMessages(Integer roomId, Integer userId);

    @Query(value = "SELECT DISTINCT\n" +
            "    m.id,\n" +
            "    m.content,\n" +
            "    m.type,\n" +
            "    p.is_active,\n" +
            "    m.sender_id,\n" +
            "    u.name AS sender_name,\n" +
            "    CASE WHEN sm.id IS NULL THEN 0 ELSE 1 END AS is_starred,\n" +
            "    m.parent_message_id,\n" +
            "    m1.content AS parent_message_content,\n" +
            "    m1.type AS parent_message_type,\n" +
            "    u1.name AS parent_message_sender,\n" +
            "    u1.id AS parent_message_sender_id,\n" +
            "    m.like_count,\n" +
            "    m.created_at,\n" +
            "    m.modified_at\n" +
            "FROM \n" +
            "    chatdb.message AS m\n" +
            "INNER JOIN \n" +
            "    chatdb.message_room AS mr ON m.id = mr.message_id\n" +
            "JOIN \n" +
            "    chatdb.user AS u ON m.sender_id = u.id\n" +
            "LEFT JOIN \n" +
            "    chatdb.message AS m1 ON m1.id = m.parent_message_id\n" +
            "LEFT JOIN \n" +
            "    chatdb.user AS u1 ON u1.id = m1.sender_id\n" +
            "LEFT JOIN \n" +
            "    chatdb.starred_message AS sm ON sm.message_id = m.id AND sm.user_id = ?2\n" +
            "LEFT JOIN \n" +
            "    chatdb.deleted_message AS dm ON m.id = dm.message_id AND dm.deleted_by = ?2\n" +
            "LEFT JOIN \n" +
            "    chatdb.participant AS p ON mr.room_id = p.room_id AND p.user_id = ?2\n" +
            "LEFT JOIN (\n" +
            "    SELECT\n" +
            "        r_join.user_id,\n" +
            "        r_join.room_id,\n" +
            "        r_join.timestamp AS join_timestamp,\n" +
            "        r_leave.timestamp AS leave_timestamp\n" +
            "    FROM\n" +
            "        chatdb.room_log r_join\n" +
            "    LEFT JOIN\n" +
            "        chatdb.room_log r_leave ON r_join.user_id = r_leave.user_id\n" +
            "                             AND r_join.room_id = r_leave.room_id\n" +
            "                             AND r_leave.action = 'leave'\n" +
            "                             AND r_leave.timestamp >= r_join.timestamp\n" +
            "    WHERE\n" +
            "        r_join.room_id = ?1\n" +
            ") AS aup ON p.user_id = aup.user_id AND p.room_id = aup.room_id\n" +
            "WHERE \n" +
            "    mr.room_id = ?1\n" +
            "    AND dm.message_id IS NULL\n" +
            "    AND (\n" +
            "        (aup.leave_timestamp IS NULL AND aup.join_timestamp <= m.created_at) OR\n" +
            "        (aup.leave_timestamp IS NOT NULL AND aup.join_timestamp <= m.created_at AND m.created_at <= aup.leave_timestamp)\n" +
            "    )\n" +
            "    AND m.content LIKE CONCAT('%',?3,'%')\n" +
            "ORDER BY\n" +
            "    m.created_at ASC",nativeQuery = true)
    List<Map<String, Object>> searchUserChatMessages(Integer id, Integer id1, String searchContent);

    @Query(value = "SELECT u.id as id, u.name as name, u.profile_pic as profile_pic, 'user' as type\n" +
            "\tFROM chatdb.user as u  where name LIKE CONCAT('%',?1,'%') and id != ?2\n" +
            "\tUNION\n" +
            "\tSELECT r.id as id, r.name as name, r.room_pic as profile_pic, 'room' as type\n" +
            "\tFROM chatdb.room as r\n" +
            "\tINNER JOIN chatdb.participant as p on p.room_id = r.id and p.user_id = ?2 and p.is_active = 1\n" +
            "\tWHERE name LIKE CONCAT('%',?1,'%')",nativeQuery = true)
    List<Map<String, Object>> searchAllChats(String searchContent, Integer id);
}
