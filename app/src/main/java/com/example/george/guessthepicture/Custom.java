package com.example.george.guessthepicture;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by George on 2015-10-20.
 */
public class Custom {
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
        for (int j = 0; j < list.size(); j++) {
            int r = new Random(System.nanoTime()).nextInt(10);
            if(times.get(0) == 0) {
                if (r > 8) {
                    Collections.rotate(list, -1);
                    Collections.rotate(times, -1);
                }
            } else {
                if (r < 9) {
                    Collections.rotate(list, -1);
                    Collections.rotate(times, -1);
                }
            }
        }
    }
}
