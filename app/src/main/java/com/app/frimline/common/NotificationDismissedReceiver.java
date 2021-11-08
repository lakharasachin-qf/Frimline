package com.app.frimline.common;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationDismissedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getExtras().getInt("notificationId");
        /* Your code to handle the event here */
        if (notificationId == 0) {
            NotificationManager notify_manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notify_manager.cancel(0);
            notify_manager.cancelAll();

        }
    }
}