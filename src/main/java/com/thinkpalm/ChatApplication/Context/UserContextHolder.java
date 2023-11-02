package com.thinkpalm.ChatApplication.Context;

import com.thinkpalm.ChatApplication.Model.UserModel;

public class UserContextHolder{
    private static final ThreadLocal<UserModel> userContextHolder= new ThreadLocal<UserModel>();
    public static void setContext(UserModel userModel){
        userContextHolder.set(userModel);
    }
    public static UserModel getContext(){
        return userContextHolder.get();
    }
    public static void clearContext(){
        userContextHolder.remove();
    }
}
