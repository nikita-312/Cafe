package com.conceptioni.cafeapp.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.activity.retrofitinterface.Service;
import com.conceptioni.cafeapp.adapter.CartItemAdapter;
import com.conceptioni.cafeapp.database.DBOpenHelper;
import com.conceptioni.cafeapp.model.CartData;
import com.conceptioni.cafeapp.utils.Constant;
import com.conceptioni.cafeapp.utils.MakeToast;
import com.conceptioni.cafeapp.utils.RecyclerTouchListener;
import com.conceptioni.cafeapp.utils.SharedPrefs;
import com.conceptioni.cafeapp.utils.TextviewRegular;
import com.google.gson.JsonObject;
import com.tabassum.shimmerRecyclerView.ShimmerRecyclerView;

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
    TextviewRegular tvrPlaceOrder, tvrCartTotal, tvrCartFee, tvrCartSubTotal, continueordertvr;
    String subtotal, total, fee,finaltotal ;
    int totalqty=0, totalprice=0;
    List<CartData> cartModelsarray = new ArrayList<>();
    List<CartData> cartModelsarraydb = new ArrayList<>();
    ImageView ivBack;
    CartItemAdapter cartItemAdapter;
    RelativeLayout emptycartll, mainrl, nointernetrl;
    LinearLayout bottom, retryll;
    DBOpenHelper dbOpenHelper;
    SQLiteDatabase sqLiteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        dbOpenHelper = new DBOpenHelper(CartActivity.this);
        sqLiteDatabase = dbOpenHelper.getWritableDatabase();
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

        cartModelsarraydb = dbOpenHelper.getAllCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
        for (int i = 0; i < cartModelsarraydb.size(); i++) {
            Log.d("++++array","+++ "+cartModelsarraydb.size());
            totalprice = 1;
            totalqty = 1;
            Log.d("++++total","+++ "+totalprice+"+++ "+totalqty);
            subtotal += String.valueOf(totalprice * totalqty);
          //  finaltotal = subtotal;
        }

        if (!cartModelsarraydb.isEmpty()) {
            mainrl.setVisibility(View.VISIBLE);
            emptycartll.setVisibility(View.GONE);
            rvCart.setVisibility(View.VISIBLE);
            bottom.setVisibility(View.VISIBLE);
            rvCart.hideShimmerAdapter();
            cartItemAdapter = new CartItemAdapter(cartModelsarraydb);
            rvCart.setAdapter(cartItemAdapter);
            tvrCartSubTotal.setText(subtotal);
            tvrCartFee.setText(fee);
            tvrCartTotal.setText(total);
        }else {
            emptycartll.setVisibility(View.VISIBLE);
            rvCart.setVisibility(View.GONE);
            bottom.setVisibility(View.GONE);
            rvCart.hideShimmerAdapter();
        }
        // ViewCart();
    }
    private void click() {
        tvrPlaceOrder.setOnClickListener(v -> placeOrder());
        rvCart.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), rvCart, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ImageView ivRemove = view.findViewById(R.id.ivRemove);
                ivRemove.setOnClickListener(v -> showDeleteAlert(position, cartModelsarraydb.get(position).getCOLUMN_ITEM_ID()));
                ImageView plusiv = view.findViewById(R.id.plusiv);
                ImageView minusiv = view.findViewById(R.id.minusiv);
                TextviewRegular tvrCartQty = view.findViewById(R.id.tvrCartQty);
                plusiv.setOnClickListener(v -> {
                    List<CartData> cartDataList;
                    cartDataList = dbOpenHelper.getAllCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
                    int count = Integer.parseInt(cartDataList.get(position).getCOLUMN_ITEMS_QUANTITY());
                    int Quantity = count + 1;
                    String finalQuantity = String.valueOf(Quantity);
                    Log.d("+++++finalQuantity","++ "+finalQuantity+"++count "+count);
                    dbOpenHelper.updatecartdata(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable), cartModelsarraydb.get(position).getCOLUMN_ITEM_ID(), cartModelsarraydb.get(position).getCOLUMN_ITEM_NAME(), "", finalQuantity, cartModelsarraydb.get(position).getCOLUMN_ITEM_TOTAL_QUANTITY(), cartModelsarraydb.get(position).getCOLUMN_ORIGINAL_PRICE(), fee, total,"","","");
                    tvrCartQty.setText(finalQuantity);

                    subtotal = String.valueOf(Integer.parseInt(subtotal) * Quantity);
                    tvrCartSubTotal.setText(subtotal);
                    //CallQuantity(tvrCartQty,finalQuantity,position, cartModelsarray.get(position).getItem_id(),cartModelsarray.get(position).getItem_name());
                });
                minusiv.setOnClickListener(v -> {
                    minusiv.setClickable(true);
                    //  if (!cartModelsarraydb.get(position).getCOLUMN_ITEMS_QUANTITY().equalsIgnoreCase("0")) {
                        List<CartData> cartDataList;
                        cartDataList = dbOpenHelper.getAllCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
                        int count = Integer.parseInt(cartDataList.get(position).getCOLUMN_ITEMS_QUANTITY());
                        int Quantity = count - 1;
                        String finalQuantity = String.valueOf(Quantity);
                        Log.d("+++++finalQuantity","-- "+finalQuantity);
                        if (Quantity >= 0) {
                            dbOpenHelper.updatecartdata(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable), cartModelsarraydb.get(position).getCOLUMN_ITEM_ID(), cartModelsarraydb.get(position).getCOLUMN_ITEM_NAME(), "", finalQuantity, cartModelsarraydb.get(position).getCOLUMN_ITEM_TOTAL_QUANTITY(), cartModelsarraydb.get(position).getCOLUMN_ORIGINAL_PRICE(), fee, total,"","","");
                            tvrCartQty.setText(finalQuantity);
                        }else{
                            minusiv.setClickable(false);
                        }

                        if (finalQuantity.equalsIgnoreCase("0")) {
                            Integer deleteRow = dbOpenHelper.deleterow(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), cartModelsarraydb.get(position).getCOLUMN_ITEM_ID());
                            if (deleteRow > 0)
                                dbOpenHelper.updateAllcartdata(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), cartModelsarraydb.get(position).getCOLUMN_ITEM_TOTAL_QUANTITY(), fee, total);
                                cartModelsarraydb.remove(position);
                                cartItemAdapter.notifyDataSetChanged();
                            if (cartModelsarraydb.isEmpty()) {
                                emptycartll.setVisibility(View.VISIBLE);
                                rvCart.setVisibility(View.GONE);
                                bottom.setVisibility(View.GONE);
                            }
                        }
                        //  CallQuantity(tvrCartQty, finalQuantity, position, cartModelsarray.get(position).getItem_id(),cartModelsarray.get(position).getItem_name());
//                    } else {
//                        new MakeToast("Quantity can not be less than 0");
//                    }
                });
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        ivBack.setOnClickListener(v -> finish());

        continueordertvr.setOnClickListener(v -> {
            startActivity(new Intent(CartActivity.this, MenuActivity.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
            finish();
        });
        retryll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartModelsarraydb = dbOpenHelper.getAllCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
                if (!cartModelsarraydb.isEmpty()) {
                    mainrl.setVisibility(View.VISIBLE);
                    emptycartll.setVisibility(View.GONE);
                    rvCart.setVisibility(View.VISIBLE);
                    bottom.setVisibility(View.VISIBLE);
                    rvCart.hideShimmerAdapter();
                    cartItemAdapter = new CartItemAdapter(cartModelsarraydb);
                    rvCart.setAdapter(cartItemAdapter);
                    tvrCartSubTotal.setText(subtotal);
                    tvrCartFee.setText(fee);
                    tvrCartTotal.setText(total);
                }else{
                    emptycartll.setVisibility(View.VISIBLE);
                    rvCart.setVisibility(View.GONE);
                    bottom.setVisibility(View.GONE);
                    rvCart.hideShimmerAdapter();
                }
                //ViewCart()
            }
        });
    }

    private void showDeleteAlert(final int pos, String ItemId) {
        new AlertDialog.Builder(CartActivity.this)
                .setTitle("Remove?")
                .setMessage("Are you sure want to remove the product?")
                .setCancelable(true)
                .setNegativeButton(android.R.string.no, (dialog, which) -> dialog.dismiss())
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Integer deleteRow = dbOpenHelper.deleterow(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), ItemId);
                        if (deleteRow > 0)
                            dbOpenHelper.updateAllcartdata(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), cartModelsarraydb.get(pos).getCOLUMN_ITEM_TOTAL_QUANTITY(), fee, total);
                        cartModelsarraydb.remove(pos);
                        cartItemAdapter.notifyDataSetChanged();
                        if (cartModelsarraydb.isEmpty()) {
                            emptycartll.setVisibility(View.VISIBLE);
                            rvCart.setVisibility(View.GONE);
                            bottom.setVisibility(View.GONE);
                        }
                    }
                })
                .create().show();
    }
//    public void ViewCart() {
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("userid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
//        jsonObject.addProperty("auth_token", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Auth_token, Constant.notAvailable));
//        jsonObject.addProperty("cafeid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable));
//
//        Service service = ApiCall.getRetrofit().create(Service.class);
//        Call<JsonObject> call = service.viewCart("application/json", jsonObject);
//
//        call.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
//                if (response.body() != null) {
//                    if (response.isSuccessful()) {
//                        try {
//                            nointernetrl.setVisibility(View.GONE);
//                            mainrl.setVisibility(View.VISIBLE);
//                            emptycartll.setVisibility(View.GONE);
//                            rvCart.setVisibility(View.VISIBLE);
//                            bottom.setVisibility(View.VISIBLE);
//                            JSONObject jsonObject1 = new JSONObject(Objects.requireNonNull(response.body()).toString());
//                            if (jsonObject1.getInt("success") == 1) {
//                                subtotal = String.valueOf(jsonObject1.optInt("subtotal"));
//                                fee = String.valueOf(jsonObject1.optInt("fee"));
//                                total = String.valueOf(jsonObject1.optInt("total"));
//                                cartModelsarray.clear();
//                                JSONArray jsonArray = jsonObject1.getJSONArray("data");
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    JSONObject object = jsonArray.getJSONObject(i);
//                                    CartModel cartModel = new CartModel();
//                                    cartModel.setItem_name(object.getString("item_name"));
//                                    cartModel.setItem_id(object.getString("item_id"));
//                                    cartModel.setPrice(object.getString("price"));
//                                    cartModel.setQty(object.getString("qty"));
//                                    cartModel.setImages(object.optString("image"));
//                                    cartModelsarray.add(cartModel);
//                                }
//                                rvCart.hideShimmerAdapter();
//                                cartItemAdapter = new CartItemAdapter(cartModelsarray);
//                                rvCart.setAdapter(cartItemAdapter);
//                                tvrCartSubTotal.setText(subtotal);
//                                tvrCartFee.setText(fee);
//                                tvrCartTotal.setText(total);
//
////                                updatedatabase(cartModelsarray,subtotal,fee,total);
//
//                            } else {
//                                rvCart.setVisibility(View.GONE);
//                                bottom.setVisibility(View.GONE);
//                                emptycartll.setVisibility(View.VISIBLE);
//                            }
//
//                            SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.Flag,"0").apply();
//                            SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.canScan,"yes").apply();
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//
//                        }
//                    } else {
//                        new MakeToast("Error while getting data");
//                        rvCart.setVisibility(View.GONE);
//                        bottom.setVisibility(View.GONE);
//                        emptycartll.setVisibility(View.VISIBLE);
//                    }
//                } else {
//                    new MakeToast("Error while getting result");
//                    rvCart.setVisibility(View.GONE);
//                    bottom.setVisibility(View.GONE);
//                    emptycartll.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
//                rvCart.hideShimmerAdapter();
//                rvCart.setVisibility(View.GONE);
//                bottom.setVisibility(View.GONE);
//                emptycartll.setVisibility(View.VISIBLE);
//                mainrl.setVisibility(View.GONE);
//                nointernetrl.setVisibility(View.VISIBLE);
//
//            }
//        });
//    }

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
                                String totalqty = String.valueOf(object.optInt("totalqty"));
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

                             Integer deleteRow = dbOpenHelper.deleterow(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), ItemId);
                             if (deleteRow > 0)
                                dbOpenHelper.updateAllcartdata(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), totalqty, fee, total);
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
                if (response.body() != null) {
                    try {
                        JSONObject object = new JSONObject(Objects.requireNonNull(response.body()).toString());
                        if (object.optInt("success") == 1) {
                            new MakeToast(object.optString("msg"));
                            SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.Flag, "1").apply();
                            SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.canScan, "no").apply();
                            startActivity(new Intent(CartActivity.this, ThankYouActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            finish();
                        } else {
                            new MakeToast(object.optString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    new MakeToast("Try after some time....");
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {

            }
        });
    }

//    private void CallQuantity(TextviewRegular tvrCartQty,String Quantity, int Position, String ItemId,String ItemName) {
//
//        JsonObject object = new JsonObject();
//        object.addProperty("userid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
//        object.addProperty("auth_token", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Auth_token, Constant.notAvailable));
//        object.addProperty("itemid", ItemId);
//        object.addProperty("qty", Quantity);
//        object.addProperty("note", "");
//        object.addProperty("cafeid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable));
//
//        Service service = ApiCall.getRetrofit().create(Service.class);
//        Call<JsonObject> call = service.AddToCart("application/json", object);
//
//        call.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
//                if (response.body() != null) {
//                    try {
//                        JSONObject object1 = new JSONObject(String.valueOf(response.body()));
//                        if (object1.optInt("success") == 1) {
//                            cartModelsarray.get(Position).setQty(object1.optString("qty"));
//                            tvrCartQty.setText(object1.optString("qty"));
//                            subtotal = String.valueOf(object1.optInt("subtotal"));
//                            fee = String.valueOf(object1.optInt("fee"));
//                            total = String.valueOf(object1.optInt("total"));
//                            String totalqty = String.valueOf(object1.optInt("totalqty"));
//                            tvrCartSubTotal.setText(subtotal);
//                            tvrCartFee.setText(fee);
//                            tvrCartTotal.setText(total);
//
//                            dbOpenHelper.updatecartdata(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id,Constant.notAvailable),SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id,Constant.notAvailable),ItemId,ItemName,"",Quantity,totalqty,"",fee,total);
//
//                        } else {
//                            if (Quantity.equalsIgnoreCase("0")) {
//                                new MakeToast(object1.optString("msg"));
//                            } else {
//                                int Quan = Integer.parseInt(Quantity);
//                                tvrCartQty.setText(Quan - 1);
//                                new MakeToast(object1.optString("msg"));
//                            }
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
//                new MakeToast(R.string.Checkyournetwork);
//            }
//        });
//
//    }

}
