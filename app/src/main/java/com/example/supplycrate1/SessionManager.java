package com.example.supplycrate1;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences usersSession;
    SharedPreferences.Editor editor;
    Context context;

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    public SessionManager(Context _context){
        context = _context;
        usersSession = context.getSharedPreferences("userLoginSession",Context.MODE_PRIVATE);
        editor  = usersSession.edit();
    }

    public void createLoginSession(String email, String password){
        editor.putBoolean(IS_LOGIN,true);

        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_PASSWORD,password);

        editor.commit();
    }

    public HashMap<String,String> getUserDetailFromSession(){
        HashMap<String,String> userData = new HashMap<String, String>();

        userData.put(KEY_EMAIL,usersSession.getString(KEY_EMAIL,null));
        userData.put(KEY_PASSWORD,usersSession.getString(KEY_PASSWORD,null));

        return userData;
    }

    public boolean checkLogin(){
        if(usersSession.getBoolean(IS_LOGIN,false)){
            return true;
        }
        else
            return false;
    }

    public void logoutUserFromSession(){
        editor.clear();
        editor.commit();
    }

}




