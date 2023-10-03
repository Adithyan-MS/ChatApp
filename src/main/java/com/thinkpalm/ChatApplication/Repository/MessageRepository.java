package com.thinkpalm.ChatApplication.Repository;


import com.thinkpalm.ChatApplication.Model.MessageModel;
import com.thinkpalm.ChatApplication.Model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MessageRepository extends JpaRepository<MessageModel,Integer> {

}
