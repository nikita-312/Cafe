package com.conceptioni.cafeapp.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tabassum.shimmerRecyclerView.ShimmerRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {
    ShimmerRecyclerView rvCart;
    TextviewRegular tvrPlaceOrder, tvrCartTotal, tvrCartFee, tvrCartSubTotal, continueordertvr;
    String TotalQty;
    int totalqty = 0;
    int totalprice = 0;
    int subtotal = 0;
    int mTotal = 0;
    double totalGST = 0;
    double gst = 0;
    List<CartData> cartModelsarray = new ArrayList<>();
    List<CartData> cartModelsarraydb = new ArrayList<>();
    ImageView ivBack;
    CartItemAdapter cartItemAdapter;
    RelativeLayout emptycartll, mainrl, nointernetrl;
    LinearLayout bottom, retryll;
    DBOpenHelper dbOpenHelper;
    SQLiteDatabase sqLiteDatabase;
    double total = 0, finaltotal = 0, fee = 0;
    NumberFormat numberFormat;
    ProgressDialog progressDialog;
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
            totalprice = Integer.parseInt(cartModelsarraydb.get(i).getCOLUMN_ORIGINAL_PRICE());
            totalqty = Integer.parseInt(cartModelsarraydb.get(i).getCOLUMN_ITEMS_QUANTITY());
            Log.d("++++gst","++ "+cartModelsarraydb.get(i).getCOLUMN_EXTRA_PRICE());
            if (!cartModelsarraydb.get(i).getCOLUMN_EXTRA_PRICE().equalsIgnoreCase("")) {
                gst = Double.parseDouble(cartModelsarraydb.get(i).getCOLUMN_EXTRA_PRICE());
            }

            subtotal = (totalprice * totalqty) + subtotal;
            finaltotal = subtotal;
            totalGST = finaltotal * gst;
            fee = totalGST / 100;
            total = finaltotal + fee;
            numberFormat = new DecimalFormat("##.##");
            String str = numberFormat.format(total);
            String str1 = numberFormat.format(fee);
            String str2 = numberFormat.format(finaltotal);
            total = Double.parseDouble(str);
            fee = Double.parseDouble(str1);
            finaltotal = Double.parseDouble(str2);
            Log.d("++++gst", "+++ " +"++"+gst +"+++ "+totalGST + "++++" + fee + "+++" + total);
        }

        if (!cartModelsarraydb.isEmpty()) {
            mainrl.setVisibility(View.VISIBLE);
            emptycartll.setVisibility(View.GONE);
            rvCart.setVisibility(View.VISIBLE);
            bottom.setVisibility(View.VISIBLE);
            rvCart.hideShimmerAdapter();
            cartItemAdapter = new CartItemAdapter(cartModelsarraydb);
            rvCart.setAdapter(cartItemAdapter);

            tvrCartSubTotal.setText(String.valueOf(finaltotal));
            tvrCartFee.setText(String.valueOf(fee));
            tvrCartTotal.setText(String.valueOf(total));
        } else {
            emptycartll.setVisibility(View.VISIBLE);
            rvCart.setVisibility(View.GONE);
            bottom.setVisibility(View.GONE);
            rvCart.hideShimmerAdapter();
        }

    }

    private void click() {
        tvrPlaceOrder.setOnClickListener(v -> SendData());
        rvCart.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), rvCart, new RecyclerTouchListener.ClickListener() {
            @SuppressLint("SetTextI18n")
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
                    TotalQty = cartDataList.get(position).getCOLUMN_ITEM_TOTAL_QUANTITY();
                    int totalqty = Integer.parseInt(TotalQty) + 1;
                    TotalQty = String.valueOf(totalqty);
                    int price = Integer.parseInt(cartDataList.get(position).getCOLUMN_ORIGINAL_PRICE());
                    String finalQuantity = String.valueOf(Quantity);
                    mTotal = (price * count);
                    finaltotal = finaltotal + price;
                    totalGST = finaltotal * gst;
                    fee = totalGST / 100;
                    total = finaltotal + fee;
                    boolean isupdate = dbOpenHelper.updatecartdata(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable), cartModelsarraydb.get(position).getCOLUMN_ITEM_ID(), cartModelsarraydb.get(position).getCOLUMN_ITEM_NAME(), cartModelsarraydb.get(position).getCOLUMN_NOTE(), finalQuantity, TotalQty, cartModelsarraydb.get(position).getCOLUMN_ORIGINAL_PRICE(), String.valueOf(fee), String.valueOf(total), cartModelsarraydb.get(position).getCOLUMN_ITEM_DESC(), cartModelsarraydb.get(position).getCOLUMN_ITEM_TYPE(), cartModelsarraydb.get(position).getCOLUMN_IMAGE(), String.valueOf(finaltotal));
                    if (isupdate) {
                        dbOpenHelper.updateAllcartdata(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), TotalQty, String.valueOf(fee), String.valueOf(total), String.valueOf(finaltotal));
                    }
                    numberFormat = new DecimalFormat("##.##");
                    String str = numberFormat.format(total);
                    String str1 = numberFormat.format(fee);
                    String str2 = numberFormat.format(finaltotal);
                    total = Double.parseDouble(str);
                    fee = Double.parseDouble(str1);
                    finaltotal = Double.parseDouble(str2);
                    tvrCartQty.setText(finalQuantity);
                    tvrCartSubTotal.setText(String.valueOf(finaltotal));
                    tvrCartFee.setText(String.valueOf(fee));
                    tvrCartTotal.setText(String.valueOf(total));
                });
                minusiv.setOnClickListener(v -> {
                    minusiv.setClickable(true);
                    List<CartData> cartDataList;
                    cartDataList = dbOpenHelper.getAllCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
                    int count = Integer.parseInt(cartDataList.get(position).getCOLUMN_ITEMS_QUANTITY());
                    int Quantity = count - 1;
                    TotalQty = cartDataList.get(position).getCOLUMN_ITEM_TOTAL_QUANTITY();
                    int totalqty = Integer.parseInt(TotalQty) - 1;
                    TotalQty = String.valueOf(totalqty);
                    int price = Integer.parseInt(cartDataList.get(position).getCOLUMN_ORIGINAL_PRICE());
                    String finalQuantity = String.valueOf(Quantity);
                    if (Quantity >= 0) {

                        mTotal = (price * count);
                        finaltotal = finaltotal - price;
                        totalGST = finaltotal * gst;
                        fee = totalGST / 100;
                        total = finaltotal + fee;
                        boolean isupdate = dbOpenHelper.updatecartdata(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable), cartModelsarraydb.get(position).getCOLUMN_ITEM_ID(), cartModelsarraydb.get(position).getCOLUMN_ITEM_NAME(), cartModelsarraydb.get(position).getCOLUMN_NOTE(), finalQuantity, TotalQty, cartModelsarraydb.get(position).getCOLUMN_ORIGINAL_PRICE(), String.valueOf(fee), String.valueOf(total), cartModelsarraydb.get(position).getCOLUMN_ITEM_DESC(), cartModelsarraydb.get(position).getCOLUMN_ITEM_TYPE(), cartModelsarraydb.get(position).getCOLUMN_IMAGE(), String.valueOf(finaltotal));
                        if (isupdate) {
                            dbOpenHelper.updateAllcartdata(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), TotalQty, String.valueOf(fee), String.valueOf(total), String.valueOf(finaltotal));
                        }
                        numberFormat = new DecimalFormat("##.##");
                        String str = numberFormat.format(total);
                        String str1 = numberFormat.format(fee);
                        String str2 = numberFormat.format(finaltotal);
                        total = Double.parseDouble(str);
                        fee = Double.parseDouble(str1);
                        finaltotal = Double.parseDouble(str2);
                        tvrCartQty.setText(finalQuantity);
                        tvrCartSubTotal.setText(String.valueOf(finaltotal));
                        tvrCartFee.setText(String.valueOf(fee));
                        tvrCartTotal.setText(String.valueOf(total));
                    } else {
                        minusiv.setClickable(false);
                    }
                    if (finalQuantity.equalsIgnoreCase("0")) {
                        Integer deleteRow = dbOpenHelper.deleterow(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), cartModelsarraydb.get(position).getCOLUMN_ITEM_ID());
                        if (deleteRow > 0)
                            dbOpenHelper.updateAllcartdata(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), TotalQty, String.valueOf(fee), String.valueOf(total), String.valueOf(finaltotal));
                        cartModelsarraydb.remove(position);
                        cartItemAdapter.notifyDataSetChanged();
                        if (cartModelsarraydb.isEmpty()) {
                            emptycartll.setVisibility(View.VISIBLE);
                            rvCart.setVisibility(View.GONE);
                            bottom.setVisibility(View.GONE);
                        }
                    }
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
                    tvrCartFee.setText(String.valueOf(fee));
                    tvrCartTotal.setText(String.valueOf(total));
                } else {
                    emptycartll.setVisibility(View.VISIBLE);
                    rvCart.setVisibility(View.GONE);
                    bottom.setVisibility(View.GONE);
                    rvCart.hideShimmerAdapter();
                }
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
                        String quant = "", amt = "",totalQTY="";
                        int lastQty=0;
                        List<CartData> cartDataList = dbOpenHelper.getCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), ItemId);
                        Log.d("+++++++++id","+++++"+ItemId);
                        Integer deleteRow = dbOpenHelper.deleterow(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), ItemId);
                        if (deleteRow > 0) {
                            for (int j = 0; j < cartDataList.size(); j++) {
                                quant = cartDataList.get(j).getCOLUMN_ITEMS_QUANTITY();
                                amt = cartDataList.get(j).getCOLUMN_ORIGINAL_PRICE();
                                totalQTY = cartDataList.get(j).getCOLUMN_ITEM_TOTAL_QUANTITY();
                            }
                            lastQty = Integer.parseInt(totalQTY) - Integer.parseInt(quant);
                            TotalQty = String.valueOf(lastQty);
                            Log.d("+++totalQ","++++ "+TotalQty);
                            int deduct = Integer.parseInt(quant) * Integer.parseInt(amt);
                            finaltotal = finaltotal - deduct;
                            totalGST = finaltotal * gst;
                            fee = totalGST / 100;
                            total = finaltotal + fee;
                            Log.d("++++remove", "++ " + deduct + "+++" + finaltotal);
                            numberFormat = new DecimalFormat("##.##");
                            String str = numberFormat.format(total);
                            String str1 = numberFormat.format(fee);
                            String str2 = numberFormat.format(finaltotal);
                            total = Double.parseDouble(str);
                            fee = Double.parseDouble(str1);
                            finaltotal = Double.parseDouble(str2);
                            tvrCartSubTotal.setText(String.valueOf(finaltotal));
                            tvrCartFee.setText(String.valueOf(fee));
                            tvrCartTotal.setText(String.valueOf(total));
                            dbOpenHelper.updateAllcartdata(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable), cartModelsarraydb.get(pos).getCOLUMN_ITEM_TOTAL_QUANTITY(), String.valueOf(fee), String.valueOf(total), String.valueOf(finaltotal));
                            cartModelsarraydb.remove(pos);
                            cartItemAdapter.notifyDataSetChanged();

                        }
                        if (cartModelsarraydb.isEmpty()) {
                            emptycartll.setVisibility(View.VISIBLE);
                            rvCart.setVisibility(View.GONE);
                            bottom.setVisibility(View.GONE);
                        }
                    }
                })
                .create().show();
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
                            progressDialog.dismiss();
                            dbOpenHelper.deletetable();

                            SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.Flag, "1").apply();
                            SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.canScan, "no").apply();
                            startActivity(new Intent(CartActivity.this, ThankYouActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            finish();
                        } else {

                            progressDialog.dismiss();
                            showErrorDialog(object.optString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                } else {
                    progressDialog.dismiss();
                    showErrorDialog("Try after some time....");
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {

            }
        });
    }

    private void SendData() {

        List<CartData> cartDataArrayList;
        cartDataArrayList = dbOpenHelper.getAllCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id,Constant.notAvailable));
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();

        if (!cartDataArrayList.isEmpty()) {
            for (int i = 0; i < cartDataArrayList.size(); i++) {
                JsonObject cartdataoject = new JsonObject();
                cartdataoject.addProperty("itemid", cartDataArrayList.get(i).getCOLUMN_ITEM_ID());
                cartdataoject.addProperty("qty", cartDataArrayList.get(i).getCOLUMN_ITEMS_QUANTITY());
                cartdataoject.addProperty("price", cartDataArrayList.get(i).getCOLUMN_ORIGINAL_PRICE());
                cartdataoject.addProperty("note", cartDataArrayList.get(i).getCOLUMN_NOTE());
                jsonArray.add(cartdataoject);
            }

            data.addProperty("userid",SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id,Constant.notAvailable));
            data.addProperty("cafeid",SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id,Constant.notAvailable));
            data.addProperty("gst",SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.gst,Constant.notAvailable));
            data.addProperty("authid",SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Auth_token,Constant.notAvailable));
            data.add("items",jsonArray);

            String jsonFormattedString = data.toString().replaceAll("\\\\", "");
            Log.d("+++++data","++++"+data);
            Addtocart(data);

        }else {
        }

    }
    private void Addtocart(JsonObject jsonObject) {
        progressDialog = new ProgressDialog(CartActivity.this);
        progressDialog.setMessage("Your order is in progress...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Service service = ApiCall.getRetrofit().create(Service.class);
        Call<JsonObject> call = service.addtocartall("application/json", jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject object = new JSONObject(Objects.requireNonNull(response.body()).toString());
                            if (object.optInt("success") == 1) {
                               placeOrder();
                            } else {
                                object.optString("delete");
                                String deltedId = object.optString("items");
                                if (object.optString("delete").equalsIgnoreCase("yes")){
                                    List<String> items = Arrays.asList(deltedId.split("\\s*,\\s*"));
                                    Log.d("++++items","+++ "+items);
                                    showDeleteDialog("Want to remove deleted items?",items);
                                }else {
                                    showErrorDialog(object.optString("msg"));

                                }
                                progressDialog.hide();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.hide();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                new MakeToast(R.string.Checkyournetwork);
            }
        });
    }
    private void showErrorDialog(String msg) {
        new AlertDialog.Builder(CartActivity.this)
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
    }

    private void showDeleteDialog(String msg,List<String> items) {
        new AlertDialog.Builder(CartActivity.this)
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        List<CartData> cartDataList1 = new ArrayList<>();
                        List<String> idStringList = new ArrayList<>();
                        cartDataList1 = dbOpenHelper.getAllCartData(SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id,Constant.notAvailable));
                        if (!cartDataList1.isEmpty()){
                            idStringList.clear();
                            for (int l = 0; l <cartDataList1.size() ; l++) {
                                for (int k = 0; k <items.size() ; k++) {
                                    if (cartDataList1.get(l).getCOLUMN_ITEM_ID().equalsIgnoreCase(items.get(k))){
//                                        idStringList.add(items.get(k));
                                        Log.d("+++++id","++++"+items.get(k));
                                        showDeleteAlert(k,items.get(k));

                                    }
                                }
                            }
                            dialogInterface.dismiss();
                        }


                    }
                })
                .create().show();
    }
}
