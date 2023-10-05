package com.thinkpalm.ChatApplication.Service;

import com.thinkpalm.ChatApplication.Model.UserData;
import com.thinkpalm.ChatApplication.Model.UserModel;
import com.thinkpalm.ChatApplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Map<String,Object> getUserDetails(String username){
        UserModel user = userRepository.findByName(username).orElse(null);
        Map<String,Object> res = new HashMap<>();
        if(user!=null){
            res.put("id",user.getId());
            res.put("name",user.getName());
            res.put("email",user.getEmail());
            res.put("phone_number",user.getPhone_number());
            res.put("bio",user.getBio());
            res.put("profilePic",user.getProfilPic());
            return res;
        }else{
            res.put("error","No such User Found!");
            return res;
        }
    }

    public String updateUserBio(Map<String, String> request) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        userRepository.updateUserBio(currentUser,request.get("bio"));
        return "Bio updated";
    }

    public List<UserData> getAllUsers() {
        List<UserModel> users = userRepository.findAll();
        List<UserData> res = new ArrayList<>();
        for(UserModel user: users){
            UserData userData = new UserData();
            userData.setId(user.getId());
            userData.setName(user.getName());
            userData.setEmail(user.getEmail());
            userData.setPhone_number(user.getPhone_number());
            userData.setBio(user.getBio());
            userData.setProfilePic(user.getProfilPic());
            res.add(userData);
        }
        return res;
    }
}
