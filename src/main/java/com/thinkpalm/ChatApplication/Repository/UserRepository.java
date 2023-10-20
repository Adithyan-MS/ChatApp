package com.thinkpalm.ChatApplication.Repository;

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
    @Query(value = "update user set bio = ?2,modified_at = ?3 where name = ?1",nativeQuery = true)
    void updateUserBio(String username, String bio, Timestamp timestamp);

    @Query(value = "SELECT id, name, profile_pic, type, max(modified_at) as max_modified_at\n" +
            "FROM (\n" +
            "    SELECT u.id, u.name, u.profile_pic, 'user' as type, max(mr1.modified_at) as modified_at\n" +
            "    FROM chatdb.message_receiver as mr1\n" +
            "    INNER JOIN chatdb.message as m ON m.id = mr1.message_id\n" +
            "    INNER JOIN chatdb.user as u ON u.id = m.sender_id\n" +
            "    WHERE mr1.receiver_id = ?1\n" +
            "    GROUP BY u.id, u.name, u.profile_pic\n" +
            "    UNION\n" +
            "    SELECT r.id, r.name, r.room_pic, 'room' as type, max(mr2.modified_at) as modified_at\n" +
            "    FROM chatdb.message_room as mr2\n" +
            "    INNER JOIN chatdb.room as r ON r.id = mr2.room_id\n" +
            "    INNER JOIN chatdb.participant as p ON p.room_id = r.id\n" +
            "    WHERE p.user_id = ?1\n" +
            "    GROUP BY r.id, r.name, r.room_pic\n" +
            ") AS combined_results\n" +
            "GROUP BY id, name, profile_pic, type\n" +
            "ORDER BY max_modified_at DESC",nativeQuery = true)
    List<Map<String,Object>> findAllChatsOfUser(Integer currentUserId);
}
