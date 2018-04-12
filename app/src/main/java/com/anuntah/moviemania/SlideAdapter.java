package com.anuntah.moviemania;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SlideAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflator;

    public SlideAdapter(Context context){
        this.context = context;
    }


    //array
    public int[] slide_image = {

            R.drawable.ic_movie_white_48dp,
            R.drawable.ic_tv_white_48dp,
            R.drawable.ic_person_white_48dp

    };

    public String[] slide_headings = {

            "Find Movies",
            "Find TV Shows",
            "Keep Track & Share"

    };




    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (RelativeLayout) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflator = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflator.inflate(R.layout.slide_layout,container,false);

        ImageView SlideImageView = view.findViewById(R.id.slide_icon);
        TextView slideTextView = view.findViewById(R.id.slide_text);

        SlideImageView.setImageResource(slide_image[position]);
        slideTextView.setText(slide_headings[position]);

        container.addView(view);


        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }
}
