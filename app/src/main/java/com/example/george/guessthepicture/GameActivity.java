package com.example.george.guessthepicture;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

public class GameActivity extends FragmentActivity {
    final static String NUMBER_CORRECT = "com.example.george.guessthepicture.CORRECT";
    final static String NUMBER_TOTAL = "com.example.george.guessthepicture.TOTAL";
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private CustomViewPager mPager;

    private FileAndDetailsHolder holder;
    private int nCorrect;
    private int nTotal;
    private CountDownTimer timer;
    private SoundPool soundPool;
    private int[] soundIDs;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if(!initialize()) {
            Toast.makeText(getApplicationContext(), "Download first", Toast.LENGTH_LONG).show();
            finish();
            return;
        } else {
            // Instantiate a ViewPager and a PagerAdapter.
            mPager = (CustomViewPager) findViewById(R.id.pager);
            PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), holder.size());
            mPager.setAdapter(mPagerAdapter);
        }

        timer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                Toast.makeText(getApplicationContext(), "Result: " + nCorrect + "/" + nTotal,
                        Toast.LENGTH_LONG).show();
                showResults();
                finish();
            }
        };
        timer.start();
    }

    @SuppressWarnings("deprecation")
    private boolean initialize() {
        nCorrect = nTotal = 0;
        holder = new FileAndDetailsHolder();
        sharedPreferences = getSharedPreferences(DownloadTask.SHARED_PREFERENCES, 0);

        File path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (path == null) {
            return false;
        }
        for (File child: path.listFiles()) {
            holder.add(child, sharedPreferences.getBoolean(child.getName(), false));
        }

        holder.shuffle();

        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        soundIDs = new int[2];
        soundIDs[0] = soundPool.load(getApplicationContext(), R.raw.correct, 1);
        soundIDs[1] = soundPool.load(getApplicationContext(), R.raw.wrong, 1);

        return holder.size() > 0;
    }

    public File getFile(int index) {
        return holder.getFile(index);
    }

    public void setGuess(int index, boolean isCorrect) {
        //inform that this slides has been played
        holder.setPlayed(index);

        //check if slide was guessed correctly
        if (isCorrect) {
            nCorrect++;
            soundPool.play(soundIDs[0], 1,1,1,0,1);
        } else {
            soundPool.play(soundIDs[1], 1,1,1,0,1);
        }
        nTotal++;
    }

    public void next() {
        //move to next slide
        mPager.setCurrentItem(mPager.getCurrentItem() + 1);
    }

    private void showResults() {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra(NUMBER_CORRECT, nCorrect);
        intent.putExtra(NUMBER_TOTAL, nTotal);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sharedPreferences = getSharedPreferences(DownloadTask.SHARED_PREFERENCES, 0);
        SharedPreferences.Editor spe = sharedPreferences.edit();
        spe.clear();
        for(int j = 0; j < holder.size(); j++) {
            spe.putBoolean(holder.getFile(j).getName(), holder.wasPlayed(j));
        }
        spe.apply();
        timer.cancel();
        finish();
    }
}