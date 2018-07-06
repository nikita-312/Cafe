package com.conceptioni.cafeapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.Log;
import android.widget.EditText;
import android.widget.RatingBar;

import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.activity.retrofitinterface.Service;
import com.conceptioni.cafeapp.utils.Constant;
import com.conceptioni.cafeapp.utils.MakeToast;
import com.conceptioni.cafeapp.utils.SharedPrefs;
import com.conceptioni.cafeapp.utils.TextviewRegular;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends AppCompatActivity {
    AppCompatRatingBar ratingbar;
    TextviewRegular tvrSubmit;
    float rating;
    EditText edtReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        init();
        clicks();
    }

    private void clicks() {
        ratingbar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            rating = ratingbar.getRating();
            Log.d("++++rating", "++ " + rating);
        });
        tvrSubmit.setOnClickListener(v -> getReview());
    }

    private void init() {
        ratingbar = findViewById(R.id.ratingbar);
        edtReview = findViewById(R.id.edtReview);
        tvrSubmit = findViewById(R.id.tvrSubmit);
    }

    public void getReview() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("cafeid", "1");
        jsonObject.addProperty("userid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
        jsonObject.addProperty("auth_token",SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Auth_token,Constant.notAvailable));
        jsonObject.addProperty("rating", rating);
        jsonObject.addProperty("comment", edtReview.getText().toString());

        Service service = ApiCall.getRetrofit().create(Service.class);
        Call<JsonObject> call = service.getReview("application/json", jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        try {
                            assert response.body() != null;
                            JSONObject object = new JSONObject(response.body().toString());
                            if (object.optInt("success") == 1) {
                                new MakeToast(object.optString("msg"));
                                startActivity(new Intent(ReviewActivity.this,HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            } else
                                new MakeToast(object.optString("msg"));

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ReviewActivity.this,HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
