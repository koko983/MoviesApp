package com.example.almowafy.moviesapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.almowafy.moviesapp.R;
import com.example.almowafy.moviesapp.models.Movie;
import com.example.almowafy.moviesapp.models.Review;
import com.example.almowafy.moviesapp.models.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by AlMowafy on 9/13/2016.
 */
public class DetailAdapter extends BaseAdapter {
    private final List<Object> mData;
    private final Context mContext;

    private final Object mLock = new Object();

    public void clear() {
        synchronized (mLock) {
            if (mData != null) {
                mData.clear();
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public DetailAdapter(Context context, List<Object> objects) {
        //super();
        mData = objects;
        mContext = context;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        //Create view
        ViewGroup root;
        if(mData.get(position) instanceof Movie){
            //LinearLayout root;
            MovieViewHolder vh;
            if(convertView==null || !(((LinearLayout)convertView).getTag() instanceof MovieViewHolder)) {
                LayoutInflater inflater = (LayoutInflater)
                        mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                root = (LinearLayout) inflater.inflate(R.layout.movie, null);
                vh = new MovieViewHolder();
                vh.detail_poster = (ImageView)root.findViewById(R.id.detail_poster);
                vh.detail_title = (TextView)root.findViewById(R.id.detail_title);
                vh.plot = (TextView)root.findViewById(R.id.plot);
                vh.ratingBar = (RatingBar)root.findViewById(R.id.ratingBar);
                vh.release_date = (TextView)root.findViewById(R.id.release_date);
                root.setTag(vh);
            }else{
                root = (LinearLayout) convertView;
                vh = (MovieViewHolder) root.getTag();
            }

            //Get BaseMovie at position
            final Movie movie = (Movie)mData.get(position);

            //Bind Data
            vh.release_date.setText(movie.getReleaseDate());
            vh.ratingBar.setRating(movie.getRating()/2);
            vh.plot.setText(movie.getPlotSummary());
            vh.detail_title.setText(movie.getSubtitle());
            String mySweetUrl = movie.getUrl();
            Picasso.with(mContext)
                    .load(mySweetUrl)
                    .into(vh.detail_poster);
            return root;
        }

        else if(mData.get(position) instanceof Trailer){
            //LinearLayout root;
            TrailerViewHolder vh;
            if(convertView==null || !(((LinearLayout)convertView).getTag() instanceof TrailerViewHolder)) {
                LayoutInflater inflater = (LayoutInflater)
                        mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                root = (LinearLayout) inflater.inflate(R.layout.trailer_item, null);
                vh = new TrailerViewHolder();
                vh.trailer_title = (TextView)root.findViewById(R.id.trailer_title);
                vh.trailer_img = (ImageView) root.findViewById(R.id.trailer_img);
                root.setTag(vh);
            }else{
                root = (LinearLayout) convertView;
                vh = (TrailerViewHolder) root.getTag();
            }

            //Get BaseMovie at position
            final Trailer trailer = (Trailer)mData.get(position);

            //Bind Data
            Picasso.with(mContext)
                    .load(trailer.getImageUrl())
                    .into(vh.trailer_img);
            vh.trailer_title.setText(trailer.getName());
            return root;
        }

        else if(mData.get(position) instanceof Review){
            //LinearLayout root;
            ReviewViewHolder vh;
            if(convertView==null || !(((LinearLayout)convertView).getTag() instanceof ReviewViewHolder)) {
                LayoutInflater inflater = (LayoutInflater)
                        mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                root = (LinearLayout) inflater.inflate(R.layout.review_item, null);
                vh = new ReviewViewHolder();
                vh.Reviewer = (TextView)root.findViewById(R.id.reviewer);
                vh.content =(TextView)root.findViewById(R.id.content);
                vh.readMore = (TextView)root.findViewById(R.id.read_more);
                root.setTag(vh);
            }else{
                root = (LinearLayout) convertView;
                vh = (ReviewViewHolder) root.getTag();
             }


            //Get BaseMovie at position
            final Review review = (Review)mData.get(position);

            String reviewContent;
            if(review.getContent().length()>200)
            {
                reviewContent = review.getContent().substring(0, 197) + " ...";
                vh.readMore.setVisibility(View.VISIBLE);
            }
            else{
                reviewContent = review.getContent();
                vh.readMore.setVisibility(View.GONE);
            }

            //Bind Data
            vh.content.setText(reviewContent);
            vh.Reviewer.setText(review.getReviewer());

            return root;
        }
        else{
            //throw new Exception("Not supported datatype!");
            return null;
        }
    }

    class ReviewViewHolder {
        TextView  Reviewer;
        TextView content;
        TextView readMore;
    }

    class MovieViewHolder {
        ImageView detail_poster;
        TextView detail_title;
        TextView release_date;
        RatingBar ratingBar;
        TextView plot;
    }

    class TrailerViewHolder {
        TextView trailer_title;
        ImageView trailer_img;
    }
}