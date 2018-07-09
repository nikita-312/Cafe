package com.conceptioni.cafeapp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.activity.retrofitinterface.Service;
import com.conceptioni.cafeapp.model.Images;
import com.conceptioni.cafeapp.model.Items;
import com.conceptioni.cafeapp.utils.Constant;
import com.conceptioni.cafeapp.utils.MakeToast;
import com.conceptioni.cafeapp.utils.SharedPrefs;
import com.conceptioni.cafeapp.utils.TextviewRegular;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DescriptionActivity extends AppCompatActivity {

    String ItemData,ItemId,Qty;
    List<Items> itemsArrayList = new ArrayList<>();
    TextviewRegular ItemPricetvr,Itemnametvr,Itemdesctvr,qtytvr,addtocarttvr;
    EditText noteset;
    ImageView plusiv,minusiv,backiv,ivCart,itemiv;
    String Flag = "A";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
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


        if (getIntent().getExtras() != null){
            ItemId = getIntent().getStringExtra("ItemId");

            itemsArrayList.clear();
            itemsArrayList = getArrayList();
            for (int i = 0; i <itemsArrayList.size() ; i++) {
                if (ItemId.equalsIgnoreCase(itemsArrayList.get(i).getItem_id())){
                    Itemnametvr.setText(itemsArrayList.get(i).getItem_name());
                    ItemPricetvr.setText(itemsArrayList.get(i).getPrice() + " Rs");
                    Itemdesctvr.setText(itemsArrayList.get(i).getDesc());
                    qtytvr.setText(itemsArrayList.get(i).getQty());
                    Qty = itemsArrayList.get(i).getQty();

                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.no_image)
                            .error(R.drawable.no_image)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .priority(Priority.HIGH);

                    Glide.with(DescriptionActivity.this).load( itemsArrayList.get(i).getImage()).apply(options).into(itemiv);
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
            CallQuantity(finalQuantity,ItemId);
        });

        minusiv.setOnClickListener(v -> {
            Flag = "A";
            int count = Integer.parseInt(Qty);
            int Quantity = count - 1;
            String finalQuantity = String.valueOf(Quantity);
            CallQuantity(finalQuantity,ItemId);
        });

        addtocarttvr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Flag = "C";
                CallQuantity(Qty,ItemId);
            }
        });

        backiv.setOnClickListener(v -> onBackPressed());

        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DescriptionActivity.this,CartActivity.class));
                finish();
            }
        });
    }

    public ArrayList<Items> getArrayList(){
        Gson gson = new Gson();
        ItemData = SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.ItemData, Constant.notAvailable);
        Type type = new TypeToken<ArrayList<Items>>() {}.getType();
        return gson.fromJson(ItemData, type);
    }

    public void CallQuantity(String Quantity, String ItemId){

        JsonObject object = new JsonObject();
        object.addProperty("userid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
        object.addProperty("auth_token", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Auth_token, Constant.notAvailable));
        object.addProperty("itemid", ItemId);
        object.addProperty("qty", Quantity);
        object.addProperty("note", noteset.getText().toString());

        Log.d("+++++quant123","++++"+object.toString());

        Service service = ApiCall.getRetrofit().create(Service.class);
        Call<JsonObject> call = service.AddToCart("application/json", object);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null){
                    try {
                        JSONObject object1 = new JSONObject(String.valueOf(response.body()));
                        if (object1.optInt("success") == 1){
                            for (int i = 0; i <itemsArrayList.size() ; i++) {
                                if (ItemId.equalsIgnoreCase(itemsArrayList.get(i).getItem_id())) {
                                    itemsArrayList.get(i).setQty(object1.optString("qty"));
                                    qtytvr.setText(object1.optString("qty"));
                                    Qty = object1.optString("qty");
                                    SaveArrylistinShared(itemsArrayList);
                                    if (Flag.equalsIgnoreCase("C")){
                                        startActivity(new Intent(DescriptionActivity.this,MenuActivity.class));
                                        finish();
                                    }
                                }
                            }


                        }else {
                            if (Quantity.equalsIgnoreCase("0")){
                                new MakeToast(object1.optString("msg"));
                            }else {
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
                new MakeToast("Please Try After Some Time");
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void SaveArrylistinShared(List<Items> itemsArrayList){
        for (int i = 0; i <1 ; i++) {
            Gson gson = new Gson();
            String json = gson.toJson(itemsArrayList);
            SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.ItemData, json).apply();
        }
    }
}
