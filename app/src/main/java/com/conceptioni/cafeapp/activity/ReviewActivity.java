package com.conceptioni.cafeapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.activity.retrofitinterface.Service;
import com.conceptioni.cafeapp.utils.MakeToast;
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
      ratingbar.setOnClickListener(v -> {
          rating = ratingbar.getRating();
          Log.d("++++rating","++ "+rating);
      });
        tvrSubmit.setOnClickListener(v -> getReview());
    }

    private void init() {
        ratingbar = findViewById(R.id.ratingbar);
        edtReview = findViewById(R.id.edtReview);
    }
    public void getReview(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("cafeid","1");
        jsonObject.addProperty("userid","2");
        jsonObject.addProperty("rating",rating);
        jsonObject.addProperty("comment",edtReview.getText().toString());
        jsonObject.addProperty("auth_token","MWNhZmUxNTMwNjk2NjQw");

        Service service = ApiCall.getRetrofit().create(Service.class);
        Call<JsonObject> call = service.getReview("application/json",jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject object = new JSONObject(response.body().toString());
                            if (object.optInt("success")==1){
                                new MakeToast(object.optString("msg"));
                            }else
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
}
