package com.example.srisri.doodle;

/**
 * Created by Preston Giorgianni on 1/30/2018.
 */

public class GlobalVars {
    private static GlobalVars mInstance= null;
    private static String userName = null;
    private static String userEmail = null;
    private static String userPw = null;

    protected GlobalVars(){}

    public void setUser (String name, String email, String pw) {
        userEmail = email;
        userName = name;
        userPw = pw;
    }

    public String getUserName(){
        return userName;
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
