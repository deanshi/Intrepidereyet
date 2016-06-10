package com.example.deanshi.intrepidereyet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import timber.log.Timber;

/**
 * Created by deanshi on 6/8/16.
 */
public class SendSlackReciever extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        SlackIOTask sendSlack = new SlackIOTask();
        sendSlack.execute();
        context.stopService(new Intent(context, GeofenceTransitionsIntentService.class));
    }


    public class SlackIOTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            MediaType jsonType = MediaType.parse("application/json; charset=utf-8");
            String jsonString = "{\"text\":\"I Am Here!\"}";
            URL url = null;
            try {
                url = new URL("https://hooks.slack.com/services/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");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Timber.d("URL Name Head: %s", url.toString());


            OkHttpClient okClient = new OkHttpClient();

            RequestBody slackBody = RequestBody.create(jsonType, jsonString);
            Request request = new Request.Builder()
                    .url(url)
                    .post(slackBody)
                    .build();

            try {
                Response response = okClient.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}