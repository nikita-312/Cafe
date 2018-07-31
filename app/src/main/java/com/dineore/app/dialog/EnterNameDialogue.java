package com.dineore.app.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.dineore.app.R;
import com.dineore.app.activity.ApiCall;
import com.dineore.app.activity.HomeActivity;
import com.dineore.app.activity.LiveOrderActivity;
import com.dineore.app.activity.retrofitinterface.Service;
import com.dineore.app.utils.Constant;
import com.dineore.app.utils.MakeToast;
import com.dineore.app.utils.SharedPrefs;
import com.dineore.app.utils.Validations;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterNameDialogue {
    private Context context;

    public EnterNameDialogue(Context context) {
        this.context = context;
    }

    public void ShowNameDialog() {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_name);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        EditText name = dialog.findViewById(R.id.nameet);
        LinearLayout submitll = dialog.findViewById(R.id.submitll);

        submitll.setOnClickListener(v -> {
            Validations validations = new Validations();
            if (!validations.isEmpty(name)){
                AddName(name.getText().toString());
            }else {
                name.setError("Enter Name");
            }

        });

        dialog.show();
    }

    private void AddName(String Name) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
        jsonObject.addProperty("username", Name);
        jsonObject.addProperty("auth_token", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.Auth_token, Constant.notAvailable));

        Service service = ApiCall.getRetrofit().create(Service.class);
        Call<JsonObject> call = service.storeusername(  "application/json", jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> callback, @NonNull Response<JsonObject> response) {
                try {
                    if (response.body() != null) {
                        JSONObject object = new JSONObject(Objects.requireNonNull(response.body()).toString());

                        if (object.optString("success").equalsIgnoreCase("1")) {
                            SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.Name,Name).apply();
                            context.startActivity(new Intent(context,HomeActivity.class));
                            ((Activity)context).finish();
                        }else {
                            showErrorDialog(object.optString("msg"));
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
    } private void showErrorDialog(String msg) {
        new AlertDialog.Builder(context)
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
    }

}
