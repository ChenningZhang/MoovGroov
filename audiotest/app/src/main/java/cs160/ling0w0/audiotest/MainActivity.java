package cs160.ling0w0.audiotest;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button btnPlay1, btnPlay2, btnPlay3;
    Button btnPlayLoop1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //button setup
        btnPlay1 = (Button) findViewById(R.id.btnP1);
        btnPlay2 = (Button) findViewById(R.id.btnP2);
        btnPlay3 = (Button) findViewById(R.id.btnP3);
        btnPlayLoop1 = (Button) findViewById(R.id.btnL1);

        //runnable setup
        MediaRunnable mr1 = new MediaRunnable(this, R.raw.test);
        MediaRunnable mr2 = new MediaRunnable(this, R.raw.test1);
        MediaRunnable mr3 = new MediaRunnable(this, R.raw.snare);

        //demo loop: |...|...|...|...
        int[] l1 = new int[16];
        for (int i = 0; i < 16; i++){
            if (i%4 == 0) {
                l1[i] = 1;
            } else {
                l1[i] = 0;
            }
        }
        // LoopRunnable(context, int: sound resource id,
        //              int[16]: loop data, int: beats per minute i.e. bpm)
        LoopRunnable lr1 = new LoopRunnable(this, R.raw.snare, l1, 120);

        final Thread t1 = new Thread(mr1);
        final Thread t2 = new Thread(mr2);
        final Thread t3 = new Thread(mr3);
        final Thread t4 = new Thread(lr1);

        btnPlay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.run();
            }
        });
        btnPlay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t2.run();
            }
        });
        btnPlay3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t3.run();
            }
        });
        btnPlayLoop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t4.run();
            }
        });

    }

    //runnable class for track playback
    //constructor: Context app context, int resource id
    //put into a new thread and do thread.run() to toggle play/stop
    private class MediaRunnable implements Runnable {
        MediaPlayer mPlayer;
        Context mContext;
        int mResourceID;

        public MediaRunnable(Context c, int resid){
            mPlayer = new MediaPlayer();
            mContext = c;
            mResourceID = resid;
        }

        @Override
        public void run(){

            if (mPlayer != null) {
                if (mPlayer.isPlaying()){
                    mPlayer.stop();
                    mPlayer.reset();
                } else {
                    mPlayer.reset();
                    try {
                        AssetFileDescriptor afd =
                                mContext.getResources().openRawResourceFd(mResourceID);
                        if (afd == null)
                            return;
                        mPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                        afd.close();
                        mPlayer.prepare();
                    } catch (IOException | IllegalArgumentException | SecurityException e) {
                        Log.e("MAIN", e.getMessage());
                    }
                    mPlayer.start();
                }
            } else {
                mPlayer = MediaPlayer.create(mContext, mResourceID);
            }
        }
    }

    //runnable class for loop playback
    //constructor: Context app context, int Resource ID, int[16] loop data (1/0) and int bpm
    //put into a new thread and do thread.run() to toggle play/stop
    private class LoopRunnable implements Runnable {

        MediaPlayer mPlayer;
        CountDownTimer mTimer;
        Context mContext;
        int mResourceID;
        int[] mLoop;
        int mBpm;
        boolean isPlaying;


        public LoopRunnable(Context c, int resid, int[] lp, int bpm){
            mPlayer = new MediaPlayer();
            mTimer = new CountDownTimer(1000000, 60000/bpm) {
                int count = 0;
                public void onTick(long millisUntilFinished) {


                    if (mPlayer.isPlaying()) {
                        mPlayer.stop();
                    }
                    if (mLoop[count] == 1) {
                        mPlayer.start();
                    }

                    if (count < 15) {
                        count++;
                    } else {
                        count = 0;
                    }
                }
                public void onFinish() {
                    count = 0;
                    mTimer.start();
                }
            };

            mContext = c;
            mResourceID = resid;
            mLoop = lp;
            mBpm = bpm;
            isPlaying = false;
        }

        @Override
        public void run(){
            mPlayer = MediaPlayer.create(mContext, mResourceID);
            if (isPlaying) {
                isPlaying = false;
                mTimer.cancel();
            } else {
                isPlaying = true;
                mTimer.start();
            }
        }
    }
}