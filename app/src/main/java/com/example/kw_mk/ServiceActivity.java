package com.example.kw_mk;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class ServiceActivity extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        App.gpsTracker = new GpsTracker(this);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        App.gpsTracker.stopUsingGPS();
        super.onDestroy();
    }
}
