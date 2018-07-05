package com.conceptioni.cafeapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.activity.retrofitinterface.Service;
import com.conceptioni.cafeapp.utils.Constant;
import com.conceptioni.cafeapp.utils.MakeToast;
import com.conceptioni.cafeapp.utils.SharedPrefs;
import com.conceptioni.cafeapp.utils.TextviewRegular;
import com.goodiebag.pinview.Pinview;
import com.google.gson.JsonObject;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("Registered")
public class OTPActivity extends AppCompatActivity {
    TextviewRegular tvrVerify;
    Pinview pinview1;
    SmsVerifyCatcher smsVerifyCatcher;
    LinearLayout verifyll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        init();
        clicks();
    }

    @Override
    protected void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }

    private void clicks() {
        tvrVerify.setOnClickListener(v -> {
            if (!pinview1.getValue().equalsIgnoreCase("")){
                checkOTP();
            }else {
                new MakeToast("Please Enter OTP");
            }
        });
//        pinview1.setPinViewEventListener(new Pinview.PinViewEventListener() {
//            @Override
//            public void onDataEntered(Pinview pinview, boolean fromUser) {
//
//            }
//        });
    }

    private void init() {
        tvrVerify = findViewById(R.id.tvrVerify);
        pinview1 = findViewById(R.id.pinview1);
        verifyll = findViewById(R.id.verifyll);



        smsVerifyCatcher = new SmsVerifyCatcher(this, message -> {
            String code = parseCode(message);//Parse verification code
            pinview1.setValue(code);//set code in edit text
            //then you can send verification code to server
        });
    }

    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{4}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }

    public void checkOTP(){

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_phoneno", "+91"+SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Phone_No, Constant.notAvailable));
        jsonObject.addProperty("OTP", pinview1.getValue());


        Service service = ApiCall.getRetrofit().create(Service.class);
        Call<JsonObject> call = service.verifyotp(  "application/json", jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> callback, @NonNull Response<JsonObject> response) {
                        try {
                            if (response.body() != null) {
                                JSONObject object = new JSONObject(Objects.requireNonNull(response.body()).toString());
                                if (object.optString("success").equalsIgnoreCase("1")){
                                    SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.Auth_token,object.optString("auth_token")).apply();
                                    SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.User_id,object.optString("id")).apply();
                                    new MakeToast(object.optString("msg"));
                                    startActivity(new Intent(OTPActivity.this,HomeActivity.class));
                                    finish();
                                }else {
                                    new MakeToast("Please Enter Correct Otp");
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                new MakeToast("Error While Retriving Otp Please Try After Some Time");
            }
        });
    }


}
