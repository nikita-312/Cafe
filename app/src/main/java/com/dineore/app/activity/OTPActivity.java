package com.dineore.app.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;

import com.dineore.app.R;
import com.dineore.app.activity.retrofitinterface.Service;
import com.dineore.app.dialog.EnterNameDialogue;
import com.dineore.app.utils.Constant;
import com.dineore.app.utils.MakeToast;
import com.dineore.app.utils.SharedPrefs;
import com.dineore.app.utils.TextviewRegular;
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
                showErrorDialog("Please Enter OTP");
            }
        });
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
        jsonObject.addProperty("deviceid", SharedPrefs.getSharedPref().getString(SharedPrefs.tokendetail.refreshtoken,Constant.notAvailable));
        Log.d("++++otp","+++ "+jsonObject);
        Service service = ApiCall.getRetrofit().create(Service.class);
        Call<JsonObject> call = service.verifyotp(  "application/json", jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> callback, @NonNull Response<JsonObject> response) {
                        try {
                            if (response.body() != null) {
                                JSONObject object = new JSONObject(Objects.requireNonNull(response.body()).toString());
                                Log.d("++++otp","+++object "+object);

                                if (object.optString("success").equalsIgnoreCase("1")){
                                    SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.Auth_token,object.optString("auth_token")).apply();
                                    SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.User_id,object.optString("id")).apply();

                                    if (object.optString("user_status").equalsIgnoreCase("old")){
                                        startActivity(new Intent(OTPActivity.this,HomeActivity.class));
                                        finish();
                                    }else if (object.optString("user_status").equalsIgnoreCase("firsttime")){
                                       new EnterNameDialogue(OTPActivity.this).ShowNameDialog();
                                    }

                                }else {
                                    showErrorDialog("Please Enter Correct Otp");
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                new MakeToast(R.string.Checkyournetwork);
            }
        });
    }
    private void showErrorDialog(String msg) {
        new AlertDialog.Builder(OTPActivity.this)
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> dialogInterface.dismiss())
                .create().show();
    }
}
