package com.conceptioni.cafeapp.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.activity.retrofitinterface.Service;
import com.conceptioni.cafeapp.adapter.RatingAdapter;
import com.conceptioni.cafeapp.model.CurrentOrderModel;
import com.conceptioni.cafeapp.model.Images;
import com.conceptioni.cafeapp.utils.Constant;
import com.conceptioni.cafeapp.utils.MakeToast;
import com.conceptioni.cafeapp.utils.SharedPrefs;
import com.conceptioni.cafeapp.utils.TextviewRegular;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingActivity extends AppCompatActivity {
    TextviewRegular tvrSubmit;
    LinearLayoutManager linearLayoutManager;
    RatingAdapter ratingAdapter;
    RecyclerView rvRating;
    ImageView ivSkip;
    List<CurrentOrderModel> currentOrderModelsArray=new ArrayList<>();
    List<Images> imagesArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        init();
        clicks();
    }

    private void clicks() {
        tvrSubmit.setOnClickListener(v -> startActivity(new Intent(RatingActivity.this,HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)));
        ivSkip.setOnClickListener(v -> startActivity(new Intent(RatingActivity.this,HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)));
    }

    private void init() {
        ivSkip = findViewById(R.id.ivSkip);
        tvrSubmit = findViewById(R.id.tvrSubmit);
        rvRating = findViewById(R.id.rvRating);
        linearLayoutManager = new LinearLayoutManager(RatingActivity.this);
        rvRating.setLayoutManager(linearLayoutManager);
        CallReviewCurrentOrder();
    }

    public void CallReviewCurrentOrder(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
        jsonObject.addProperty("orderid",SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.orderid,Constant.notAvailable));
        jsonObject.addProperty("auth_token",SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Auth_token,Constant.notAvailable));

        Service service = ApiCall.getRetrofit().create(Service.class);
        Call<JsonObject> call = service.reviewCurrentOrder("auth_token",jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null){
                    if (response.isSuccessful()) {
                        try {
                            JSONObject object = new JSONObject(response.body().toString());
                            if (object.optInt("success") == 1){
                                currentOrderModelsArray.clear();
                                JSONArray jsonArray = object.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object1 = jsonArray.getJSONObject(i);
                                    CurrentOrderModel currentOrderModel = new CurrentOrderModel();
                                    currentOrderModel.setItem_id(object1.optString("item_id"));
                                    currentOrderModel.setItem_name(object1.optString("item_name"));
                                    currentOrderModel.setLike(object1.optString("like"));
                                    currentOrderModel.setUnlike(object1.optString("unlike"));
                                    currentOrderModelsArray.add(currentOrderModel);
                                    imagesArrayList.clear();
                                    JSONArray array = object.getJSONArray("image");
                                    for (int j = 0; j < array.length(); j++) {
                                        Images images1 = new Images();
                                        images1.setImages(array.getString(0));
                                        currentOrderModel.setImagesList(imagesArrayList);
                                        imagesArrayList.add(images1);
                                    }
                                    currentOrderModel.setImagesList(imagesArrayList);
                                    currentOrderModelsArray.add(currentOrderModel);
                                }
                                ratingAdapter = new RatingAdapter(imagesArrayList,currentOrderModelsArray);
                                rvRating.setAdapter(ratingAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                new MakeToast("Error while getting data");
            }
        });
    }

}
