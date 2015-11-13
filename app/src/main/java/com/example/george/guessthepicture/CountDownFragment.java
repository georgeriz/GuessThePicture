package com.example.george.guessthepicture;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class CountDownFragment extends Fragment {
    GameActivity gameActivity;
    ImageView mImageView;
    CountDownTimer countDownTimer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameActivity = (GameActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_countdown,
                container, false);
        mImageView = (ImageView) rootView.findViewById(R.id.countdown_image);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        countDownTimer = new CountDownTimer(4500, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished > 4300) {
                    gameActivity.soundPool.play(gameActivity.soundIDs[2], 1, 1, 1, 0, 1);
                } else if (millisUntilFinished < 3000 && millisUntilFinished > 2800) {
                    Picasso.with(gameActivity).load(R.drawable.miniwikia_button_2).into(mImageView);
                    gameActivity.soundPool.play(gameActivity.soundIDs[2], 1, 1, 1, 0, 1);
                } else if (millisUntilFinished < 1500 && millisUntilFinished > 1300) {
                    Picasso.with(gameActivity).load(R.drawable.miniwikia_button_1).into(mImageView);
                    gameActivity.soundPool.play(gameActivity.soundIDs[2], 1, 1, 1, 0, 1);
                }
            }

            @Override
            public void onFinish() {
                gameActivity.soundPool.play(gameActivity.soundIDs[3], 1, 1, 1, 0, 1);
                gameActivity.startGame();
            }
        };
        countDownTimer.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (countDownTimer != null)
            countDownTimer.cancel();
    }
}
