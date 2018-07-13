package com.conceptioni.cafeapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
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
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

public class LiveOrderActivity extends AppCompatActivity {
    private static final String SHOWCASE_ID = "simple example";
    ShimmerRecyclerView rvliveOrder;
    LinearLayout llBottom, bottom, retryll;
    TextviewRegular tvrCartTotal, tvrCartFee, tvrCartSubTotal, continuetvr, paymenttvr;
    String subtotal, total, fee;
    List<CartModel> cartModelsarray = new ArrayList<>();
    ImageView ivBack, reorderiv;
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
        continuetvr.setOnClickListener(v -> {
            startActivity(new Intent(LiveOrderActivity.this, MenuActivity.class));
            finish();
        });
        ivBack.setOnClickListener(v -> finish());
        retryll.setOnClickListener(v -> viewLiveOrder());

        reorderiv.setOnClickListener(v -> CallReorder());
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
        reorderiv = findViewById(R.id.reorderiv);
        mainrl = findViewById(R.id.mainrl);
        nointernetrl = findViewById(R.id.nointernetrl);
        retryll = findViewById(R.id.retryll);

        SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.Flag,"1").apply();

        presentShowcaseView();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LiveOrderActivity.this);
        rvliveOrder.setLayoutManager(linearLayoutManager);
        rvliveOrder.showShimmerAdapter();
        viewLiveOrder();
    }

    private void presentShowcaseView() {
        new MaterialShowcaseView.Builder(this)
                .setMaskColour(Color.argb(150, 0, 0, 0))
                .setTarget(reorderiv)
                .setTitleText("Re-Order")
                .setDismissText("GOT IT")
                .setContentText("want to re-order?")
                .setDelay(1000) // optional but starting animations immediately in onCreate can make them choppy
                .singleUse(SHOWCASE_ID) // provide a unique ID used to ensure it is only shown once
                .useFadeAnimation() // remove comment if you want to use fade animations for Lollipop & up
                .setShapePadding(20)
                .show();
    }

    public void viewLiveOrder() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
        jsonObject.addProperty("auth_token", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Auth_token, Constant.notAvailable));
        jsonObject.addProperty("cafeid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable));
        Log.d("+++++++obiect", "++++" + jsonObject.toString());

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
                                cartModelsarray.clear();
                                subtotal = String.valueOf(jsonObject1.optInt("subtotal"));
                                fee = String.valueOf(jsonObject1.optInt("fee"));
                                total = String.valueOf(jsonObject1.optInt("total"));

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
                                tvrCartSubTotal.setText(subtotal);
                                tvrCartFee.setText(fee);
                                tvrCartTotal.setText(total);
                            } else {
                                rvliveOrder.setVisibility(View.GONE);
                                bottom.setVisibility(View.GONE);
                                emptycartll.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    } else {
                        new MakeToast("Error while getting data");
                        rvliveOrder.setVisibility(View.GONE);
                        bottom.setVisibility(View.GONE);
                        emptycartll.setVisibility(View.VISIBLE);
                    }
                } else {
                    new MakeToast("Error while getting result");
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

    private void CallReorder() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
        jsonObject.addProperty("auth_token", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Auth_token, Constant.notAvailable));
        jsonObject.addProperty("cafeid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable));
        Log.d("+++++++object", "++++" + jsonObject.toString());

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
                                new MakeToast(object.optString("msg"));
                                viewLiveOrder();
                            } else {
                                new MakeToast(object.optString("msg"));
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
}
