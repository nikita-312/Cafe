package com.dineore.app.firebase;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.dineore.app.R;
import com.dineore.app.activity.ApiCall;
import com.dineore.app.activity.LoginActivity;
import com.dineore.app.activity.retrofitinterface.Service;
import com.dineore.app.database.DBOpenHelper;
import com.dineore.app.utils.Constant;
import com.dineore.app.utils.MakeToast;
import com.dineore.app.utils.SharedPrefs;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    DBOpenHelper dbOpenHelper;
    SQLiteDatabase sqLiteDatabase;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        dbOpenHelper = new DBOpenHelper(MyFirebaseMessagingService.this);
        sqLiteDatabase = dbOpenHelper.getWritableDatabase();
        if (remoteMessage.getData() != null) {
            try {
                JSONObject object = new JSONObject(remoteMessage.getData());
                String auth = object.getString("auth_token");
                String type = object.getString("type");

                if (type.equalsIgnoreCase("logout")) {
                   ScanCafe(auth);
                } else if (type.equalsIgnoreCase("delete")) {
                    /*if menu screen is active then update or refresh*/
                    if (Constant.active) {
                        updateChatScreen();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateChatScreen() {
        Intent intent = new Intent("broadcast_chat_message");
        this.sendBroadcast(intent);
    }
    private void ScanCafe(String auth) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userid", SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.User_id, Constant.notAvailable));
        jsonObject.addProperty("auth_token", auth);
        Service service = ApiCall.getRetrofit().create(Service.class);
        Call<JsonObject> call = service.sessionexpire("application/json", jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        try {

                            JSONObject object = new JSONObject(Objects.requireNonNull(response.body()).toString());
                            Log.d("+++++++","+++++"+object.toString());
                            if (object.optInt("success") == 1) {
                                dbOpenHelper.deletetable();
                                SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.Flag, "0").apply();
                                SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.canScan, "yes").apply();
                                SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.Auth_token, auth).apply();
                                SharedPrefs.getSharedPref().edit().remove(SharedPrefs.userSharedPrefData.User_id).apply();
                                SharedPrefs.getSharedPref().edit().remove(SharedPrefs.userSharedPrefData.Cafe_Id).apply();
                                SharedPrefs.getSharedPref().edit().remove(SharedPrefs.userSharedPrefData.table_number).apply();
                                SharedPrefs.getSharedPref().edit().remove(SharedPrefs.userSharedPrefData.User_id).apply();
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            }

                            else {
                                SharedPrefs.getSharedPref().edit().remove(SharedPrefs.userSharedPrefData.User_id).apply();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                new MakeToast(R.string.Checkyournetwork);
            }
        });
    }

}