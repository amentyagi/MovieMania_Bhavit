package com.anuntah.moviemania;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment {

    ArrayList<Movie> popular_movie=new ArrayList<>();

    RecyclerView recyclerpopular;

    private Retrofit retrofit=new Retrofit.Builder().baseUrl(Constants.base_url).addConverterFactory(GsonConverterFactory.create()).build();
    MovieAPI movieAPI=retrofit.create(MovieAPI.class);
    ArrayList<Movie> trailer_movielist=new ArrayList<>();

    public MoviesFragment() {
        // Required empty public constructor
    }


    MoviesRecyclerAdapter moviesRecyclerAdapter;
    RecyclerView recyclerView;
    TrailerRecyclerAdapter trailerRecyclerAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_movies, container, false);
        recyclerView=view.findViewById(R.id.recyclerInTheatre);
        recyclerpopular =view.findViewById(R.id.recyclerPopular);

        fetchTrailers();



        trailerRecyclerAdapter=new TrailerRecyclerAdapter(getContext(),trailer_movielist);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(trailerRecyclerAdapter);

        moviesRecyclerAdapter=new MoviesRecyclerAdapter(getContext(),popular_movie);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerpopular.setLayoutManager(layoutManager);
        recyclerpopular.setAdapter(moviesRecyclerAdapter);

        fetchPopularMovies();

        return view;
    }

    private void fetchPopularMovies() {
        Map<String,String> query=new HashMap<>();
        query.put(Constants.LANG,Constants.LANGUAGE);
        query.put(Constants.SORT_BY,Constants.POPULAR);
        Call<Movie_testclass> call=movieAPI.getPopularMovieList(query);
        call.enqueue(new Callback<Movie_testclass>() {
            @Override
            public void onResponse(Call<Movie_testclass> call, Response<Movie_testclass> response) {
                Log.d("result",response.toString());
                Movie_testclass movie_testclass=response.body();
                popular_movie=movie_testclass.getResults();
                moviesRecyclerAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(),popular_movie.size()+"",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Movie_testclass> call, Throwable t) {
                Toast.makeText(getContext(),"error",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchTrailers() {
        Map<String,String> query=new HashMap<>();
        query.put(Constants.LANG,Constants.LANGUAGE);

        Call<Movie_testclass> call=movieAPI.getUpcomingMovieList(query);
        call.enqueue(new Callback<Movie_testclass>() {
            @Override
            public void onResponse(Call<Movie_testclass> call, Response<Movie_testclass> response) {
                Movie_testclass results=response.body();
                Log.d("result",response.toString());
                ArrayList<Movie> upcoming_list=results.getResults();

                trailer_movielist=fetchVideos(upcoming_list);
                trailerRecyclerAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(),String.valueOf(trailer_movielist.size()),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Movie_testclass> call, Throwable t) {

            }
        });
    }

    private ArrayList<Movie> fetchVideos(final ArrayList<Movie> upcomingmovielist) {
        for (final Movie movie:upcomingmovielist) {
            Call<TrailersTestClass> call = movieAPI.getTrailerList(Integer.toString(movie.getId()));
            call.enqueue(new Callback<TrailersTestClass>() {
                @Override
                public void onResponse(Call<TrailersTestClass> call, Response<TrailersTestClass> response) {
                    TrailersTestClass results = response.body();
                    ArrayList<Trailers> trailersArrayList = results.getResults();
                    for(Trailers trailers:trailersArrayList){
                        if(trailers.getType().equals("Trailer")) {
                            trailer_movielist.add(new Movie(movie.getId(),movie.getTitle(),movie.getRelease_date(),movie.getPoster_path(),movie.getGenre_ids(),trailers.getKey(),movie.getBackdrop_path()));
                            break;
                        }
                    }
                    trailerRecyclerAdapter.notifyDataSetChanged();

                }

                @Override
                public void onFailure(Call<TrailersTestClass> call, Throwable t) {
                    Toast.makeText(getContext(),"nokey",Toast.LENGTH_SHORT).show();
                    Log.d("error",t.toString());
                }
            });

        }

        return trailer_movielist;
    }

}
