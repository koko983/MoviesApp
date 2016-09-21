package com.example.almowafy.moviesapp;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.almowafy.moviesapp.adapters.DetailAdapter;
import com.example.almowafy.moviesapp.data.MoviesDbHelper;
import com.example.almowafy.moviesapp.models.BaseMovie;
import com.example.almowafy.moviesapp.models.Movie;
import com.example.almowafy.moviesapp.models.Review;
import com.example.almowafy.moviesapp.models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by AlMowafy on 9/18/2016.
 */
public class DetailsFragment extends Fragment {
    private ListView mListView;
    private DetailAdapter m_adapter;
    private ArrayList<Object> OverallList;
    Movie detailedMovie;
    private int numOfReviews = 0;
    private int numOfTrailers = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_detail, container, false);

        //initialize references
        OverallList = new ArrayList<>();
        mListView = (ListView)fragmentView.findViewById(R.id.detail_list);

        try {
            detailedMovie = getArguments().getParcelable("Movie");
            //Movie detailedMovie = MainActivity.movies.get(index);
            OverallList.add(detailedMovie);
            m_adapter = new DetailAdapter(getActivity(), OverallList);
            mListView.setAdapter(m_adapter);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Couldn't fetch data about this movie", Toast.LENGTH_LONG).show();
            return fragmentView;
        }

        setHasOptionsMenu(true);
        //Update the adapter for the reviews/ trailers list
        GetTrailersReviews mTask = new GetTrailersReviews();
        mTask.execute(detailedMovie.getId());

        return fragmentView;
    }

    public void clearFragmentData(){
        m_adapter.clear();
    }


    /* The Menu stuff */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.deatil_menu, menu);
        final MenuItem action_favorite = menu.findItem(R.id.favorite_action);
        if(isMovieFavorite())
        {action_favorite.setIcon(R.drawable.star2);}
        else
        {action_favorite.setIcon(R.drawable.star1);}
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(OverallList.size()>0 && detailedMovie != null && m_adapter.getCount()>0){
            if (item.getItemId() == R.id.favorite_action)
            {
                if(isMovieFavorite()){
                    item.setIcon(R.drawable.star1);
                    removeFromFavorites(detailedMovie);
                }else{
                    item.setIcon(R.drawable.star2);
                    addToFavorite(detailedMovie);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void addToFavorite(Movie detailedMovie){
        MoviesDbHelper dbHelper = new MoviesDbHelper(getActivity());
        dbHelper.addNewFavorite(detailedMovie);
    }

    public void removeFromFavorites(BaseMovie movie){
        MoviesDbHelper dbHelper = new MoviesDbHelper(getActivity());
        dbHelper.deleteFavorite(movie.getId());
    }

    public boolean isMovieFavorite(){
        MoviesDbHelper dbHelper = new MoviesDbHelper(getActivity());
        return dbHelper.isMovieInFavs(this.detailedMovie);
    }



    public class GetTrailersReviews extends AsyncTask<Integer , Void, Void> {
        private final String LOG_TAG = GetTrailersReviews.class.getSimpleName();

        @Override
        protected Void doInBackground(Integer... ids) {
            String id = Integer.toString(ids[0]);

            // FIRST : REVIEWS
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            String baseUrl = "http://api.themoviedb.org/3/movie/" + id + "/videos?";
            String apiKey = "api_key=" + BuildConfig.API_KEY;
            String mySweetUrl = baseUrl.concat(apiKey);
            forecastJsonStr = DetailsActivity.getUrlContent(mySweetUrl);
            if(forecastJsonStr != null){
                try {
                    //ArrayList<Review> tempItems = new ArrayList<>();
                    JSONObject jsonRootObject = new JSONObject(forecastJsonStr);

                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray resultsJsonArray = jsonRootObject.optJSONArray("results");

                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < resultsJsonArray.length(); i++) {
                        JSONObject jsonVideo = resultsJsonArray.getJSONObject(i);
                        //int id = Integer.parseInt(jsonObject.optString("id").toString());
                        String watchId = jsonVideo.optString("key");
                        String name = jsonVideo.optString("name");

                        Trailer video = new Trailer(watchId, name);
                        //adding detailedMovie to the shared (movies)
                        OverallList.add(video);
                    }
                    numOfTrailers = resultsJsonArray.length();
                    //return tempItems;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



            mySweetUrl = mySweetUrl.replace("videos", "reviews");
            forecastJsonStr = DetailsActivity.getUrlContent(mySweetUrl);
            if(forecastJsonStr != null) {
                try {
                    //ArrayList<Review> tempItems = new ArrayList<>();
                    JSONObject jsonRootObject = new JSONObject(forecastJsonStr);

                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray = jsonRootObject.optJSONArray("results");

                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        //int id = Integer.parseInt(jsonObject.optString("id").toString());
                        String reviewer = jsonObject.optString("author");
                        String content = jsonObject.optString("content");

                        Review review = new Review(reviewer, content);
                        //adding detailedMovie to the shared (movies)
                        OverallList.add(review);
                    }
                    numOfReviews = jsonArray.length();
                    //return tempItems;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }














            return null;
        }

        @Override
        protected void onPostExecute(Void nothing) {
            if(OverallList != null){
                m_adapter = new DetailAdapter(getActivity(), OverallList);
                mListView.setAdapter(m_adapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(OverallList.get(position) instanceof Trailer){
                            Trailer currTrailer = (Trailer)OverallList.get(position);
                            String testUrl = currTrailer.getUrl();
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(testUrl));
                            //intent.setData(Uri.parse(currTrailer.getUrl()));
                            startActivity(intent);
                        }
                        if(OverallList.get(position) instanceof Review){
                            Review currReview = (Review) OverallList.get(position);
                            // custom dialog
                            final Dialog dialog = new Dialog(getActivity());
                            dialog.setContentView(R.layout.review_dialog);
                            dialog.setTitle("Review By: " + currReview.getReviewer());

                            TextView text = (TextView) dialog.findViewById(R.id.text);
                            text.setText(currReview.getContent());
                            dialog.show();
                        }
                    }
                });
            }
            else{
                Toast.makeText(getActivity(), "Network connection error!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
