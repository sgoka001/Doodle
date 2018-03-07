package com.example.srisri.doodle;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * Created by Preston Giorgianni on 1/30/2018.
 */

public class GlobalVars extends Application {
    private static GlobalVars mInstance= null;
    private static String userName = null;
    private static String userEmail = null;
    private static String userPw = null;
    private static String userID = null;
    private  static String inviteID = null;

    private static String eventID = null;

    protected GlobalVars(){}

    public static void setUser (String name, String email, String pw, String id) {
        userEmail = email;
        userName = name;
        userPw = pw;
        userID = id;
    }
    //Kristen- created another constructor for the google login, not sure if this will mess anything else up
    public  void setUser(String name, String email,String id)
    {
        userEmail = email;
        userName = name;
        userID = id;
    }

    public void setEvent(String id)
    {
        eventID = id;
    }

    public  void setInvite(String id)
    {
        inviteID = id;
    }

    public void setUserId (String id)
    {
        userID = id;
    }

    public void setInviteID(String id) {inviteID = id;}

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

    public static String getEventID() {
        return eventID;
    }

    public static void setEventID(String eventID) {
        GlobalVars.eventID = eventID;
    }

    public static  String getInvite(){return inviteID;}

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
