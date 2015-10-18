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
    String[] urls = new String[]{
            "http://imageshack.com/a/img903/2326/1SxlwD.png",
            "http://imageshack.com/a/img911/3747/VfAtSv.jpg",
            "http://imageshack.com/a/img905/9069/U4Mn9z.png",
            "http://imageshack.com/a/img911/519/kWViay.png",
            "http://imageshack.com/a/img905/451/NV64JH.jpg",
            "http://imageshack.com/a/img908/6282/3q3c9n.jpg",
            "http://imageshack.com/a/img633/5608/TowVW5.jpg",
            "http://imageshack.com/a/img633/5922/WigLai.jpg",
            "http://imageshack.com/a/img909/5145/BJUcnf.jpg",
            "http://imageshack.com/a/img633/7128/orbP0e.jpg"
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
