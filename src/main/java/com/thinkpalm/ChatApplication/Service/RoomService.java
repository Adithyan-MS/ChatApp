package com.thinkpalm.ChatApplication.Service;

import com.thinkpalm.ChatApplication.Util.AppContext;
import com.thinkpalm.ChatApplication.Model.*;
import com.thinkpalm.ChatApplication.Repository.ParticipantModelRepository;
import com.thinkpalm.ChatApplication.Repository.RoomRepository;
import com.thinkpalm.ChatApplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final ParticipantModelRepository participantModelRepository;
    private final ImageService imageService;

    @Autowired
    public RoomService(RoomRepository roomRepository, UserRepository userRepository, ParticipantModelRepository participantModelRepository, ImageService imageService){
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.participantModelRepository = participantModelRepository;
        this.imageService = imageService;
    }

    public String createRoom(CreateRoomRequest createRoomRequest) {
        UserModel currentUser = userRepository.findByName(AppContext.getUserName()).orElse(null);
        if (currentUser != null) {
            RoomModel room = new RoomModel();
            room.setName(createRoomRequest.getName());
            room.setDescription(createRoomRequest.getDesc());
            room.setRoom_pic(createRoomRequest.getPic());
            roomRepository.save(room);

            ParticipantModel participantModel = new ParticipantModel();
            participantModel.setRoom(room);
            participantModel.setUser(currentUser);
            participantModel.setIs_admin(true);
            participantModel.setIs_active(true);
            participantModel.setJoined_at(Timestamp.valueOf(LocalDateTime.now()));
            participantModelRepository.save(participantModel);

            for (Integer participant : createRoomRequest.getParticipants()) {
                UserModel user = userRepository.findById(participant).orElse(null);
                if (user != null) {
                    ParticipantModel participantModel1 = new ParticipantModel();
                    participantModel1.setRoom(room);
                    participantModel1.setUser(user);
                    participantModel1.setIs_admin(false);
                    participantModel1.setIs_active(true);
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
        UserModel currentUser = userRepository.findByName(AppContext.getUserName()).orElse(null);
        RoomModel room = roomRepository.findById(roomId).orElse(null);
        if(room!=null){
                if(participantModelRepository.isUserAdmin(roomId,currentUser.getId()).orElse(false)){
                    String response="";
                    for (Integer memberId : memberIds){
                        UserModel user = userRepository.findById(memberId).orElse(null);
                        if(user!=null){
                            ParticipantModel participant = new ParticipantModel();
                            participant.setRoom(room);
                            participant.setUser(user);
                            participant.setIs_admin(false);
                            participant.setIs_active(true);
                            participant.setJoined_at(Timestamp.valueOf(LocalDateTime.now()));
                            participantModelRepository.save(participant);
                            response = response + "\n" + "User "+memberId+" successfully added.";
                        }else{
                            response = response + "\n" + "User "+memberId+" is invalid User!";
                        }
                    }
                    return response;
                }
                else {
                    return "User is not an admin!";
                }
        }
        else{
            return "No such room!";
        }
    }

    public String removeMember(Integer roomId,List<Integer> memberIds){
        UserModel currentUser = userRepository.findByName(AppContext.getUserName()).orElse(null);
        RoomModel room = roomRepository.findById(roomId).orElse(null);
        if(room!=null){
            if(participantModelRepository.isUserAdmin(roomId,currentUser.getId()).orElse(false)){
                String response="";
                for (Integer memberId : memberIds){
                    if(participantModelRepository.existsRoomParticipant(roomId,memberId)!=0){
                        participantModelRepository.deactivateParticipant(roomId,memberId, Timestamp.valueOf(LocalDateTime.now()));
                        response = response+"\n"+memberId+" removed";
                    }else{
                        response = response+"\n"+memberId+" is not a participant";
                    }
                }
                return response;
            }else{
                return "You are not an Admin!";
            }
        }
        else{
            return "No such room!";
        }
    }

    public String joinRoom(Integer roomId){
        UserModel currentUser = userRepository.findByName(AppContext.getUserName()).orElse(null);
        RoomModel room = roomRepository.findById(roomId).orElse(null);
        if(room!=null){
            if(participantModelRepository.existsRoomParticipant(roomId,currentUser.getId())==0){
                ParticipantModel participant = new ParticipantModel();
                participant.setUser(currentUser);
                participant.setRoom(room);
                participant.setIs_admin(false);
                participant.setIs_active(true);
                participant.setJoined_at(Timestamp.valueOf(LocalDateTime.now()));
                participantModelRepository.save(participant);
                return "user joined.";
            }else{
                return "user is already a member!";
            }
        }
        else{
            return "No such room!";
        }
    }

    public String exitRoom(Integer roomId){
        UserModel currentUser = userRepository.findByName(AppContext.getUserName()).orElse(null);
        RoomModel room = roomRepository.findById(roomId).orElse(null);
        if(room!=null){
            if(participantModelRepository.isUserAdmin(roomId, currentUser.getId()).orElse(false)){
                if(participantModelRepository.getRoomAdminCount(roomId)>1){
                    participantModelRepository.deactivateParticipant(roomId, currentUser.getId(),Timestamp.valueOf(LocalDateTime.now()));
                    return "user exited from room.";
                }else{
                    return "Can't exit, you are the only admin!";
                }
            }else{
                participantModelRepository.deactivateParticipant(roomId, currentUser.getId(), Timestamp.valueOf(LocalDateTime.now()));
                return "user exited from room.";
            }
        }
        else{
            return "No such room!";
        }
    }

    public String makeRoomAdmin(Integer roomId,Integer otherUserId){
        UserModel currentUser = userRepository.findByName(AppContext.getUserName()).orElse(null);
        RoomModel room = roomRepository.findById(roomId).orElse(null);
        if(room!=null){
            if(participantModelRepository.isUserAdmin(roomId,currentUser.getId()).orElse(false)){
                if(!participantModelRepository.isUserAdmin(roomId,otherUserId).orElse(false)){
                    if(participantModelRepository.makeRoomAdmin(roomId,otherUserId)>0){
                        return "User " + otherUserId + " is now an Admin";
                    }else{
                        return "User " + otherUserId + " can't be an Admin!";
                    }
                }else{
                    return "User " + otherUserId + " is already an Admin!";
                }
            }
            else {
                return "You are not an admin!";
            }
        }
        else{
            return "No such room!";
        }
    }

    public String dismissRoomAdmin(Integer roomId,Integer otherUserId){
        UserModel currentUser = userRepository.findByName(AppContext.getUserName()).orElse(null);
        RoomModel room = roomRepository.findById(roomId).orElse(null);
        if(room!=null){
            if(participantModelRepository.isUserAdmin(roomId,currentUser.getId()).orElse(false)){
                if(participantModelRepository.isUserAdmin(roomId,otherUserId).orElse(false)){
                    if(participantModelRepository.dismissRoomAdmin(roomId,otherUserId)>0){
                        return "User " + otherUserId + " is successfully dismissed as Admin";
                    }else{
                        return "User " + otherUserId + " can't be dismissed as Admin!";
                    }
                }else{
                    return "User " + otherUserId + " is not an Admin!";
                }
            }
            else {
                return "You are not an admin!";
            }
        }
        else{
            return "No such room!";
        }
    }

    public List<Map<String, Object>> getRoomParticipants(Integer roomId){
        return participantModelRepository.getRoomParticipants(roomId);
    }

    public List<Map<String, Object>> getPastRoomParticipants(Integer roomId){
        return participantModelRepository.getPastRoomParticipants(roomId);
    }

    public String uploadPicture(Integer roomId, MultipartFile multipartFile) {
        UserModel currentUser = userRepository.findByName(AppContext.getUserName()).orElse(null);
        if(participantModelRepository.isUserAdmin(roomId,currentUser.getId()).orElse(false)){
            return imageService.uploadPicture(roomId,multipartFile);
        }else{
            return "You are not an Admin!";
        }
    }
}
