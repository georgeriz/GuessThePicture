package com.example.george.guessthepicture;

import android.content.Intent;
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
import java.nio.charset.MalformedInputException;

/**
 * Created by George on 2015-11-04.
 */
public class GetURLsTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        return downloadUrl(params[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        Log.i(MainActivity.TAG, "The result is: " + result);
        try {
            JSONObject json = new JSONObject(result);
            JSONArray jra = json.getJSONArray("results");
            int size = jra.length();
            Log.i(MainActivity.TAG, "the size is: " + size);
            for (int i = 0; i < size; i++) {
                Log.i(MainActivity.TAG, "item: " + jra.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(MainActivity.TAG, "json");
        }
    }

    private String downloadUrl(String myUrl) {
        InputStream is = null;
        String response = "";
        try {
            URL url = new URL(myUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int response_code = connection.getResponseCode();
            if(response_code == HttpURLConnection.HTTP_OK) {
                is = connection.getInputStream();
                response = convertInputStreamToString(is);
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
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
}
