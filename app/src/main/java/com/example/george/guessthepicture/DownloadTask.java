package com.example.george.guessthepicture;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Created by George on 2015-10-18.
 */
public class DownloadTask extends AsyncTask<String, Integer, Long> {
    private Context myContext;

    public DownloadTask(Context context) {
        myContext = context;
    }

    @Override
    protected Long doInBackground(String... params) {
        File path = myContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        for (File file: path.listFiles()){
            file.delete();
        }
        int i = 0;
        for(String url: params) {
            try {
                InputStream is = (InputStream) new URL(url).getContent();
                File file = new File(path, "new_image_" + i++ + ".png");
                Bitmap bm = BitmapFactory.decodeStream(is);
                is.close();
                OutputStream os = new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.PNG, 90, os);
                os.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Long result) {
        Log.i(MainActivity.TAG, "Download and save complete");
        Toast.makeText(myContext, "Download and save complete", Toast.LENGTH_LONG).show();
    }
}
