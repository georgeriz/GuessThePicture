package com.example.george.guessthepicture;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class GameActivity extends FragmentActivity {
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private CustomViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    private Custom punch;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        punch = new Custom();
        File path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        sp = getSharedPreferences(DownloadTask.SHARED_PREFERENCES, 0);

        for (File child: path.listFiles()){
            punch.add(child, sp.getInt(child.getName(), 0));
        }
        //get "times" accessed

        //choose randomly
        //Collections.shuffle(files);
        punch.shuffle();
        //debugging
        for(int i = 0; i < punch.size(); i++){
            Log.i(MainActivity.TAG, "times: " + punch.getTimes(i));
        }

        if(punch.size() == 0) {
            Toast.makeText(getApplicationContext(), "Download first", Toast.LENGTH_LONG).show();
            finish();
        } else {
            // Instantiate a ViewPager and a PagerAdapter.
            mPager = (CustomViewPager) findViewById(R.id.pager);
            mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), punch.size());
            mPager.setAdapter(mPagerAdapter);
        }
    }

    public void next() {
        mPager.setCurrentItem(mPager.getCurrentItem() + 1);
    }

    public File getFile(int index) {
        return punch.getFile(index);
    }

    public void setTimes(int index) {
        punch.setTimes(index);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor spe = sp.edit();
        spe.clear();
        for(int j = 0; j < punch.size(); j++) {
            spe.putInt(punch.getFile(j).getName(), punch.getTimes(j));
        }
        spe.apply();
    }
}