package com.example.george.guessthepicture;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.widget.Toast;

import java.io.File;

public class GameActivity extends FragmentActivity implements TimeUpFragment.OnFragmentInteractionListener{
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
    public SoundPool soundPool;
    public int[] soundIDs;
    private SharedPreferences sharedPreferences;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if (!initialize()) {
            Toast.makeText(getApplicationContext(), "Download first", Toast.LENGTH_LONG).show();
            finish();
        } else {
            //first show a countdown fragment
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment = new CountDownFragment();
            fragmentTransaction.add(R.id.game_container, fragment, "countdown_fragment");
            fragmentTransaction.commit();
        }
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
        for (File child : path.listFiles()) {
            holder.add(child, sharedPreferences.getBoolean(child.getName(), false));
        }

        holder.shuffle();

        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        soundIDs = new int[4];
        soundIDs[0] = soundPool.load(getApplicationContext(), R.raw.correct, 1);
        soundIDs[1] = soundPool.load(getApplicationContext(), R.raw.wrong, 1);
        soundIDs[2] = soundPool.load(getApplicationContext(), R.raw.countdown, 1);
        soundIDs[3] = soundPool.load(getApplicationContext(), R.raw.start_game, 1);

        return holder.size() > 0;
    }

    public void startGame() {
        //remove the countdown fragment
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragmentManager.findFragmentByTag("countdown_fragment"));
        fragmentTransaction.commit();
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (CustomViewPager) findViewById(R.id.pager);
        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), holder.size());
        mPager.setAdapter(mPagerAdapter);

        timer = new CountDownTimer(8000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished <= 5000) {
                    soundPool.play(soundIDs[2], 1, 1, 1, 0, 1);
                }
            }

            @Override
            public void onFinish() {
                mPager.setVisibility(View.GONE);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = new TimeUpFragment();
                fragmentTransaction.add(R.id.game_container, fragment, "timeup_fragment");
                fragmentTransaction.commit();
            }
        };
        timer.start();
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
            soundPool.play(soundIDs[0], 1, 1, 1, 0, 1);
        } else {
            soundPool.play(soundIDs[1], 1, 1, 1, 0, 1);
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
        for (int j = 0; j < holder.size(); j++) {
            spe.putBoolean(holder.getFile(j).getName(), holder.wasPlayed(j));
        }
        spe.apply();
        if (timer != null)
            timer.cancel();
        if (!isFinishing())
            finish();
    }

    @Override
    public void onFragmentInteraction() {
        showResults();
        finish();
    }
}