package com.example.diabetescarbapp.utils;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.diabetescarbapp.R;

import java.time.LocalTime;
import java.util.Calendar;

public class AlarmManagerBuilder {

    private static AlarmManagerBuilder instance;

    public static AlarmManagerBuilder getInstance() {
        if (instance == null) {
            instance = new AlarmManagerBuilder();
        }
        return instance;
    }

    private AlarmManagerBuilder() {}

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void build(Context context, LocalTime time, Request requestCode, String title, String message) {
        createChannel(context);
        setUpAlarm(context, time, requestCode.ordinal(), title, message);
    }

    public void destroy(Context context, Request requestCode) {
        cancelAlarm(context, requestCode.ordinal());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void createChannel(Context context) {
            NotificationChannel channel = new NotificationChannel("alarm", "Alarm", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void setUpAlarm(Context context, LocalTime time, int requestCode, String title, String message) {
        // Set the alarm to start at a specific time every day
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.set(Calendar.HOUR_OF_DAY, time.getHour());
        calendar.set(Calendar.MINUTE, time.getMinute());
        calendar.set(Calendar.SECOND, time.getSecond());

        Intent intent = new Intent(context, AlarmReceiver.class);

        Bundle bundle = new Bundle();
        bundle.putInt("id", requestCode);
        bundle.putString("title", title);
        bundle.putString("message", message);

        intent.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private static void cancelAlarm(Context context, int requestCode) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public static class AlarmReceiver extends BroadcastReceiver {
        private final long[] pattern = {500,500,500,500,500,500,500,500,500};
        private final Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        @Override
        public void onReceive(Context context, Intent intent) {
            // Create the notification

            int id = intent.getIntExtra("id", 0);
            String title = intent.getStringExtra("title");
            String message = intent.getStringExtra("message");

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "alarm")
                    .setSmallIcon(R.drawable.ic_alarm)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setAutoCancel(true)
                    .setLights(Color.BLUE, 500, 500)
                    .setVibrate(pattern)
                    .setStyle(new NotificationCompat.BigTextStyle())
                    .setSound(alarmSound);

            // Trigger the notification
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(id, builder.build());
        }
    }
}

