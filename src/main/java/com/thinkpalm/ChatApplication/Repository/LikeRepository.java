package com.thinkpalm.ChatApplication.Repository;

import com.thinkpalm.ChatApplication.Model.LikeModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface LikeRepository extends JpaRepository<LikeModel,Integer> {

    @Query(value = "select count(*) from like_message where user_id=?1 and message_id=?2",nativeQuery = true)
    Integer checkAlreadyLiked(Integer userId, Integer messageId);

    @Modifying
    @Transactional
    @Query(value = "delete from like_message where user_id = ?1 and message_id = ?2",nativeQuery = true)
    void deleteLiked(Integer userId, Integer messageId);

    @Query(value = "select count(*) from like_message where message_id = ?",nativeQuery = true)
    Integer getMessageLikeCount(Integer messageId);

    @Query(value = "select u.id,u.name,u.profile_pic,lm.created_at from like_message as lm inner join user as u on lm.user_id = u.id where lm.message_id = ?",nativeQuery = true)
    List<Map<String, Object>> getMessageLikedUsers(Integer messageId);
}
