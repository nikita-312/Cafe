package com.conceptioni.cafeapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.adapter.MenuAdapter;
import com.conceptioni.cafeapp.adapter.MenuItemAdapter;
import com.tabassum.shimmerRecyclerView.ShimmerRecyclerView;

public class MenuActivity extends AppCompatActivity {

    ImageView ivCart;
    ShimmerRecyclerView rvCategory,rvCategoryitem;
    MenuAdapter menuAdapter;
    MenuItemAdapter menuItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initmenu();
        clicks();
    }

    private void clicks() {
        ivCart.setOnClickListener(v -> {
              startActivity(new Intent(MenuActivity.this,CartActivity.class));
        });
    }

    private void initmenu() {
        ivCart = findViewById(R.id.ivCart);
        rvCategory = findViewById(R.id.rvCategory);
        rvCategoryitem = findViewById(R.id.rvCategoryitem);

        menuAdapter = new MenuAdapter();
        menuItemAdapter = new MenuItemAdapter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MenuActivity.this);
        rvCategory.setLayoutManager(linearLayoutManager);
        rvCategory.setAdapter(menuAdapter);
        rvCategory.showShimmerAdapter();

        rvCategory.postDelayed(new Runnable() {
            @Override
            public void run() {
                rvCategory.hideShimmerAdapter();
            }
        }, 5000);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(MenuActivity.this);
        rvCategoryitem.setLayoutManager(linearLayoutManager1);
        rvCategoryitem.setAdapter(menuItemAdapter);
        rvCategoryitem.showShimmerAdapter();

        rvCategoryitem.postDelayed(new Runnable() {
            @Override
            public void run() {
                rvCategoryitem.hideShimmerAdapter();
            }
        }, 5000);
    }


}
