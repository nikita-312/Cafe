package com.conceptioni.cafeapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

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
    List<Category> filtercategoryList = new ArrayList<>();
    List<Items> itemsArrayList = new ArrayList<>();
    List<Items> itemsArrayList2 = new ArrayList<>();
    List<Items> filteritemsArrayList = new ArrayList<>();
    List<Items> itemsArrayList1 = new ArrayList<>();
    List<Images> imagesArrayList = new ArrayList<>();
    SwitchCompat veg;
    boolean isVeg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

    }

    private void clicks() {
        ivCart.setOnClickListener(v -> startActivity(new Intent(MenuActivity.this, CartActivity.class)));

      veg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              if (isChecked){
                  isVeg = true;
                  new MakeToast("Veg");
                  ShowData();
              }else {
                  isVeg = false;
                  new MakeToast("All");
              }
          }
      });
    }

    private void ShowData(){
        filteritemsArrayList.clear();
        if (!itemsArrayList.isEmpty()){
            for (int i = 0; i <itemsArrayList.size() ; i++) {
                if (itemsArrayList.get(i).getItem_type().equalsIgnoreCase("veg")){
                    filteritemsArrayList.get(i).setItem_id(itemsArrayList.get(i).getItem_id());
                    filteritemsArrayList.get(i).setItem_name(itemsArrayList.get(i).getItem_name());
                    filteritemsArrayList.get(i).setPrice(itemsArrayList.get(i).getPrice());
                    filteritemsArrayList.get(i).setDesc(itemsArrayList.get(i).getDesc());
                    filteritemsArrayList.get(i).setQty(itemsArrayList.get(i).getQty());
                    filteritemsArrayList.get(i).setItem_type(itemsArrayList.get(i).getItem_type());
                    filteritemsArrayList.get(i).setImage(itemsArrayList.get(i).getImage());
//                    Items items = new Items();
//                    items.setItem_id();
//                    items.setItem_name(itemdata.optString("item_name"));
//                    items.setPrice(itemdata.optString("price"));
//                    items.setDesc(itemdata.optString("desc"));
//                    items.setQty(itemdata.optString("qty"));
//                    items.setItem_type(itemdata.optString("item_type"));

                }
                Log.d("+++++size","++++"+filteritemsArrayList.size());
            }
        }
//        if (!categoryList.isEmpty()){
//            filtercategoryList.clear();
//            itemsArrayList2.clear();
//            for (int i = 0; i <categoryList.size() ; i++) {
//                filteritemsArrayList = categoryList.get(i).getItems();
//               if (!filteritemsArrayList.isEmpty()){
//                   for (int j = 0; j <filteritemsArrayList.size() ; j++) {
//                       if (filteritemsArrayList.get(j).getItem_type().equalsIgnoreCase("veg")){
//                           Log.d("+++++size","+++++"+j);
////                          itemsArrayList2 = categoryList.get(j).getItems();
//                       }
//                   }
//
////                   filtercategoryList.get(i).setItems(itemsArrayList2);
////
//               }
//            }
//        }
    }

    private void initmenu() {
        ivCart = findViewById(R.id.ivCart);
        rvCategory = findViewById(R.id.rvCategory);
        rvCategoryitem = findViewById(R.id.rvCategoryitem);
        veg = findViewById(R.id.veg);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MenuActivity.this);
        rvCategory.setLayoutManager(linearLayoutManager);
        rvCategory.showShimmerAdapter();

        rvCategory.addOnItemTouchListener(new RecyclerTouchListener(MenuActivity.this, rvCategory, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                view.findViewById(R.id.llMain).setOnClickListener(v -> {
                    view.findViewById(R.id.llMain).setBackgroundResource(R.drawable.orange);
                    view.findViewById(R.id.tvrCatName).setVisibility(View.GONE);
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
    protected void onResume() {
        super.onResume();
        initmenu();
        clicks();
    }

    public void GetMenu() {
        JsonObject object = new JsonObject();
        object.addProperty("cafeid", "1");
        object.addProperty("userid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
        object.addProperty("auth_token", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Auth_token, Constant.notAvailable));

        Log.d("+++++object","++++"+object.toString());

        Service service = ApiCall.getRetrofit().create(Service.class);
        Call<JsonObject> call = service.getMenuItem("application/json", object);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject data = new JSONObject(Objects.requireNonNull(response.body()).toString());
                            if (data.optString("success").equalsIgnoreCase("1")){
                                itemsArrayList.clear();
                                categoryList.clear();
                                imagesArrayList.clear();
                                JSONArray categoryarray = data.getJSONArray("category");
                                Log.d("++++size","++++"+categoryarray.length());
                                for (int i = 0; i <categoryarray.length() ; i++) {
                                    JSONObject categorydata = categoryarray.getJSONObject(i);
                                    Category category = new Category();
                                    category.setCid(categorydata.optString("cid"));
                                    category.setCname(categorydata.optString("cname"));
                                    category.setCimage(categorydata.optString("cimage"));

                                    JSONArray itemarray = categorydata.getJSONArray("items");
                                    for (int j = 0; j <itemarray.length() ; j++) {
                                        JSONObject itemdata = itemarray.getJSONObject(j);
                                        Items items = new Items();
                                        items.setItem_id(itemdata.optString("item_id"));
                                        items.setItem_name(itemdata.optString("item_name"));
                                        items.setPrice(itemdata.optString("price"));
                                        items.setDesc(itemdata.optString("desc"));
                                        items.setQty(itemdata.optString("qty"));
                                        items.setItem_type(itemdata.optString("item_type"));

                                        JSONArray images = itemdata.getJSONArray("image");
                                        for (int k = 0; k <images.length() ; k++) {
                                            Images images1 = new Images();
                                            images1.setImages(images.getString(k));
                                            imagesArrayList.add(images1);
                                        }
                                        items.setImage(imagesArrayList);
                                        itemsArrayList.add(items);
                                    }
                                    category.setItems(itemsArrayList);
                                    categoryList.add(category);
                                }

                                categoryList.size();


                                rvCategory.hideShimmerAdapter();
                                menuAdapter = new MenuAdapter(categoryList);
                                rvCategory.setAdapter(menuAdapter);

                                SetAdapter(itemsArrayList1);

                                for (int i = 0; i <categoryList.size() ; i++) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(imagesArrayList);
                                    SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.Imageata, json).apply();
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
                new MakeToast("Error while getting result");
                rvCategory.hideShimmerAdapter();
                rvCategoryitem.hideShimmerAdapter();
            }
        });
    }

    private void SetAdapter(List<Items> itemsArrayList1){
        itemsArrayList1.clear();
        for (int i = 0; i <1 ; i++) {
            itemsArrayList1 = categoryList.get(0).getItems();
            Gson gson = new Gson();
            String json = gson.toJson(itemsArrayList1);
            SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.ItemData, json).apply();
            menuItemAdapter = new MenuItemAdapter(itemsArrayList1,imagesArrayList);
            rvCategoryitem.hideShimmerAdapter();
            rvCategoryitem.setAdapter(menuItemAdapter);
        }
    }

}
