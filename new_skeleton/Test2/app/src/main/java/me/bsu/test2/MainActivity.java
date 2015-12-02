package me.bsu.test2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MAIN_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.projects),
                        new PrimaryDrawerItem().withName(R.string.beats),
                        new PrimaryDrawerItem().withName(R.string.sounds)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Log.d(TAG, String.format("Navigation drawer item %d clicked", position));
                        ProjectsFragment projFrag = (ProjectsFragment) getSupportFragmentManager()
                                .findFragmentByTag(ProjectsFragment.TAG);
                        BeatsFragment beatsFrag = (BeatsFragment) getSupportFragmentManager()
                                .findFragmentByTag(BeatsFragment.TAG);
                        SoundsFragment soundsFrag = (SoundsFragment) getSupportFragmentManager()
                                .findFragmentByTag(SoundsFragment.TAG);
                        switch (position) {
                            case 0:
                                if (projFrag == null) {
                                    Log.d(TAG, "creating new projects fragment");
                                    getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_fragment_container, new ProjectsFragment(), ProjectsFragment.TAG).addToBackStack(null).commit();
                                }
                                Log.d(TAG, "projects!");
                                break;
                            case 1:
                                if (beatsFrag == null) {
                                    Log.d(TAG, "creating new beats frag");
                                    getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_fragment_container, new BeatsFragment(), BeatsFragment.TAG).addToBackStack(null).commit();
                                }
                                Log.d(TAG, "beats!");
                                break;
                            case 2:
                                if (soundsFrag == null) {
                                    Log.d(TAG, "creating new sounds frag");
                                    getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_fragment_container, new SoundsFragment(), SoundsFragment.TAG).addToBackStack(null).commit();
                                }
                                Log.d(TAG, "sounds!");
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                })
                .build();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_activity_fragment_container, new ProjectsFragment(), ProjectsFragment.TAG)
                    .commit();
        }


    }

}
