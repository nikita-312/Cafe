package com.conceptioni.cafeapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.activity.retrofitinterface.Service;
import com.conceptioni.cafeapp.adapter.MenuAdapter;
import com.conceptioni.cafeapp.adapter.MenuItemAdapter;
import com.conceptioni.cafeapp.model.Category;
import com.conceptioni.cafeapp.model.Items;
import com.conceptioni.cafeapp.utils.Constant;
import com.conceptioni.cafeapp.utils.MakeToast;
import com.conceptioni.cafeapp.utils.RecyclerTouchListener;
import com.conceptioni.cafeapp.utils.SharedPrefs;
import com.conceptioni.cafeapp.utils.TextviewBold;
import com.conceptioni.cafeapp.utils.TextviewRegular;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tabassum.shimmerRecyclerView.ShimmerRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity {

    ImageView ivCart, cafeiv;
    ShimmerRecyclerView rvCategory, rvCategoryitem;
    MenuAdapter menuAdapter;
    MenuItemAdapter menuItemAdapter;
    List<Category> categoryList = new ArrayList<>();
    List<Items> vegItemsList = new ArrayList<>();
//    List<Items> itemsArrayList1 = new ArrayList<>();
    LinearLayout viewliveorderll, retryll, scancafell;
    RelativeLayout nointernetrl, mainrl, cartrl;
    TextviewBold quantitytvb, pricetvb;
    TextviewRegular tvrNodata;
    int pos = 0;
    SwitchCompat vegswitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initmenu();
        clicks();
        if (categoryList.size()>0)
        categoryList.get(pos).setIsselect(false);
    }

    private void initmenu() {
        ivCart = findViewById(R.id.ivCart);
        rvCategory = findViewById(R.id.rvCategory);
        rvCategoryitem = findViewById(R.id.rvCategoryitem);
        scancafell = findViewById(R.id.scancafell);
        viewliveorderll = findViewById(R.id.viewliveorderll);
        nointernetrl = findViewById(R.id.nointernetrl);
        mainrl = findViewById(R.id.mainrl);
        retryll = findViewById(R.id.retryll);
        cartrl = findViewById(R.id.cartrl);
        quantitytvb = findViewById(R.id.quantitytvb);
        pricetvb = findViewById(R.id.pricetvb);
        tvrNodata = findViewById(R.id.tvrNodata);
        cafeiv = findViewById(R.id.cafeiv);
        vegswitch = findViewById(R.id.vegswitch);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MenuActivity.this);
        rvCategory.setLayoutManager(linearLayoutManager);
        rvCategory.showShimmerAdapter();

//        Log.d("++++++")

        rvCategory.addOnItemTouchListener(new RecyclerTouchListener(MenuActivity.this, rvCategory, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                view.findViewById(R.id.llMain).setOnClickListener(v -> {
                    categoryList.get(pos).setIsselect(false);
                    categoryList.get(position).setIsselect(true);
                    pos = position;
                    SetAdapter(categoryList.get(position).getItems());
                    menuAdapter.notifyDataSetChanged();

                    Gson gson = new Gson();
                    String json = gson.toJson(categoryList.get(position).getItems());
                    SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.ItemData, json).apply();

                });
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(MenuActivity.this);
        rvCategoryitem.setLayoutManager(linearLayoutManager1);
        rvCategoryitem.showShimmerAdapter();

        GetMenu();

    }

    @Override
    protected void onPause() {
        super.onPause();
//        ShowFilterData();
    }

    private void clicks() {
        ivCart.setOnClickListener(v -> startActivity(new Intent(MenuActivity.this, CartActivity.class)));

        scancafell.setOnClickListener(v ->
                ScanCafe()
        );

        viewliveorderll.setOnClickListener(v -> startActivity(new Intent(MenuActivity.this, LiveOrderActivity.class)));

        retryll.setOnClickListener(v -> GetMenu());

//        vegswitch.setChecked(false);

        vegswitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                ShowVegData(categoryList.get(pos).getItems());
            else
                ShowAllData(categoryList.get(pos).getItems());
        });

    }

    private void ShowVegData(List<Items> itemsArrayList) {
        vegItemsList.clear();
        if (!itemsArrayList.isEmpty()) {
            for (int i = 0; i < itemsArrayList.size(); i++) {
                if (itemsArrayList.get(i).getItem_type().equalsIgnoreCase("veg")) {
                    Items items = new Items();
                    items.setImage(itemsArrayList.get(i).getImage());
                    items.setItem_id(itemsArrayList.get(i).getItem_id());
                    items.setItem_name(itemsArrayList.get(i).getItem_name());
                    items.setPrice(itemsArrayList.get(i).getPrice());
                    items.setDesc(itemsArrayList.get(i).getDesc());
                    items.setQty(itemsArrayList.get(i).getQty());
                    items.setItem_type(itemsArrayList.get(i).getItem_type());
                    vegItemsList.add(items);
                }
                if (vegItemsList.isEmpty()) {
                    new MakeToast("No veg item found");
                    SetAdapter(itemsArrayList);
                } else {
                    SetAdapter(vegItemsList);
                }

            }
        }
    }

//    private void ShowNonVegData(List<Items> itemsArrayList) {
//        vegItemsList.clear();
////        itemsArrayList = categoryList.get(SharedPrefs.getSharedPref().getInt(SharedPrefs.userSharedPrefData.id, 0)).getItems();
//        if (!itemsArrayList.isEmpty()) {
//            for (int i = 0; i < itemsArrayList.size(); i++) {
//                if (itemsArrayList.get(i).getItem_type().equalsIgnoreCase("nonveg")) {
//                    Items items = new Items();
//                    items.setImage(itemsArrayList.get(i).getImage());
//                    items.setItem_id(itemsArrayList.get(i).getItem_id());
//                    items.setItem_name(itemsArrayList.get(i).getItem_name());
//                    items.setPrice(itemsArrayList.get(i).getPrice());
//                    items.setDesc(itemsArrayList.get(i).getDesc());
//                    items.setQty(itemsArrayList.get(i).getQty());
//                    items.setItem_type(itemsArrayList.get(i).getItem_type());
//                    vegItemsList.add(items);
//                }
//                if (vegItemsList.isEmpty()) {
//                    new MakeToast("No non-veg item found");
//                    SetAdapter(itemsArrayList);
//                } else {
//                    SetAdapter(vegItemsList);
//                }
//            }
//        }
//    }

    private void ShowAllData(List<Items> itemsArrayList) {
        SetAdapter(itemsArrayList);
    }

    public void GetMenu() {
        JsonObject object = new JsonObject();
        object.addProperty("cafeid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable));
        object.addProperty("userid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
        object.addProperty("auth_token", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Auth_token, Constant.notAvailable));

        Log.d("+++++object", "++++" + object.toString());

        Service service = ApiCall.getRetrofit().create(Service.class);
        Call<JsonObject> call = service.getMenuItem("application/json", object);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        try {
                            nointernetrl.setVisibility(View.GONE);
                            mainrl.setVisibility(View.VISIBLE);
                            JSONObject data = new JSONObject(Objects.requireNonNull(response.body()).toString());
                            if (data.optString("success").equalsIgnoreCase("1")) {
                                categoryList.clear();

                                JSONArray categoryarray = data.getJSONArray("category");
                                for (int i = 0; i < categoryarray.length(); i++) {
                                    JSONObject categorydata = categoryarray.getJSONObject(i);
                                    Category category = new Category();
                                    category.setCid(categorydata.optString("cid"));
                                    category.setCname(categorydata.optString("cname"));
                                    category.setCimage(categorydata.optString("cimage"));
                                    if (i == 0) {
                                        category.setIsselect(true);
                                    } else {
                                        category.setIsselect(false);
                                    }
                                    List<Items> itemsArrayList = new ArrayList<>();

                                    JSONArray itemarray = categorydata.getJSONArray("items");
                                    for (int j = 0; j < itemarray.length(); j++) {
                                        JSONObject itemdata = itemarray.getJSONObject(j);
                                        Items items = new Items();
                                        items.setItem_id(itemdata.optString("item_id"));
                                        items.setItem_name(itemdata.optString("item_name"));
                                        items.setPrice(itemdata.optString("price"));
                                        items.setDesc(itemdata.optString("desc"));
                                        items.setQty(itemdata.optString("qty"));
                                        items.setItem_type(itemdata.optString("item_type"));
                                        items.setImage(itemdata.optString("image"));
                                        itemsArrayList.add(items);
                                    }
                                    category.setItems(itemsArrayList);
                                    categoryList.add(category);
                                }
                                rvCategory.hideShimmerAdapter();
                                menuAdapter = new MenuAdapter(categoryList);
                                rvCategory.setAdapter(menuAdapter);

                                List<Items> itemsArrayList1;
                                itemsArrayList1 = categoryList.get(0).getItems();
                                Gson gson = new Gson();
                                String json = gson.toJson(itemsArrayList1);
                                SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.ItemData, json).apply();
                                if (categoryList.size() > 0)
                                    SetAdapter(categoryList.get(0).getItems());
                                ShowCartLayout(categoryList);

                            } else {
                                new MakeToast(data.optString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
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
                rvCategory.hideShimmerAdapter();
                rvCategoryitem.hideShimmerAdapter();
                mainrl.setVisibility(View.GONE);
                nointernetrl.setVisibility(View.VISIBLE);
            }
        });
    }

    private void SetAdapter(List<Items> itemsArrayList1) {
        if (!itemsArrayList1.isEmpty()) {
            tvrNodata.setVisibility(View.GONE);
            rvCategoryitem.setVisibility(View.VISIBLE);
            menuItemAdapter = new MenuItemAdapter(itemsArrayList1);
            rvCategoryitem.hideShimmerAdapter();
            rvCategoryitem.setAdapter(menuItemAdapter);
        } else {
            rvCategoryitem.setVisibility(View.GONE);
            tvrNodata.setVisibility(View.VISIBLE);
        }


    }

    @SuppressLint("SetTextI18n")
    private void ShowCartLayout(List<Category> categoryList) {
        List<Items> itemsArrayList1 = new ArrayList<>();
        itemsArrayList1.clear();
        for (int i = 0; i < categoryList.size(); i++) {
            itemsArrayList1 = categoryList.get(i).getItems();
            if (!itemsArrayList1.get(i).getQty().equalsIgnoreCase("0")) {
                cartrl.setVisibility(View.VISIBLE);
                quantitytvb.setText("Add Item To Cart");

            } else {
                cartrl.setVisibility(View.GONE);
            }
        }
    }

    private void ScanCafe() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
        jsonObject.addProperty("auth_token", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Auth_token, Constant.notAvailable));

        Log.d("+++++type", "+++ " + jsonObject.toString());

        Service service = ApiCall.getRetrofit().create(Service.class);
        Call<JsonObject> call = service.sessionexpire("application/json", jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject object = new JSONObject(Objects.requireNonNull(response.body()).toString());
                            if (object.optInt("success") == 1) {
                                new MakeToast(object.optString("msg"));
                                startActivity(new Intent(MenuActivity.this, HomeActivity.class));
                                finish();
                                SharedPrefs.getSharedPref().edit().remove(SharedPrefs.userSharedPrefData.Cafe_Id).apply();
                                SharedPrefs.getSharedPref().edit().remove(SharedPrefs.userSharedPrefData.table_number).apply();

                            } else
                                new MakeToast(object.optString("msg"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                new MakeToast(R.string.Checkyournetwork);
            }
        });
    }

}
