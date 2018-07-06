package com.conceptioni.cafeapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.utils.TextviewRegular;

public class CardActivity extends AppCompatActivity {
    LinearLayout llNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        init();
        clicks();
    }

    private void clicks() {
        llNext.setOnClickListener(v -> {
            startActivity(new Intent(CardActivity.this,VisitAgainActivity.class));
            finish();
        });
    }

    private void init() {
        llNext =findViewById(R.id.llNext);
    }
}
