package com.example.george.guessthepicture;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by George on 2015-10-20.
 */
public class Custom implements Parcelable {
    private ArrayList<File> list;
    private ArrayList<Integer> times;

    public Custom() {
        list = new ArrayList<>();
        times = new ArrayList<>();
    }

    public void add(File f, int i) {
        list.add(f);
        times.add(i);
    }

    public int size() {
        return list.size();
    }

    public File getFile(int index) {
        return list.get(index);
    }

    public int getTimes(int index) {
        return times.get(index);
    }

    public void setTimes(int index) {
        times.set(index, 1);
    }

    public void shuffle() {
        long seed = System.nanoTime();
        Collections.shuffle(list, new Random(seed));
        Collections.shuffle(times, new Random(seed));
        ArrayList<File> up_list = new ArrayList<>();
        ArrayList<File> down_list = new ArrayList<>();
        ArrayList<Integer> up_times = new ArrayList<>();
        ArrayList<Integer> down_times = new ArrayList<>();
        for (int j = 0; j < list.size(); j++) {
            int r = new Random(System.nanoTime()).nextInt(10);
            if(times.get(j) != 0 && r < 9 ) {
                down_list.add(list.get(j));
                down_times.add(times.get(j));
            } else {
                up_list.add(list.get(j));
                up_times.add(times.get(j));
            }
        }
        up_list.addAll(down_list);
        up_times.addAll(down_times);
        list.clear();
        times.clear();
        list = up_list;
        times = up_times;
    }

    //for the parcelable
    public Custom(Parcel source) {
        list = source.readArrayList(null);
        times = source.readArrayList(null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(list);
        dest.writeList(times);
    }

    public static final Creator<Custom> CREATOR = new Creator<Custom>() {
        @Override
        public Custom createFromParcel(Parcel in) {
            return new Custom(in);
        }

        @Override
        public Custom[] newArray(int size) {
            return new Custom[size];
        }
    };
}
