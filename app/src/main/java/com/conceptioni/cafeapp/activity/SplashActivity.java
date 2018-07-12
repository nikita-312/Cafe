package com.conceptioni.cafeapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.utils.Constant;
import com.conceptioni.cafeapp.utils.SharedPrefs;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initsplash();
    }

    private void initsplash() {
        new Handler().postDelayed(() -> {
            if (!SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable).equalsIgnoreCase(Constant.notAvailable) && !SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Name, Constant.notAvailable).equalsIgnoreCase(Constant.notAvailable) ) {
                if (!SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id,Constant.notAvailable).equalsIgnoreCase(Constant.notAvailable) && !SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Table_status,Constant.notAvailable).equalsIgnoreCase(Constant.notAvailable) ){
                    startActivity(new Intent(SplashActivity.this, MenuActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                }

            } else {
//                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }

        }, 2000);

    }
}
