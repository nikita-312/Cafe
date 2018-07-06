package com.conceptioni.cafeapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.utils.TextviewRegular;

public class CafeInfoActivity extends AppCompatActivity {
    TextviewRegular tvrCafeName,tvrtableNumber,tvrContinue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_info);
        init();
        clicks();
    }

    private void clicks() {
        tvrContinue.setOnClickListener(v -> startActivity(new Intent(CafeInfoActivity.this,MenuActivity.class)));
    }

    private void init() {
        tvrCafeName = findViewById(R.id.tvrCafeName);
        tvrtableNumber = findViewById(R.id.tvrtableNumber);
        tvrContinue = findViewById(R.id.tvrContinue);
    }
}
