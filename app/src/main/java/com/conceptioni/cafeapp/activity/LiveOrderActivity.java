package com.conceptioni.cafeapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.activity.retrofitinterface.Service;
import com.conceptioni.cafeapp.adapter.LiveOrderAdapter;
import com.conceptioni.cafeapp.model.CartModel;
import com.conceptioni.cafeapp.utils.Constant;
import com.conceptioni.cafeapp.utils.MakeToast;
import com.conceptioni.cafeapp.utils.RecyclerTouchListener;
import com.conceptioni.cafeapp.utils.SharedPrefs;
import com.conceptioni.cafeapp.utils.TextviewRegular;
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

public class LiveOrderActivity extends AppCompatActivity {
    ShimmerRecyclerView rvliveOrder;
    LinearLayout llBottom, bottom, retryll;
    TextviewRegular tvrCartTotal, tvrCartFee, tvrCartSubTotal, continuetvr, paymenttvr;
    String subtotal,total,fee;


    List<CartModel> cartModelsarray = new ArrayList<>();
    ImageView ivBack;
    LiveOrderAdapter liveOrderAdapter;
    RelativeLayout emptycartll, mainrl, nointernetrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_order);
        init();
        click();
    }

    private void click() {
        paymenttvr.setOnClickListener(v -> {
            startActivity(new Intent(LiveOrderActivity.this, CardActivity.class));
            finish();
        });
        continuetvr.setOnClickListener(v -> finish());
        ivBack.setOnClickListener(v -> finish());
        retryll.setOnClickListener(v -> viewLiveOrder());
    }

    private void init() {
        rvliveOrder = findViewById(R.id.rvliveOrder);
        llBottom = findViewById(R.id.llBottom);
        bottom = findViewById(R.id.bottom);
        tvrCartTotal = findViewById(R.id.tvrCartTotal);
        tvrCartFee = findViewById(R.id.tvrCartFee);
        tvrCartSubTotal = findViewById(R.id.tvrCartSubTotal);
        continuetvr = findViewById(R.id.continuetvr);
        paymenttvr = findViewById(R.id.paymenttvr);
        ivBack = findViewById(R.id.ivBack);
        emptycartll = findViewById(R.id.emptycartll);

        mainrl = findViewById(R.id.mainrl);
        nointernetrl = findViewById(R.id.nointernetrl);
        retryll = findViewById(R.id.retryll);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LiveOrderActivity.this);
        rvliveOrder.setLayoutManager(linearLayoutManager);
        rvliveOrder.showShimmerAdapter();
        viewLiveOrder();

        rvliveOrder.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), rvliveOrder, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
             ImageView  reorderiv = view.findViewById(R.id.reorderiv);
             reorderiv.setOnClickListener(v -> CallReorder(cartModelsarray.get(position).getItem_id()));
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    public void viewLiveOrder() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
        jsonObject.addProperty("auth_token", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Auth_token, Constant.notAvailable));
        jsonObject.addProperty("cafeid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable));
        jsonObject.addProperty("gst", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.gst, Constant.notAvailable));

        Service service = ApiCall.getRetrofit().create(Service.class);
        Call<JsonObject> call = service.ViewLiveOrder("application/json", jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {

                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        try {
                            nointernetrl.setVisibility(View.GONE);
                            mainrl.setVisibility(View.VISIBLE);
                            emptycartll.setVisibility(View.GONE);
                            rvliveOrder.setVisibility(View.VISIBLE);
                            bottom.setVisibility(View.VISIBLE);
                            JSONObject jsonObject1 = new JSONObject(Objects.requireNonNull(response.body()).toString());
                            if (jsonObject1.getInt("success") == 1) {
                                SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.Flag,"1").apply();
                                SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.canScan,"no").apply();
                                subtotal = jsonObject1.optString("subtotal");
                                fee = jsonObject1.optString("gst");
                                total = jsonObject1.optString("total");
                                cartModelsarray.clear();
                                JSONArray jsonArray = jsonObject1.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    CartModel cartModel = new CartModel();
                                    cartModel.setItem_name(object.optString("item_name"));
                                    cartModel.setItem_id(object.optString("item_id"));
                                    cartModel.setPrice(object.optString("price"));
                                    cartModel.setQty(object.optString("qty"));
                                    cartModel.setImages(object.optString("image"));
                                    cartModelsarray.add(cartModel);
                                }
                                rvliveOrder.hideShimmerAdapter();
                                liveOrderAdapter = new LiveOrderAdapter(cartModelsarray);
                                rvliveOrder.setAdapter(liveOrderAdapter);
                                tvrCartSubTotal.setText(String.valueOf(subtotal));
                                tvrCartFee.setText(String.valueOf(fee));
                                tvrCartTotal.setText(String.valueOf(total));
                            } else {
                                SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.Flag,"0").apply();
                                SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.canScan,"yes").apply();
                                rvliveOrder.setVisibility(View.GONE);
                                bottom.setVisibility(View.GONE);
                                emptycartll.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        showErrorDialog("Error while getting data");
                        rvliveOrder.setVisibility(View.GONE);
                        bottom.setVisibility(View.GONE);
                        emptycartll.setVisibility(View.VISIBLE);
                    }
                } else {
                    showErrorDialog("Error while getting data");
                    rvliveOrder.setVisibility(View.GONE);
                    bottom.setVisibility(View.GONE);
                    emptycartll.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                rvliveOrder.hideShimmerAdapter();
                rvliveOrder.setVisibility(View.GONE);
                bottom.setVisibility(View.GONE);
                emptycartll.setVisibility(View.VISIBLE);
                mainrl.setVisibility(View.GONE);
                nointernetrl.setVisibility(View.VISIBLE);
            }
        });
    }

    private void CallReorder(String itemId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
        jsonObject.addProperty("auth_token", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Auth_token, Constant.notAvailable));
        jsonObject.addProperty("cafeid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable));
        jsonObject.addProperty("itemid", itemId);

        Service service = ApiCall.getRetrofit().create(Service.class);
        Call<JsonObject> call = service.reorder("application/json", jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject object = new JSONObject(Objects.requireNonNull(response.body()).toString());
                            if (object.optInt("success") == 1) {

                                viewLiveOrder();
                            } else {
                                showErrorDialog(object.optString("msg"));
                            }
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

    private void showErrorDialog(String msg) {
        new AlertDialog.Builder(LiveOrderActivity.this)
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> dialogInterface.dismiss())
                .create().show();
    }
}
