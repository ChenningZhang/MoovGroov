/*
 * Copyright (C) 2014 Caleb Sabatini
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.csab.soundboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private SoundPlayer mSoundPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSoundPlayer = new SoundPlayer(this);
        Sound[] soundArray = SoundStore.getSounds(this);
        System.out.println(soundArray[11].getName());

        GridView gridView = (GridView) findViewById(R.id.gridView);

        final ArrayAdapter<Sound> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, soundArray);

        final HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        final ArrayList<String> beats = new ArrayList<String>();

        beats.add("beat5");
        beats.add("beat5");
        beats.add("beat6");


        hashMap.put("beat1", 0);
        hashMap.put("beat2",1);
        hashMap.put("beat3",2);
        hashMap.put("beat4", 3);
        hashMap.put("beat5", 11);
        hashMap.put("beat6", 12);


        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {

                while(true){
                    for (int i = 0; i <beats.size(); i++){
                        String pos = beats.get(i);
                        position = hashMap.get(pos);
                        Sound sound = (Sound) parent.getItemAtPosition(position);
                        mSoundPlayer.playSound(sound);
                        try {
                            Thread.sleep(1000);                 //1000 milliseconds is one second.
                        } catch(InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }


                    }



                }

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mSoundPlayer.release();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
