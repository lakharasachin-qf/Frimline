package com.app.frimline.Common;

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

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    String GROUP_KEY = "com.app.frimline.Notification";

    private String catName;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    String imageURL;
    String msg;
    String title;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e("remoteMessage", remoteMessage.getData().toString());
        if (!remoteMessage.getData().isEmpty())
            shownotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("msg"));
        else {
            title = remoteMessage.getNotification().getTitle();
            msg = remoteMessage.getNotification().getBody();
            imageURL = remoteMessage.getNotification().getImageUrl().toString();

            Log.e("remoteMessage", remoteMessage.getNotification().getTitle().toString());
            Log.e("remoteMessage", remoteMessage.getNotification().getImageUrl().toString());

            shownotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
    }


    private void shownotification(String title, String msg) {

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
//        if (copiedMessage != null && copiedMessage.equalsIgnoreCase("addBrand")) {
//            intent = new Intent(this, ViewBrandActivity.class);
//        } else if (copiedMessage != null && copiedMessage.equalsIgnoreCase("addFrame")) {
//            intent = new Intent(this, ViewBrandActivity.class);
//        } else {
//            if (cat_id.equals("0"))
//                intent = new Intent(this, HomeActivity.class);
//            else {
//                intent = new Intent(this, ImageCategoryDetailActivity.class);
//                intent.putExtra("notification", "1");
//                intent.putExtra("cat_id", cat_id);
//                intent.putExtra("catName", catName);
//            }
//
//        }


        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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
            Bitmap bitmap = getBitmapfromUrl(imageURL);
            notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null)).setLargeIcon(bitmap);
        }

        notificationBuilder.setSmallIcon(R.drawable.notification_logo);
        notificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify("Brand Mania", (int) System.currentTimeMillis(), notificationBuilder.build());
    }

    private PendingIntent createOnDismissedIntent(Context context) {
        Intent intent = new Intent(context, NotificationDismissedReceiver.class);
        intent.putExtra("notificationId", 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 108, intent, 0);
        return pendingIntent;
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
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


