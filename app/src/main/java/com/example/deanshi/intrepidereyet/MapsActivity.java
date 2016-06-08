package com.example.deanshi.intrepidereyet;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MapsActivity extends AppCompatActivity {

    @BindView(R.id.button_id)
    Button serviceStart;

    public final static int SECOND_VALUE = 1;
    public final static double MILI_TO_SECOND = 1000;


    @OnClick(R.id.button_id)
    void startService() {
        Timber.d("Button has been clicked");
        Intent i = new Intent(this, MyService.class);
        startService(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Timber.d("Added Timber to the project");
        }

        ButterKnife.bind(this);

    }


}
