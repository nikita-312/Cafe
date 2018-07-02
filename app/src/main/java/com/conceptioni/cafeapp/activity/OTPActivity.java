package com.conceptioni.cafeapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.utils.TextviewRegular;

@SuppressLint("Registered")
public class OTPActivity extends AppCompatActivity {
    TextviewRegular tvrVerify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        init();
        clicks();
    }

    private void clicks() {
        tvrVerify.setOnClickListener(v -> startActivity(new Intent(OTPActivity.this,MenuActivity.class)));
    }

    private void init() {
        tvrVerify = findViewById(R.id.tvrVerify);
    }
}
