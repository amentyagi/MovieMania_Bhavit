package com.anuntah.moviemania.Movies.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.anuntah.moviemania.Movies.Constants.Constants;
import com.anuntah.moviemania.R;
import com.anuntah.moviemania.Movies.Networking.Trailers;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VideosPagerAdapter extends PagerAdapter {

    private ArrayList<Trailers> trailers;
    private Context context;

    public interface setOnTrailerClicked{
        void OnVideoClicked(int pos);
    }

    setOnTrailerClicked listener;

    public VideosPagerAdapter(ArrayList<Trailers> trailers, Context context,setOnTrailerClicked listener) {
        this.trailers = trailers;
        this.context = context;
        this.listener=listener;
    }

    @Override
    public int getCount() {
        return trailers.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myImageLayout = inflater.inflate(R.layout.slide, view, false);
         ImageView ytthumbnail=myImageLayout
                .findViewById(R.id.ytthumb);

         ytthumbnail.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 listener.OnVideoClicked(position);
             }
         });
        Picasso.get().load(Constants.YTIMAGE_URI+""+trailers.get(position).getKey()+"/maxresdefault.jpg").fit().into(ytthumbnail);
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

}
