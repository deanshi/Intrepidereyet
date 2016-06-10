package com.example.deanshi.intrepidereyet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.gson.annotations.SerializedName;

import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import timber.log.Timber;

/**
 * Created by deanshi on 6/8/16.
 */
public class SendSlackReciever extends BroadcastReceiver {

    public static final String BASE_URL = "https://hooks.slack.com/services/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/";

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
                    .baseUrl("https://hooks.slack.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            SlackService slackService = retrofit.create(SlackService.class);

            slackService.sendSlackMessage("{\"text\":\"Retrofit Example Text\"}").enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    Timber.d("Got response from server: %s", response.body());
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Timber.d("Failed to connect to server");
                }
            });

            return null;
        }
    }

    public interface SlackService {
        @Headers({"Content-type: application/json"})
        @POST("services/T026B13VA/B1FD8L8DN/tM77kIegYKZ78z4oF4reBjVQ")
        Call<String> sendSlackMessage(@Body String message);

    }

    public class SlackUser {
        @SerializedName("text")
        String textVal;

        public SlackUser(String text) {
            this.textVal = text;
        }

    }


}