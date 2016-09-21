package com.example.almowafy.moviesapp;

import com.example.almowafy.moviesapp.models.Movie;

/**
 * Created by AlMowafy on 9/17/2016.
 */
public interface ItemSelectedListener {
    public void showMovieDetails(Movie detailedMovie);
    public void notifyForSortChange();
}