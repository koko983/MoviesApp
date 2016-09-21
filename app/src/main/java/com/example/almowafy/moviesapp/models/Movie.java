package com.example.almowafy.moviesapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie extends BaseMovie implements Parcelable {

    String plotSummary;
    String releaseDate;
    float rating;

    public Movie(int id, String url, String title, String plotSummary, String releaseDate, float rating){
        super(id, url, title);
        this.plotSummary = plotSummary;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }

    /* Parcelable class members */
    private Movie(Parcel in){
        super(in);
        plotSummary = in.readString();
        releaseDate = in.readString();
        rating = in.readFloat();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(plotSummary);
        dest.writeString(releaseDate);
        dest.writeFloat(rating);
    }

    @Override
    public int describeContents() {
        return 0;
    }



    /* Setters & Getters */
    public String getPlotSummary() {
        return plotSummary;
    }

    public void setPlotSummary(String plotSummary) {
        this.plotSummary = plotSummary;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
