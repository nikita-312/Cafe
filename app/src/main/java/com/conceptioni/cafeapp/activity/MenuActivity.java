package com.conceptioni.cafeapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.adapter.MenuAdapter;
import com.conceptioni.cafeapp.adapter.MenuItemAdapter;

public class MenuActivity extends AppCompatActivity {

    RecyclerView categoryrv,categoryitemrv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initmenu();
    }

    private void initmenu() {
        categoryrv = findViewById(R.id.categoryrv);
        categoryitemrv = findViewById(R.id.categoryitemrv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MenuActivity.this);
        categoryrv.setLayoutManager(linearLayoutManager);

        MenuAdapter menuAdapter = new MenuAdapter();
        categoryrv.setAdapter(menuAdapter);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(MenuActivity.this);
        categoryitemrv.setLayoutManager(linearLayoutManager1);

        MenuItemAdapter menuItemAdapter = new MenuItemAdapter();
        categoryitemrv.setAdapter(menuItemAdapter);
    }
}
