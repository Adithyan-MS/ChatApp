package com.thinkpalm.ChatApplication.Repository;

import com.thinkpalm.ChatApplication.Model.MessageRoomModel;
import com.thinkpalm.ChatApplication.Model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface MessageRoomRepository extends JpaRepository<MessageRoomModel,Integer> {
    @Query(value = "SELECT\n" +
            "\tm.id, \n" +
            "\tm.content,m.type\n" +
            "\tp.is_active,\n" +
            "\tm.sender_id,\n" +
            "    u.name as sender_name,\n" +
            "    CASE WHEN sm.id IS NULL THEN 0 ELSE 1 END as is_starred,\n" +
            "\tm.parent_message_id, \n" +
            "\tm1.content as parent_message_content,\n" +
            "\tm1.type as parent_message_type,\n" +
            "\tu1.name as parent_message_sender,\n" +
            "\tm.like_count, \n" +
            "\tm.created_at,\n" +
            "\tm.modified_at\n" +
            "\tFROM \n" +
            "\tchatdb.message AS m\n" +
            "\tINNER JOIN \n" +
            "\t\tchatdb.message_room AS mr ON m.id = mr.message_id\n" +
            "\tJOIN \n" +
            "\t\tchatdb.user AS u ON m.sender_id = u.id\n" +
            "\tLEFT JOIN\n" +
            "\t\tchatdb.message as m1 on m1.id = m.parent_message_id\n" +
            "\tLEFT JOIN\n" +
            "\t\tchatdb.user as u1 on u1.id = m1.sender_id\n" +
            "\tLEFT JOIN \n" +
            "\t\tchatdb.starred_message as sm on sm.message_id = m.id and sm.user_id = ?2\n" +
            "\tLEFT JOIN \n" +
            "\t\tchatdb.deleted_message AS dm ON m.id = dm.message_id AND dm.deleted_by = ?2\n" +
            "\tLEFT JOIN \n" +
            "\t\tchatdb.participant AS p ON mr.room_id = p.room_id AND p.user_id = ?2\n" +
            "\tWHERE \n" +
            "\t\tmr.room_id = ?1\n" +
            "\t\tAND dm.message_id IS NULL\n" +
            "\t\tAND (\n" +
            "\t\t\t\t(p.is_active = false and m.created_at >= p.joined_at AND (p.left_at IS NULL OR m.created_at <= p.left_at))\n" +
            "\t\t\tor\n" +
            "\t\t\t\t(p.is_active = true AND (p.left_at is null and m.created_at >= p.joined_at))\n" +
            "\t\t\t)\n" +
            "\tORDER BY\n" +
            "\tm.created_at",nativeQuery = true)
    List<Map<String, Object>> getAllRoomMessages(Integer roomId, Integer userId);

    @Query(value = "SELECT\n" +
            "\tm.id, \n" +
            "\tm.content,m.type\n" +
            "\tp.is_active,\n" +
            "\tm.sender_id,\n" +
            "    u.name as sender_name,\n" +
            "    CASE WHEN sm.id IS NULL THEN 0 ELSE 1 END as is_starred,\n" +
            "\tm.parent_message_id, \n" +
            "\tm1.content as parent_message_content,\n" +
            "\tm1.type as parent_message_type,\n" +
            "\tu1.name as parent_message_sender,\n" +
            "\tm.like_count, \n" +
            "\tm.created_at,\n" +
            "\tm.modified_at\n" +
            "\tFROM \n" +
            "\tchatdb.message AS m\n" +
            "\tINNER JOIN \n" +
            "\t\tchatdb.message_room AS mr ON m.id = mr.message_id\n" +
            "\tJOIN \n" +
            "\t\tchatdb.user AS u ON m.sender_id = u.id\n" +
            "\tLEFT JOIN\n" +
            "\t\tchatdb.message as m1 on m1.id = m.parent_message_id\n" +
            "\tLEFT JOIN\n" +
            "\t\tchatdb.user as u1 on u1.id = m1.sender_id\n" +
            "\tLEFT JOIN \n" +
            "\t\tchatdb.starred_message as sm on sm.message_id = m.id and sm.user_id = ?2\n" +
            "\tLEFT JOIN \n" +
            "\t\tchatdb.deleted_message AS dm ON m.id = dm.message_id AND dm.deleted_by = ?2\n" +
            "\tLEFT JOIN \n" +
            "\t\tchatdb.participant AS p ON mr.room_id = p.room_id AND p.user_id = ?2\n" +
            "\tWHERE \n" +
            "\t\tmr.room_id = ?1\n" +
            "\t\tAND dm.message_id IS NULL\n" +
            "\t\tAND (\n" +
            "\t\t\t\t(p.is_active = false and m.created_at >= p.joined_at AND (p.left_at IS NULL OR m.created_at <= p.left_at))\n" +
            "\t\t\tor\n" +
            "\t\t\t\t(p.is_active = true AND (p.left_at is null and m.created_at >= p.joined_at))\n" +
            "\t\t\t)\n" +
            "\t\tAND m.content LIKE CONCAT('%',?3,'%')\n" +
            "\tORDER BY\n" +
            "\tm.created_at",nativeQuery = true)
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
