package com.example.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(1, nb.build());

        String num,msg;

        num = intent.getExtras().getString("extra");
        msg = intent.getExtras().getString("extra2");

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(num,null,msg,null,null);

    }
}