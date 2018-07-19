package com.conceptioni.cafeapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.conceptioni.cafeapp.R;


public class HomeActivity extends AppCompatActivity {

    LinearLayout scanqrll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        allclick();

    }

    private void allclick() {
        scanqrll.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, QrCodeScanActivity.class));
            finish();
        });
    }

    private void init() {
        scanqrll = findViewById(R.id.scanqrll);
    }


}
