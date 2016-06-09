package com.example.deanshi.intrepidereyet;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
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
        SlackIOTask startSlack = new SlackIOTask();
        startSlack.execute();

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

            RequestBody slackBody = RequestBody.create(jsonType, jsonString);

            OkHttpClient okClient = new OkHttpClient();

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
