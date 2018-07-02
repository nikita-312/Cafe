package com.conceptioni.cafeapp.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
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

import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.activity.retrofitinterface.Service;
import com.conceptioni.cafeapp.utils.MakeToast;
import com.conceptioni.cafeapp.utils.SharedPrefs;
import com.conceptioni.cafeapp.utils.Validations;
import com.google.gson.JsonObject;

import java.security.Permission;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.PermissionUtils;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RuntimePermissions
public class LoginActivity extends AppCompatActivity {

    LinearLayout sendotpll;
    int REQUEST_PERMISSION_SETTING = 0;
    EditText phonenoet;
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
        //test1
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
                boolean showRationale = false;
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
        new MakeToast(R.string.denied_read_sms);
        Log.d("++++++++result","+++++"+PermissionUtils.hasSelfPermissions(LoginActivity.this,Manifest.permission.READ_PHONE_STATE));

    }

    public void SendOtp(){

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_phoneno", "+91"+phonenoet.getText().toString());

        Log.d("+++++++jsonobject","+++++"+jsonObject.toString());

        Service service = ApiCall.getRetrofit().create(Service.class);
        Call<JsonObject> call = service.sendotp(  "application/json", jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> callback, @NonNull Response<JsonObject> response) {
                JsonObject res = response.body();
                Log.e("====Responce", "===" + res);
                if (res != null) {
                    if (response.isSuccessful())
                    loginprogress.setVisibility(View.GONE);
                    SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.Phone_No,phonenoet.getText().toString()).apply();
                    startActivity(new Intent(LoginActivity.this,OTPActivity.class));
                    finish();
                } else {
                    loginprogress.setVisibility(View.GONE);
                    new MakeToast("Please enter correct otp");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loginprogress.setVisibility(View.GONE);
            }
        });
    }
}
