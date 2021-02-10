package com.ksld.appemergencia;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class notificationService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onDestroy();
            }
        },20000);///Para q dure 0.33 min desde q se crearon
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNChannel();
        String mensaje = intent.getStringExtra("msg");
        Intent mainIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mainIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, "Canal1")
                .setContentTitle("HELP")
                .setContentText(mensaje)
                .setSmallIcon(R.drawable.icon_seguridad)
                .setContentIntent(pendingIntent).build();
        startForeground(2, notification);
        return START_NOT_STICKY;
    }

    private void createNChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(
                    "Canal1",
                    "Segurity",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
