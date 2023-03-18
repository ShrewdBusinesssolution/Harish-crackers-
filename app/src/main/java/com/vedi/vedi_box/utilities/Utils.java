package com.vedi.vedi_box.utilities;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.vedi.vedi_box.notification.Config;

public class Utils extends Application {

    private static Utils utils;

    @Override
    public void onCreate() {
        super.onCreate();


        utils = this;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel(Config.id, Config.description, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(Config.description);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);

        }
    }

    public static synchronized Utils getInstance() {
        return utils;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener){
        ConnectivityReceiver.connectivityReceiverListener=listener;
    }
}
