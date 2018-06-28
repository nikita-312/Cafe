package com.conceptioni.cafeapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.conceptioni.cafeapp.R;

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
    }
}
