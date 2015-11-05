package com.example.george.guessthepicture;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetURLsTask extends AsyncTask<String, Void, String> {
    UrlsDownloadListener urlsDownloadListener;

    public void setUrlsDownloadListener(UrlsDownloadListener urlsDownloadListener) {
        this.urlsDownloadListener = urlsDownloadListener;
    }

    @Override
    protected String doInBackground(String... params) {
        return downloadUrl(params[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.isEmpty()) {
            urlsDownloadListener.onUrlsDownloadFail();
        } else {
            Log.i(MainActivity.TAG, "The result is: " + result);
            try {
                JSONObject jsonResult = new JSONObject(result);
                JSONArray jsonUrlArray = jsonResult.getJSONArray("results");
                int arraySize = jsonUrlArray.length();
                Log.i(MainActivity.TAG, "the size is: " + arraySize);
                String[] urlArray = new String[arraySize];
                for (int i = 0; i < arraySize; i++) {
                    urlArray[i] =jsonUrlArray.getString(i);
                    Log.i(MainActivity.TAG, "item: " + jsonUrlArray.getString(i));
                }
                urlsDownloadListener.onUrlsDownloadSuccessful(urlArray);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i(MainActivity.TAG, "json");
            }
        }
    }

    private String downloadUrl(String myUrl) {
        InputStream is;
        String response = "";
        try {
            URL url = new URL(myUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int response_code = connection.getResponseCode();
            if(response_code == HttpURLConnection.HTTP_OK) {
                is = connection.getInputStream();
                response = convertInputStreamToString(is);
                is.close();
            } else {
                Log.i(MainActivity.TAG, "response code: " + response_code + " response: " +
                        connection.getResponseMessage());
            }
            connection.disconnect();
        } catch (MalformedURLException e){
            e.printStackTrace();
            Log.i(MainActivity.TAG, "bad url");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(MainActivity.TAG, "i/o");
        }
        return response;
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line;
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
        return result;
    }

    public interface UrlsDownloadListener {
        void onUrlsDownloadSuccessful(String[] urls);
        void onUrlsDownloadFail();
    }
}
