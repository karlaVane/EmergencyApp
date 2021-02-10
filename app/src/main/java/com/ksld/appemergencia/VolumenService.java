package com.ksld.appemergencia;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class VolumenService extends Service {
    private static final String TAG = "KeyEvent";
    private static final String segu = "SEGURITY";
    private static final int NOTIFICATION = 1;

    private NotificationManager nManager;
    int miVolumen , volumeIni ;
    int contador = 0;
    boolean paso1 = false;
    boolean paso2 = false;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if("android.media.VOLUME_CHANGED_ACTION".equals(intent.getAction())){
                miVolumen = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE",0);
                int volumenAnt = intent.getIntExtra("android.media.EXTRA_PREV_VOLUME_STREAM_VALUE", 0);
                if (!paso1){
                    volumeIni = intent.getIntExtra("android.media.EXTRA_PREV_VOLUME_STREAM_VALUE", 0);
                    paso1 = true;
                }
                if(volumeIni >3){
                    paso1 = false;
                    String mensajeError = "Porfavor baje el volumen "+miVolumen;
                    mensaje(mensajeError);
                }

                if (miVolumen > volumenAnt && paso1){
                    int numResult = abs(miVolumen - volumeIni);
                    //contador += 1;
                    if (numResult == 3){
                        paso2 = true;
                    }
                    if (numResult != 3 && paso2){///Si ya esta falso no quiero q entre
                        paso2 = false;
                    }
                    String mensajeGuia = "Su Volumen inicial es "+volumeIni+ " su volumen actual es "+miVolumen;
                    mensaje(mensajeGuia);
                }
                if (miVolumen < volumenAnt && paso1){
                    //contador -=1;
                    if (paso2 && abs(miVolumen - volumeIni) == 2){
                        paso2 = false;
                        Log.d(TAG, "Evento válido");
                        Intent i = new Intent(VolumenService.this,MainActivity.class);
                        VolumenService.this.startActivity(i);
                    }
                    String mensajeGuiaA = "Su Volumen inicial es "+volumeIni+ " su volumen actual es "+miVolumen;
                    mensaje(mensajeGuiaA);
                }

                Log.d(TAG, "Mi Volumen = "+ miVolumen);
                ///Log.d(TAG, "Volumen Anterior = "+ volumenAnt);//Bueno para comprobar
                Log.d(TAG, "Volumen de Inicio  = "+ volumeIni);
                //Log.d(TAG, "CONTADOR -> "+contador);
            }
        }
    };

    private void notificaionServicio(IntentFilter filter){
        //Envio al broadcast el filtro
        registerReceiver(broadcastReceiver, filter);
        ///Creo un Notification Manager
        //NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent(VolumenService.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(///Creo un pending itenm
                getApplicationContext(),
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext(),
                segu
        );
        builder.setSmallIcon(R.drawable.icon_seguridad);
        builder.setContentTitle("Segurity Service");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentText("Running");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(false);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            if(nManager != null && nManager.getNotificationChannel(segu) == null){
                NotificationChannel notificationChannel = new NotificationChannel(
                        segu,
                        "Segurity Service",
                        NotificationManager.IMPORTANCE_HIGH
                );
                notificationChannel.setDescription("Canal en Ejecución :v");
                nManager.createNotificationChannel(notificationChannel);
            }
        }
        startForeground(NOTIFICATION, builder.build());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        nManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        miToast("Servidor Iniciado");
        //Aca se crea un intent filter para filtrar con el .addAction("lo q we want escuchar en el broadcast ")
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        notificaionServicio(filter);
        return START_STICKY;///Para iniciar y no cerrar
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Cierra la notificacion
        nManager.cancel(NOTIFICATION);
        unregisterReceiver(broadcastReceiver);//Terminael broadcast mmm parece q esto daba "error"
        stopSelf();///Termina el servicio
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public static int abs(int numero){
        if (numero > 0){
            return numero;
        }else{
            return -numero;
        }
    }

    public void mensaje(String msg){
        Intent noti = new Intent(VolumenService.this,notificationService.class);
        noti.putExtra("msg", msg);
        //miToast(msg);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            startForegroundService(noti);
        }else{
            //Si no soporta ejecuto como servicio normal
            startService(noti);
        }
    }

    public void  miToast(String msg){
        Toast.makeText(VolumenService.this, msg,Toast.LENGTH_SHORT).show();
    }
}
