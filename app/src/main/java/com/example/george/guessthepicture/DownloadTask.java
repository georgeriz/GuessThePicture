package com.example.george.guessthepicture;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
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
    private final int NOTIFICATION_ID = 1;
    private final int PENDING_INTENT_REQUEST_CODE = 0;
    private Context myContext;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;

    public DownloadTask(Context context) {
        myContext = context;
        Intent intent = new Intent(myContext, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(myContext,
                PENDING_INTENT_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotifyManager = (NotificationManager) myContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(myContext);
        mBuilder.setContentTitle("Images Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.icon_notification_cards_spades)
        .setContentIntent(pendingIntent);
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
                File file = new File(path, "new_image_" + i++ + ".jpeg");
                Bitmap bm = BitmapFactory.decodeStream(is);
                is.close();
                OutputStream os = new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.JPEG, 90, os);
                os.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            mBuilder.setProgress(10, i, false);
            mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Long result) {
        Log.i(MainActivity.TAG, "Download and save complete");
        Toast.makeText(myContext, "Download and save complete", Toast.LENGTH_LONG).show();
        mBuilder.setContentText("Download complete")
                .setProgress(0, 0, false);
        mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
