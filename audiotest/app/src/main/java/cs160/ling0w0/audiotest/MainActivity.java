package cs160.ling0w0.audiotest;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button btnPlay1, btnPlay2, btnPlay3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlay1 = (Button) findViewById(R.id.btnP1);
        btnPlay2 = (Button) findViewById(R.id.btnP2);
        btnPlay3 = (Button) findViewById(R.id.btnP3);

        MediaRunnable mr1 = new MediaRunnable(this, R.raw.test);
        MediaRunnable mr2 = new MediaRunnable(this, R.raw.test1);
        MediaRunnable mr3 = new MediaRunnable(this, R.raw.test2);

        final Thread t1 = new Thread(mr1);
        final Thread t2 = new Thread(mr2);
        final Thread t3 = new Thread(mr3);

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

    }

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
}