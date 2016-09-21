package com.example.almowafy.moviesapp;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.almowafy.moviesapp.adapters.GridItemAdapter;
import com.example.almowafy.moviesapp.data.MoviesDbHelper;
import com.example.almowafy.moviesapp.models.BaseMovie;
import com.example.almowafy.moviesapp.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by AlMowafy on 9/17/2016.
 */
public class GridFragment extends Fragment {
    private final String SORT_POPULAR = "popular";
    private final String SORT_TOP = "top_rated";
    private final String SORT_FAV = "favorite";
    private final String POSITION_KEY = "position";
    private final String SORT_kEY = "sort_order";
    ItemSelectedListener IListener;

    int mPosition = ListView.INVALID_POSITION;
    String currentSort = SORT_POPULAR;
    GridView gridView;
    RelativeLayout progress;
    private GridItemAdapter m_adapter;
    public ArrayList<Movie> movies;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_grid, container, false);

        //progress = (RelativeLayout) fragmentView.findViewById(R.id.progress);
        gridView = (GridView)fragmentView.findViewById(R.id.gridView1);
        movies = new ArrayList<>();

        if(savedInstanceState != null)
        {
            //progress.setVisibility(View.GONE);
            if( savedInstanceState.containsKey(POSITION_KEY)){
                mPosition = savedInstanceState.getInt(POSITION_KEY);
            }
            if(savedInstanceState.containsKey(SORT_kEY)){
                currentSort = savedInstanceState.getString(SORT_kEY);
            }
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie dItem = movies.get(position);
                IListener.showMovieDetails(dItem);
                mPosition = position;
            }
        });

        setHasOptionsMenu(true);
        FetchMovies fetchMoviesTask = new FetchMovies();
        fetchMoviesTask.execute(currentSort);

        return fragmentView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mPosition != ListView.INVALID_POSITION)
            outState.putInt(POSITION_KEY, mPosition);
        outState.putString(SORT_kEY, currentSort);
    }

    public void setItemSelectedListener(ItemSelectedListener IListener){
        this.IListener = IListener;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FetchMovies weatherTask = new FetchMovies();
        switch (item.getItemId()) {
            case R.id.popular_option:
                weatherTask.execute("popular");
                //progress.setVisibility(View.VISIBLE);
                currentSort = SORT_POPULAR;
                IListener.notifyForSortChange();
                break;

            case R.id.top_rated_option:
                //progress.setVisibility(View.VISIBLE);
                weatherTask.execute("top_rated");
                currentSort = SORT_TOP;
                IListener.notifyForSortChange();
                break;

            case R.id.favorite_option:
                MoviesDbHelper dbHelper = new MoviesDbHelper(getActivity());
                // set the movies list in order to be ready for click listener
                movies = dbHelper.getFavorites();
                if (movies != null && movies.size()>0){
                    if(m_adapter!=null){
                        m_adapter.clear();
                    for(Movie movie : movies){
                        m_adapter.add(movie);
                    }
                    }else{
                        // have to cast each "Movie" element to its base to pass the whole list
                        ArrayList<BaseMovie> baseMovies = new ArrayList<>();
                        for(Movie movie: movies){
                            baseMovies.add(movie);
                        }
                        m_adapter = new GridItemAdapter(getActivity(), R.layout.list_item, baseMovies);
                        gridView.setAdapter(m_adapter);
                    }
                }
                else{
                    if(m_adapter!=null)
                        m_adapter.clear(); //clear the viewed list
                    Toast.makeText(getActivity(), "Nothing to view!", Toast.LENGTH_LONG).show();
                    break;
                }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

//    private ArrayList<Movie> getInitialGridItems(){
//        ArrayList<Movie> movies = new ArrayList<>();
//        String baseImgUrl = "http://image.tmdb.org/t/p/w185/";
//        for (int i =0; i<5; i++)
//            movies.add(new Movie(297761 ,baseImgUrl + "/e1mjopzAS2KNsvpbpahQ1a6SkSn.jpg","Suicide Squad", "That's a plot summary", "2016-08-03", 5.9f));
//        return movies;
//    }

    public class FetchMovies extends AsyncTask<String, Void, ArrayList<BaseMovie>> {

        private final String LOG_TAG = FetchMovies.class.getSimpleName();

        @Override
        protected ArrayList<BaseMovie> doInBackground(String... preferences) {
            //TODO : remove this at the end of testing the app
//            try {
//                Thread.sleep(3000);
//            } catch(InterruptedException ex) {
//                Thread.currentThread().interrupt();
//            }
            String sortParam;
            String baseImgUrl = "http://image.tmdb.org/t/p/w185/";

            if(preferences.length > 0) //array ain't empty
                sortParam = preferences[0];
            else
                sortParam = SORT_POPULAR; //default

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String forecastJsonStr = null;
            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                String baseUrl = "http://api.themoviedb.org/3/movie/" + sortParam + "?";
                String apiKey = "api_key=" + BuildConfig.API_KEY;
                String mySweetUrl = baseUrl.concat(apiKey);
                URL url = new URL(mySweetUrl);

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
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
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
            try {
                ArrayList<BaseMovie> tempBaseMovies = new ArrayList<>();
                JSONObject jsonRootObject = new JSONObject(forecastJsonStr);

                //Get the instance of JSONArray that contains JSONObjects
                JSONArray jsonArray = jsonRootObject.optJSONArray("results");
                movies.clear();

                //Iterate the jsonArray and print the info of JSONObjects
                for(int i=0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    //int id = Integer.parseInt(jsonObject.optString("id").toString());
                    int id = jsonObject.optInt("id");
                    String img_url = jsonObject.optString("poster_path");
                    img_url = baseImgUrl + img_url;
                    String title = jsonObject.optString("original_title");
                    String releaseDate = jsonObject.optString("release_date");
                    String plot = jsonObject.optString("overview");
                    String ratingStr = jsonObject.getString("vote_average");
                    float rating;
                    try{
                        rating = Float.parseFloat(ratingStr);
                    }
                    catch(Exception e){
                        rating = 0;
                    }
                    Movie dItem = new Movie(id, img_url, title, plot, releaseDate, rating);
                    BaseMovie tempBaseMovie = dItem;

                    // set the movies list in order to be ready for click listener
                    // adding detailedMovie to the shared (movies)
                    movies.add(dItem);
                    // adding tempBaseMovie to the returned (tempBaseMovies) to be displayed in UI
                    tempBaseMovies.add(tempBaseMovie);
                }
                return tempBaseMovies;
            } catch (JSONException e) {e.printStackTrace();}
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<BaseMovie> baseMovies) {
            if(getActivity()==null)
                return;
            //progress.setVisibility(View.GONE);
            if (baseMovies != null){
//                m_adapter.clear();
//                for(BaseMovie baseMovie : baseMovies){
//                    m_adapter.add(baseMovie);
//                }
                if(GridFragment.this == null)
                    Log.e("FRAG", "Fragment is still not instantiated");
                m_adapter = new GridItemAdapter(getActivity(), R.layout.list_item, baseMovies);
                gridView.setAdapter(m_adapter);
                if(mPosition != ListView.INVALID_POSITION)
                    gridView.smoothScrollToPosition(mPosition);
            }
            else{
                Toast.makeText(getActivity(), "Network connection error!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
