package com.example.deanshi.intrepidereyet;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * App Name: Intrepidereyet
 * Author: Dean Shi
 * Use: Starts a service that uses Geofencing to determine if the user is 50m from Intrepid Labs
 * Requires: MainActivity.java, GeofenceTransitionsIntentService.java, SendSlackReciever.java
 */


public class MapsActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {

    public static final double INTREPID_LAT = 42.3671520;
    public static final double INTREPID_LONG = -71.0801970;
    public static final float GEOFENCE_METERS = 50;

    public static Geofence intrepidGeofence;
    public static GoogleApiClient mGoogleApiClient;

    @BindView(R.id.button_id)
    Button serviceStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        intrepidGeofence = new Geofence.Builder()
                .setRequestId("Intrepid_Geofence")
                .setCircularRegion(
                        INTREPID_LAT,
                        INTREPID_LONG,
                        GEOFENCE_METERS
                )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Timber.d("Added Timber to the project");
        }
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @OnClick(R.id.button_id)
    void startService() {
        Timber.d("Button has been clicked");

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.GeofencingApi.addGeofences(
                mGoogleApiClient,
                getGeofencingRequest(),
                getGeofencePendingIntent()
        ).setResultCallback(this);

    }

    private GeofencingRequest getGeofencingRequest() {
        Timber.d("Getting geofencing request");
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        Timber.d(intrepidGeofence.toString());
        builder.addGeofence(intrepidGeofence);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        Timber.d("Processing Geofence Intent");
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        return PendingIntent.getService(this, 0, intent, 0);
    }

    // Methods that are required, but not used.
    @Override
    public void onConnected(@Nullable Bundle bundle) { Timber.d("Google API connection success"); }

    @Override
    public void onConnectionSuspended(int i) { Timber.d("Google API connection failed"); }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { Timber.d("Google API connection failed"); }

    @Override
    public void onResult(@NonNull Status status) {
        Timber.d("Finished setting up Intrepid Geofence");
    }
}
