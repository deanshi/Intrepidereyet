package com.example.deanshi.intrepidereyet;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Timer;
import java.util.TimerTask;

import timber.log.Timber;


/**
 * Created by deanshi on 6/7/16.
 */
public class MyService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public final static double INTREPID_LATITUDE = 42.3669292;
    public final static double INTREPID_LONGITUDE = -71.0800936;
    public final static float RADIUS_DISTANCE = 50;
    public final static long INTERVAL_TIME = 15 * 60 * 1000;


    private static GoogleApiClient mGoogleApiClient;
    public static final String TAG = MapsActivity.class.getSimpleName();
    public static float[] results = new float[] {0};
    public static Location location;
    public static LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
        }
    };


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Timber.d("Service has been created and started");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
        return Service.START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Timber.d("Service has been bound");
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Timber.d("Connection established");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        LocationRequest locationRequest = new LocationRequest().create();
        locationRequest.setInterval(INTERVAL_TIME);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, locationListener);
        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Location.distanceBetween(location.getLatitude(), location.getLongitude(), INTREPID_LATITUDE, INTREPID_LONGITUDE, results);

        Runnable distanceThread = new Runnable() {
            @Override
            public void run() {

                while (results[0] < 50) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    Location.distanceBetween(location.getLatitude(), location.getLongitude(), INTREPID_LATITUDE, INTREPID_LONGITUDE, results);
                    Timber.d("Results: %f", results[0]);
                }
            }
        };
        distanceThread.run();





    }

    @Override
    public void onConnectionSuspended(int i) {
        Timber.d("Connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Timber.d("Connection Failed");

    }
}
