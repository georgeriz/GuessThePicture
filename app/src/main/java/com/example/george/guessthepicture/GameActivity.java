package com.example.george.guessthepicture;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;

public class GameActivity extends FragmentActivity
        implements TimeUpFragment.OnFragmentInteractionListener {
    final static String NUMBER_CORRECT = "com.example.george.guessthepicture.CORRECT";
    final static String NUMBER_TOTAL = "com.example.george.guessthepicture.TOTAL";

    private CustomViewPager mPager;

    private FileAndDetailsHolder holder;
    private int nCorrect;
    private int nTotal;
    private CountDownTimer timer;
    public SoundPool soundPool;
    public int[] soundIDs;
    private SharedPreferences sharedPreferences;
    private FragmentManager fragmentManager;
    private InterstitialAd mInterstitialAd;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if (!initialize()) {
            Toast.makeText(getApplicationContext(), R.string.download_images_first,
                    Toast.LENGTH_LONG).show();
            finish();
        } else {
            //first show a countdown fragment
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment = new CountDownFragment();
            fragmentTransaction.add(R.id.game_container, fragment, "countdown_fragment");
            fragmentTransaction.commit();

            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
            soundIDs = new int[5];
            soundIDs[0] = soundPool.load(getApplicationContext(), R.raw.correct, 1);
            soundIDs[1] = soundPool.load(getApplicationContext(), R.raw.wrong, 1);
            soundIDs[2] = soundPool.load(getApplicationContext(), R.raw.countdown, 1);
            soundIDs[3] = soundPool.load(getApplicationContext(), R.raw.start_game, 1);
            soundIDs[4] = soundPool.load(getApplicationContext(), R.raw.time_up, 1);

            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    requestNewInterstitial();
                    showResults();
                }
            });
            requestNewInterstitial();
        }
    }

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

    private void showAd() {
        if (mInterstitialAd.isLoaded()) {
            Log.i(MainActivity.TAG, "ad was Loaded");
            mInterstitialAd.show();
        } else {
            Log.i(MainActivity.TAG, "ad was not loaded");
            showResults();
        }
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("24819F61D704E0FB4247107D9081EDB9")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (hasFocus) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
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
        showAd();
        finish();
    }
}