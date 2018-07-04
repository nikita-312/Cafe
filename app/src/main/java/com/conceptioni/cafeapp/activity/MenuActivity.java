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
import com.conceptioni.cafeapp.model.Images;
import com.conceptioni.cafeapp.model.Items;
import com.conceptioni.cafeapp.utils.Constant;
import com.conceptioni.cafeapp.utils.MakeToast;
import com.conceptioni.cafeapp.utils.RecyclerTouchListener;
import com.conceptioni.cafeapp.utils.SharedPrefs;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tabassum.shimmerRecyclerView.ShimmerRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    List<Items> itemsArrayList1 = new ArrayList<>();
    List<Images> imagesArrayList = new ArrayList<>();

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
                view.findViewById(R.id.llMain).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        view.findViewById(R.id.llMain).setBackgroundResource(R.drawable.orange_menu_drawable);
                        view.findViewById(R.id.tvrCatName).setVisibility(View.GONE);
                    }
                });
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

        Service service = ApiCall.getRetrofit().create(Service.class);
        Call<JsonObject> call = service.getMenuItem("application/json", object);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject data = new JSONObject(response.body().toString());
                            if (data.optString("success").equalsIgnoreCase("1")){
                                categoryList.clear();
                                JSONArray categoryarray = data.getJSONArray("category");
                                for (int i = 0; i <categoryarray.length() ; i++) {
                                    JSONObject categorydata = categoryarray.getJSONObject(i);
                                    Category category = new Category();
                                    category.setCid(categorydata.optString("cid"));
                                    category.setCname(categorydata.optString("cname"));
                                    category.setCimage(categorydata.optString("cimage"));

                                    itemsArrayList.clear();
                                    JSONArray itemarray = categorydata.getJSONArray("items");
                                    for (int j = 0; j <itemarray.length() ; j++) {
                                        JSONObject itemdata = itemarray.getJSONObject(j);
                                        Items items = new Items();
                                        items.setItem_id(itemdata.optString("item_id"));
                                        items.setItem_name(itemdata.optString("item_name"));
                                        items.setPrice(itemdata.optString("price"));
                                        items.setDesc(itemdata.optString("desc"));

                                        JSONArray images = itemdata.getJSONArray("image");
                                        for (int k = 0; k <images.length() ; k++) {
                                            Images images1 = new Images();
                                            images1.setImages(images.getString(k));
                                            imagesArrayList.add(images1);
                                            items.setImage(imagesArrayList);
                                        }
                                        itemsArrayList.add(items);
                                        category.setItems(itemsArrayList);
                                    }

                                    categoryList.add(category);
                                }
                                rvCategory.hideShimmerAdapter();
                                menuAdapter = new MenuAdapter(categoryList);
                                rvCategory.setAdapter(menuAdapter);

                                itemsArrayList1.clear();
                                for (int i = 0; i <1 ; i++) {
                                    itemsArrayList1 = categoryList.get(i).getItems();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(itemsArrayList1);
                                    SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.ItemData, json).apply();
                                    menuItemAdapter = new MenuItemAdapter(itemsArrayList1);
                                    rvCategoryitem.hideShimmerAdapter();
                                    rvCategoryitem.setAdapter(menuItemAdapter);
                                }

                            }else {
                                new MakeToast(data.optString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.d("+++++successelse","+++++" +t.getMessage());
                new MakeToast("Error while getting result");
                rvCategory.hideShimmerAdapter();
                rvCategoryitem.hideShimmerAdapter();
            }
        });
    }

}
