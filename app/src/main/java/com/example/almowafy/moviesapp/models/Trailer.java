package com.example.almowafy.moviesapp.models;

/**
 * Created by AlMowafy on 9/13/2016.
 */
public class Trailer{
    private String watchId; //key
    private String name;

    public Trailer(String watchId, String name) {
        this.watchId = watchId;
        this.name = name;
    }


    /* Setters & Getters */
    // IMPORTANT: if u ever save this to database, use this getter for the data to be consistent when being retrieved
    public String getWatchId() {
        return watchId;
    }

    public String getUrl(){return "https://www.youtube.com/watch?v=" + watchId;}

    public String getImageUrl(){return "http://img.youtube.com/vi/" + watchId + "/0.jpg";}

    public void setWatchId(String watchId) {
        this.watchId = watchId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}