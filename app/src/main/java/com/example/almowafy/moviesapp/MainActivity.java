package com.example.almowafy.moviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.example.almowafy.moviesapp.models.Movie;

public class MainActivity extends AppCompatActivity implements ItemSelectedListener {
    final String GRID_FRAGMENT_TAG = "gridfragment";
    final String DETAILS_FRAGMENT_TAG = "detailsfragment";
    boolean mTwoPane;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout flPanel2 = (FrameLayout) findViewById(R.id.fl_panelTwo);
        if (null == flPanel2) {
            mTwoPane = false;
        } else {
            mTwoPane = true;
        }

        if (null == savedInstanceState) {
            GridFragment gridFragment = new GridFragment();
            gridFragment.setItemSelectedListener(this);
            getFragmentManager().beginTransaction().replace(R.id.fl_panelOne, gridFragment, GRID_FRAGMENT_TAG).commit();
        }else{
            GridFragment gridFragment = (GridFragment)getFragmentManager().findFragmentByTag(GRID_FRAGMENT_TAG);
            gridFragment.setItemSelectedListener(this);
        }
    }

    @Override
    public void showMovieDetails(Movie detailedMovie) {
        if (mTwoPane) {
            DetailsFragment detailsFragment= new DetailsFragment();
            Bundle extras = new Bundle();
            extras.putParcelable("Movie", detailedMovie);
            detailsFragment.setArguments(extras);
            getFragmentManager().beginTransaction().replace(R.id.fl_panelTwo, detailsFragment, DETAILS_FRAGMENT_TAG).commit();
        } else {
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent.putExtra("Movie", detailedMovie);
        startActivity(intent);}
    }

    @Override
    public void notifyForSortChange() {
        if(mTwoPane){
            DetailsFragment detailsFragment = (DetailsFragment)getFragmentManager().findFragmentByTag(DETAILS_FRAGMENT_TAG);
            detailsFragment.clearFragmentData();
        }
    }
}