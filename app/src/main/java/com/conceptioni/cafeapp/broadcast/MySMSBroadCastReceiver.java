package com.conceptioni.cafeapp.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;

public class MySMSBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get Bundle object contained in the SMS intent passed in
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (Object aPdusObj : pdusObj) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                    // String senderAddress = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();

                    String verificationCode = getVerificationCode(message);

                    Intent hhtpIntent = new Intent(context, MySMSBroadCastReceiver.class);
                    hhtpIntent.putExtra("otp", verificationCode);
                    context.startService(hhtpIntent);
                }
            }
        } catch (Exception e) {
            // Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private String getVerificationCode(String message) {
        String code = null;
        code=message.substring(message.length()-7,message.length()-1);
        return code;
    }
}
