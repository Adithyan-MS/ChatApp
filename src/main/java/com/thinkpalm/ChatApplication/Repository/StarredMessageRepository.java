package com.thinkpalm.ChatApplication.Repository;

import com.thinkpalm.ChatApplication.Model.StarredMessageModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface StarredMessageRepository extends JpaRepository<StarredMessageModel,Integer> {

    @Query(value = "select count(*) from starred_message where user_id=?1 and message_id=?2",nativeQuery = true)
    Integer checkAlreadyStarred(Integer id, Integer messageId);

    @Modifying
    @Transactional
    @Query(value = "delete from starred_message where user_id = ?1 and message_id = ?2",nativeQuery = true)
    void deleteLiked(Integer id, Integer messageId);
}
