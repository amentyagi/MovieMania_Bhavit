package com.anuntah.moviemania.TvShows;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anuntah.moviemania.Movies.Constants.Constants;
import com.anuntah.moviemania.Movies.Networking.MovieAPI;
import com.anuntah.moviemania.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class TvFragment extends Fragment {

    Retrofit retrofit;
    MovieAPI movieAPI;




    public TvFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_tv, container, false);

        retrofit=new Retrofit.Builder().baseUrl(Constants.base_url).addConverterFactory(GsonConverterFactory.create()).build();
        movieAPI=retrofit.create(MovieAPI.class);

        setUpTvShowsFragment();

        return view;
    }

    void setUpTvShowsFragment() {

        fetchTvShowsOnAirToday();

    }

    void fetchTvShowsOnAirToday() {



    }

}
