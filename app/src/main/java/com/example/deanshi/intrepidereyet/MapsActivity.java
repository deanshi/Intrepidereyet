package com.example.deanshi.intrepidereyet;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MapsActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.button_id)
    Button serviceStart;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = MapsActivity.class.getSimpleName();


    @OnClick(R.id.button_id)
    void startService() {
        Timber.d("Button has been clicked");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();



        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Timber.d("Added Timber to the project");
        }

        ButterKnife.bind(this);

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Timber.d("Connection established");
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
