package com.example.deanshi.intrepidereyet;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;

import timber.log.Timber;

/**
 * Created by deanshi on 6/8/16.
 */
public class SendSlackActivity extends Activity {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SendSlackActivity(String name) {
        super(name);
    }

}
