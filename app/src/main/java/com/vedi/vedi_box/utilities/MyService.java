package com.vedi.vedi_box.utilities;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;

import com.vedi.vedi_box.views.MainActivity;

public class MyService extends Service {
    Handler handler = new Handler();
    private final Runnable periodicUpdate = new Runnable() {
        @Override
        public void run() {

            handler.postDelayed(periodicUpdate, 1 * 1000 - SystemClock.elapsedRealtime() % 1000);
            Intent broadCastIntent = new Intent();
            broadCastIntent.setAction(MainActivity.BroadCastStringForAction);
            broadCastIntent.putExtra("online_status", "" + isOnline(MyService.this));
            sendBroadcast(broadCastIntent);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Inside", "OnCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.post(periodicUpdate);
        return START_STICKY;
    }

    public boolean isOnline(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        return ni != null && ni.isConnectedOrConnecting();
    }
}
