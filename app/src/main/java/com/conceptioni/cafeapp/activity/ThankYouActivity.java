package com.conceptioni.cafeapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              finish();
            }
        });
        tvrContinueshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ThankYouActivity.this, LiveOrderActivity.class));
                finish();
            }
        });
    }
}
