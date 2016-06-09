package com.example.deanshi.intrepidereyet;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import timber.log.Timber;

/**
 * Created by deanshi on 6/8/16.
 */
public class SendSlackActivity extends AppCompatActivity {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param Used to name the worker thread, important only for debugging.
     *
     *
     *
     *
     *
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendslack);




    }


}
