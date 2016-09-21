package com.example.almowafy.moviesapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.almowafy.moviesapp.R;
import com.example.almowafy.moviesapp.models.BaseMovie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by AlMowafy on 8/12/2016.
 */
public class GridItemAdapter extends ArrayAdapter<BaseMovie> {
    private final List<BaseMovie> mData;
    private final Context mContext;

    public GridItemAdapter(Context context, int resource, List<BaseMovie> objects) {
        super(context, resource, objects);
        mData = objects;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Create view
        ViewGroup root;
        ViewHolder vh;
        if(convertView==null) {
            LayoutInflater inflater = (LayoutInflater)
                    mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            root = (ViewGroup) inflater.inflate(R.layout.list_item, null);
            vh = new ViewHolder();
            vh.poster = (ImageView) root.findViewById(R.id.poster);
            //vh.subtitleTV = (TextView) root.findViewById(R.id.subtitle);
            root.setTag(vh);
        }else{
            root = (ViewGroup) convertView;
            vh = (ViewHolder) root.getTag();
        }

        //Get BaseMovie at position
        final BaseMovie baseMovie = mData.get(position);
        //Bind Data
        Picasso.with(mContext)
                .load(baseMovie.getUrl())
                .into(vh.poster);
        vh.poster.setContentDescription(baseMovie.getSubtitle());
        //vh.subtitleTV.setText(baseMovie.getSubtitle());

        return root;
    }

    class ViewHolder {
        ImageView poster;
        //TextView subtitleTV;
    }
}