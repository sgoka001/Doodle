package com.example.srisri.doodle;

/**
 * Created by Preston Giorgianni on 1/30/2018.
 */

public class GlobalVars {
    private static GlobalVars mInstance= null;
    private static String userName = null;
    private static String userEmail = null;
    private static String userPw = null;
    private static String userId = null;

    protected GlobalVars(){}

    public static void setUser (String name, String email, String pw) {
        userEmail = email;
        userName = name;
        userPw = pw;
    }
    //Kristen- created another constructor for the google login, not sure if this will mess anything else up
    public  void setUser(String name, String email)
    {
        userEmail = email;
        userName = name;
    }

    public static void setUserId(String Id)
    {
        userId = Id;
    }

    public static String getUserName(){
        return userName;
    }

    public static String getUserId(){
        return userId;
    }

    public String getUserEmail(){
        return userEmail;
    }

    public boolean checkPw(String check){
        return userPw.equals(check);
    }

    public static synchronized GlobalVars getInstance(){
        if(null == mInstance){
            mInstance = new GlobalVars();
        }
        return mInstance;
    }
}
