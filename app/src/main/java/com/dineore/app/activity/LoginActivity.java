package com.dineore.app.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.dineore.app.R;
import com.dineore.app.activity.retrofitinterface.Service;
import com.dineore.app.utils.MakeToast;
import com.dineore.app.utils.SharedPrefs;
import com.dineore.app.utils.Validations;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RuntimePermissions
public class LoginActivity extends AppCompatActivity {

    LinearLayout sendotpll;
    int REQUEST_PERMISSION_SETTING = 0;
    EditText phonenoet,nameet;
    ProgressBar loginprogress;
    Validations validations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        validations = new Validations();
        initlogin();
        clicklogin();
        LoginActivityPermissionsDispatcher.read_smsWithPermissionCheck(LoginActivity.this);

    }

    private void clicklogin() {
        sendotpll.setOnClickListener(v -> {
                if (!validations.isEmpty(phonenoet)){
                    if (validations.isValidPhoneNumber(phonenoet)){
                        loginprogress.setVisibility(View.VISIBLE);
                        SendOtp();
                    }else {
                        phonenoet.setError(getString(R.string.validphoneno));
                    }
                }else {
                    phonenoet.setError(getString(R.string.phoneno));
                }
        });
    }

    private void initlogin() {
        sendotpll = findViewById(R.id.sendotpll);
        phonenoet = findViewById(R.id.phonenoet);
        loginprogress = findViewById(R.id.loginprogress);
        nameet = findViewById(R.id.nameet);

    }

    @NeedsPermission({Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS})
    void read_sms() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LoginActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
        for (int i = 0, len = permissions.length; i < len; i++) {
            String permission = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                // user rejected the permission
                boolean showRationale;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    showRationale = shouldShowRequestPermissionRationale( permission );
                    if (!showRationale) {
                        PermissionDialogue();
                        // user also CHECKED "never ask again"
                        // you can either enable some fall back,
                        // disable features of your app
                        // or open another dialog explaining
                        // again the permission and directing to
                        // the app setting
                    }
                }

            }
        }
    }

    private void PermissionDialogue(){
        new AlertDialog.Builder(this)
                .setMessage(R.string.permissionmsg)
                .setPositiveButton(R.string.grant_permission, (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                })
                .setNegativeButton(R.string.button_deny, (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                }).show();

    }


    @OnShowRationale({Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS})
    void show_read_sms_dialogue(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.need_sms_read)
                .setPositiveButton(R.string.button_allow, (dialog, button) -> request.proceed())
                .setNegativeButton(R.string.button_deny, (dialog, button) -> request.cancel())
                .show();
    }

    @OnPermissionDenied({Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS})
    void denied_read_sms() {
        showErrorDialog(R.string.denied_read_sms);
    }

    public void SendOtp(){

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_phoneno", "+91"+phonenoet.getText().toString());
        Log.d("++++login","++ "+jsonObject);
        Service service = ApiCall.getRetrofit().create(Service.class);
        Call<JsonObject> call = service.sendotp(  "application/json", jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> callback, @NonNull Response<JsonObject> response) {
                try {
                    JSONObject jsonObject1 = new JSONObject(Objects.requireNonNull(response.body()).toString());
                    Log.d("++++login","++res "+jsonObject1);

                    if (jsonObject1.optString("success").equalsIgnoreCase("1")){
                        loginprogress.setVisibility(View.GONE);
                        SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.Phone_No,phonenoet.getText().toString()).apply();
                        startActivity(new Intent(LoginActivity.this,OTPActivity.class));
                        finish();
                    }else {
                        new MakeToast("Please Enter Correct Phone Number");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                loginprogress.setVisibility(View.GONE);
                new MakeToast(R.string.Checkyournetwork);
            }
        });
    }
    private void showErrorDialog(int msg) {
        new AlertDialog.Builder(LoginActivity.this)
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> dialogInterface.dismiss())
                .create().show();
    }
}
