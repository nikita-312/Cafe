package com.conceptioni.cafeapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.activity.retrofitinterface.Service;
import com.conceptioni.cafeapp.adapter.CartItemAdapter;
import com.conceptioni.cafeapp.model.CartModel;
import com.conceptioni.cafeapp.model.Images;
import com.conceptioni.cafeapp.utils.Constant;
import com.conceptioni.cafeapp.utils.MakeToast;
import com.conceptioni.cafeapp.utils.RecyclerTouchListener;
import com.conceptioni.cafeapp.utils.SharedPrefs;
import com.conceptioni.cafeapp.utils.TextviewRegular;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {
    RecyclerView rvCart;
    TextviewRegular tvrPlaceOrder,tvrCartTotal,tvrCartFee,tvrCartSubTotal;
    String subtotal,total,fee;
    CartModel cartModel = new CartModel();
    List<CartModel> cartModelsarray = new ArrayList<>();
    List<Images> imagesArrayList = new ArrayList<>();
    CartItemAdapter cartItemAdapter;
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
        rvCart.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), rvCart, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ImageView ivRemove = view.findViewById(R.id.ivRemove);
                ivRemove.setOnClickListener(v -> showDeleteAlert(position));
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void showDeleteAlert(final int pos){
        new AlertDialog.Builder(CartActivity.this)
                .setTitle("Remove?")
                .setMessage("Are you sure want to remove the product?")
                .setCancelable(true)
                .setNegativeButton(android.R.string.no, (dialog, which) -> dialog.dismiss())
                .setPositiveButton(android.R.string.yes, (arg0, arg1) -> removeCart(pos))
                .create().show();
    }

    private void init() {
        rvCart=findViewById(R.id.rvCart);
        tvrPlaceOrder=findViewById(R.id.tvrPlaceOrder);
        tvrCartTotal=findViewById(R.id.tvrCartTotal);
        tvrCartFee=findViewById(R.id.tvrCartFee);
        tvrCartSubTotal=findViewById(R.id.tvrCartSubTotal);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CartActivity.this);
        rvCart.setLayoutManager(linearLayoutManager);

        ViewCart();
    }
    public void ViewCart(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userid", "2");
        jsonObject.addProperty("auth_token","MmNhZmUxNTMwNjE1NTA3");

        Service  service = ApiCall.getRetrofit().create(Service.class);
        Call<JsonObject> call = service.viewCart("application/json",jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body() != null){
                    if (response.isSuccessful()){
                        try {
                            JSONObject jsonObject1 = new JSONObject(response.body().toString());
                                if (jsonObject1.getInt("success")==1){

                                    subtotal = String.valueOf(jsonObject1.optInt("subtotal"));
                                   fee = String.valueOf(jsonObject1.optInt("fee"));
                                   total = String.valueOf(jsonObject1.optInt("total"));

                                    cartModelsarray.clear();
                                    JSONArray jsonArray = jsonObject1.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        cartModel.setItem_name(object.optString("item_name"));
                                        cartModel.setItem_id(object.optString("item_id"));
                                        cartModel.setPrice(object.optString("price"));
                                        cartModel.setQty(object.optString("qty"));

                                        imagesArrayList.clear();
                                        Images images1 = null;
                                        JSONArray array = object.getJSONArray("image");
                                        for (int j = 0; j < array.length(); j++) {
                                             images1 = new Images();
                                            images1.setImages(array.getString(0));
                                            cartModel.setImages(imagesArrayList);
                                        }
                                        imagesArrayList.add(images1);

                                        cartModelsarray.add(cartModel);
                                        cartModel.setImages(imagesArrayList);
                                         cartItemAdapter = new CartItemAdapter(cartModelsarray,imagesArrayList);
                                        rvCart.setAdapter(cartItemAdapter);
                                    }
                                    tvrCartSubTotal.setText(subtotal);
                                    tvrCartFee.setText(fee);
                                    tvrCartTotal.setText(total);
                                }

                        } catch (JSONException e) {
                            e.printStackTrace();

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

    public void removeCart(final int pos){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("itemid",cartModelsarray.get(pos).getItem_id());
        jsonObject.addProperty("userid","2");
        jsonObject.addProperty("auth_token","MmNhZmUxNTMwNjE1NTA3");

        Service  service = ApiCall.getRetrofit().create(Service.class);
        Call<JsonObject> call = service.removeCart("application/json",jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null){
                    if (response.isSuccessful()){
                        try {
                            JSONObject object = new JSONObject(Objects.requireNonNull(response.body()).toString());
                            if (object.optInt("success")==1){
                                new MakeToast(object.optString("msg"));
                                cartModelsarray.remove(pos);
                                cartItemAdapter.notifyDataSetChanged();
                                if (cartModelsarray.size() == 0){
                                    tvrCartSubTotal.setText("00.00");
                                    tvrCartFee.setText("00.00");
                                    tvrCartTotal.setText("00.00");
                                }
                            }else{
                                new MakeToast(object.optString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }

}
