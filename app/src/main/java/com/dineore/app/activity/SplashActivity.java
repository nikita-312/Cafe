package com.dineore.app.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.dineore.app.R;
import com.dineore.app.database.DBOpenHelper;
import com.dineore.app.utils.Constant;
import com.dineore.app.utils.SharedPrefs;

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
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }

        }, 2000);

    }
}
