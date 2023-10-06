package com.thinkpalm.ChatApplication.Service;

import com.thinkpalm.ChatApplication.Model.CreateRoomRequest;
import com.thinkpalm.ChatApplication.Model.ParticipantModel;
import com.thinkpalm.ChatApplication.Model.RoomModel;
import com.thinkpalm.ChatApplication.Model.UserModel;
import com.thinkpalm.ChatApplication.Repository.ParticipantModelRepository;
import com.thinkpalm.ChatApplication.Repository.RoomRepository;
import com.thinkpalm.ChatApplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final ParticipantModelRepository participantModelRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository,UserRepository userRepository,ParticipantModelRepository participantModelRepository){
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.participantModelRepository = participantModelRepository;
    }

    public String createRoom(CreateRoomRequest createRoomRequest) {
        UserModel currentUser = userRepository.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);
        if (currentUser != null) {
            RoomModel room = new RoomModel();
            room.setName(createRoomRequest.getName());
            room.setDescription(createRoomRequest.getDesc());
            room.setRoom_pic(createRoomRequest.getPic());
            room.setCreated_at(Timestamp.valueOf(LocalDateTime.now()));
            roomRepository.save(room);

            ParticipantModel participantModel = new ParticipantModel();
            participantModel.setRoom(room);
            participantModel.setUser(currentUser);
            participantModel.setIs_admin(true);
            participantModel.setJoined_at(Timestamp.valueOf(LocalDateTime.now()));
            participantModelRepository.save(participantModel);

            for (String participant : createRoomRequest.getParticipants()) {
                UserModel user = userRepository.findByName(participant).orElse(null);
                if (user != null) {
                    ParticipantModel participantModel1 = new ParticipantModel();
                    participantModel1.setRoom(room);
                    participantModel1.setUser(user);
                    participantModel1.setIs_admin(false);
                    participantModel1.setJoined_at(Timestamp.valueOf(LocalDateTime.now()));
                    participantModelRepository.save(participantModel1);
                }
            }
            return "room created!";
        }else{
            return "can't create room!";
        }
    }

}
