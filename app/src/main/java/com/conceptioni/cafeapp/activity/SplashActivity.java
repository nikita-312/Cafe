package com.conceptioni.cafeapp.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.database.DBOpenHelper;
import com.conceptioni.cafeapp.utils.Constant;
import com.conceptioni.cafeapp.utils.SharedPrefs;

public class SplashActivity extends AppCompatActivity {

    DBOpenHelper dbOpenHelper;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        dbOpenHelper = new DBOpenHelper(SplashActivity.this);
        sqLiteDatabase = dbOpenHelper.getWritableDatabase();
        initsplash();
    }

    private void initsplash() {

        new Handler().postDelayed(() -> {

//            && !SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Name,
//                    Constant.notAvailable).equalsIgnoreCase(Constant.notAvailable)
            if (!SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id,
                    Constant.notAvailable).equalsIgnoreCase(Constant.notAvailable)) {
                if (!SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id,Constant.notAvailable).equalsIgnoreCase(Constant.notAvailable)
                        && !SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Table_status,Constant.notAvailable).equalsIgnoreCase(Constant.notAvailable) ){
                    startActivity(new Intent(SplashActivity.this, MenuActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                }
            } else {
//                if (isTableExists("ADDTOCART",true)){
//                    boolean isexist = isTableExists("ADDTOCART",true);
//                    Log.d("+++++is","+++++"+isexist);
//                }
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }

        }, 2000);

    }

//    public boolean isTableExists(String tableName, boolean openDb) {
//        if(openDb) {
//            if(sqLiteDatabase == null || !sqLiteDatabase.isOpen()) {
//                sqLiteDatabase = dbOpenHelper.getReadableDatabase();
//            }
//
//            if(!sqLiteDatabase.isReadOnly()) {
//                sqLiteDatabase.close();
//                sqLiteDatabase = dbOpenHelper.getReadableDatabase();
//            }
//        }
//
//        Cursor cursor = sqLiteDatabase.rawQuery("select from ADDTOCART where ADDTOCART = '"+tableName+"'", null);
//        if(cursor!=null) {
//            if(cursor.getCount()>0) {
//                cursor.close();
//                return true;
//            }
//            cursor.close();
//        }
//        return false;
//    }
}
