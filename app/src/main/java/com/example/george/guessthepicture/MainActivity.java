package com.example.george.guessthepicture;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {
    final static String TAG = "my_app_info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void onStartClicked(View v) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void onRefreshClicked(View v) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            Boolean wifi_only = sharedPref.getBoolean("pref_wifi_only", true);
            if (!wifi_only || networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                String state = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    GetURLsTask getURLsTask = new GetURLsTask();
                    getURLsTask.setUrlsDownloadListener(new GetURLsTask.UrlsDownloadListener() {
                        @Override
                        public void onUrlsDownloadSuccessful(String[] urls) {
                            DownloadTask downloadTask = new DownloadTask(getApplicationContext(),
                                    urls.length);
                            downloadTask.execute(urls);
                        }

                        @Override
                        public void onUrlsDownloadFail() {
                            Toast.makeText(getApplicationContext(),
                                    R.string.load_fail, Toast.LENGTH_LONG).show();
                        }
                    });
                    getURLsTask.execute(getString(R.string.main_url));
                } else {
                    Toast.makeText(this, R.string.external_storage_fail, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, R.string.wifi_fail, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, R.string.network_fail, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
