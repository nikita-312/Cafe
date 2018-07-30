package com.conceptioni.cafeapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.utils.TextviewRegular;

public class ThankYouActivity extends AppCompatActivity {
    TextviewRegular tvrContinueshop;
    ImageView ivBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);
        init();
    }

    private void init() {
        ivBack = findViewById(R.id.ivBack);
        tvrContinueshop = findViewById(R.id.tvrContinueshop);

        ivBack.setOnClickListener(view -> finish());
        tvrContinueshop.setOnClickListener(view -> {
            startActivity(new Intent(ThankYouActivity.this, LiveOrderActivity.class));
            finish();
        });
    }
}
