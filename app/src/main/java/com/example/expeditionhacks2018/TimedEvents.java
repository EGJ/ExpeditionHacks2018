package com.example.expeditionhacks2018;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Greg on 4/15/18.
 */

public class TimedEvents extends BroadcastReceiver {
    DataRelay dataRelay;
    @Override
    public void onReceive(Context context, Intent intent) {
        String someKey = intent.getStringExtra("UUID");
        dataRelay = (DataRelay) context.getApplicationContext();
        if (dataRelay.existingFences.get(someKey) == false)
        {
            //call the pin interface now.
            Intent i = new Intent(context, pinActivity.class);
            context.startActivity(i);

        }




    }
}
