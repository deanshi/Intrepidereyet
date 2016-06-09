package com.example.deanshi.intrepidereyet;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
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

import timber.log.Timber;


/**
 * Created by deanshi on 6/7/16.
 */
public class MyService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public final static double INTREPID_LATITUDE = 42.3669292;
    public final static double INTREPID_LONGITUDE = -71.0800936;
    public final static long INTERVAL_TIME = 1000;
    public final static int ID_VALUE = 60; //Random value used in notify. What is the best practice for this?

    private static GoogleApiClient mGoogleApiClient;
    public static final String TAG = MapsActivity.class.getSimpleName();
    public static float[] results = new float[] {0};

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Timber.d(TAG, "Service has been created and started");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Timber.d(TAG, "Connection established");
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
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
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Location.distanceBetween(location.getLatitude(), location.getLongitude(), INTREPID_LATITUDE, INTREPID_LONGITUDE, results);

        if (results[0] < 30) {
            Timber.d(TAG, "Results: %f", results[0]);
            Timber.d("Entered Intrepid Range");
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("You have entered the Intrepid area")
                    .setContentText("Click to send a message to Slack")
                    .setAutoCancel(true);
            //Attempted to use Notification.flags |= Notification.Flags_AUTO_CANCEL, did not work, is depreciated?

            Intent resultIntent = new Intent(this, SendSlackReciever.class);
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent resultPendingIntent = PendingIntent.getBroadcast(this, 0, resultIntent, 0);
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(ID_VALUE, mBuilder.build());

            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

        }

    }

    //Unused, required methods
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Timber.d(TAG, "Service has been bound");
        return null;
    }

    @Override
    public void onConnectionSuspended(int i) {
        Timber.d(TAG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Timber.d(TAG, "Connection Failed");

    }
}
