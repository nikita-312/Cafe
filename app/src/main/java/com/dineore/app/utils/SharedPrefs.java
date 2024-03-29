package com.dineore.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.dineore.app.CafeApp;

public class SharedPrefs {

    public static SharedPreferences getSharedPref() {
        String sharedPrefenceName = "CafeApp";
        return CafeApp.getContext().getSharedPreferences(sharedPrefenceName, Context.MODE_PRIVATE);
    }

    public interface userSharedPrefData {
       String Phone_No = "Phone_No";
       String User_id = "User_id";
       String Auth_token = "Auth_token";

       String ItemData = "ItemData";
       String orderid = "orderid";
       String Cafe_Id = "Cafe_Id";
       String table_number = "table_number";
       String Name = "Name";
       String Table_status = "Table_status";
       String Flag = "Flag";
       String canScan = "canScan";
       String gst = "gst";
    }

    public interface tokendetail {
        String refreshtoken = "refreshtoken";
    }

}
