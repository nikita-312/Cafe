package com.conceptioni.cafeapp.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.utils.TextviewRegular;

public class ThankYouActivity extends AppCompatActivity {
    TextviewRegular tvrContinueshop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);



        init();
        clicks();
    }

    private void clicks() {
        tvrContinueshop.setOnClickListener(v -> startActivity(new Intent(ThankYouActivity.this,ReviewActivity.class)));
    }

    private void init() {
        tvrContinueshop = findViewById(R.id.tvrContinueshop);
        new Handler().postDelayed(() -> {
            startActivity(new Intent(ThankYouActivity.this, LiveOrderActivity.class));
            finish();
        }, 2000);
    }
}
