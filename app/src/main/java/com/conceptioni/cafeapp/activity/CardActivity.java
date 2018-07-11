package com.conceptioni.cafeapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.activity.retrofitinterface.Service;
import com.conceptioni.cafeapp.utils.Constant;
import com.conceptioni.cafeapp.utils.MakeToast;
import com.conceptioni.cafeapp.utils.SharedPrefs;
import com.conceptioni.cafeapp.utils.TextviewRegular;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardActivity extends AppCompatActivity {
    LinearLayout llNext, llCash, llCard, retryll;
    String type;
    ImageView pay_cashiv, pay_cardiv,ivBack;
    TextviewRegular paybycashtvr, paybycardtvr;
    RelativeLayout mainrl,nointernetrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        init();
        clicks();
    }

    private void clicks() {
        llNext.setOnClickListener(v -> {
            if (type!= null) {
                CallCard();
            }            else new MakeToast("Please choose payment method");


        });
        llCard.setOnClickListener(v -> {
            llCard.setBackgroundResource(R.drawable.select_card_drawable);
            pay_cardiv.setImageDrawable(getResources().getDrawable(R.drawable.pay_card_select));
            paybycardtvr.setTextColor(getResources().getColor(R.color.colorwhite));
            llCash.setBackgroundResource(R.drawable.card_drawable);
            pay_cashiv.setImageDrawable(getResources().getDrawable(R.drawable.pay_cash));
            paybycashtvr.setTextColor(getResources().getColor(R.color.colorFont));
            type = "Card";
        });


        llCash.setOnClickListener(v -> {
            llCash.setBackgroundResource(R.drawable.select_card_drawable);
            pay_cashiv.setImageDrawable(getResources().getDrawable(R.drawable.pay_cash_select));
            paybycashtvr.setTextColor(getResources().getColor(R.color.colorwhite));
            pay_cardiv.setImageDrawable(getResources().getDrawable(R.drawable.pay_card));
            llCard.setBackgroundResource(R.drawable.card_drawable);
            paybycardtvr.setTextColor(getResources().getColor(R.color.colorFont));
            type = "Cash";
        });
        ivBack.setOnClickListener(v -> finish());
        retryll.setOnClickListener(v -> {
            if (type != null) {
                CallCard();
            }
            else new MakeToast("Please choose payment method");
        });
    }

    private void init() {
        llNext = findViewById(R.id.llNext);
        llCard = findViewById(R.id.llCard);
        llCash = findViewById(R.id.llCash);
        pay_cashiv = findViewById(R.id.pay_cashiv);
        pay_cardiv = findViewById(R.id.pay_cardiv);
        paybycashtvr = findViewById(R.id.paybycashtvr);
        paybycardtvr = findViewById(R.id.paybycardtvr);
        ivBack = findViewById(R.id.ivBack);
        nointernetrl = findViewById(R.id.nointernetrl);
        mainrl = findViewById(R.id.mainrl);
        retryll = findViewById(R.id.retryll);
    }

    public void CallCard() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
        jsonObject.addProperty("auth_token", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Auth_token, Constant.notAvailable));
        jsonObject.addProperty("payment", type);
        jsonObject.addProperty("cafeid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Cafe_Id, Constant.notAvailable));
        Log.d("+++++type", "+++ " + type);
        Service service = ApiCall.getRetrofit().create(Service.class);
        Call<JsonObject> call = service.makePayment("application/json", jsonObject);
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
                                new MakeToast(object.optString("msg"));

                                SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.orderid,object.getString("orderid")).apply();
                                startActivity(new Intent(CardActivity.this, RatingActivity.class));
                                finish();
                            } else
                                new MakeToast(object.optString("msg"));

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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
