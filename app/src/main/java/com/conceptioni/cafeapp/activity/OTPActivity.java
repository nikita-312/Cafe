package com.conceptioni.cafeapp.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.activity.retrofitinterface.Service;
import com.conceptioni.cafeapp.utils.Constant;
import com.conceptioni.cafeapp.utils.MakeToast;
import com.conceptioni.cafeapp.utils.SharedPrefs;
import com.conceptioni.cafeapp.utils.TextviewRegular;
import com.goodiebag.pinview.Pinview;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("Registered")
public class OTPActivity extends AppCompatActivity {
    TextviewRegular tvrVerify;
    Pinview pinview1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        init();
        clicks();
    }

    private void clicks() {
        tvrVerify.setOnClickListener(v -> startActivity(new Intent(OTPActivity.this,MenuActivity.class)));
        pinview1.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                Toast.makeText(OTPActivity.this, pinview.getValue(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        tvrVerify = findViewById(R.id.tvrVerify);
        pinview1 = findViewById(R.id.pinview1);

        registerReceiver(messageReceiver,new IntentFilter("broadcast_otp"));
    }

    private BroadcastReceiver messageReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String otp = intent.getStringExtra("otp_confirm");
            if (!otp.equalsIgnoreCase("null") && otp.length() > 5) {
                pinview1.setValue(otp);
                checkOTP();
            }

        }
    };

    public void checkOTP(){

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_phoneno", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Phone_No, Constant.notAvailable));
        jsonObject.addProperty("OTP", pinview1.getValue());

        Log.d("+++++++jsonobject","+++++"+jsonObject.toString());

        Service service = ApiCall.getRetrofit().create(Service.class);
        Call<JsonObject> call = service.sendotp(  "application/json", jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> callback, @NonNull Response<JsonObject> response) {
                JsonObject res = response.body();
                Log.e("====Responce", "===" + res);
                if (res != null) {

                } else {
                    new MakeToast("Please enter correct otp");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


}
