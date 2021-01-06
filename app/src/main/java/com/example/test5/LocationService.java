package com.example.test5;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

public class LocationService extends Service {

    private LocationManager locationManager;    //ロケーションマネージャー

    private Context context;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int requestCode = 0;
        String channelId = "default";
        String title = context.getString(R.string.app_name);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, requestCode,
                        intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager =
                (NotificationManager) context.
                        getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel(
                channelId, title, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Silent Notification");

        channel.setSound(null, null);

        channel.enableLights(false);
        channel.setLightColor(Color.BLUE);

        channel.enableVibration(false);

        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(context, channelId)
                    .setContentTitle(title)
                    .setSmallIcon(android.R.drawable.btn_star)
                    .setContentText("GPS")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .build();

            startForeground(1, notification);
        }

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected void stopGPS(){
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopGPS();
    }
}