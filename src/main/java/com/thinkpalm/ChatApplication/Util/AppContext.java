package com.thinkpalm.ChatApplication.Util;

import java.util.HashMap;
import java.util.Map;

public class AppContext {
    private static String USERNAME = "USERNAME";
    private static ThreadLocal<Map<String, String>> appContext = ThreadLocal.withInitial(HashMap::new);
    public static String getUserName() {
        Map<String, String> contextMap = appContext.get();
        return contextMap.get(USERNAME);
    }
    public static void setUserName(String username) {
        Map<String, String> contextMap = appContext.get();
        contextMap.put(USERNAME, username);
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