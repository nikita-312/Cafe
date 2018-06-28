package com.conceptioni.cafeapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.conceptioni.cafeapp.R;

public class LoginActivity extends AppCompatActivity {

    LinearLayout sendotpll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initlogin();
        clicklogin();
        //test1
    }

    private void clicklogin() {
        sendotpll.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this,MenuActivity.class));
            finish();
        });
    }

    private void initlogin() {
        sendotpll = findViewById(R.id.sendotpll);
    }
}
