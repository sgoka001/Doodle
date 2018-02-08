package com.example.srisri.doodle;

/**
 * Created by Preston Giorgianni on 1/30/2018.
 */

public class GlobalVars {
    private static GlobalVars mInstance= null;
    private static String userName = null;
    private static String userEmail = null;
    private static String userPw = null;
    private static String userID = null;

    protected GlobalVars(){}

    public static void setUser (String name, String email, String pw, String id) {
        userEmail = email;
        userName = name;
        userPw = pw;
        userID = id;
    }
    //Kristen- created another constructor for the google login, not sure if this will mess anything else up
    public  void setUser(String name, String email)
    {
        userEmail = email;
        userName = name;
    }

    public static String getUserName(){
        return userName;
    }

    public static String getUserID(){return userID;}

    public static String getUserEmail(){
        return userEmail;
    }

    public static boolean checkPw(String check){
        return userPw.equals(check);
    }

    public static void logout(){
        userPw = null;
        userName = null;
        userEmail = null;
        userID = null;
    }

    public static synchronized GlobalVars getInstance(){
        if(null == mInstance){
            mInstance = new GlobalVars();
        }
        return mInstance;
    }
}
