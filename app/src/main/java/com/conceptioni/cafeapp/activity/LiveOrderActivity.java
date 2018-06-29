package com.conceptioni.cafeapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.adapter.CartItemAdapter;
import com.conceptioni.cafeapp.adapter.LiveOrderAdapter;

public class LiveOrderActivity extends AppCompatActivity {
    RecyclerView rvliveOrder;
    LinearLayout llBottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_order);
        init();
        click();
    }

    private void click() {
        llBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LiveOrderActivity.this,CardActivity.class));
            }
        });
    }

    private void init() {
        rvliveOrder=findViewById(R.id.rvliveOrder);
        llBottom=findViewById(R.id.llBottom);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LiveOrderActivity.this);
        rvliveOrder.setLayoutManager(linearLayoutManager);
        LiveOrderAdapter liveOrderAdapter = new LiveOrderAdapter();
        rvliveOrder.setAdapter(liveOrderAdapter);
    }
}
