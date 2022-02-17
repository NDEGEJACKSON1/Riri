package com.example.riri;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.Random;

public class NotificationReceiver extends BroadcastReceiver {

    MediaPlayer mp;

    @Override
    public void onReceive(Context context, Intent intent) {

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mp = MediaPlayer.create(context.getApplicationContext(), alarmSound);
        mp.start();

        String title = intent.getStringExtra("title");
        String time = "Your Reminder at " + intent.getStringExtra("time");
        String desc = intent.getStringExtra("desc");

        notificationDialog(context,title,desc,time);
    }

    private void notificationDialog(Context context,String title,String desc,String time) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "Riri Reminder App";

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, home_page.class), 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Riri Reminder Notification", NotificationManager.IMPORTANCE_MAX);
            // Configure the notification channel.
            notificationChannel.setDescription("Riri Reminder Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(false)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.riri)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setTicker("Riri")
                .setContentTitle(title)
                .setContentText(desc)
                .setContentIntent(contentIntent)
                .setSubText(time)
                .setVibrate(new long[]{500, 1000, 500, 1000})
                .setContentInfo("Information");

        notificationManager.notify(1, notificationBuilder.build());
    }
}

