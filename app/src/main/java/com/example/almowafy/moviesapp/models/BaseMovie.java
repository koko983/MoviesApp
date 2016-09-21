package com.example.almowafy.moviesapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by AlMowafy on 8/12/2016.
 */
public class BaseMovie implements Parcelable {
    protected int id;
    protected String Url;
    protected String subtitle;

//    public BaseMovie(String i, String y){
//        this.Url = i;
//        this.subtitle = y;
//    }

    public BaseMovie(int id, String url, String title){
        this.id = id;
        this.Url = url;
        this.subtitle = title;
    }

    protected BaseMovie(Parcel in){
        id = in.readInt();
        Url = in.readString();
        subtitle = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(Url);
        dest.writeString(subtitle);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<BaseMovie> CREATOR = new Parcelable.Creator<BaseMovie>() {
        public BaseMovie createFromParcel(Parcel in){
            return new BaseMovie(in);
        }

        @Override
        public BaseMovie[] newArray(int size) {
            return new BaseMovie[size];
        }
    };



    /* Setters & Getters */
    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        this.Url = url;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
}