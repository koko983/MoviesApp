package com.example.almowafy.moviesapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.almowafy.moviesapp.models.BaseMovie;
import com.example.almowafy.moviesapp.models.Movie;

import java.util.ArrayList;

/**
 * Created by AlMowafy on 9/16/2016.
 */
public class MoviesDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 6;

    private static final String TABLE_FAVORITES = "favorites";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_PUBLIC_ID = "public_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_IMG_URL = "img_path";
    private static final String COLUMN_PLOT = "plot";
    private static final String COLUMN_RELEASE_DATE = "release_date";
    private static final String COLUMN_RATING = "rating";


    public MoviesDbHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_FAVORITES + "("+
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COLUMN_PUBLIC_ID + " INTEGER UNIQUE, "+
                COLUMN_TITLE + " TEXT, "+
                COLUMN_IMG_URL + " TEXT, "+
                COLUMN_PLOT + " TEXT, " +
                COLUMN_RELEASE_DATE + " TEXT, " +
                COLUMN_RATING + " REAL " +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }

    //Add new favorite
    public void addNewFavorite(Movie movie){
        ContentValues values = new ContentValues();
        values.put(COLUMN_PUBLIC_ID, movie.getId());
        values.put(COLUMN_TITLE, movie.getSubtitle());
        values.put(COLUMN_IMG_URL, movie.getUrl());
        values.put(COLUMN_PLOT, movie.getPlotSummary());
        values.put(COLUMN_RELEASE_DATE, movie.getReleaseDate());
        values.put(COLUMN_RATING, movie.getRating());
        SQLiteDatabase db = getWritableDatabase();
        long id = db.insert(TABLE_FAVORITES, null, values);
        db.close();
    }

    //Delete a favorite
    public void deleteFavorite(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_FAVORITES+" WHERE "+ COLUMN_PUBLIC_ID + " = " + id + ";");
        db.close();
    }

    public ArrayList<Movie> getFavorites(){
        ArrayList<Movie> mList = new ArrayList<>();
        Movie baseMovie;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_FAVORITES + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            int id = c.getInt(c.getColumnIndex(COLUMN_PUBLIC_ID));
            String url = c.getString(c.getColumnIndex(COLUMN_IMG_URL));
            String title = c.getString(c.getColumnIndex(COLUMN_TITLE));
            String plot = c.getString(c.getColumnIndex(COLUMN_PLOT));
            String date = c.getString(c.getColumnIndex(COLUMN_RELEASE_DATE));
            float rating = c.getFloat(c.getColumnIndex(COLUMN_RATING));

            baseMovie = new Movie(id,
                    url,
                    title,
                    plot,
                    date,
                    rating
                    );
            mList.add(baseMovie);
            c.moveToNext();
        }
        db.close();
        return mList;
    }

    public boolean isMovieInFavs(BaseMovie baseMovie){
        SQLiteDatabase db = getReadableDatabase();
        try {
            String query = "SELECT * FROM " + TABLE_FAVORITES + " WHERE " + COLUMN_PUBLIC_ID + " = " + baseMovie.getId() + ";";
            Cursor c = db.rawQuery(query, null);
            //return c.getCount()>0;
            int myCount = c.getCount();
            boolean result = myCount > 0;
            return result;
        }
        finally {
            db.close();
        }
    }
}