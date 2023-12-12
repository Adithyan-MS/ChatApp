package com.thinkpalm.ChatApplication.Service;

import com.thinkpalm.ChatApplication.Model.RoomModel;
import com.thinkpalm.ChatApplication.Util.AppContext;
import com.thinkpalm.ChatApplication.Exception.UserNotFoundException;
import com.thinkpalm.ChatApplication.Model.UserModel;
import com.thinkpalm.ChatApplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public UserModel getUserDetails(String username){
        UserModel user = userRepository.findByName(username).orElse(null);
        if (user!=null){
            user.setPassword(null);
            return user;
        }else{
            throw new UserNotFoundException("User Not Found!");
        }
    }

    public String updateUserBio(Map<String, String> request) {
        String currentUser = AppContext.getUserName();
        if(!currentUser.isEmpty()){
            userRepository.updateUserBio(currentUser,request.get("bio"));
            return request.get("bio");
        }else{
            throw new UserNotFoundException("User Not Found!");
        }
    }

    public List<UserModel> getAllUsers(){
        List<UserModel> users = userRepository.findAll();
        for(UserModel user: users){
            user.setPassword(null);
        }
        return users;
    }

    public List<Map<String,Object>> getAllChatsOfUsers() {
        UserModel currentUser = userRepository.findByName(AppContext.getUserName()).orElse(null);
        if (currentUser!=null){
            List<Map<String, Object>> users = userRepository.findAllChatsOfUser(currentUser.getId());
            return users;
        }else{
            throw new UserNotFoundException("User Not Found!");
        }
    }

    public List<Map<String, Object>> searchChats(String searchName) {
        UserModel currentUser = userRepository.findByName(AppContext.getUserName()).orElse(null);
        if (currentUser!=null){
            List<Map<String, Object>> result  =  userRepository.searchChats(currentUser.getId(), searchName);
            return result;
        }else{
            throw new UserNotFoundException("User Not Found!");
        }
    }

    public List<Map<String, Object>> searchUsers(String searchName) {
        UserModel currentUser = userRepository.findByName(AppContext.getUserName()).orElse(null);
        if (currentUser!=null){
            List<Map<String, Object>> result  =  userRepository.searchUsers(searchName,currentUser.getId());
            return result;
        }else{
            throw new UserNotFoundException("User Not Found!");
        }
    }

    public List<Map<String, Object>> getCommonRooms(String otherUserName) {
        UserModel currentUser = userRepository.findByName(AppContext.getUserName()).orElse(null);
        UserModel otherUser = userRepository.findByName(otherUserName).orElse(null);
        if(otherUser!=null){
            return userRepository.getCommonRooms(currentUser.getId(),otherUser.getId());
        }else {
            throw new UserNotFoundException("Can't find this user!");
        }
    }


}
