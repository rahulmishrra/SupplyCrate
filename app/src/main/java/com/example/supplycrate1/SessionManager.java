package com.example.supplycrate1;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    //Global variables
    SharedPreferences usersSession;
    SharedPreferences.Editor editor;
    Context context;

    //Session names
    public static final String SESSION_CUSTOMER = "custLoginSession";
    public static final String SESSION_MERCHANT = "mrchLoginSession";

    //Customer session variables
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_NAME = "customer_name";
    public static final String KEY_SELECTSTORENAME = "STORE_NAME";
    public static final String KEY_LOCATION = "location";

    //Merchant session variables

    private static final String IS_MERCHANTLOGIN = "IsMerchantLoggedIn";
    public static final String KEY_MERCHANTEMAIL = "memail";
    public static final String KEY_MERCHANTPASSWORD = "mpassword";
    public static final String KEY_MERCHANTBNAME = "bname";
    public static final String KEY_MERCHANTNAME = "mownername";
    public static final String KEY_MERCHANTPHONE = "mphone";
    public static final String KEY_MERCHANTLOCATION = "mlocation";

    public SessionManager(Context _context,String sessionName){
        context = _context;
        usersSession = context.getSharedPreferences(sessionName,Context.MODE_PRIVATE);
        editor  = usersSession.edit();
    }

    /*
    Customer Session
    * */
    public void createLoginSession(String email, String password, String name){
        editor.putBoolean(IS_LOGIN,true);

        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_PASSWORD,password);
        editor.putString(KEY_NAME,name);

        editor.commit();
    }
/**/
    public void custSelectStore(String storeName){
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(KEY_SELECTSTORENAME,storeName);
        editor.commit();
    }

    public void setLocation(String location){
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(KEY_LOCATION,location);
        editor.commit();
    }

    public HashMap<String,String> getUserDetailFromSession(){
        HashMap<String,String> userData = new HashMap<String, String>();

        userData.put(KEY_EMAIL,usersSession.getString(KEY_EMAIL,null));
        userData.put(KEY_PASSWORD,usersSession.getString(KEY_PASSWORD,null));
        userData.put(KEY_NAME,usersSession.getString(KEY_NAME,null));
        userData.put(KEY_SELECTSTORENAME,usersSession.getString(KEY_SELECTSTORENAME,null));
        userData.put(KEY_LOCATION,usersSession.getString(KEY_LOCATION,null));

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

    /*
    Merchant Session
    * */

    public void createmrchLoginSession(String _memail, String _mpassword, String _bname, String _mownername, String _mphone){
        editor.putBoolean(IS_MERCHANTLOGIN,true);

        editor.putString(KEY_MERCHANTEMAIL,_memail);
        editor.putString(KEY_MERCHANTPASSWORD,_mpassword);
        editor.putString(KEY_MERCHANTBNAME,_bname);
        editor.putString(KEY_MERCHANTNAME,_mownername);
        editor.putString(KEY_MERCHANTPHONE,_mphone);

        editor.commit();
    }

    public void setMerchantLocation(String Loc){
        editor.putBoolean(IS_MERCHANTLOGIN,true);

        editor.putString(KEY_MERCHANTLOCATION,Loc);
        editor.commit();
    }

    public HashMap<String,String> getMerchantDetailFromSession(){
        HashMap<String,String> merchData = new HashMap<String, String>();

        merchData.put(KEY_MERCHANTEMAIL,usersSession.getString(KEY_MERCHANTEMAIL,null));
        merchData.put(KEY_MERCHANTPASSWORD,usersSession.getString(KEY_MERCHANTPASSWORD,null));
        merchData.put(KEY_MERCHANTBNAME,usersSession.getString(KEY_MERCHANTBNAME,null));
        merchData.put(KEY_MERCHANTNAME,usersSession.getString(KEY_MERCHANTNAME,null));
        merchData.put(KEY_MERCHANTPHONE,usersSession.getString(KEY_MERCHANTPHONE,null));
        merchData.put(KEY_MERCHANTLOCATION,usersSession.getString(KEY_MERCHANTLOCATION,null));

        return merchData;
    }

    public boolean checkMerchantLogin(){
        if(usersSession.getBoolean(IS_MERCHANTLOGIN,false)){
            return true;
        }
        else
            return false;
    }

    public void logoutMerchantFromSession(){
        editor.clear();
        editor.commit();
    }
}

