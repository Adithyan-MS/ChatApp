package com.thinkpalm.ChatApplication.Repository;

import com.thinkpalm.ChatApplication.Model.DeletedMessageModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeletedMessageRepository extends JpaRepository<DeletedMessageModel,Integer> {
}
