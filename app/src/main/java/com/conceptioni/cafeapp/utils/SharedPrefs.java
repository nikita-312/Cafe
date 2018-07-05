package com.conceptioni.cafeapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.conceptioni.cafeapp.CafeApp;


/**
 * Created by abg on 12/22/2015.
 */
public class SharedPrefs {

    SharedPreferences sharedPreferences;
    private Context mContext;
    private static String sharedPrefenceName = "CafeApp";

    public static SharedPreferences getSharedPref() {
        return CafeApp.getContext().getSharedPreferences(sharedPrefenceName, Context.MODE_PRIVATE);
    }

    public interface userSharedPrefData {
       String Phone_No = "Phone_No";
       String User_id = "User_id";
       String Auth_token = "Auth_token";

       String ItemData = "ItemData";
       String Imageata = "Imageata";
    }

}
