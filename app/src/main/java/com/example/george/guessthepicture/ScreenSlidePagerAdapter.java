package com.example.george.guessthepicture;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    private final int mSize;

    public ScreenSlidePagerAdapter(FragmentManager fm, int size) {
        super(fm);
        mSize = size;
    }

    @Override
    public Fragment getItem(int position) {
        return ScreenSlidePageFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return mSize;
    }
}
