package com.thinkpalm.ChatApplication.Repository;

import com.thinkpalm.ChatApplication.Model.ParticipantModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ParticipantModelRepository extends JpaRepository<ParticipantModel,Integer> {

    @Query(value = "select count(*) from participant where room_id = ?1 and user_id = ?2",nativeQuery = true)
    Integer existsRoomUser(Integer roomId, Integer id);
    @Query(value = "select is_admin from participant where room_id = ?1 and user_id = ?2",nativeQuery = true)
    boolean isUserAdmin(Integer roomId, Integer userid);

    @Query(value = "select count(*) from participant where roomId=? and is_admin=1",nativeQuery = true)
    Integer findAllAdmins(Integer roomId);
}
