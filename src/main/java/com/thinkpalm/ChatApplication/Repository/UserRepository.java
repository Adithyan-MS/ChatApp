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

    @Query(value = "SELECT id, name, profile_pic, type, max(modified_at) as max_modified_at\n" +
            "\tFROM (\n" +
            "\t\tSELECT u.id, u.name, u.profile_pic, 'user' as type, mr1.modified_at\n" +
            "\t\t\tFROM chatdb.message_receiver as mr1 \n" +
            "\t\t\tINNER JOIN chatdb.message as m ON m.id = mr1.message_id \n" +
            "\t\t\tINNER JOIN chatdb.user as u ON u.id = \n" +
            "\t\t\t\tCASE\n" +
            "\t\t\t\t\tWHEN m.sender_id = ?1 THEN mr1.receiver_id\n" +
            "\t\t\t\t\tWHEN mr1.receiver_id = ?1 THEN m.sender_id\n" +
            "\t\t\t\tEND\n" +
            "\t\t\tWHERE\n" +
            "\t\t\t\tm.sender_id = ?1 OR mr1.receiver_id = ?1\n" +
            "\t\tUNION\n" +
            "\t\tSELECT r.id,r.name, r.room_pic, 'room' as type,CASE WHEN p.left_at IS not null THEN p.left_at ELSE MAX(mr2.modified_at) END as timestamp\n" +
            "\t\t\tFROM chatdb.message_room as mr2\n" +
            "\t\t\tINNER JOIN chatdb.room as r ON r.id = mr2.room_id\n" +
            "\t\t\tINNER JOIN chatdb.participant as p ON p.room_id = r.id\n" +
            "\t\t\tWHERE p.user_id = ?1 or \n" +
            "\t\t\t\t(p.is_active is false and mr2.modified_at < p.left_at)\n" +
            "\t\t\tGROUP BY r.id, r.name, r.room_pic, p.left_at\n" +
            "\t\tUNION\n" +
            "\t\tSELECT r.id, r.name, r.room_pic, 'room' as type, r.created_at as modified_at\n" +
            "\t\t\tFROM chatdb.room as r\n" +
            "\t\t\tINNER JOIN chatdb.participant as p ON p.room_id = r.id\n" +
            "\t\t\tWHERE p.user_id = ?1\n" +
            "\t\t) AS combined_results\n" +
            "\t\tGROUP BY id, name, profile_pic, type\n" +
            "\t\tORDER BY max_modified_at DESC;",nativeQuery = true)
    List<Map<String,Object>> findAllChatsOfUser(Integer currentUserId);

    @Query(value = "select * from user where name = ?1 or phone_number = ?2",nativeQuery = true)
    List<UserModel> existByNameOrPhonenumber(String name, String phoneNumber);

    @Query(value = "SELECT id, name, profile_pic, type, max(modified_at) as max_modified_at\n" +
            "\tFROM (\n" +
            "\t\tSELECT u.id, u.name, u.profile_pic, 'user' as type, mr1.modified_at\n" +
            "\t\t\tFROM chatdb.message_receiver as mr1 \n" +
            "\t\t\tINNER JOIN chatdb.message as m ON m.id = mr1.message_id \n" +
            "\t\t\tINNER JOIN chatdb.user as u ON u.id = \n" +
            "\t\t\t\tCASE\n" +
            "\t\t\t\t\tWHEN m.sender_id = ?1 THEN mr1.receiver_id\n" +
            "\t\t\t\t\tWHEN mr1.receiver_id = ?1 THEN m.sender_id\n" +
            "\t\t\t\tEND\n" +
            "\t\t\tWHERE\n" +
            "\t\t\t\tm.sender_id = ?1 OR mr1.receiver_id = ?1\n" +
            "\t\tUNION\n" +
            "\t\tSELECT r.id,r.name, r.room_pic, 'room' as type,CASE WHEN p.left_at IS not null THEN p.left_at ELSE MAX(mr2.modified_at) END as timestamp\n" +
            "\t\t\tFROM chatdb.message_room as mr2\n" +
            "\t\t\tINNER JOIN chatdb.room as r ON r.id = mr2.room_id\n" +
            "\t\t\tINNER JOIN chatdb.participant as p ON p.room_id = r.id\n" +
            "\t\t\tWHERE p.user_id = ?1 or \n" +
            "\t\t\t\t(p.is_active is false and mr2.modified_at < p.left_at)\n" +
            "\t\t\tGROUP BY r.id, r.name, r.room_pic, p.left_at\n" +
            "\t\tUNION\n" +
            "\t\tSELECT r.id, r.name, r.room_pic, 'room' as type, r.created_at as modified_at\n" +
            "\t\t\tFROM chatdb.room as r\n" +
            "\t\t\tINNER JOIN chatdb.participant as p ON p.room_id = r.id\n" +
            "\t\t\tWHERE p.user_id = ?1\n" +
            "\t\t) AS combined_results\n" +
            "\t\twhere name LIKE CONCAT('%',?2,'%')\n" +
            "\t\tGROUP BY id, name, profile_pic, type\n" +
            "\t\tORDER BY max_modified_at DESC;",nativeQuery = true)
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
