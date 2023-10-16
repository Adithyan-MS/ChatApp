package com.thinkpalm.ChatApplication.Repository;

import com.thinkpalm.ChatApplication.Model.UserModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel,Integer> {
    Optional<UserModel> findByName(String username);

    @Transactional
    @Modifying
    @Query(value = "update user set bio = ?2,modified_at = ?3 where name = ?1",nativeQuery = true)
    void updateUserBio(String username, String bio, Timestamp timestamp);
//
//    @Query
//    List<Object> findAllActiveChats();
}
