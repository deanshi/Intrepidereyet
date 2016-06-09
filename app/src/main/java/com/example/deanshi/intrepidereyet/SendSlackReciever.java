package com.example.deanshi.intrepidereyet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;
import timber.log.Timber;

/**
 * Created by deanshi on 6/8/16.
 */
public class SendSlackReciever extends BroadcastReceiver {

    public static final String BASE_URL  = "https://hooks.slack.com/services/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";

    @Override
    public void onReceive(Context context, Intent intent) {
        SlackIOTask startSlack = new SlackIOTask();
        startSlack.execute();
        context.stopService(new Intent(context, GeofenceTransitionsIntentService.class));
    }


    public class SlackIOTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


            return null;
        }
    }

    public interface SlackService {
        @POST(BASE_URL)
        Call<Response> sendSlackMessage(@Body SlackUser user);

    }

    public class SlackUser {
        @SerializedName("text")
        String textVal;

        public SlackUser(String text) {
            this.textVal = text;
        }

    }



}