package com.app.frimline.Common;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.app.frimline.R;
import com.app.frimline.screens.CategoryRootActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String GROUP_KEY = "com.app.frimline.Notification";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String imageURL = null;
        String msg = null;
        String title = null;

        if (!remoteMessage.getData().isEmpty()) {
            try {
                remoteMessage.getData();
                JSONObject jsonObject = new JSONObject(Objects.requireNonNull(remoteMessage.getData().get("data")));
                title = jsonObject.getString("title");
                msg = jsonObject.getString("message");
                imageURL = jsonObject.getString("image");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            title = Objects.requireNonNull(remoteMessage.getNotification()).getTitle();
            msg = remoteMessage.getNotification().getBody();
            imageURL = Objects.requireNonNull(remoteMessage.getNotification().getImageUrl()).toString();
        }

        if (title != null)
            displayNotifications(title, msg, imageURL);
    }


    @SuppressLint("UnspecifiedImmutableFlag")
    private void displayNotifications(String title, String msg, String imageURL) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CONSTANT.CHANNEL_ID, CONSTANT.CHANNEL_NAME, importance);
            mChannel.setDescription(CONSTANT.CHANNEL_DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }

        Intent intent = new Intent(this, CategoryRootActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CONSTANT.CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_logo)
                .setLargeIcon(BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.notification_logo))
                .setContentTitle(title)
                .setGroupSummary(true)
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setPriority(Notification.PRIORITY_MAX)
                .setSound(defaultSoundUri)
                .setGroup(GROUP_KEY)
                .setDeleteIntent(createOnDismissedIntent(this))
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            notificationBuilder.setContentText(Html.fromHtml(msg, Html.FROM_HTML_MODE_COMPACT));
            notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(Html.fromHtml(msg, Html.FROM_HTML_MODE_COMPACT)));
        } else {
            notificationBuilder.setContentText(Html.fromHtml(msg));
            notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(Html.fromHtml(msg)));
        }

        if (imageURL != null && !imageURL.isEmpty()) {
            Bitmap bitmap = getBitmapsUrl(imageURL);
            notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null)).setLargeIcon(bitmap);
        }

        notificationBuilder.setSmallIcon(R.drawable.notification_logo);
        notificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify("Frimline", (int) System.currentTimeMillis(), notificationBuilder.build());
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private PendingIntent createOnDismissedIntent(Context context) {
        Intent intent = new Intent(context, NotificationDismissedReceiver.class);
        intent.putExtra("notificationId", 0);
        return PendingIntent.getBroadcast(this, 108, intent, 0);
    }

    public Bitmap getBitmapsUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            Log.e("awesome", "Error in getting notification image: " + e.getLocalizedMessage());
            return null;
        }
    }
}


