package com.thinkpalm.ChatApplication.Repository;

import com.thinkpalm.ChatApplication.Model.MessageRoomModel;
import com.thinkpalm.ChatApplication.Model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface MessageRoomRepository extends JpaRepository<MessageRoomModel,Integer> {
    @Query(value = "SELECT \n" +
            "    m.id, \n" +
            "    m.content, \n" +
            "    m.sender_id,\n" +
            "    u.name as sender_name,\n" +
            "    CASE WHEN sm.id IS NULL THEN 0 ELSE 1 END as is_starred,\n" +
            "    m.parent_message_id, \n" +
            "    m1.content as parent_message_content,\n" +
            "    u1.name as parent_message_sender,\n" +
            "    m.like_count, \n" +
            "    m.created_at,\n" +
            "    m.modified_at\n" +
            "FROM \n" +
            "    chatdb.message AS m\n" +
            "INNER JOIN \n" +
            "    chatdb.message_room AS mr ON m.id = mr.message_id\n" +
            "JOIN \n" +
            "    chatdb.user AS u ON m.sender_id = u.id\n" +
            "LEFT JOIN \n" +
            "    chatdb.message as m1 on m1.id = m.parent_message_id\n" +
            "LEFT JOIN \n" +
            "    chatdb.user as u1 on u1.id = m1.sender_id\n" +
            "LEFT JOIN \n" +
            "    chatdb.starred_message as sm on sm.message_id = m.id and sm.user_id = ?2\n" +
            "LEFT JOIN \n" +
            "    chatdb.deleted_message AS dm ON m.id = dm.message_id AND dm.deleted_by = ?2\n" +
            "LEFT JOIN \n" +
            "    chatdb.participant AS p ON mr.room_id = p.room_id AND p.user_id = ?2\n" +
            "WHERE \n" +
            "    mr.room_id = ?1\n" +
            "    AND dm.message_id IS NULL\n" +
            "    AND (\n" +
            "        (p.is_active = false AND m.created_at >= p.joined_at AND (p.left_at IS NULL OR m.created_at <= p.left_at))\n" +
            "        OR\t\n" +
            "        (p.is_active = true AND \n" +
            "\t\t\t(p.left_at is null and m.created_at >= p.joined_at)\n" +
            "            OR\n" +
            "            (p.left_at is not null and ((m.created_at >= p.created_at and m.created_at < p.left_at) or  m.created_at >= p.joined_at))\n" +
            "    ))\n" +
            "ORDER BY \n" +
            "    m.created_at",nativeQuery = true)
    List<Map<String, Object>> getAllRoomMessages(Integer roomId, Integer userId);

    @Query(value = "SELECT \n" +
            "    m.id, \n" +
            "    m.content, \n" +
            "    m.sender_id,\n" +
            "    u.name as sender_name,\n" +
            "    CASE WHEN sm.id IS NULL THEN 0 ELSE 1 END as is_starred,\n" +
            "    m.parent_message_id, \n" +
            "    m1.content as parent_message_content,\n" +
            "    u1.name as parent_message_sender,\n" +
            "    m.like_count, \n" +
            "    m.created_at,\n" +
            "    m.modified_at\n" +
            "FROM \n" +
            "    chatdb.message AS m\n" +
            "INNER JOIN \n" +
            "    chatdb.message_room AS mr ON m.id = mr.message_id\n" +
            "JOIN \n" +
            "    chatdb.user AS u ON m.sender_id = u.id\n" +
            "LEFT JOIN \n" +
            "    chatdb.message as m1 on m1.id = m.parent_message_id\n" +
            "LEFT JOIN \n" +
            "    chatdb.user as u1 on u1.id = m1.sender_id\n" +
            "LEFT JOIN \n" +
            "    chatdb.starred_message as sm on sm.message_id = m.id and sm.user_id = ?2\n" +
            "LEFT JOIN \n" +
            "    chatdb.deleted_message AS dm ON m.id = dm.message_id AND dm.deleted_by = ?2\n" +
            "LEFT JOIN \n" +
            "    chatdb.participant AS p ON mr.room_id = p.room_id AND p.user_id = ?2\n" +
            "WHERE \n" +
            "    mr.room_id = ?1\n" +
            "    AND dm.message_id IS NULL\n" +
            "    AND (\n" +
            "        (p.is_active = false AND m.created_at >= p.joined_at AND (p.left_at IS NULL OR m.created_at <= p.left_at))\n" +
            "        OR\t\n" +
            "        (p.is_active = true AND \n" +
            "\t\t\t(p.left_at is null and m.created_at >= p.joined_at)\n" +
            "            OR\n" +
            "            (p.left_at is not null and ((m.created_at >= p.created_at and m.created_at < p.left_at) or  m.created_at >= p.joined_at))\n" +
            "    ))\n" +
            "    AND m.content LIKE CONCAT('%',?3,'%')\n" +
            "ORDER BY ",nativeQuery = true)
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
