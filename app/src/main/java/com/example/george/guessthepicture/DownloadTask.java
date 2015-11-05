package com.example.george.guessthepicture;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by George on 2015-10-18.
 */
public class DownloadTask extends AsyncTask<String, Integer, Integer> {
    private final int NOTIFICATION_ID = 1;
    private final int PENDING_INTENT_REQUEST_CODE = 0;
    static final String SHARED_PREFERENCES = "com.example.george.guessthepicture.SHARED_PREFERENCES";
    private final Context myContext;
    private final int maxNumberOfFiles;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private File pictures_path;

    public DownloadTask(Context context, int maxNumberOfFiles) {
        myContext = context;
        this.maxNumberOfFiles = maxNumberOfFiles;
        pictures_path = myContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Intent intent = new Intent(myContext, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(myContext,
                PENDING_INTENT_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotifyManager = (NotificationManager) myContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(myContext);
        mBuilder.setContentTitle("Images Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.ic_get_app_white_24dp)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
    }

    @Override
    protected Integer doInBackground(String... params) {
        for (File file : pictures_path.listFiles()) {
            file.delete();
        }
        int numberOfFile = 0;
        for (String url : params) {
            try {
                //input stream from url
                InputStream is = (InputStream) new URL(url).getContent();
                Bitmap bm = BitmapFactory.decodeStream(is);
                is.close();
                //output stream to file
                File file = new File(pictures_path, "new_image_" + numberOfFile++ + ".jpeg");
                OutputStream os = new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.JPEG, 90, os);
                os.close();
                //publish progress
                publishProgress(numberOfFile);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.i(MainActivity.TAG, "bad url");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.i(MainActivity.TAG, "file not found");
            } catch (IOException e) {
                e.printStackTrace();
                Log.i(MainActivity.TAG, "i/o");
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        mBuilder.setProgress(maxNumberOfFiles, progress[0], false);
        mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    @Override
    protected void onPostExecute(Integer result) {
        Log.i(MainActivity.TAG, "Download and save complete");
        Toast.makeText(myContext, "Download and save complete", Toast.LENGTH_LONG).show();
        mBuilder.setContentText("Download complete").setProgress(0, 0, false);
        mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
        updateSharedPreferences();
    }

    private void updateSharedPreferences() {
        SharedPreferences sharedPreferences = myContext.getSharedPreferences(SHARED_PREFERENCES, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        for(File child: pictures_path.listFiles()) {
            editor.putBoolean(child.getName(), false);
        }
        editor.apply();
    }
}
