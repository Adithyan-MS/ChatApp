package com.thinkpalm.ChatApplication.Service;

import com.thinkpalm.ChatApplication.Model.*;
import com.thinkpalm.ChatApplication.Repository.ParticipantModelRepository;
import com.thinkpalm.ChatApplication.Repository.RoomRepository;
import com.thinkpalm.ChatApplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final ParticipantModelRepository participantModelRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository, UserRepository userRepository, ParticipantModelRepository participantModelRepository){
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

    public String addMember(Integer roomId,List<Integer> memberIds){
        UserModel currentUser = userRepository.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);
        RoomModel room = roomRepository.findById(roomId).orElse(null);
        if(room!=null){
            if(participantModelRepository.existsRoomUser(roomId,currentUser.getId())!=0){
                if(participantModelRepository.isUserAdmin(roomId,currentUser.getId())){
                    for (Integer memberId : memberIds){
                        UserModel user = userRepository.findById(memberId).orElse(null);
                        if(user!=null){
                            ParticipantModel participant = new ParticipantModel();
                            participant.setRoom(room);
                            participant.setUser(user);
                            participant.setIs_admin(false);
                            participant.setJoined_at(Timestamp.valueOf(LocalDateTime.now()));
                            participantModelRepository.save(participant);
                        }
                    }
                    return "Member successfully added.";
                }
                else {
                    return "User is not an admin!";
                }
            }else{
                return "User is not a room member!";
            }
        }
        else{
            return "No such room!";
        }
    }

    public String removeMember(Integer roomId,List<Integer> memberIds){
        UserModel currentUser = userRepository.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);
        RoomModel room = roomRepository.findById(roomId).orElse(null);
        if(room!=null){
            if(participantModelRepository.existsRoomUser(roomId,currentUser.getId())!=0){
                if(participantModelRepository.isUserAdmin(roomId,currentUser.getId())){
                    String response="";
                    for (Integer memberId : memberIds){
                        if(participantModelRepository.existsRoomUser(roomId,memberId)!=0){
                            participantModelRepository.deleteParticipant(roomId,memberId);
                            response = response+"\n"+memberId+" removed";
                        }else{
                            response = response+"\n"+memberId+" is not a participant";
                        }
                    }
                    return response;
                }
                else {
                    return "User is not an admin!";
                }
            }else{
                return "User is not a room member!";
            }
        }
        else{
            return "No such room!";
        }
    }

    public String joinRoom(Integer roomId){
        UserModel currentUser = userRepository.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);
        RoomModel room = roomRepository.findById(roomId).orElse(null);
        if(room!=null){
            ParticipantModel participant = new ParticipantModel();
            participant.setUser(currentUser);
            participant.setRoom(room);
            participant.setIs_admin(false);
            participant.setJoined_at(Timestamp.valueOf(LocalDateTime.now()));
            participantModelRepository.save(participant);
            return "user joined.";
        }
        else{
            return "No such room!";
        }
    }

    public String exitRoom(Integer roomId){
        UserModel currentUser = userRepository.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);
        RoomModel room = roomRepository.findById(roomId).orElse(null);
        if(room!=null){
            if(participantModelRepository.isUserAdmin(roomId, currentUser.getId() && participantModelRepository.findAllAdmins(roomId)!=0)


            participantModelRepository.(roomId, currentUser.getId());
            return "user exited from room.";
        }
        else{
            return "No such room!";
        }
    }
}
