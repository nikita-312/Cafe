package com.conceptioni.cafeapp.activity;

import android.annotation.SuppressLint;
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
import com.conceptioni.cafeapp.adapter.CartItemAdapter;
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

public class CartActivity extends AppCompatActivity {
    ShimmerRecyclerView rvCart;
    TextviewRegular tvrPlaceOrder, tvrCartTotal, tvrCartFee, tvrCartSubTotal,continueordertvr;
    String subtotal, total, fee;
    List<CartModel> cartModelsarray = new ArrayList<>();
    ImageView ivBack;
    CartItemAdapter cartItemAdapter;
    RelativeLayout emptycartll,mainrl,nointernetrl;
    LinearLayout bottom,retryll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        init();
        click();
    }

    private void init() {
        rvCart = findViewById(R.id.rvCart);
        tvrPlaceOrder = findViewById(R.id.tvrPlaceOrder);
        tvrCartTotal = findViewById(R.id.tvrCartTotal);
        tvrCartFee = findViewById(R.id.tvrCartFee);
        tvrCartSubTotal = findViewById(R.id.tvrCartSubTotal);
        emptycartll = findViewById(R.id.emptycartll);
        bottom = findViewById(R.id.bottom);
        ivBack = findViewById(R.id.ivBack);
        continueordertvr = findViewById(R.id.continueordertvr);
        mainrl = findViewById(R.id.mainrl);
        nointernetrl = findViewById(R.id.nointernetrl);
        retryll = findViewById(R.id.retryll);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CartActivity.this);
        rvCart.setLayoutManager(linearLayoutManager);
        rvCart.showShimmerAdapter();

        ViewCart();
    }

    private void click() {
        tvrPlaceOrder.setOnClickListener(v -> placeOrder());
        rvCart.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), rvCart, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ImageView ivRemove = view.findViewById(R.id.ivRemove);
                ivRemove.setOnClickListener(v -> showDeleteAlert(position, cartModelsarray.get(position).getItem_id()));
                ImageView plusiv = view.findViewById(R.id.plusiv);
                ImageView minusiv = view.findViewById(R.id.minusiv);
                TextviewRegular tvrCartQty = view.findViewById(R.id.tvrCartQty);
                plusiv.setOnClickListener(v -> {
                    int count = Integer.parseInt(cartModelsarray.get(position).getQty());
                    int Quantity = count + 1;
                    String finalQuantity = String.valueOf(Quantity);
                    CallQuantity(tvrCartQty,finalQuantity,position, cartModelsarray.get(position).getItem_id());
                });
                minusiv.setOnClickListener(v -> {
                    if (!cartModelsarray.get(position).getQty().equalsIgnoreCase("0")) {
                        int count = Integer.parseInt(cartModelsarray.get(position).getQty());
                        int Quantity = count - 1;
                        String finalQuantity = String.valueOf(Quantity);
                        CallQuantity(tvrCartQty, finalQuantity, position, cartModelsarray.get(position).getItem_id());
                    } else {
                        new MakeToast("Quantity can not be less than 0");
                    }

                });
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        ivBack.setOnClickListener(v -> finish());

        continueordertvr.setOnClickListener(v -> {
            startActivity(new Intent(CartActivity.this,MenuActivity.class));
            finish();
        });
        retryll.setOnClickListener(v -> ViewCart());
    }

    private void showDeleteAlert(final int pos, String ItemId) {
        new AlertDialog.Builder(CartActivity.this)
                .setTitle("Remove?")
                .setMessage("Are you sure want to remove the product?")
                .setCancelable(true)
                .setNegativeButton(android.R.string.no, (dialog, which) -> dialog.dismiss())
                .setPositiveButton(android.R.string.yes, (arg0, arg1) -> removeCart(pos, ItemId))
                .create().show();
    }

    public void ViewCart() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
        jsonObject.addProperty("auth_token", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Auth_token, Constant.notAvailable));
        jsonObject.addProperty("cafeid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable));

        Service service = ApiCall.getRetrofit().create(Service.class);
        Call<JsonObject> call = service.viewCart("application/json", jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        try {
                            nointernetrl.setVisibility(View.GONE);
                            mainrl.setVisibility(View.VISIBLE);
                            emptycartll.setVisibility(View.GONE);
                            rvCart.setVisibility(View.VISIBLE);
                            bottom.setVisibility(View.VISIBLE);
                            JSONObject jsonObject1 = new JSONObject(Objects.requireNonNull(response.body()).toString());
                            if (jsonObject1.getInt("success") == 1) {
                                subtotal = String.valueOf(jsonObject1.optInt("subtotal"));
                                fee = String.valueOf(jsonObject1.optInt("fee"));
                                total = String.valueOf(jsonObject1.optInt("total"));
                                cartModelsarray.clear();
                                JSONArray jsonArray = jsonObject1.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    CartModel cartModel = new CartModel();
                                    cartModel.setItem_name(object.getString("item_name"));
                                    cartModel.setItem_id(object.getString("item_id"));
                                    cartModel.setPrice(object.getString("price"));
                                    cartModel.setQty(object.getString("qty"));
                                    cartModel.setImages(object.optString("image"));
                                    cartModelsarray.add(cartModel);
                                }
                                rvCart.hideShimmerAdapter();
                                cartItemAdapter = new CartItemAdapter(cartModelsarray);
                                rvCart.setAdapter(cartItemAdapter);
                                tvrCartSubTotal.setText(subtotal);
                                tvrCartFee.setText(fee);
                                tvrCartTotal.setText(total);
                            } else {
                                rvCart.setVisibility(View.GONE);
                                bottom.setVisibility(View.GONE);
                                emptycartll.setVisibility(View.VISIBLE);
                            }

                            SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.Flag,"0").apply();
                            SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.canScan,"yes").apply();

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    } else {
                        new MakeToast("Error while getting data");
                        rvCart.setVisibility(View.GONE);
                        bottom.setVisibility(View.GONE);
                        emptycartll.setVisibility(View.VISIBLE);
                    }
                } else {
                    new MakeToast("Error while getting result");
                    rvCart.setVisibility(View.GONE);
                    bottom.setVisibility(View.GONE);
                    emptycartll.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                rvCart.hideShimmerAdapter();
                rvCart.setVisibility(View.GONE);
                bottom.setVisibility(View.GONE);
                emptycartll.setVisibility(View.VISIBLE);
                mainrl.setVisibility(View.GONE);
                nointernetrl.setVisibility(View.VISIBLE);

            }
        });
    }

    public void removeCart(final int pos, String ItemId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("itemid", ItemId);
        jsonObject.addProperty("userid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
        jsonObject.addProperty("auth_token", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Auth_token, Constant.notAvailable));

        Service service = ApiCall.getRetrofit().create(Service.class);
        Call<JsonObject> call = service.removeCart("application/json", jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject object = new JSONObject(Objects.requireNonNull(response.body()).toString());
                            if (object.optInt("success") == 1) {
                                new MakeToast(object.optString("msg"));
                                subtotal = String.valueOf(object.optInt("subtotal"));
                                total = String.valueOf(object.optInt("total"));
                                fee = String.valueOf(object.optInt("fee"));
                                cartModelsarray.remove(pos);
                                cartItemAdapter.notifyDataSetChanged();
                                if (cartModelsarray.size() == 0) {
                                    rvCart.setVisibility(View.GONE);
                                    bottom.setVisibility(View.GONE);
                                    emptycartll.setVisibility(View.VISIBLE);
                                }
                                tvrCartSubTotal.setText(subtotal);
                                tvrCartFee.setText(fee);
                                tvrCartTotal.setText(total);
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
                new MakeToast("Error while removing item");
            }
        });

    }

    public void placeOrder() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
        jsonObject.addProperty("auth_token", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Auth_token, Constant.notAvailable));
        jsonObject.addProperty("cafeid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable));
        Service service = ApiCall.getRetrofit().create(Service.class);
        Call<JsonObject> call = service.placeOrder("application/json", jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null){
                    try {
                        JSONObject object = new JSONObject(Objects.requireNonNull(response.body()).toString());
                        if (object.optInt("success") == 1){
                            new MakeToast(object.optString("msg"));
                            SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.Flag,"1").apply();
                            SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.canScan,"no").apply();
                            startActivity(new Intent(CartActivity.this, ThankYouActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            finish();
                        }else {
                            new MakeToast(object.optString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    new MakeToast("Try after some time....");
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {

            }
        });
    }

    private void CallQuantity(TextviewRegular tvrCartQty,String Quantity, int Position, String ItemId) {

        JsonObject object = new JsonObject();
        object.addProperty("userid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
        object.addProperty("auth_token", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Auth_token, Constant.notAvailable));
        object.addProperty("itemid", ItemId);
        object.addProperty("qty", Quantity);
        object.addProperty("note", "");
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
                            cartModelsarray.get(Position).setQty(object1.optString("qty"));
                            tvrCartQty.setText(object1.optString("qty"));
                            subtotal = object1.optString("subtotal");
                            fee = object1.optString("fee");
                            total = object1.optString("total");

                            tvrCartSubTotal.setText(subtotal);
                            tvrCartFee.setText(fee);
                            tvrCartTotal.setText(total);
                        } else {
                            if (Quantity.equalsIgnoreCase("0")) {
                                new MakeToast(object1.optString("msg"));
                            } else {
                                int Quan = Integer.parseInt(Quantity);
                                tvrCartQty.setText(Quan - 1);
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

}
