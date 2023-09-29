package com.thinkpalm.ChatApplication.Repository;

import com.thinkpalm.ChatApplication.Model.MessageReceiverModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageReceiverRepository extends JpaRepository<MessageReceiverModel,Integer> {
}
