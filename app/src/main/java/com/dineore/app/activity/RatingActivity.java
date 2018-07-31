package com.dineore.app.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.dineore.app.R;
import com.dineore.app.activity.retrofitinterface.Service;
import com.dineore.app.adapter.RatingAdapter;
import com.dineore.app.database.DBOpenHelper;
import com.dineore.app.model.CurrentOrderModel;
import com.dineore.app.utils.Constant;
import com.dineore.app.utils.MakeToast;
import com.dineore.app.utils.SharedPrefs;
import com.dineore.app.utils.TextviewRegular;
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

public class RatingActivity extends AppCompatActivity {
    TextviewRegular tvrSubmit;
    LinearLayoutManager linearLayoutManager;
    RatingAdapter ratingAdapter;
    RecyclerView rvRating;
    ImageView ivSkip;
    List<CurrentOrderModel> currentOrderModelsArray = new ArrayList<>();
    RelativeLayout mainrl, nointernetrl;
    LinearLayout retryll;
    ProgressBar progress;
    DBOpenHelper dbOpenHelper;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        dbOpenHelper = new DBOpenHelper(RatingActivity.this);
        sqLiteDatabase = dbOpenHelper.getWritableDatabase();
        init();
        clicks();
    }

    private void clicks() {
        tvrSubmit.setOnClickListener(v -> ScanCafe());
        ivSkip.setOnClickListener(v -> ScanCafe());
        retryll.setOnClickListener(v -> CallReviewCurrentOrder());
    }

    private void init() {
        ivSkip = findViewById(R.id.ivSkip);
        tvrSubmit = findViewById(R.id.tvrSubmit);
        rvRating = findViewById(R.id.rvRating);
        nointernetrl = findViewById(R.id.nointernetrl);
        mainrl = findViewById(R.id.mainrl);
        retryll = findViewById(R.id.retryll);
        progress = findViewById(R.id.progress);

        linearLayoutManager = new LinearLayoutManager(RatingActivity.this);
        rvRating.setLayoutManager(linearLayoutManager);
        CallReviewCurrentOrder();
    }

    /*api for show list of item user order*/
    public void CallReviewCurrentOrder() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
        jsonObject.addProperty("orderid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.orderid, Constant.notAvailable));
        jsonObject.addProperty("auth_token", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Auth_token, Constant.notAvailable));

        progress.setVisibility(View.VISIBLE);

        Service service = ApiCall.getRetrofit().create(Service.class);
        Call<JsonObject> call = service.reviewCurrentOrder("application/json", jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        try {
                            nointernetrl.setVisibility(View.GONE);
                            mainrl.setVisibility(View.VISIBLE);
                            JSONObject object = new JSONObject(Objects.requireNonNull(response.body()).toString());

                            if (object.optInt("success") == 1) {
                                progress.setVisibility(View.GONE);
                                currentOrderModelsArray.clear();
                                JSONArray jsonArray = object.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object1 = jsonArray.getJSONObject(i);
                                    CurrentOrderModel currentOrderModel = new CurrentOrderModel();
                                    currentOrderModel.setItem_id(object1.optString("item_id"));
                                    currentOrderModel.setItem_name(object1.optString("item_name"));
                                    currentOrderModel.setLike(object1.optString("like"));
                                    currentOrderModel.setUnlike(object1.optString("unlike"));
                                    currentOrderModel.setImage(object1.optString("image"));
                                    currentOrderModelsArray.add(currentOrderModel);
                                }
                                ratingAdapter = new RatingAdapter(currentOrderModelsArray);
                                rvRating.setAdapter(ratingAdapter);
                            }else {
                                progress.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                mainrl.setVisibility(View.GONE);
                nointernetrl.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPrefs.getSharedPref().edit().remove(SharedPrefs.userSharedPrefData.orderid).apply();
        SharedPrefs.getSharedPref().edit().remove(SharedPrefs.userSharedPrefData.Cafe_Id).apply();
    }

    @Override
    public void onBackPressed() {
        dbOpenHelper.deletetable();
        SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.Flag, "0").apply();
        SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.canScan,"yes").apply();
        SharedPrefs.getSharedPref().edit().remove(SharedPrefs.userSharedPrefData.Cafe_Id).apply();
        SharedPrefs.getSharedPref().edit().remove(SharedPrefs.userSharedPrefData.table_number).apply();
        startActivity(new Intent(RatingActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    /*session expire api*/
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
                                startActivity(new Intent(RatingActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                finish();
                            } else
                                showErrorDialog(object.optString("msg"));
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
        new AlertDialog.Builder(RatingActivity.this)
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> dialogInterface.dismiss())
                .create().show();
    }

}
