package com.conceptioni.cafeapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.activity.retrofitinterface.Service;
import com.conceptioni.cafeapp.adapter.MenuAdapter;
import com.conceptioni.cafeapp.adapter.MenuItemAdapter;
import com.conceptioni.cafeapp.database.DBOpenHelper;
import com.conceptioni.cafeapp.model.CartData;
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

    ImageView ivCart;
    ShimmerRecyclerView rvCategory, rvCategoryitem;
    MenuAdapter menuAdapter;
    MenuItemAdapter menuItemAdapter;
    List<Category> categoryList = new ArrayList<>();
    List<Items> vegItemsList = new ArrayList<>();
    List<CartData> cartDataArrayList = new ArrayList<>();
    LinearLayout viewliveorderll, retryll, scancafell;
    RelativeLayout nointernetrl, mainrl, cartrl;
    TextviewBold quantitytvb, viewtvb;
    TextviewRegular tvrNodata;
    int pos = 0;
    SwitchCompat vegswitch;
    String TotalQty = "0", Flag = "";
    boolean isVeg = false;
    DBOpenHelper dbOpenHelper;
    SQLiteDatabase sqLiteDatabase;
    ProgressBar addtocartprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        dbOpenHelper = new DBOpenHelper(MenuActivity.this);
        sqLiteDatabase = dbOpenHelper.getWritableDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initmenu();
        clicks();
        pos = 0;
        if (categoryList.size() > 0) {
            categoryList.get(pos).setIsselect(false);
        }
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
        tvrNodata = findViewById(R.id.tvrNodata);
        vegswitch = findViewById(R.id.vegswitch);
        viewtvb = findViewById(R.id.viewtvb);
        addtocartprogress = findViewById(R.id.addtocartprogress);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MenuActivity.this);
        rvCategory.setLayoutManager(linearLayoutManager);
        rvCategory.showShimmerAdapter();


        rvCategory.addOnItemTouchListener(new RecyclerTouchListener(MenuActivity.this, rvCategory, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                view.findViewById(R.id.llMain).setOnClickListener(v -> {
                    for (int i = 0; i < categoryList.size(); i++) {
                        if (categoryList.get(i).isselect) {
                            categoryList.get(i).setIsselect(false);
                        }
                    }
                    categoryList.get(pos).setIsselect(false);
                    categoryList.get(position).setIsselect(true);
                    pos = position;

                    if (isVeg) {
                        ShowVegData(categoryList.get(position).getItems());
                    } else {
                        ShowAllData(categoryList.get(position).getItems());
                    }
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

        rvCategoryitem.addOnItemTouchListener(new RecyclerTouchListener(MenuActivity.this, rvCategoryitem, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ImageView plusiv = view.findViewById(R.id.plusiv);
                ImageView minusiv = view.findViewById(R.id.minusiv);
                LinearLayout itemll = view.findViewById(R.id.itemll);
                TextviewRegular tvrCartQty = view.findViewById(R.id.quantytvr);
                ProgressBar progressBar = view.findViewById(R.id.progress);
                List<Items> itemsList;

                itemsList = categoryList.get(pos).getItems();
                List<Items> finalItemsList = itemsList;
                plusiv.setOnClickListener(v -> {
                    Flag = "A";
                    int count = Integer.parseInt(finalItemsList.get(position).getQty());
                    int Quantity = count + 1;
                    String finalQuantity = String.valueOf(Quantity);
                    progressBar.setVisibility(View.GONE);

                    int totalqty = Integer.parseInt(TotalQty) + 1;
                    TotalQty = String.valueOf(totalqty);

                    Log.d("+++Total", "++++" + TotalQty);

//                    cartDataArrayList = dbOpenHelper.getCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), finalItemsList.get(position).getItem_id());
//
//                    if (cartDataArrayList.isEmpty()) {
//                        dbOpenHelper.addCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable), finalItemsList.get(position).getItem_id(), finalItemsList.get(position).getItem_name(), "", finalQuantity, TotalQty, itemsList.get(position).getPrice(), "", "");
//                    } else {
//                        for (int i = 0; i < cartDataArrayList.size(); i++) {
//                            if (cartDataArrayList.get(i).getCOLUMN_ITEM_ID().equalsIgnoreCase(finalItemsList.get(position).getItem_id())) {
//                                dbOpenHelper.updatecartdata(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable), finalItemsList.get(position).getItem_id(), finalItemsList.get(position).getItem_name(), "", finalQuantity, TotalQty, itemsList.get(position).getPrice(), "", "");
//                            } else {
//                                dbOpenHelper.addCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable), finalItemsList.get(position).getItem_id(), finalItemsList.get(position).getItem_name(), "", finalQuantity, TotalQty, itemsList.get(position).getPrice(), "", "");
//                            }
//                        }
//
//                    }
                    addorupdatedataindatabase(position, finalItemsList, finalQuantity);
                    setData(tvrCartQty, finalQuantity, position, finalItemsList, TotalQty);

                });
                minusiv.setOnClickListener(v -> {
                    if (!finalItemsList.get(position).getQty().equalsIgnoreCase("0")) {
                        minusiv.setClickable(true);
                        Flag = "M";
                        int count = Integer.parseInt(finalItemsList.get(position).getQty());
                        int Quantity = count - 1;
                        String finalQuantity = String.valueOf(Quantity);
                        progressBar.setVisibility(View.GONE);

                        int totalqty = Integer.parseInt(TotalQty) - 1;
                        TotalQty = String.valueOf(totalqty);


                        addorupdatedataindatabase(position, finalItemsList, finalQuantity);
                        setData(tvrCartQty, finalQuantity, position, finalItemsList, TotalQty);
                    } else {
                        minusiv.setClickable(false);
                    }

                });
                itemll.setOnClickListener(view1 -> {
                    startActivity(new Intent(MenuActivity.this, DescriptionActivity.class).putExtra("ItemId", finalItemsList.get(position).getItem_id()).putExtra("Total", TotalQty));
                    Log.d("+++++idmenu", "+++++" + finalItemsList.get(position).getItem_id());
                });
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        GetMenu();
    }

    private void addorupdatedataindatabase(int position, List<Items> finalItemsList, String finalQuantity) {
        cartDataArrayList.clear();
        cartDataArrayList = dbOpenHelper.getCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), finalItemsList.get(position).getItem_id());

        if (cartDataArrayList.isEmpty()) {
            dbOpenHelper.addCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable), finalItemsList.get(position).getItem_id(), finalItemsList.get(position).getItem_name(), "", finalQuantity, TotalQty, finalItemsList.get(position).getPrice(), "", "");
        } else {
            for (int i = 0; i < cartDataArrayList.size(); i++) {
                if (cartDataArrayList.get(i).getCOLUMN_ITEM_ID().equalsIgnoreCase(finalItemsList.get(position).getItem_id())) {
                    if (finalQuantity.equalsIgnoreCase("0")) {
                        Integer deletedRows = dbOpenHelper.deleterow(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), finalItemsList.get(position).getItem_id());
                        if (deletedRows > 0) {
                            boolean isupdate = dbOpenHelper.updateAllcartdata(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), TotalQty, "", "");
                            if (isupdate) {
                                List<CartData> cartData;
                                cartData = dbOpenHelper.getAllCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
                            }
                        }
                    }
//                    dbOpenHelper.updatecartdata(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable), finalItemsList.get(position).getItem_id(), finalItemsList.get(position).getItem_name(), "", finalQuantity, TotalQty, finalItemsList.get(position).getPrice(), "", "");
                } else {
                    dbOpenHelper.addCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable), finalItemsList.get(position).getItem_id(), finalItemsList.get(position).getItem_name(), "", finalQuantity, TotalQty, finalItemsList.get(position).getPrice(), "", "");
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void setData(TextviewRegular tvrCartQty, String Quantity, int Position, List<Items> itemsList, String totalqty) {
        SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.Flag, "0").apply();
        itemsList.get(Position).setQty(Quantity);
        tvrCartQty.setText(Quantity);
        SaveArrylistinShared(itemsList);
        if (!SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Flag, Constant.notAvailable).equalsIgnoreCase(Constant.notAvailable)) {
            if (SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Flag, Constant.notAvailable).equalsIgnoreCase("0")) {
                if (!totalqty.equalsIgnoreCase("0")) {
                    cartrl.setVisibility(View.VISIBLE);
                    quantitytvb.setVisibility(View.VISIBLE);
                    checkdatabasestatus();
                    viewtvb.setText("View Cart");
                } else {
                    cartrl.setVisibility(View.GONE);
                }
            } else {
                cartrl.setVisibility(View.VISIBLE);
                checkdatabasestatus();
                quantitytvb.setVisibility(View.GONE);
                viewtvb.setText("View Live Order");
            }
        } else {
            if (!totalqty.equalsIgnoreCase("0")) {
                cartrl.setVisibility(View.VISIBLE);
                quantitytvb.setVisibility(View.VISIBLE);
                checkdatabasestatus();
                viewtvb.setText("View Cart");
            } else {
                cartrl.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void clicks() {
        scancafell.setOnClickListener(view -> {
            if (SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.canScan, Constant.notAvailable).equalsIgnoreCase("yes")) {
                ScanCafe();
            } else {
                new MakeToast("You still have live order!");
            }
        });

        viewliveorderll.setOnClickListener(view -> startActivity(new Intent(MenuActivity.this, LiveOrderActivity.class)));

        retryll.setOnClickListener(v -> GetMenu());

        vegswitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                isVeg = true;
                ShowVegData(categoryList.get(pos).getItems());
            } else {
                isVeg = false;
                ShowAllData(categoryList.get(pos).getItems());
            }

        });

        cartrl.setOnClickListener(v -> {
            if (SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Flag, Constant.notAvailable).equalsIgnoreCase("0")) {
                startActivity(new Intent(MenuActivity.this, CartActivity.class));
            } else {
                startActivity(new Intent(MenuActivity.this, LiveOrderActivity.class));
            }
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

                    if (vegItemsList.isEmpty()) {
                        SetAdapter(itemsArrayList);
                    } else {
                        SetAdapter(vegItemsList);
                    }

                } else {
                    tvrNodata.setVisibility(View.VISIBLE);
                    rvCategoryitem.setVisibility(View.GONE);
                }

            }
        }
    }

    private void ShowAllData(List<Items> itemsArrayList) {
        SetAdapter(itemsArrayList);
    }

    public void GetMenu() {
        JsonObject object = new JsonObject();
        object.addProperty("cafeid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable));
        object.addProperty("userid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
        object.addProperty("auth_token", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Auth_token, Constant.notAvailable));

        Service service = ApiCall.getRetrofit().create(Service.class);
        Call<JsonObject> call = service.getMenuItem("application/json", object);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        try {
                            nointernetrl.setVisibility(View.GONE);
                            mainrl.setVisibility(View.VISIBLE);
                            JSONObject data = new JSONObject(Objects.requireNonNull(response.body()).toString());
                            if (data.optString("success").equalsIgnoreCase("1")) {
                                TotalQty = data.optString("totalQty");
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
                                if (categoryList.size() > 0) {
                                    SetAdapter(categoryList.get(0).getItems());
                                }

                                if (!SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Flag, Constant.notAvailable).equalsIgnoreCase(Constant.notAvailable)) {
                                    if (SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Flag, Constant.notAvailable).equalsIgnoreCase("0")) {
                                        if (!TotalQty.equalsIgnoreCase("0")) {
                                            cartrl.setVisibility(View.VISIBLE);
                                            quantitytvb.setVisibility(View.VISIBLE);
//                                            checkdatabasestatus();
                                            cartDataArrayList.clear();
                                            cartDataArrayList = dbOpenHelper.getlastinsertCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
                                            if (cartDataArrayList.isEmpty()) {
                                                quantitytvb.setText(TotalQty + " items in cart");
                                            } else {
                                                for (int i = 0; i < cartDataArrayList.size(); i++) {
                                                    quantitytvb.setText(cartDataArrayList.get(i).getCOLUMN_ITEM_TOTAL_QUANTITY() + " items in cart");
                                                }
                                            }
                                            viewtvb.setText("View Cart");
                                        } else {
                                            cartrl.setVisibility(View.GONE);
                                        }
                                    } else {
                                        cartrl.setVisibility(View.VISIBLE);
//                                        checkdatabasestatus();
                                        cartDataArrayList.clear();
                                        cartDataArrayList = dbOpenHelper.getlastinsertCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
                                        if (cartDataArrayList.isEmpty()) {
                                            quantitytvb.setText(TotalQty + " items in cart");
                                        } else {
                                            for (int i = 0; i < cartDataArrayList.size(); i++) {
                                                quantitytvb.setText(cartDataArrayList.get(i).getCOLUMN_ITEM_TOTAL_QUANTITY() + " items in cart");
                                            }
                                        }
                                        quantitytvb.setVisibility(View.GONE);
                                        viewtvb.setText("View Live Order");
                                    }
                                } else {
                                    if (!TotalQty.equalsIgnoreCase("0")) {
                                        cartrl.setVisibility(View.VISIBLE);
                                        quantitytvb.setVisibility(View.VISIBLE);
//                                        checkdatabasestatus();
                                        cartDataArrayList.clear();
                                        cartDataArrayList = dbOpenHelper.getlastinsertCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
                                        if (cartDataArrayList.isEmpty()) {
                                            quantitytvb.setText(TotalQty + " items in cart");
                                        } else {
                                            for (int i = 0; i < cartDataArrayList.size(); i++) {
                                                quantitytvb.setText(cartDataArrayList.get(i).getCOLUMN_ITEM_TOTAL_QUANTITY() + " items in cart");
                                            }
                                        }
                                    } else {
                                        cartrl.setVisibility(View.GONE);
                                    }
                                }
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

    private void ScanCafe() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
        jsonObject.addProperty("auth_token", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Auth_token, Constant.notAvailable));

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
                                dbOpenHelper.deletetable();
                                SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.Flag, "0").apply();
                                SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.canScan, "yes").apply();
                                SharedPrefs.getSharedPref().edit().remove(SharedPrefs.userSharedPrefData.Cafe_Id).apply();
                                SharedPrefs.getSharedPref().edit().remove(SharedPrefs.userSharedPrefData.table_number).apply();
                                startActivity(new Intent(MenuActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                finish();

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

    private void SaveArrylistinShared(List<Items> itemsArrayList) {
        for (int i = 0; i < 1; i++) {
            Gson gson = new Gson();
            String json = gson.toJson(itemsArrayList);
            SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.ItemData, json).apply();
        }
    }

    @SuppressLint("SetTextI18n")
    private void checkdatabasestatus() {
        Log.d("+++++value", "++++++");
        cartDataArrayList.clear();
        cartDataArrayList = dbOpenHelper.getlastinsertCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
        if (cartDataArrayList.isEmpty()) {
            quantitytvb.setText(TotalQty + " items in cart");
        } else {
            for (int i = 0; i < cartDataArrayList.size(); i++) {
                quantitytvb.setText(cartDataArrayList.get(i).getCOLUMN_ITEM_TOTAL_QUANTITY() + " items in cart");
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}