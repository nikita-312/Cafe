package com.conceptioni.cafeapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.activity.retrofitinterface.Service;
import com.conceptioni.cafeapp.database.DBOpenHelper;
import com.conceptioni.cafeapp.model.CartData;
import com.conceptioni.cafeapp.model.Items;
import com.conceptioni.cafeapp.utils.Constant;
import com.conceptioni.cafeapp.utils.MakeToast;
import com.conceptioni.cafeapp.utils.SharedPrefs;
import com.conceptioni.cafeapp.utils.TextviewRegular;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DescriptionActivity extends AppCompatActivity {

    String ItemData, ItemId, Qty, ItemName, Price, TotalQty = "";
    List<Items> itemsArrayList = new ArrayList<>();
    TextviewRegular ItemPricetvr, Itemnametvr, Itemdesctvr, qtytvr, addtocarttvr;
    EditText noteset;
    ImageView plusiv, minusiv, backiv, ivCart, itemiv;
    String Flag = "A";
    List<CartData> cartDataArrayList = new ArrayList<>();
    DBOpenHelper dbOpenHelper;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        dbOpenHelper = new DBOpenHelper(DescriptionActivity.this);
        sqLiteDatabase = dbOpenHelper.getWritableDatabase();
        init();
        allclick();
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        ItemPricetvr = findViewById(R.id.ItemPricetvr);
        Itemnametvr = findViewById(R.id.Itemnametvr);
        Itemdesctvr = findViewById(R.id.Itemdesctvr);
        noteset = findViewById(R.id.noteset);
        qtytvr = findViewById(R.id.qtytvr);
        plusiv = findViewById(R.id.plusiv);
        minusiv = findViewById(R.id.minusiv);
        backiv = findViewById(R.id.backiv);
        addtocarttvr = findViewById(R.id.addtocarttvr);
        ivCart = findViewById(R.id.ivCart);
        itemiv = findViewById(R.id.itemiv);

        if (getIntent().getExtras() != null) {
            ItemId = getIntent().getStringExtra("ItemId");
            TotalQty = getIntent().getStringExtra("Total");

            itemsArrayList.clear();
            itemsArrayList = getArrayList();
            for (int i = 0; i < itemsArrayList.size(); i++) {
                Log.d("+++++iddesc","+++for "+ItemId+"+++ "+itemsArrayList.get(i).getItem_id());

                if (ItemId.equalsIgnoreCase(itemsArrayList.get(i).getItem_id())) {
                    Log.d("+++++iddesc","+++if ");

                    Itemnametvr.setText(itemsArrayList.get(i).getItem_name());
                    ItemPricetvr.setText(itemsArrayList.get(i).getPrice() + " Rs");
                    Itemdesctvr.setText(itemsArrayList.get(i).getDesc());
                    qtytvr.setText(itemsArrayList.get(i).getQty());
                    Qty = itemsArrayList.get(i).getQty();
                    ItemName = itemsArrayList.get(i).getItem_name();
                    Price = itemsArrayList.get(i).getPrice();
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.no_image)
                            .error(R.drawable.no_image)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .priority(Priority.HIGH);

                    Glide.with(DescriptionActivity.this).load(itemsArrayList.get(i).getImage()).apply(options).into(itemiv);
                }
            }
        }
    }

    private void allclick() {
        plusiv.setOnClickListener(v -> {
            Flag = "A";
            int count = Integer.parseInt(Qty);
            int Quantity = count + 1;
            String finalQuantity = String.valueOf(Quantity);
            int totalqty = Integer.parseInt(TotalQty) + 1;
            TotalQty = String.valueOf(totalqty);

            setData(finalQuantity);
            cartDataArrayList = dbOpenHelper.getCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), ItemId);

            if (cartDataArrayList.isEmpty()) {
                dbOpenHelper.addCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable), ItemId, ItemName, noteset.getText().toString(), finalQuantity, TotalQty, Price, "", "");
            } else {
                dbOpenHelper.addCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable), ItemId, ItemName, noteset.getText().toString(), finalQuantity, TotalQty, Price, "", "");
            }

            cartDataArrayList.clear();
            cartDataArrayList = dbOpenHelper.getCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id,Constant.notAvailable),ItemId);

            if (cartDataArrayList.isEmpty()){
                dbOpenHelper.addCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id,Constant.notAvailable),SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id,Constant.notAvailable),ItemId, ItemName,noteset.getText().toString(),finalQuantity,"",Price,"","");
            }else {
                dbOpenHelper.addCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id,Constant.notAvailable),SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id,Constant.notAvailable),ItemId, ItemName,noteset.getText().toString(),finalQuantity,"",Price,"","");
            }

//            CallQuantity(finalQuantity, ItemId);
        });

        minusiv.setOnClickListener(v -> {
            if (!Qty.equalsIgnoreCase("0") && !Qty.equalsIgnoreCase("1")) {
                Flag = "A";
                int count = Integer.parseInt(Qty);
                int Quantity = count - 1;
                String finalQuantity = String.valueOf(Quantity);

//                CallQuantity(finalQuantity, ItemId);

                int totalqty = Integer.parseInt(TotalQty) - 1;
                TotalQty = String.valueOf(totalqty);

                Log.d("+++Total", "++++" + TotalQty);

                setData(finalQuantity);
                cartDataArrayList.clear();
                cartDataArrayList = dbOpenHelper.getCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), ItemId);

                if (cartDataArrayList.isEmpty()) {
                    dbOpenHelper.addCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable), ItemId, ItemName, noteset.getText().toString(), finalQuantity, TotalQty, Price, "", "");
                } else {
                    dbOpenHelper.addCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable), ItemId, ItemName, noteset.getText().toString(), finalQuantity, TotalQty, Price, "", "");
                }

                if (cartDataArrayList.isEmpty()){
                    dbOpenHelper.addCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id,Constant.notAvailable),SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id,Constant.notAvailable),ItemId, ItemName,noteset.getText().toString(),finalQuantity,"",Price,"","");
                }else {
                    dbOpenHelper.addCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id,Constant.notAvailable),SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id,Constant.notAvailable),ItemId, ItemName,noteset.getText().toString(),finalQuantity,"",Price,"","");
                }


//                CallQuantity(finalQuantity, ItemId);
            }
        });

        addtocarttvr.setOnClickListener(v -> {
            Flag = "C";
            CallQuantity(Qty, ItemId);
        });

        backiv.setOnClickListener(v -> onBackPressed());

        ivCart.setOnClickListener(v -> {
            startActivity(new Intent(DescriptionActivity.this, CartActivity.class));
            finish();
        });
    }

    public ArrayList<Items> getArrayList() {
        Gson gson = new Gson();
        ItemData = SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.ItemData, Constant.notAvailable);
        Type type = new TypeToken<ArrayList<Items>>() {
        }.getType();
        return gson.fromJson(ItemData, type);
    }

    public void CallQuantity(String Quantity, String ItemId) {

        JsonObject object = new JsonObject();
        object.addProperty("userid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
        object.addProperty("auth_token", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Auth_token, Constant.notAvailable));
        object.addProperty("itemid", ItemId);
        object.addProperty("qty", Quantity);
        object.addProperty("note", noteset.getText().toString());
        object.addProperty("cafeid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable));

        Service service = ApiCall.getRetrofit().create(Service.class);
        Call<JsonObject> call = service.AddToCart("application/json", object);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JSONObject object1 = new JSONObject(String.valueOf(response.body()));
                        if (object1.optInt("success") == 1) {
                            for (int i = 0; i < itemsArrayList.size(); i++) {
                                if (ItemId.equalsIgnoreCase(itemsArrayList.get(i).getItem_id())) {
                                    itemsArrayList.get(i).setQty(object1.optString("qty"));
                                    qtytvr.setText(object1.optString("qty"));
                                    Qty = object1.optString("qty");
                                    SaveArrylistinShared(itemsArrayList);
                                    String subtotal = object1.optString("subtotal");
                                    String fee = object1.optString("fee");
                                    String total = object1.optString("total");
                                    String totalqty = object1.getString("totalqty");

                                    Log.d("+++++iddesctotal","+++++"+totalqty);

                                    cartDataArrayList.clear();
                                    cartDataArrayList = dbOpenHelper.getCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), ItemId);

                                    if (cartDataArrayList.isEmpty()) {
                                        Log.d("+++++iddescadd","+++++"+ItemId);
                                        dbOpenHelper.addCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable), ItemId, ItemName, noteset.getText().toString(), Quantity, totalqty, Price, fee, total);
                                    } else {
                                        for (int j = 0; j < cartDataArrayList.size(); j++) {
                                            if (cartDataArrayList.get(j).getCOLUMN_ITEM_ID().equalsIgnoreCase(ItemId)) {
                                                Log.d("+++++iddescupdate","+++++"+ItemId+"+++totalqty "+totalqty);
                                                dbOpenHelper.updatecartdata(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable), ItemId, ItemName, noteset.getText().toString(), Quantity, totalqty, Price, fee, total);
                                            } else {
                                                Log.d("+++++iddescelseadd","+++++"+ItemId);
                                                dbOpenHelper.addCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable), ItemId, ItemName, noteset.getText().toString(), Quantity, totalqty, Price, fee, total);
                                            }
                                        }
                                    }
                                    if (Flag.equalsIgnoreCase("C")) {
                                        startActivity(new Intent(DescriptionActivity.this, MenuActivity.class));
                                        finish();
                                    }
                                }
                            }
                        } else {
                            if (Quantity.equalsIgnoreCase("0")) {
                                new MakeToast(object1.optString("msg"));
                            } else {
                                int Quan = Integer.parseInt(Quantity);
                                qtytvr.setText(Quan - 1);
                                new MakeToast(object1.optString("msg"));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                new MakeToast(R.string.Checkyournetwork);
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void setData(String Quantity) {
        Log.d("++++++setData","++++"+Quantity);
        if (!itemsArrayList.isEmpty()) {
            for (int i = 0; i < itemsArrayList.size(); i++) {
                itemsArrayList.get(i).setQty(Quantity);
                qtytvr.setText(Quantity);
                SaveArrylistinShared(itemsArrayList);
            }
        }else {
            Log.d("++++++setDataelse","++++"+Quantity);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void SaveArrylistinShared(List<Items> itemsArrayList) {
        for (int i = 0; i < 1; i++) {
            Gson gson = new Gson();
            String json = gson.toJson(itemsArrayList);
            SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.ItemData, json).apply();
        }
    }
}
