package com.conceptioni.cafeapp.firebase;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.conceptioni.cafeapp.utils.SharedPrefs;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        final Intent intent = new Intent("tokenrefresh");
        final LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(MyFirebaseInstanceIDService.this);
        intent.putExtra("token_in _fcm_id",refreshedToken);
        broadcastManager.sendBroadcast(intent);

        SharedPrefs.getSharedPref().edit().putString(SharedPrefs.tokendetail.refreshtoken, refreshedToken).apply();
        Log.e("=====tokeninshared",SharedPrefs.getSharedPref().getString(SharedPrefs.tokendetail.refreshtoken,"N/A"));

        sendRegistrationToServer(refreshedToken);

    }

    private void sendRegistrationToServer(String refreshedToken) {

    }


}