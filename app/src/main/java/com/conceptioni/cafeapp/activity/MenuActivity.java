package com.conceptioni.cafeapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.activity.retrofitinterface.Service;
import com.conceptioni.cafeapp.adapter.MenuAdapter;
import com.conceptioni.cafeapp.adapter.MenuItemAdapter;
import com.conceptioni.cafeapp.model.Category;
import com.conceptioni.cafeapp.model.Items;
import com.conceptioni.cafeapp.model.Menu;
import com.conceptioni.cafeapp.model.MenuModel;
import com.conceptioni.cafeapp.utils.MakeToast;
import com.google.gson.JsonObject;
import com.tabassum.shimmerRecyclerView.ShimmerRecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity {

    ImageView ivCart;
    ShimmerRecyclerView rvCategory,rvCategoryitem;
    MenuAdapter menuAdapter;
    MenuItemAdapter menuItemAdapter;
    List<Category> menuModels;

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

        menuAdapter = new MenuAdapter(menuModels);
        menuItemAdapter = new MenuItemAdapter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MenuActivity.this);
        rvCategory.setLayoutManager(linearLayoutManager);


        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(MenuActivity.this);
        rvCategoryitem.setLayoutManager(linearLayoutManager1);
        rvCategoryitem.setAdapter(menuItemAdapter);
        rvCategoryitem.showShimmerAdapter();

        rvCategoryitem.postDelayed(() -> rvCategoryitem.hideShimmerAdapter(), 5000);

        GetMenu();

        rvCategory.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }
    public void GetMenu(){
        JsonObject object = new JsonObject();
        object.addProperty("cafeid","1");

        Service service = ApiCall.getRetrofit().create(Service.class);
        Call<Menu> call = service.getMenuItem("application/json",object);

        call.enqueue(new Callback<Menu>() {
            @Override
            public void onResponse(Call<Menu> call, Response<Menu> response) {
                    if (response.isSuccessful()){
                        menuModels.clear();
                        menuModels = response.body().getCategory();
                        rvCategory.setAdapter(menuAdapter);
                        rvCategory.showShimmerAdapter();

                        rvCategory.postDelayed(() -> rvCategory.hideShimmerAdapter(), 5000);
                        menuModels.get(1).getItems();
                    }else
                        new MakeToast("Error while getting data");
            }
            @Override
            public void onFailure(Call<Menu> call, Throwable t) {
                new MakeToast("Error while getting result");
            }
        });
    }

}
