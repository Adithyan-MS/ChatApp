package com.thinkpalm.ChatApplication.Repository;

import com.thinkpalm.ChatApplication.Model.RoomModel;
import com.thinkpalm.ChatApplication.Model.UserModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel,Integer> {
    Optional<UserModel> findByName(String username);

    @Transactional
    @Modifying
    @Query(value = "update user set bio = ?2 where name = ?1",nativeQuery = true)
    void updateUserBio(String username, String bio);

//    @Query(value = "SELECT id, name, profile_pic, type, max(modified_at) as max_modified_at\n" +
//            "\tFROM (\n" +
//            "\t\tSELECT u.id, u.name, u.profile_pic, 'user' as type, mr1.modified_at\n" +
//            "\t\t\tFROM chatdb.message_receiver as mr1 \n" +
//            "\t\t\tINNER JOIN chatdb.message as m ON m.id = mr1.message_id \n" +
//            "\t\t\tINNER JOIN chatdb.user as u ON u.id = \n" +
//            "\t\t\t\tCASE\n" +
//            "\t\t\t\t\tWHEN m.sender_id = ?1 THEN mr1.receiver_id\n" +
//            "\t\t\t\t\tWHEN mr1.receiver_id = ?1 THEN m.sender_id\n" +
//            "\t\t\t\tEND\n" +
//            "\t\t\tWHERE\n" +
//            "\t\t\t\tm.sender_id = ?1 OR mr1.receiver_id = ?1\n" +
//            "\t\tUNION\n" +
//            "\t\tSELECT r.id,r.name, r.room_pic, 'room' as type,CASE WHEN p.left_at IS not null THEN p.left_at ELSE MAX(mr2.modified_at) END as timestamp\n" +
//            "\t\t\tFROM chatdb.message_room as mr2\n" +
//            "\t\t\tINNER JOIN chatdb.room as r ON r.id = mr2.room_id\n" +
//            "\t\t\tINNER JOIN chatdb.participant as p ON p.room_id = r.id\n" +
//            "\t\t\tWHERE p.user_id = ?1 or \n" +
//            "\t\t\t\t(p.is_active is false and mr2.modified_at < p.left_at)\n" +
//            "\t\t\tGROUP BY r.id, r.name, r.room_pic, p.left_at\n" +
//            "\t\tUNION\n" +
//            "\t\tSELECT r.id, r.name, r.room_pic, 'room' as type, r.created_at as modified_at\n" +
//            "\t\t\tFROM chatdb.room as r\n" +
//            "\t\t\tINNER JOIN chatdb.participant as p ON p.room_id = r.id\n" +
//            "\t\t\tWHERE p.user_id = ?1\n" +
//            "\t\t) AS combined_results\n" +
//            "\t\tGROUP BY id, name, profile_pic, type\n" +
//            "\t\tORDER BY max_modified_at DESC;",nativeQuery = true)

    @Query(value = "WITH ranked_messages AS (\n" +
            "    SELECT\n" +
            "        u.id,\n" +
            "        u.name,\n" +
            "        u.profile_pic,\n" +
            "        'user' as type,\n" +
            "        m.content as latest_message,\n" +
            "        m.sender_id as latest_message_sender_id,\n" +
            "        u1.name as latest_message_sender_name,\n" +
            "        mr1.modified_at as max_modified_at,\n" +
            "        ROW_NUMBER() OVER (PARTITION BY u.id ORDER BY mr1.modified_at DESC) as message_rank\n" +
            "    FROM\n" +
            "        chatdb.message_receiver as mr1\n" +
            "        INNER JOIN chatdb.message as m ON m.id = mr1.message_id\n" +
            "        INNER JOIN chatdb.user as u1 ON u1.id = m.sender_id\n" +
            "        INNER JOIN chatdb.user as u ON u.id = \n" +
            "            CASE\n" +
            "                WHEN m.sender_id = ?1 THEN mr1.receiver_id\n" +
            "                WHEN mr1.receiver_id = ?1 THEN m.sender_id\n" +
            "            END\n" +
            "        LEFT JOIN chatdb.deleted_message as dm1 ON dm1.message_id = m.id AND dm1.deleted_by = ?1\n" +
            "    WHERE\n" +
            "        (m.sender_id = ?1 OR mr1.receiver_id = ?1)\n" +
            "        AND dm1.message_id IS NULL\n" +
            "    UNION\n" +
            "    SELECT\n" +
            "        r.id,\n" +
            "        r.name,\n" +
            "        r.room_pic as profile_pic,\n" +
            "        'room' as type,\n" +
            "        m.content as latest_message,\n" +
            "        m.sender_id as latest_message_sender_id,\n" +
            "        u.name as latest_message_sender_name,\n" +
            "        CASE WHEN p.left_at IS not null THEN p.left_at ELSE MAX(m.modified_at) END as max_modified_at,\n" +
            "        ROW_NUMBER() OVER (PARTITION BY r.id ORDER BY mr2.modified_at DESC) as message_rank\n" +
            "    FROM\n" +
            "        chatdb.message_room as mr2\n" +
            "        INNER JOIN chatdb.room as r ON r.id = mr2.room_id\n" +
            "        INNER JOIN chatdb.message as m ON m.id = mr2.message_id\n" +
            "        INNER JOIN chatdb.user as u ON u.id = m.sender_id\n" +
            "        INNER JOIN chatdb.participant as p ON p.room_id = r.id\n" +
            "        LEFT JOIN chatdb.deleted_message as dm2 ON dm2.message_id = m.id AND dm2.deleted_by = ?1\n" +
            "    WHERE\n" +
            "        (p.user_id = ?1 or (p.is_active is false and mr2.modified_at < p.left_at))\n" +
            "        AND dm2.message_id IS NULL\n" +
            "    GROUP BY r.id, r.name, r.room_pic, m.id\n" +
            ")\n" +
            "SELECT\n" +
            "    id,\n" +
            "    name,\n" +
            "    profile_pic,\n" +
            "    type,\n" +
            "    latest_message,\n" +
            "    latest_message_sender_id,\n" +
            "    latest_message_sender_name,\n" +
            "    max_modified_at\n" +
            "FROM\n" +
            "    ranked_messages\n" +
            "WHERE\n" +
            "    message_rank = 1\n" +
            "ORDER BY\n" +
            "    max_modified_at DESC;",nativeQuery = true)
    List<Map<String,Object>> findAllChatsOfUser(Integer currentUserId);

    @Query(value = "select * from user where name = ?1 or phone_number = ?2",nativeQuery = true)
    List<UserModel> existByNameOrPhonenumber(String name, String phoneNumber);

    @Query(value = "WITH ranked_messages AS (\n" +
            "    SELECT\n" +
            "        u.id,\n" +
            "        u.name,\n" +
            "        u.profile_pic,\n" +
            "        'user' as type,\n" +
            "        m.content as latest_message,\n" +
            "        m.sender_id as latest_message_sender_id,\n" +
            "        u1.name as latest_message_sender_name,\n" +
            "        mr1.modified_at as max_modified_at,\n" +
            "        ROW_NUMBER() OVER (PARTITION BY u.id ORDER BY mr1.modified_at DESC) as message_rank\n" +
            "    FROM\n" +
            "        chatdb.message_receiver as mr1\n" +
            "        INNER JOIN chatdb.message as m ON m.id = mr1.message_id\n" +
            "        INNER JOIN chatdb.user as u1 ON u1.id = m.sender_id\n" +
            "        INNER JOIN chatdb.user as u ON u.id = \n" +
            "            CASE\n" +
            "                WHEN m.sender_id = ?1 THEN mr1.receiver_id\n" +
            "                WHEN mr1.receiver_id = ?1 THEN m.sender_id\n" +
            "            END\n" +
            "        LEFT JOIN chatdb.deleted_message as dm1 ON dm1.message_id = m.id AND dm1.deleted_by = ?1\n" +
            "    WHERE\n" +
            "        (m.sender_id = ?1 OR mr1.receiver_id = ?1)\n" +
            "        AND dm1.message_id IS NULL\n" +
            "    UNION\n" +
            "    SELECT\n" +
            "        r.id,\n" +
            "        r.name,\n" +
            "        r.room_pic as profile_pic,\n" +
            "        'room' as type,\n" +
            "        m.content as latest_message,\n" +
            "        m.sender_id as latest_message_sender_id,\n" +
            "        u.name as latest_message_sender_name,\n" +
            "        CASE WHEN p.left_at IS not null THEN p.left_at ELSE MAX(m.modified_at) END as max_modified_at,\n" +
            "        ROW_NUMBER() OVER (PARTITION BY r.id ORDER BY mr2.modified_at DESC) as message_rank\n" +
            "    FROM\n" +
            "        chatdb.message_room as mr2\n" +
            "        INNER JOIN chatdb.room as r ON r.id = mr2.room_id\n" +
            "        INNER JOIN chatdb.message as m ON m.id = mr2.message_id\n" +
            "        INNER JOIN chatdb.user as u ON u.id = m.sender_id\n" +
            "        INNER JOIN chatdb.participant as p ON p.room_id = r.id\n" +
            "        LEFT JOIN chatdb.deleted_message as dm2 ON dm2.message_id = m.id AND dm2.deleted_by = ?1\n" +
            "    WHERE\n" +
            "        (p.user_id = ?1 or (p.is_active is false and mr2.modified_at < p.left_at))\n" +
            "        AND dm2.message_id IS NULL\n" +
            "    GROUP BY r.id, r.name, r.room_pic, m.id\n" +
            ")\n" +
            "SELECT\n" +
            "    id,\n" +
            "    name,\n" +
            "    profile_pic,\n" +
            "    type,\n" +
            "    latest_message,\n" +
            "    latest_message_sender_id,\n" +
            "    latest_message_sender_name,\n" +
            "    max_modified_at\n" +
            "FROM\n" +
            "    ranked_messages\n" +
            "WHERE\n" +
            "    message_rank = 1\n" +
            "    and name LIKE CONCAT('%',?2,'%')\n" +
            "ORDER BY\n" +
            "    max_modified_at DESC;",nativeQuery = true)
    List<Map<String, Object>> searchChats(Integer currentUserId,String searchName);

    @Query(value = "SELECT r.*\n" +
            "FROM chatdb.room r\n" +
            "JOIN chatdb.participant p ON r.id = p.room_id\n" +
            "WHERE p.user_id = ?1 OR p.user_id = ?2\n" +
            "GROUP BY r.id\n" +
            "HAVING COUNT(DISTINCT p.user_id) = 2",nativeQuery = true)
    List<Map<String, Object>> getCommonRooms(Integer id, Integer id1);

    @Query(value = "SELECT u.id,u.name,u.profile_pic FROM chatdb.user as u where u.name LIKE CONCAT('%',?1,'%') && u.id != ?2",nativeQuery = true)
    List<Map<String, Object>> searchUsers(String searchName,Integer currentUserId);
}
