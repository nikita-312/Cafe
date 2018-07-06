package com.conceptioni.cafeapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

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

public class CardActivity extends AppCompatActivity {
    LinearLayout llNext,llCash,llCard;
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        init();
        clicks();
    }

    private void clicks() {
        llNext.setOnClickListener(v -> {
            startActivity(new Intent(CardActivity.this,VisitAgainActivity.class));
            finish();
        });
        llCard.setOnClickListener(v -> type = "Card");
        llCash.setOnClickListener(v -> type = "Cash");

    }

    private void init() {
        llNext =findViewById(R.id.llNext);
        llCard =findViewById(R.id.llCard);
        llCash =findViewById(R.id.llCash);
        if (type != null) {
            CallCard();
        }else
            new MakeToast("Please choose payment method");
    }
    public void CallCard() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
        jsonObject.addProperty("auth_token", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Auth_token, Constant.notAvailable));
        jsonObject.addProperty("payment", type);

        Log.d("+++++type","+++ "+type);
        Service service = ApiCall.getRetrofit().create(Service.class);
        Call<JsonObject> call = service.makePayment("application/json",jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null){
                    if (response.isSuccessful()){
                        try {
                            JSONObject object = new JSONObject(response.body().toString());
                            if (object.optInt("success") == 0){
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
                    new MakeToast("Error while selecting payment method");
            }
        });
    }
}
