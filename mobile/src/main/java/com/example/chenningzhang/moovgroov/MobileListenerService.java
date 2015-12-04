package com.example.chenningzhang.moovgroov;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

public class MobileListenerService extends WearableListenerService {
    private static final String FINISH_WATCH_BEATS_ACTIVITY = "/finish_watch_beats_activity";
    private static final String RECORD_BEATS_ACTIVITY = "/record_beats_activity";

    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("MOBILE BEAT", "Received FINISH for beat");

        if (messageEvent.getPath().equalsIgnoreCase(FINISH_WATCH_BEATS_ACTIVITY)){
            Toast.makeText(this, "MOBILE FINISH BEATS", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this , MainActivity.class);
            intent.putExtra("SOURCE", "beatComplete");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (messageEvent.getPath().equalsIgnoreCase(RECORD_BEATS_ACTIVITY)){
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Toast.makeText(this, "BEAT DETECTED: " + value, Toast.LENGTH_SHORT).show();
            // TODO Figure out what to do with this data
       } else {
            super.onMessageReceived(messageEvent);
        }

    }



}
