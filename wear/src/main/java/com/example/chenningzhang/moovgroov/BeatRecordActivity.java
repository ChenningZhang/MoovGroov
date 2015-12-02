package com.example.chenningzhang.moovgroov;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

public class BeatRecordActivity extends Activity {
    private GoogleApiClient mGoogleApiClient;
    private static final String FINISH_WATCH_BEATS_ACTIVITY = "/finish_watch_beats_activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beat_record);

        // create Google Client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.d("WEAR BEATS", "Failed to connect");
                    }
                })
                .addApi(Wearable.API).build();

        mGoogleApiClient.connect();

        ImageButton newButton = (ImageButton) findViewById(R.id.tempButton);
        newButton.setImageResource(R.drawable.wear_beat_main);
        if (newButton != null)
            newButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendMessage(FINISH_WATCH_BEATS_ACTIVITY, "FINISH");
                    Log.d("WATCH BEAT", "Finish Beat");


                }
            });

    }

    // Send message through api to Mobile
    private void sendMessage( final String path, final String text ) {
        Log.d("MOBILE SEND MESSAGE", "Start Beat 1");
        new Thread( new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( mGoogleApiClient ).await();
                for(Node node : nodes.getNodes()) {
                    Log.d("MOBILE SEND MESSAGE", "Start Beat with NODES");
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mGoogleApiClient, node.getId(), path, text.getBytes() ).await();
                }
            }
        }).start();
    }
}
