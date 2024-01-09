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

    @Query(value = "WITH ranked_messages AS (\n" +
            "\tSELECT\n" +
            "\t\tu.id,\n" +
            "\t\tu.name,\n" +
            "\t\tu.profile_pic,\n" +
            "\t\t'user' as type,\n" +
            "\t\tm.content as latest_message,\n" +
            "\t\tm.type as latest_message_type,\n" +
            "\t\tm.sender_id as latest_message_sender_id,\n" +
            "\t\tu1.name as latest_message_sender_name,\n" +
            "\t\tmr1.modified_at as max_modified_at,\n" +
            "\t\tROW_NUMBER() OVER (PARTITION BY u.id ORDER BY mr1.modified_at DESC) as message_rank\n" +
            "\tFROM\n" +
            "\t\tchatdb.message_receiver as mr1\n" +
            "\t\tINNER JOIN chatdb.message as m ON m.id = mr1.message_id\n" +
            "\t\tINNER JOIN chatdb.user as u1 ON u1.id = m.sender_id\n" +
            "\t\tINNER JOIN chatdb.user as u ON u.id = \n" +
            "\t\t\tCASE\n" +
            "\t\t\t\tWHEN m.sender_id = ?1 THEN mr1.receiver_id\n" +
            "\t\t\t\tWHEN mr1.receiver_id = ?1 THEN m.sender_id\n" +
            "\t\t\tEND\n" +
            "\t\tLEFT JOIN chatdb.deleted_message as dm1 ON dm1.message_id = m.id AND dm1.deleted_by = ?1\n" +
            "\tWHERE\n" +
            "\t\t(m.sender_id = ?1 OR mr1.receiver_id = ?1)\n" +
            "\t\tAND dm1.message_id IS NULL\n" +
            "\tUNION\n" +
            "\tSELECT\n" +
            "\t\tr.id,\n" +
            "\t\tr.name,\n" +
            "\t\tr.room_pic as profile_pic,\n" +
            "\t\t'room' as type,\n" +
            "\t\tm.content as latest_message,\n" +
            "\t\tm.type as latest_message_type,\n" +
            "\t\tu.id as latest_message_sender_id,\n" +
            "\t\tu.name as latest_message_sender_name,\n" +
            "\t\tCASE WHEN p.left_at IS not null THEN p.left_at WHEN m.sender_id is null THEN r.created_at ELSE MAX(m.modified_at) END as max_modified_at,\n" +
            "\t\tROW_NUMBER() OVER (PARTITION BY r.id ORDER BY mr2.modified_at DESC) as message_rank\n" +
            "\tFROM\n" +
            "\t\tchatdb.room as r\n" +
            "\t\tLEFT JOIN chatdb.message_room as mr2 ON r.id = mr2.room_id\n" +
            "\t\tLEFT JOIN chatdb.message as m ON m.id = mr2.message_id\n" +
            "\t\tLEFT JOIN chatdb.user as u ON u.id = CASE WHEN m.sender_id IS NOT NULL THEN m.sender_id ELSE r.created_by END\n" +
            "\t\tINNER JOIN chatdb.participant as p ON p.room_id = r.id\n" +
            "\t\tLEFT JOIN chatdb.deleted_message as dm2 ON dm2.message_id = m.id AND dm2.deleted_by = ?1\n" +
            "\tWHERE\n" +
            "\t\t((p.user_id = ?1 and (p.is_active is false and mr2.modified_at < p.left_at))\n" +
            "\t\tor\n" +
            "\t\t(p.user_id = ?1 and (p.is_active is true)))\n" +
            "\t\tAND p.is_deleted = false\n" +
            "\t\tAND dm2.message_id IS NULL\n" +
            "\tGROUP BY r.id, r.name, r.room_pic, m.id\n" +
            ")\n" +
            "SELECT\n" +
            "\tid,\n" +
            "\tname,\n" +
            "\tprofile_pic,\n" +
            "\ttype,\n" +
            "\tlatest_message,\n" +
            "\tlatest_message_type,\n" +
            "\tlatest_message_sender_id,\n" +
            "\tlatest_message_sender_name,\n" +
            "\tmax_modified_at\n" +
            "FROM\n" +
            "\tranked_messages\n" +
            "WHERE\n" +
            "\tmessage_rank = 1\n" +
            "ORDER BY\n" +
            "\tmax_modified_at DESC",nativeQuery = true)
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
            "        m.type as latest_message_type,\n" +
            "        m.sender_id as latest_message_sender_id,\n" +
            "        u1.name as latest_message_sender_name,\n" +
            "        mr1.modified_at as max_modified_at,\n" +
            "        ROW_NUMBER() OVER (PARTITION BY u.id ORDER BY mr1.modified_at DESC) as message_rank\n" +
            "    FROM\n" +
            "        chatdb.message_receiver as mr1\n" +
            "        INNER JOIN chatdb.message as m ON m.id = mr1.message_id\n" +
            "        INNER JOIN chatdb.user as u1 ON u1.id = m.sender_id\n" +
            "        RIGHT JOIN chatdb.user as u ON u.id = \n" +
            "            CASE\n" +
            "                WHEN m.sender_id = ?1 THEN mr1.receiver_id\n" +
            "                WHEN mr1.receiver_id = ?1 THEN m.sender_id\n" +
            "            END\n" +
            "        LEFT JOIN chatdb.deleted_message as dm1 ON dm1.message_id = m.id AND dm1.deleted_by = ?1\n" +
            "    WHERE\n" +
            "        (m.sender_id = ?1 OR mr1.receiver_id = ?1)\n" +
            "        OR dm1.message_id IS NULL\n" +
            "        AND u.id <> ?1\n" +
            "    UNION\n" +
            "    SELECT\n" +
            "        r.id,\n" +
            "        r.name,\n" +
            "        r.room_pic as profile_pic,\n" +
            "        'room' as type,\n" +
            "        m.content as latest_message,\n" +
            "        m.type as latest_message_type,\n" +
            "        u.id as latest_message_sender_id,\n" +
            "        u.name as latest_message_sender_name,\n" +
            "        CASE WHEN p.left_at IS not null THEN p.left_at WHEN m.sender_id is null THEN r.created_at ELSE MAX(m.modified_at) END as max_modified_at,\n" +
            "        ROW_NUMBER() OVER (PARTITION BY r.id ORDER BY mr2.modified_at DESC) as message_rank\n" +
            "    FROM\n" +
            "        chatdb.room as r\n" +
            "        LEFT JOIN chatdb.message_room as mr2 ON r.id = mr2.room_id\n" +
            "        LEFT JOIN chatdb.message as m ON m.id = mr2.message_id\n" +
            "        LEFT JOIN chatdb.user as u ON u.id = CASE WHEN m.sender_id IS NOT NULL THEN m.sender_id ELSE r.created_by END\n" +
            "        INNER JOIN chatdb.participant as p ON p.room_id = r.id\n" +
            "        LEFT JOIN chatdb.deleted_message as dm2 ON dm2.message_id = m.id AND dm2.deleted_by = ?1\n" +
            "    WHERE\n" +
            "       ((p.user_id = ?1 and (p.is_active is false and mr2.modified_at < p.left_at))\n" +
            "       or\n" +
            "       (p.user_id = ?1 and (p.is_active is true)))\n" +
            "       AND p.is_deleted = false\n" +
            "       AND dm2.message_id IS NULL\n" +
            "    GROUP BY r.id, r.name, r.room_pic, m.id\n" +
            ")\n" +
            "SELECT\n" +
            "    id,\n" +
            "    name,\n" +
            "    profile_pic,\n" +
            "    type,\n" +
            "    latest_message,\n" +
            "    latest_message_type,\n" +
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

    @Query(value = "SELECT\n" +
            "        u.id,\n" +
            "        u.name,\n" +
            "        u.profile_pic,\n" +
            "        'user' as type,\n" +
            "        m.content as latest_message,\n" +
            "        m.type as latest_message_type,\n" +
            "        m.id as latest_message_id,\n" +
            "        m.sender_id as latest_message_sender_id,\n" +
            "        u1.name as latest_message_sender_name,\n" +
            "        sm.modified_at as max_modified_at\n" +
            "    FROM\n" +
            "        chatdb.starred_message AS sm\n" +
            "        INNER JOIN chatdb.message_receiver as mr1 ON mr1.message_id = sm.message_id\n" +
            "        INNER JOIN chatdb.message as m ON m.id = mr1.message_id\n" +
            "        INNER JOIN chatdb.user as u1 ON u1.id = m.sender_id\n" +
            "        INNER JOIN chatdb.user as u ON u.id = \n" +
            "            CASE\n" +
            "                WHEN m.sender_id = ?1 THEN mr1.receiver_id\n" +
            "                WHEN mr1.receiver_id = ?1 THEN m.sender_id\n" +
            "            END\n" +
            "        LEFT JOIN chatdb.deleted_message AS dm1 ON dm1.message_id = m.id AND dm1.deleted_by = ?1\n" +
            "    WHERE\n" +
            "        (m.sender_id = ?1 OR mr1.receiver_id = ?1)\n" +
            "        AND sm.user_id = ?1\n" +
            "        AND dm1.message_id IS NULL\n" +
            "    UNION    \n" +
            "    SELECT\n" +
            "        r.id,\n" +
            "        r.name,\n" +
            "        r.room_pic as profile_pic,\n" +
            "        'room' as type,\n" +
            "        m.content as latest_message,\n" +
            "        m.type as latest_message_type,\n" +
            "        m.id as latest_message_id,\n" +
            "        u.id as latest_message_sender_id,\n" +
            "        u.name as latest_message_sender_name,\n" +
            "\t\tsm.modified_at as max_modified_at\n" +
            "    FROM\n" +
            "\t\tchatdb.starred_message as sm\n" +
            "        INNER JOIN chatdb.message_room as mr2 ON mr2.message_id = sm.message_id\n" +
            "        INNER JOIN chatdb.message as m ON m.id = mr2.message_id\n" +
            "        INNER JOIN chatdb.room as r ON r.id = mr2.room_id\n" +
            "        INNER JOIN chatdb.user as u ON u.id = m.sender_id\n" +
            "        INNER JOIN chatdb.participant as p ON p.room_id = r.id\n" +
            "        LEFT JOIN chatdb.deleted_message as dm2 ON dm2.message_id = m.id AND dm2.deleted_by = ?1\n" +
            "    WHERE\n" +
            "        (p.user_id = ?1 AND sm.user_id = ?1)\n" +
            "        AND dm2.message_id IS NULL\n" +
            "    GROUP BY r.id, r.name, r.room_pic, m.id\n" +
            "    ORDER BY max_modified_at DESC;",nativeQuery = true)
    List<Map<String, Object>> findAllStarredMessages(Integer id);

    @Query(value = "SELECT\n" +
            "        u.id,\n" +
            "        u.name,\n" +
            "        u.profile_pic,\n" +
            "        'user' as type,\n" +
            "        m.content as latest_message,\n" +
            "        m.type as latest_message_type,\n" +
            "        m.id as latest_message_id,\n" +
            "        m.sender_id as latest_message_sender_id,\n" +
            "        u1.name as latest_message_sender_name,\n" +
            "        sm.user_id as starred_by,\n" +
            "        sm.modified_at as max_modified_at\n" +
            "    FROM\n" +
            "        chatdb.starred_message AS sm\n" +
            "        INNER JOIN chatdb.message_receiver as mr1 ON mr1.message_id = sm.message_id\n" +
            "        INNER JOIN chatdb.message as m ON m.id = mr1.message_id\n" +
            "        INNER JOIN chatdb.user as u1 ON u1.id = m.sender_id\n" +
            "        INNER JOIN chatdb.user as u ON u.id = \n" +
            "            CASE\n" +
            "                WHEN m.sender_id = ?1 THEN mr1.receiver_id\n" +
            "                WHEN mr1.receiver_id = ?1 THEN m.sender_id\n" +
            "            END\n" +
            "        LEFT JOIN chatdb.deleted_message AS dm1 ON dm1.message_id = m.id AND dm1.deleted_by = ?1\n" +
            "    WHERE\n" +
            "        (m.sender_id = ?1 OR mr1.receiver_id = ?1)\n" +
            "        AND sm.user_id = ?1\n" +
            "        AND dm1.message_id IS NULL\n" +
            "        AND u.name LIKE concat('%',?2,'%')\n" +
            "    UNION    \n" +
            "    SELECT\n" +
            "        r.id,\n" +
            "        r.name,\n" +
            "        r.room_pic as profile_pic,\n" +
            "        'room' as type,\n" +
            "        m.content as latest_message,\n" +
            "        m.type as latest_message_type,\n" +
            "        m.id as latest_message_id,\n" +
            "        u.id as latest_message_sender_id,\n" +
            "        u.name as latest_message_sender_name,\n" +
            "        sm.user_id as starred_by,\n" +
            "\t\tsm.modified_at as max_modified_at\n" +
            "    FROM\n" +
            "\t\tchatdb.starred_message as sm\n" +
            "        INNER JOIN chatdb.message_room as mr2 ON mr2.message_id = sm.message_id\n" +
            "        INNER JOIN chatdb.message as m ON m.id = mr2.message_id\n" +
            "        INNER JOIN chatdb.room as r ON r.id = mr2.room_id\n" +
            "        INNER JOIN chatdb.user as u ON u.id = m.sender_id\n" +
            "        INNER JOIN chatdb.participant as p ON p.room_id = r.id\n" +
            "        LEFT JOIN chatdb.deleted_message as dm2 ON dm2.message_id = m.id AND dm2.deleted_by = ?1\n" +
            "    WHERE\n" +
            "        (p.user_id = ?1 AND sm.user_id = ?1)\n" +
            "        AND dm2.message_id IS NULL\n" +
            "        AND r.name LIKE concat('%',?2,'%')\n" +
            "    GROUP BY r.id, r.name, r.room_pic, m.id\n" +
            "    ORDER BY max_modified_at DESC",nativeQuery = true)
    List<Map<String, Object>> searchStarredMessageChats(Integer id, String searchName);
}
