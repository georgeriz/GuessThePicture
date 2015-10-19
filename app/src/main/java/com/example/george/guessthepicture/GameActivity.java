package com.example.george.guessthepicture;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

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

    public static ArrayList<File> files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        File path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        files = new ArrayList<>();
        for (File child: path.listFiles()){
            files.add(child);
        }

        if(files.size() == 0) {
            Toast.makeText(getApplicationContext(), "Download first", Toast.LENGTH_LONG).show();
            finish();
        } else {
            // Instantiate a ViewPager and a PagerAdapter.
            mPager = (CustomViewPager) findViewById(R.id.pager);
            mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), files.size());
            mPager.setAdapter(mPagerAdapter);
        }
    }

    public void next() {
        mPager.setCurrentItem(mPager.getCurrentItem() + 1);
    }
}