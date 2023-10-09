package com.thinkpalm.ChatApplication.Repository;

import com.thinkpalm.ChatApplication.Model.MessageHistoryModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageHistoryRepository extends JpaRepository<MessageHistoryModel,Integer> {
}
