package com.example.george.guessthepicture;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    final static String TAG = "my_app_info";
    String[] urls = new String[] {
            "http://i.imgur.com/7spzG.png",
            "http://i.imgur.com/7spzG.png",
            "http://i.imgur.com/7spzG.png",
            "http://i.imgur.com/7spzG.png",
            "http://i.imgur.com/7spzG.png",
            "http://i.imgur.com/7spzG.png","http://i.imgur.com/7spzG.png",
            "http://i.imgur.com/7spzG.png",
            "http://i.imgur.com/7spzG.png",
            "http://i.imgur.com/7spzG.png",
            "http://i.imgur.com/7spzG.png",
            "http://i.imgur.com/7spzG.png","http://i.imgur.com/7spzG.png","http://i.imgur.com/7spzG.png",
            "http://i.imgur.com/7spzG.png",
            "http://i.imgur.com/7spzG.png",
            "http://i.imgur.com/7spzG.png",
            "https://pixabay.com/static/uploads/photo/2014/08/18/23/11/paprika-421087_640.jpg"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadTask downloadTask = new DownloadTask(getApplicationContext());
        downloadTask.execute(urls);
    }

    public void onStartClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), GameActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
