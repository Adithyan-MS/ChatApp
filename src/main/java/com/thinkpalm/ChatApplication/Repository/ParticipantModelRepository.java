package com.thinkpalm.ChatApplication.Repository;

import com.thinkpalm.ChatApplication.Model.ParticipantModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantModelRepository extends JpaRepository<ParticipantModel,Integer> {
}
