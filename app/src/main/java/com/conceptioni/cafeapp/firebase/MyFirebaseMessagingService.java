package com.conceptioni.cafeapp.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.conceptioni.cafeapp.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService  extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        simpelsendnotification();
    }

    private void simpelsendnotification() {
//        Random random = new Random();
//        int rand = random.nextInt(1000);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyFirebaseMessagingService.this);
        builder.setContentTitle(getString(R.string.app_name));
        builder.setContentText("Hidden Push");
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setSound(uri);
        builder.setOnlyAlertOnce(true);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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

}