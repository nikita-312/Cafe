package com.conceptioni.cafeapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.activity.retrofitinterface.Service;
import com.conceptioni.cafeapp.adapter.MenuAdapter;
import com.conceptioni.cafeapp.adapter.MenuItemAdapter;
import com.conceptioni.cafeapp.model.Category;
import com.conceptioni.cafeapp.model.Items;
import com.conceptioni.cafeapp.model.Menu;
import com.conceptioni.cafeapp.utils.Constant;
import com.conceptioni.cafeapp.utils.MakeToast;
import com.conceptioni.cafeapp.utils.RecyclerTouchListener;
import com.conceptioni.cafeapp.utils.SharedPrefs;
import com.google.gson.JsonObject;
import com.tabassum.shimmerRecyclerView.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity {

    ImageView ivCart;
    ShimmerRecyclerView rvCategory, rvCategoryitem;
    MenuAdapter menuAdapter;
    MenuItemAdapter menuItemAdapter;
    List<Category> categoryList = new ArrayList<>();
    List<Items> itemsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initmenu();
        clicks();
    }

    private void clicks() {
        ivCart.setOnClickListener(v -> startActivity(new Intent(MenuActivity.this, CartActivity.class)));
    }

    private void initmenu() {
        ivCart = findViewById(R.id.ivCart);
        rvCategory = findViewById(R.id.rvCategory);
        rvCategoryitem = findViewById(R.id.rvCategoryitem);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MenuActivity.this);
        rvCategory.setLayoutManager(linearLayoutManager);
        rvCategory.showShimmerAdapter();

        GetMenu();

        rvCategory.addOnItemTouchListener(new RecyclerTouchListener(MenuActivity.this, rvCategory, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(MenuActivity.this);
        rvCategoryitem.setLayoutManager(linearLayoutManager1);
        rvCategoryitem.showShimmerAdapter();


//        rvCategoryitem.postDelayed(() -> rvCategoryitem.hideShimmerAdapter(), 5000);


    }

    public void GetMenu() {
        JsonObject object = new JsonObject();
        object.addProperty("cafeid", "1");
        object.addProperty("userid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
        object.addProperty("auth_token", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Auth_token, Constant.notAvailable));

        Log.d("+++++++jsonobject","+++++"+object.toString());

        Service service = ApiCall.getRetrofit().create(Service.class);
        Call<Menu> call = service.getMenuItem("application/json", object);

        call.enqueue(new Callback<Menu>() {
            @Override
            public void onResponse(@NonNull Call<Menu> call, @NonNull Response<Menu> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.ItemData,response.body().toString()).apply();
                        categoryList = response.body().getCategory();
                        rvCategory.hideShimmerAdapter();
                        menuAdapter = new MenuAdapter(categoryList);
                        rvCategory.setAdapter(menuAdapter);
                        itemsArrayList.clear();
                        for (int i = 0; i <1 ; i++) {
                            itemsArrayList = categoryList.get(i).getItems();
                            menuItemAdapter = new MenuItemAdapter(itemsArrayList);
                            rvCategoryitem.hideShimmerAdapter();
                            rvCategoryitem.setAdapter(menuItemAdapter);

                        }

                    } else{
                        new MakeToast("Error while getting data");
                        rvCategory.hideShimmerAdapter();
                        rvCategoryitem.hideShimmerAdapter();
                    }
                } else {
                    new MakeToast("Error while getting data");
                    rvCategory.hideShimmerAdapter();
                    rvCategoryitem.hideShimmerAdapter();
                }

            }

            @Override
            public void onFailure(@NonNull Call<Menu> call, @NonNull Throwable t) {
                new MakeToast("Error while getting result");
                rvCategory.hideShimmerAdapter();
                rvCategoryitem.hideShimmerAdapter();
            }
        });
    }

}
