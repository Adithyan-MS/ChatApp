package com.thinkpalm.ChatApplication.Repository;

import com.thinkpalm.ChatApplication.Model.ParticipantModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ParticipantModelRepository extends JpaRepository<ParticipantModel,Integer> {

    @Query(value = "select count(*) from participant where room_id = ?1 and user_id = ?2",nativeQuery = true)
    Integer existsRoomParticipant(Integer roomId, Integer userid);
    @Query(value = "select is_admin from participant where room_id = ?1 and user_id = ?2",nativeQuery = true)
    Optional<Boolean> isUserAdmin(Integer roomId, Integer userid);

    @Query(value = "select count(*) from participant where room_id=? and is_admin=true",nativeQuery = true)
    Integer getRoomAdminCount(Integer roomId);

    @Transactional
    @Modifying
    @Query(value = "update participant set is_admin = true where room_id = ?1 and user_id = ?2",nativeQuery = true)
    int makeRoomAdmin(Integer roomId, Integer otherUserId);

    @Transactional
    @Modifying
    @Query(value = "update participant set is_admin = false where room_id = ?1 and user_id = ?2",nativeQuery = true)
    int dismissRoomAdmin(Integer roomId, Integer otherUserId);

    @Query(value = "SELECT u.id, u.name, u.profile_pic, p.is_admin\n" +
            "FROM participant AS p\n" +
            "INNER JOIN user AS u ON p.user_id = u.id\n" +
            "WHERE p.room_id = ? AND p.is_active = true\n" +
            "ORDER BY\n" +
            "  CASE\n" +
            "    WHEN p.is_admin = true THEN false\n" +
            "    ELSE true\n" +
            "  END, u.name",nativeQuery = true)
    List<Map<String, Object>> getRoomParticipants(Integer roomId);

    @Query(value = "SELECT u.id, u.name, u.profile_pic, p.is_admin,p.left_at\n" +
            "FROM participant AS p\n" +
            "INNER JOIN user AS u ON p.user_id = u.id\n" +
            "WHERE p.room_id = ? AND p.is_active = false\n" +
            "ORDER BY\n" +
            "  CASE\n" +
            "    WHEN p.is_admin = true THEN false\n" +
            "    ELSE true\n" +
            "  END, u.name",nativeQuery = true)
    List<Map<String, Object>> getPastRoomParticipants(Integer roomId);

    @Transactional
    @Modifying
    @Query(value = "update participant set is_active = false,left_at = ?3 where room_id = ?1 and user_id = ?2",nativeQuery = true)
    void deactivateParticipant(Integer roomId, Integer memberId, Timestamp timestamp);

}


