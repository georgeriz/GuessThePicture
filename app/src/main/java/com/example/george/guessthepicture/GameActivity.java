package com.example.george.guessthepicture;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
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

    private Custom punch;
    private int nCorrect;
    private int nTotal;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initialize();

        if(punch.size() == 0) {
            Toast.makeText(getApplicationContext(), "Download first", Toast.LENGTH_LONG).show();
            finish();
        } else {
            // Instantiate a ViewPager and a PagerAdapter.
            mPager = (CustomViewPager) findViewById(R.id.pager);
            PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), punch.size());
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

    private void initialize() {
        nCorrect = nTotal = 0;
        punch = new Custom();
        File path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        SharedPreferences sp = getSharedPreferences(DownloadTask.SHARED_PREFERENCES, 0);

        for (File child: path.listFiles()) {
            punch.add(child, sp.getBoolean(child.getName(), false));
        }
        //choose randomly
        punch.shuffle();
    }

    public File getFile(int index) {
        return punch.getFile(index);
    }

    public void setCorrectGuess(int index, boolean isCorrect) {
        //inform that this slides has been played
        punch.setPlayed(index);

        //check if slide was guessed correctly
        if(isCorrect) {
            nCorrect++;
        }
        nTotal++;

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
        SharedPreferences sp = getSharedPreferences(DownloadTask.SHARED_PREFERENCES, 0);
        SharedPreferences.Editor spe = sp.edit();
        spe.clear();
        for(int j = 0; j < punch.size(); j++) {
            spe.putBoolean(punch.getFile(j).getName(), punch.wasPlayed(j));
        }
        spe.apply();
        timer.cancel();
        finish();
    }
}