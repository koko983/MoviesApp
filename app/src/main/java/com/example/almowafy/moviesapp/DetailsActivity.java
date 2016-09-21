package com.example.almowafy.moviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.almowafy.moviesapp.models.Movie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailsActivity extends AppCompatActivity {
    Movie detailedMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        if (null == savedInstanceState) {
            DetailsFragment detailsFragment = new DetailsFragment();
            try {
                detailedMovie = intent.getParcelableExtra("Movie");
            } catch (Exception e) {
                Toast.makeText(DetailsActivity.this, "Couldn't fetch data about this movie", Toast.LENGTH_LONG).show();
                return;
            }
            Bundle extras = new Bundle();
            extras.putParcelable("Movie", detailedMovie);
            detailsFragment.setArguments(extras);
            getFragmentManager().beginTransaction().add(R.id.fl_detailFragment, detailsFragment).commit();
        }
    }

    /* Some helpers */
    public static String getUrlContent(String targetUrl){
        final String LOG_TAG = "getUrlContent Helper";
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String urlContent = "";
        try{
            URL url = new URL(targetUrl);
            // Create the request to Movie DB, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.
                return null;
            }
            urlContent = buffer.toString();
        }
        catch (IOException ex){
            Log.e(LOG_TAG,"Error", ex);
            return null;
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return urlContent;
    }
}