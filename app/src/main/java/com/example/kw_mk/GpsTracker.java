package com.example.kw_mk;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import static com.example.kw_mk.App.db;
import static com.example.kw_mk.App.myLocation;
import static com.example.kw_mk.App.orderData;
import static com.example.kw_mk.App.payMenuListItem2;
import static com.example.kw_mk.App.storeLocation;

public class GpsTracker extends Service implements LocationListener {

    private final Context mContext;
    Location location;
    double latitude;
    double longitude;
    static int alarm = 0;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 1000;
    protected LocationManager locationManager;


    public GpsTracker(Context context) {
        this.mContext = context;
        getLocation();
        alarm = 0;
    }


    // 거리 측정
    void distacne(Location start, Location End) {
        String meter;
        int dis;
        double distance;

        distance = start.distanceTo(End);
        dis = (int) distance; // 거리계산 값
        if (dis < 500 && alarm == 0) {
            alarm++;
            meter = Double.toString(distance);
            Log.d("디스탠스 ::: ", "다와감");
            // 알람주기
            db.collection("Store_Info").document(App.orderEmail).collection("Reserve").add(orderData).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    DocumentReference doc = task.getResult();
                    for (int i = 0; i < payMenuListItem2.size(); i++) {
                        doc.collection("주문목록").document(payMenuListItem2.get(i).payName).set(payMenuListItem2.get(i));
                    }
                    payMenuListItem2.clear();
                }
            });
            stopUsingGPS();
        }
        Log.d("디스탠스 ::: ", "" + dis);
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {

            } else {
                int hasFineLocationPermission = ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION);
                if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                        hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
                } else
                    return null;

                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.d("@@@", "" + e.toString());
        }
        return location;
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            myLocation = location;
            if (storeLocation != null) {
                distacne(myLocation, storeLocation);
            }
        } else {
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GpsTracker.this);
        }
    }
}