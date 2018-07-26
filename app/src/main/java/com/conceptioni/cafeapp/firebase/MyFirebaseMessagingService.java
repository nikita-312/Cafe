package com.conceptioni.cafeapp.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.conceptioni.cafeapp.CafeApp;
import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.activity.LoginActivity;
import com.conceptioni.cafeapp.utils.Constant;
import com.conceptioni.cafeapp.utils.SharedPrefs;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import static android.support.v4.app.NotificationCompat.Builder;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData() != null) {
            try {
                JSONObject object = new JSONObject(remoteMessage.getData());
                String auth = object.getString("auth_token");
                String type = object.getString("type");

                Log.d("++++++type", "+++++" + object.toString());

                if (type.equalsIgnoreCase("logout")) {
                    SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.Flag, "0").apply();
                    SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.canScan, "yes").apply();
                    SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.Auth_token, auth).apply();
                    SharedPrefs.getSharedPref().edit().remove(SharedPrefs.userSharedPrefData.User_id).apply();
                    SharedPrefs.getSharedPref().edit().remove(SharedPrefs.userSharedPrefData.Cafe_Id).apply();
                    SharedPrefs.getSharedPref().edit().remove(SharedPrefs.userSharedPrefData.table_number).apply();
                    SharedPrefs.getSharedPref().edit().remove(SharedPrefs.userSharedPrefData.User_id).apply();
                    if (!CafeApp.isAppIsInBackground(getApplicationContext())) {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    }
                } else if (type.equalsIgnoreCase("delete")) {
                    if (Constant.active) {
                        updateChatScreen();
                        simpelsendnotification();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void simpelsendnotification() {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Builder builder = new Builder(MyFirebaseMessagingService.this);
        builder.setContentTitle(getString(R.string.app_name));
        builder.setContentText("You are login with other device. And you can access app only in one device");
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setSound(uri);
        builder.setOnlyAlertOnce(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher));
            builder.setColor(this.getResources().getColor(R.color.colorwhite));
        }

        Intent resultIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.notify(Integer.parseInt("0"), builder.build());
    }

    private void updateChatScreen() {
        Intent intent = new Intent("broadcast_chat_message");
        this.sendBroadcast(intent);
    }
}