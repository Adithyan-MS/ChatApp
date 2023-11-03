package com.thinkpalm.ChatApplication.Util;

import java.util.Map;

public class AppContext {
    private static String USERNAME = "USERNAME";
    private static final ThreadLocal<Map<String,String>> appContext = new ThreadLocal<Map<String,String>>();
    public static String getUserName() {
        return appContext.get().get(USERNAME);
    }
    public static void setUserName(String username) {
        AppContext.getContext().put(USERNAME,username);
    }
    public static void setContext(Map<String,String> context){
        appContext.set(context);
    }
    public static Map<String,String> getContext(){
        return appContext.get();
    }
    public static void clearContext(){
        appContext.remove();
    }
}
