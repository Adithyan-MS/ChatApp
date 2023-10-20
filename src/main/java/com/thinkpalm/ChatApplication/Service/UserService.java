package com.thinkpalm.ChatApplication.Service;

import com.thinkpalm.ChatApplication.Model.UserModel;
import com.thinkpalm.ChatApplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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
        user.setPassword(null);
        return user;
    }

    public String updateUserBio(Map<String, String> request) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        userRepository.updateUserBio(currentUser,request.get("bio"), Timestamp.valueOf(LocalDateTime.now()));
        return "Bio updated";
    }

    public List<UserModel> getAllUsers(){
        List<UserModel> users = userRepository.findAll();
        for(UserModel user: users){
            user.setPassword(null);
        }
        return users;
    }

    public List<Map<String,Object>> getAllChatsOfUsers() {
        UserModel currentUser = userRepository.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);
        List<Map<String, Object>> users = userRepository.findAllChatsOfUser(currentUser.getId());
        return users;
    }
}
