package com.conceptioni.cafeapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;

import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.utils.Constant;
import com.conceptioni.cafeapp.utils.SharedPrefs;

public class SplashActivity extends AppCompatActivity {

    LinearLayout loginll,signupll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initsplash();
        clicksplash();
    }

    private void clicksplash() {
        loginll.setOnClickListener(v -> {
            startActivity(new Intent(SplashActivity.this,LoginActivity.class));
            finish();
        });
        signupll.setOnClickListener(v -> {
            startActivity(new Intent(SplashActivity.this,SignUpActivity.class));
            finish();
        });


    }

    private void initsplash() {
        loginll = findViewById(R.id.loginll);
        signupll = findViewById(R.id.signupll);

        Log.d("+++++id","++++"+SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id,Constant.notAvailable));

        if (!SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable).equalsIgnoreCase(Constant.notAvailable)){
            startActivity(new Intent(SplashActivity.this,MenuActivity.class));
            finish();
        }
    }
}
