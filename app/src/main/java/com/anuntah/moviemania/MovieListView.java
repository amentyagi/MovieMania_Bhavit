package com.anuntah.moviemania;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieListView extends AppCompatActivity {


    private Retrofit retrofit=new Retrofit.Builder().baseUrl(Constants.base_url).addConverterFactory(GsonConverterFactory.create()).build();
    MovieAPI movieAPI=retrofit.create(MovieAPI.class);

    RecyclerView recyclerView;
    ArrayList<Movie> topratedmovies=new ArrayList<>();

    ArrayList<Movie> upcomingmovie=new ArrayList<>();

    ArrayList<Movie> intheatres=new ArrayList<>();
    ArrayList<Movie> popular_movie=new ArrayList<>();


    MovieListAdapter upcomingRecyclerAdapter,popularRecyclerAdapter,intheatresRecyclerAdapter,topratedRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_movies);

        recyclerView=findViewById(R.id.recyclerview);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL));

        final Intent intent=getIntent();
        String type=intent.getStringExtra(Constants.UPCOMING);
        if(type.equals("upcoming")){
            fetchUpcomingMovies();
             upcomingRecyclerAdapter=new MovieListAdapter(new MovieListAdapter.setOnMovieClickListner() {
                @Override
                public void OnMovieClicked(int pos) {
                    ArrayList<Integer> idlist=new ArrayList<>();
//                    for(Movie movie:upcomingmovie){
//                        idlist.add(movie.getId());
//                    }
//                    Intent intent1=new Intent(MovieListView.this,MovieDetail.class);
//                    intent1.putIntegerArrayListExtra(Constants.ID,idlist);
//                    startActivity(intent1);
                }
            }, this, upcomingmovie);
             recyclerView.setAdapter(upcomingRecyclerAdapter);
        }
        if(type.equals("popular")){
            fetchPopularMovies();

            popularRecyclerAdapter=new MovieListAdapter(new MovieListAdapter.setOnMovieClickListner() {
                @Override
                public void OnMovieClicked(int pos) {
                    ArrayList<Integer> idlist=new ArrayList<>();
                    for(Movie movie:popular_movie){
                        idlist.add(movie.getId());
                    }
                    Intent intent1=new Intent(MovieListView.this,MovieDetail.class);
                    intent1.putIntegerArrayListExtra(Constants.ID,idlist);
                }
            }, this, popular_movie);
            recyclerView.setAdapter(popularRecyclerAdapter);
        }
        if(type.equals("intheatres")){
            fetchNowShowingMovies();

            intheatresRecyclerAdapter=new MovieListAdapter(new MovieListAdapter.setOnMovieClickListner() {
                @Override
                public void OnMovieClicked(int pos) {
                    ArrayList<Integer> idlist=new ArrayList<>();
                    for(Movie movie:intheatres){
                        idlist.add(movie.getId());
                    }
                    Intent intent1=new Intent(MovieListView.this,MovieDetail.class);
                    intent1.putIntegerArrayListExtra(Constants.ID,idlist);
                }
            }, this, intheatres);
            recyclerView.setAdapter(intheatresRecyclerAdapter);
        }
        if(type.equals("toprated")){
            fetchTopratedMovies();

            topratedRecyclerAdapter=new MovieListAdapter(new MovieListAdapter.setOnMovieClickListner() {
                @Override
                public void OnMovieClicked(int pos) {
                    ArrayList<Integer> idlist=new ArrayList<>();
                    for(Movie movie:topratedmovies){
                        idlist.add(movie.getId());
                    }
                    Intent intent1=new Intent(MovieListView.this,MovieDetail.class);
                    intent1.putIntegerArrayListExtra(Constants.ID,idlist);
                }
            }, this, topratedmovies);
            recyclerView.setAdapter(topratedRecyclerAdapter);
        }

    }

    private void fetchTopratedMovies() {

        Call<Movie_testclass> call=movieAPI.getTopRated(20);
        call.enqueue(new Callback<Movie_testclass>() {
            @Override
            public void onResponse(Call<Movie_testclass> call, Response<Movie_testclass> response) {
                topratedmovies.clear();
                Log.d("result",response.toString());
                Movie_testclass movie_testclass=response.body();
                if (movie_testclass != null) {
                    topratedmovies.addAll(movie_testclass.getResults());
                }
                topratedRecyclerAdapter.notifyDataSetChanged();
//                Toast.makeText(getContext(),topratedmovies.size()+"",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Movie_testclass> call, Throwable t) {

            }
        });
    }

    private void fetchNowShowingMovies() {
        Call<Movie_testclass> call=movieAPI.getNowShowing(20);
        call.enqueue(new Callback<Movie_testclass>() {
            @Override
            public void onResponse(Call<Movie_testclass> call, Response<Movie_testclass> response) {
                Movie_testclass testclass=response.body();
                intheatres.addAll(testclass.getResults());
                intheatresRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Movie_testclass> call, Throwable t) {

            }
        });
    }

    private void fetchPopularMovies() {
        Map<String,String> query=new HashMap<>();
        query.put(Constants.LANG,Constants.LANGUAGE);
        query.put(Constants.SORT_BY,Constants.POPULAR);
        Call<Movie_testclass> call=movieAPI.getPopularMovieList(20);
        call.enqueue(new Callback<Movie_testclass>() {
            @Override
            public void onResponse(Call<Movie_testclass> call, Response<Movie_testclass> response) {
                popular_movie.clear();
                Log.d("result",response.toString());
                Movie_testclass movie_testclass=response.body();
                if (movie_testclass != null) {
                    popular_movie.addAll(movie_testclass.getResults());
                }
                popularRecyclerAdapter.notifyDataSetChanged();
//                Toast.makeText(getContext(),popular_movie.size()+"",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Movie_testclass> call, Throwable t) {
                Toast.makeText(MovieListView.this,"error",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void fetchUpcomingMovies() {

        Map<String,String> query=new HashMap<>();
        query.put(Constants.LANG,Constants.LANGUAGE);

        Call<Movie_testclass> call=movieAPI.getUpcomingMovieList(query,20);
        call.enqueue(new Callback<Movie_testclass>() {
            @Override
            public void onResponse(Call<Movie_testclass> call, Response<Movie_testclass> response) {
                Movie_testclass testclass=response.body();
                Log.d("yes",response.toString());
                upcomingmovie.addAll(testclass.getResults());
                upcomingRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Movie_testclass> call, Throwable t) {

            }
        });
    }
}
