package com.thinkpalm.ChatApplication.Service;

import com.thinkpalm.ChatApplication.Exception.DuplicateEntryException;
import com.thinkpalm.ChatApplication.Exception.InvalidDataException;
import com.thinkpalm.ChatApplication.Exception.RoomNotFoundException;
import com.thinkpalm.ChatApplication.Exception.UserNotFoundException;
import com.thinkpalm.ChatApplication.Util.AppContext;
import com.thinkpalm.ChatApplication.Model.*;
import com.thinkpalm.ChatApplication.Repository.ParticipantModelRepository;
import com.thinkpalm.ChatApplication.Repository.RoomRepository;
import com.thinkpalm.ChatApplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
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

    public RoomModel createRoom(CreateRoomRequest createRoomRequest) {
        UserModel currentUser = userRepository.findByName(AppContext.getUserName()).orElse(null);
        if (currentUser != null) {
            if(!roomRepository.existByRoomName(createRoomRequest.getName()).isEmpty()){
                throw new DuplicateEntryException("Room name already exist!");
            }else{
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
                return room;
            }
        }else{
            throw new UserNotFoundException("User not Found!");
        }
    }

    public String addMember(Integer roomId,List<Integer> memberIds) throws IllegalAccessException {
        UserModel currentUser = userRepository.findByName(AppContext.getUserName()).orElse(null);
        RoomModel room = roomRepository.findById(roomId).orElse(null);
        if(room!=null){
                if(participantModelRepository.isUserAdmin(roomId,currentUser.getId()).orElse(false)){
                    String response="";
                    for (Integer memberId : memberIds){
                        UserModel user = userRepository.findById(memberId).orElse(null);
                        if(user!=null){
                            ParticipantModel participant = participantModelRepository.findByRoomAndUser(roomId,memberId).orElse(null);
                            if(participant!=null){
                                if(participant.getIs_active()){
                                    throw new DuplicateEntryException("You are already a member!");
                                }else{
                                    participant.setIs_active(true);
                                    participantModelRepository.save(participant);
                                    return "user joined.";
                                }
                            }else{
                                ParticipantModel newParticipant = new ParticipantModel();
                                newParticipant.setUser(user);
                                newParticipant.setRoom(room);
                                newParticipant.setIs_admin(false);
                                newParticipant.setIs_active(true);
                                newParticipant.setJoined_at(Timestamp.valueOf(LocalDateTime.now()));
                                participantModelRepository.save(newParticipant);
                                response = response + "\n" + "User "+memberId+" successfully added.";
                            }
                        }else{
                            response = response + "\n" + "User "+memberId+" is invalid User!";
                        }
                    }
                    return response;
                }
                else {
                    throw new IllegalAccessException("You are not an Admin!");
                }
        }
        else{
            throw new RoomNotFoundException("Room Not Found!");
        }
    }

    public String removeMember(Integer roomId,List<Integer> memberIds) throws IllegalAccessException {
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
                throw new IllegalAccessException("You are not an Admin!");
            }
        }
        else{
            throw new RoomNotFoundException("Room Not Found!");
        }
    }

    public String joinRoom(Integer roomId){
        UserModel currentUser = userRepository.findByName(AppContext.getUserName()).orElse(null);
        RoomModel room = roomRepository.findById(roomId).orElse(null);
        if(room!=null){
            ParticipantModel participant = participantModelRepository.findByRoomAndUser(roomId,currentUser.getId()).orElse(null);
            if(participant!=null){
                if(participant.getIs_active()){
                    throw new DuplicateEntryException("You are already a member!");
                }else{
                    participant.setIs_active(true);
                    participantModelRepository.save(participant);
                    return "user joined.";
                }
            }else{
                ParticipantModel newParticipant = new ParticipantModel();
                newParticipant.setUser(currentUser);
                newParticipant.setRoom(room);
                newParticipant.setIs_admin(false);
                newParticipant.setIs_active(true);
                newParticipant.setJoined_at(Timestamp.valueOf(LocalDateTime.now()));
                participantModelRepository.save(newParticipant);
                return "user joined.";
            }
        }
        else{
            throw new RoomNotFoundException("Room not found!");
        }
    }

    public String exitRoom(Integer roomId) throws IllegalAccessException {
        UserModel currentUser = userRepository.findByName(AppContext.getUserName()).orElse(null);
        RoomModel room = roomRepository.findById(roomId).orElse(null);
        if(room!=null){
            if(participantModelRepository.isUserAdmin(roomId, currentUser.getId()).orElse(false)){
                if(participantModelRepository.getRoomAdminCount(roomId)>1){
                    participantModelRepository.deactivateParticipant(roomId, currentUser.getId(),Timestamp.valueOf(LocalDateTime.now()));
                    return "user exited from room.";
                }else{
                    throw new IllegalAccessException("Can't exit, you are the only admin!");
                }
            }else{
                participantModelRepository.deactivateParticipant(roomId, currentUser.getId(), Timestamp.valueOf(LocalDateTime.now()));
                return "user exited from room.";
            }
        }
        else{
            throw new RoomNotFoundException("Room Not Found");
        }
    }

    public String makeRoomAdmin(Integer roomId,Integer otherUserId) throws IllegalAccessException {
        UserModel currentUser = userRepository.findByName(AppContext.getUserName()).orElse(null);
        RoomModel room = roomRepository.findById(roomId).orElse(null);
        if(room!=null){
            if(participantModelRepository.isUserAdmin(roomId,currentUser.getId()).orElse(false)){
                if(!participantModelRepository.isUserAdmin(roomId,otherUserId).orElse(false)){
                    if(participantModelRepository.makeRoomAdmin(roomId,otherUserId)>0){
                        return "User " + otherUserId + " is now an Admin";
                    }else{
                        throw new InvalidDataException("User " + otherUserId + " can't be an Admin");
                    }
                }else{
                    throw new InvalidDataException("User " + otherUserId + " is already an Admin!");
                }
            }
            else {
                throw new IllegalAccessException("You are not an Admin!");
            }
        }
        else{
            throw new RoomNotFoundException("Room Not Found");
        }
    }

    public String dismissRoomAdmin(Integer roomId,Integer otherUserId) throws IllegalAccessException {
        UserModel currentUser = userRepository.findByName(AppContext.getUserName()).orElse(null);
        RoomModel room = roomRepository.findById(roomId).orElse(null);
        if(room!=null){
            if(participantModelRepository.isUserAdmin(roomId,currentUser.getId()).orElse(false)){
                if(participantModelRepository.isUserAdmin(roomId,otherUserId).orElse(false)){
                    if(participantModelRepository.dismissRoomAdmin(roomId,otherUserId)>0){
                        return "User " + otherUserId + " is successfully dismissed as Admin";
                    }else{
                        throw new InvalidDataException("User " + otherUserId + " can't be dismissed as Admin!");
                    }
                }else{
                    throw new InvalidDataException("User " + otherUserId + " is not an Admin!");
                }
            }
            else {
                throw new IllegalAccessException("You are not an Admin!");
            }
        }
        else{
            throw new RoomNotFoundException("Room Not Found");
        }
    }

    public List<Map<String, Object>> getRoomParticipants(Integer roomId){
        return participantModelRepository.getRoomParticipants(roomId);
    }

    public List<Map<String, Object>> getPastRoomParticipants(Integer roomId){
        return participantModelRepository.getPastRoomParticipants(roomId);
    }

    public RoomModel getRoomDetails(String roomName) {
        RoomModel room = roomRepository.findByName(roomName);
        if(room!=null){
            return room;
        }else{
            throw new RoomNotFoundException("Room Not Found!");
        }
    }

    public Boolean IsUserPartcicpant(Integer roomId, Integer userId) {
        RoomModel room = roomRepository.findById(roomId).orElse(null);
        if(room!=null){
            UserModel user = userRepository.findById(userId).orElse(null);
            if(user!=null){
                if(participantModelRepository.existsRoomParticipant(roomId,userId)!=0){
                    return true;
                }else{
                    return false;
                }
            }else{
                throw new UserNotFoundException("User not found!");
            }
        }else{
            throw new RoomNotFoundException("Room not found!");
        }
    }
}
