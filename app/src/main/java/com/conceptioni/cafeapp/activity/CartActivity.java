package com.conceptioni.cafeapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.adapter.CartItemAdapter;
import com.conceptioni.cafeapp.utils.TextviewRegular;

public class CartActivity extends AppCompatActivity {
    RecyclerView rvCart;
    TextviewRegular tvrPlaceOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        init();
        click();
    }

    private void click() {
        tvrPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this,LiveOrderActivity.class));
            }
        });
    }

    private void init() {
        rvCart=findViewById(R.id.rvCart);
        tvrPlaceOrder=findViewById(R.id.tvrPlaceOrder);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CartActivity.this);
        rvCart.setLayoutManager(linearLayoutManager);
        CartItemAdapter cartItemAdapter = new CartItemAdapter();
        rvCart.setAdapter(cartItemAdapter);
    }
}
