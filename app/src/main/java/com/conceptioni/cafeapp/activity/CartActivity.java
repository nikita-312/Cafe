package com.conceptioni.cafeapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.activity.retrofitinterface.Service;
import com.conceptioni.cafeapp.adapter.CartItemAdapter;
import com.conceptioni.cafeapp.model.CartModel;
import com.conceptioni.cafeapp.utils.Constant;
import com.conceptioni.cafeapp.utils.MakeToast;
import com.conceptioni.cafeapp.utils.SharedPrefs;
import com.conceptioni.cafeapp.utils.TextviewRegular;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {
    RecyclerView rvCart;
    TextviewRegular tvrPlaceOrder,tvrCartTotal,tvrCartFee,tvrCartSubTotal;
    String subtotal,total,fee;
    CartModel cartModel = new CartModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        init();
        click();
    }

    private void click() {
        tvrPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this,LiveOrderActivity.class));
            }
        });
    }

    private void init() {
        rvCart=findViewById(R.id.rvCart);
        tvrPlaceOrder=findViewById(R.id.tvrPlaceOrder);
        tvrCartTotal=findViewById(R.id.tvrCartTotal);
        tvrCartFee=findViewById(R.id.tvrCartFee);
        tvrCartSubTotal=findViewById(R.id.tvrCartSubTotal);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CartActivity.this);
        rvCart.setLayoutManager(linearLayoutManager);
        CartItemAdapter cartItemAdapter = new CartItemAdapter();
        rvCart.setAdapter(cartItemAdapter);

        ViewCart();
    }
    public void ViewCart(){
        Log.d("++++viewcart","++++in");

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
        jsonObject.addProperty("auth_token",SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Auth_token, Constant.notAvailable));

        Service  service = ApiCall.getRetrofit().create(Service.class);
        Call<JsonObject> call = service.viewCart("application/json",jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("++++viewcart","++++onresponse");

                if (response.body() != null){
                    if (response.isSuccessful()){
                        try {
                            Log.d("++++viewcart","++++try");
                            JSONObject jsonObject1 = new JSONObject(response.body().toString());
                                if (jsonObject1.optInt("success")==1){
                                    Log.d("++++viewcart","++++success");

                                    subtotal = String.valueOf(jsonObject1.optInt("subtotal"));
                                   fee = String.valueOf(jsonObject1.optInt("fee"));
                                   total = String.valueOf(jsonObject1.optInt("total"));
                                    JSONArray jsonArray = jsonObject1.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        cartModel.setItem_name(object.optString("item_name"));
                                        cartModel.setPrice(object.optString("price"));
                                        cartModel.setQty(object.optString("qty"));
                                        JSONArray array = object.getJSONArray("image");
                                        for (int j = 0; j < array.length(); j++) {
                                            array.getString(j);

                                        }
                                    }
                                    Log.d("++++total","+++ "+subtotal);
                                    tvrCartSubTotal.setText(subtotal);
                                    tvrCartFee.setText(fee);
                                    tvrCartTotal.setText(total);
                                }
                            Log.d("++++viewcart","++++else");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("++++viewcart","++++catch");

                        }
                    }else{
                        new MakeToast("Error while getting data");
                    }
                }else{
                    new MakeToast("Error while getting result");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}
