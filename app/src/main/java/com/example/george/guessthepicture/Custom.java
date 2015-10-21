package com.example.george.guessthepicture;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by George on 2015-10-20.
 */
public class Custom {
    private class Item {
        private File file;
        private Boolean played;

        public Item(File file, Boolean played) {
            this.file = file;
            this.played = played;
        }

        public File getFile() { return file; }
        public Boolean getPlayed() { return played; }

        public void setPlayed() { played = true; }
    }
    private ArrayList<Item> list;

    public Custom() { list = new ArrayList<>(); }

    public void add(File f, boolean played) { list.add(new Item(f, played)); }

    public int size() {
        return list.size();
    }

    public File getFile(int index) {
        return list.get(index).getFile();
    }

    public boolean wasPlayed(int index) {
        return list.get(index).getPlayed();
    }

    public void setPlayed(int index) { list.get(index).setPlayed(); }

    public void shuffle() {
        Collections.shuffle(list);
        LinkedList<Item> linkedList = new LinkedList<>();
        for (Item item: list) {
            int r = new Random(System.nanoTime()).nextInt(10);
            if(item.getPlayed() && r < 9 ) {
                linkedList.addLast(item);
            } else {
                linkedList.addFirst(item);
            }
        }
        list.clear();
        list.addAll(linkedList);
    }
}
