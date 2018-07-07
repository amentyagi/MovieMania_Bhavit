package com.anuntah.moviemania.TvShows;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.anuntah.moviemania.MovieDatabase;
import com.anuntah.moviemania.Movies.Adapter.MovieListAdapter;
import com.anuntah.moviemania.Movies.Constants.Constants;
import com.anuntah.moviemania.Movies.MovieDetail;
import com.anuntah.moviemania.Movies.Networking.Movie;
import com.anuntah.moviemania.Movies.Networking.Movie_testclass;
import com.anuntah.moviemania.R;
import com.anuntah.moviemania.TvShows.Adapter.TvListAdapter;
import com.anuntah.moviemania.TvShows.AsyncTask.TvShowAsyncTask;
import com.anuntah.moviemania.TvShows.NetworkingAndDAO.TvAPI;
import com.anuntah.moviemania.TvShows.NetworkingAndDAO.TvShow;
import com.anuntah.moviemania.TvShows.NetworkingAndDAO.TvShow_testclass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TvListView extends AppCompatActivity {


    private Retrofit retrofit=new Retrofit.Builder().baseUrl(Constants.base_url).addConverterFactory(GsonConverterFactory.create()).build();
    TvAPI tvAPI=retrofit.create(TvAPI.class);

    RecyclerView recyclerView;

    ArrayList<TvShow> tvtopratedList =new ArrayList<>();
    ArrayList<TvShow> tvonairtodayList =new ArrayList<>();
    ArrayList<TvShow> tvonairList =new ArrayList<>();
    ArrayList<TvShow> tvpopularList =new ArrayList<>();
//    ArrayList<Movie> genremovie=new ArrayList<>();
    MovieDatabase movieDatabase;

    int genreid;
    TvListAdapter onairtodayRecyclerAdapter,popularRecyclerAdapter, onairRecyclerAdapter,topratedRecyclerAdapter,genreListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_movies);

        movieDatabase=MovieDatabase.getInstance(this);

        recyclerView=findViewById(R.id.recyclerview);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL));

        final Intent intent=getIntent();
        String type;
        type=intent.getStringExtra(Constants.UPCOMING);
        if(type==null)
            type="";
        genreid=intent.getIntExtra("id",-1);

//        if(genreid!=-1){
//            genreListAdapter=new MovieListAdapter(new MovieListAdapter.setOnMovieClickListner() {
//                @Override
//                public void OnMovieClicked(int pos) {
//                    ArrayList<Integer> idlist=new ArrayList<>();
//                    for(Movie movie:genremovie){
//                        idlist.add(movie.getId());
//                    }
//
//                    Intent intent1=new Intent(TvListView.this,MovieDetail.class);
//                    intent1.putIntegerArrayListExtra(Constants.ID,idlist);
//                    intent1.putExtra(Constants.POS,pos);
//                    startActivity(intent1);
//                }
//            },this,genremovie);
//            recyclerView.setAdapter(genreListAdapter);
//            fetchGenreMovie();
//        }

        if(type.equals("onairtoday")){
            onairtodayRecyclerAdapter =new TvListAdapter(new TvListAdapter.setOnMovieClickListner() {
                @Override
                public void OnMovieClicked(int pos) {

                }
            }, this, tvonairtodayList);
             recyclerView.setAdapter(onairtodayRecyclerAdapter);

            tvonairtodayList.addAll(movieDatabase.getTvDAO().getMoviesUpcomingIntheatres(Constants.UPCOMING));
            onairtodayRecyclerAdapter.notifyDataSetChanged();
            fetchTvShowsOnAirToday();

        }
        if(type.equals("popular")){

            popularRecyclerAdapter=new TvListAdapter(new TvListAdapter.setOnMovieClickListner() {
                @Override
                public void OnMovieClicked(int pos) {

                }
            }, this, tvpopularList);
            recyclerView.setAdapter(popularRecyclerAdapter);

            tvpopularList.addAll(movieDatabase.getTvDAO().getTvPopular(Constants.pOPULARS));
            popularRecyclerAdapter.notifyDataSetChanged();
            fetchTvShowsPopular();

        }
        if(type.equals("onair")){

            onairRecyclerAdapter =new TvListAdapter(new TvListAdapter.setOnMovieClickListner() {
                @Override
                public void OnMovieClicked(int pos) {
                    //
                }
            }, this, tvonairList);
            recyclerView.setAdapter(onairRecyclerAdapter);
            tvonairList.addAll(movieDatabase.getTvDAO().getMoviesUpcomingIntheatres(Constants.INTHEATRES));
            onairRecyclerAdapter.notifyDataSetChanged();
            fetchTvShowsOnAir();
        }
        if(type.equals("toprated")){
            topratedRecyclerAdapter=new TvListAdapter(new TvListAdapter.setOnMovieClickListner() {
                @Override
                public void OnMovieClicked(int pos) {

                }
            }, this, tvtopratedList);
            recyclerView.setAdapter(topratedRecyclerAdapter);

            tvtopratedList.addAll(movieDatabase.getTvDAO().getTvTopRated(Constants.TOPRATED));
            topratedRecyclerAdapter.notifyDataSetChanged();
            fetchTvShowsTopRated();
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {

                }
            }
        });

    }

//    private void fetchGenreMovie() {
//        Call<Movie_testclass> call=movieAPI.getGenreMovie(genreid);
//        call.enqueue(new Callback<Movie_testclass>() {
//            @Override
//            public void onResponse(Call<Movie_testclass> call, Response<Movie_testclass> response) {
//                genremovie.clear();
//                Log.d("tag",response.toString());
//                Movie_testclass movie_testclass=response.body();
//                if (movie_testclass != null) {
//                    for(Movie movie:movie_testclass.getResults())
//                    genremovie.add(movie);
//                }
//                if(movie_testclass==null)
//                    fetchGenreMovie();
//                genreListAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(Call<Movie_testclass> call, Throwable t) {
//                Log.d("tag","error");
//                fetchGenreMovie();
//            }
//        });
//    }

    private void fetchTvShowsTopRated() {
        Call<TvShow_testclass> call=tvAPI.getTopRatedTvShow(1);
        call.enqueue(new Callback<TvShow_testclass>() {
            @Override
            public void onResponse(Call<TvShow_testclass> call, Response<TvShow_testclass> response) {
                TvShow_testclass testclass=response.body();

                if(testclass!=null){
                    tvtopratedList.clear();
                    tvtopratedList.addAll(testclass.getResults());

                    //new TvShowAsyncTask(movieDatabase, Constants.TOPRATED).execute(tvtopratedList.toArray(new TvShow[tvtopratedList.size()]));
                    topratedRecyclerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<TvShow_testclass> call, Throwable t) {
                fetchTvShowsTopRated();
            }
        });
    }

    private void fetchTvShowsOnAir() {
        Call<TvShow_testclass> call=tvAPI.getTvOnAir(1);
        call.enqueue(new Callback<TvShow_testclass>() {
            @Override
            public void onResponse(Call<TvShow_testclass> call, Response<TvShow_testclass> response) {
                TvShow_testclass testclass=response.body();
                Log.d("tv",response.toString());
                if (testclass != null) {
                    Log.d("tv",response.toString());
                    tvonairList.clear();
                    ArrayList<TvShow> tvShowArrayList = new ArrayList<>(testclass.getResults());
                    tvonairList.addAll(tvShowArrayList);
                    onairRecyclerAdapter.notifyDataSetChanged();
                    //new TvShowAsyncTask(movieDatabase, Constants.INTHEATRES).execute(tvShowArrayList.toArray(new TvShow[tvShowArrayList.size()]));

                }
            }

            @Override
            public void onFailure(Call<TvShow_testclass> call, Throwable t) {
                fetchTvShowsOnAir();
            }
        });
    }

    private void fetchTvShowsPopular() {
        Call<TvShow_testclass> call=tvAPI.getPopularTvShows(1);
        call.enqueue(new Callback<TvShow_testclass>() {
            @Override
            public void onResponse(Call<TvShow_testclass> call, Response<TvShow_testclass> response) {
                TvShow_testclass testclass=response.body();

                if(testclass!=null){
                    tvpopularList.clear();
                    tvpopularList.addAll(testclass.getResults());
                    //new TvShowAsyncTask(movieDatabase, Constants.pOPULARS).execute(tvpopularList.toArray(new TvShow[tvpopularList.size()]));
                    popularRecyclerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<TvShow_testclass> call, Throwable t) {
                fetchTvShowsPopular();
            }
        });
    }



    void fetchTvShowsOnAirToday() {

        Call<TvShow_testclass> call=tvAPI.getTvAiringToday(1);
        call.enqueue(new Callback<TvShow_testclass>() {
            @Override
            public void onResponse(Call<TvShow_testclass> call, Response<TvShow_testclass> response) {
                TvShow_testclass testclass=response.body();
                Log.d("tv",response.toString());
                if (testclass != null) {
                    Log.d("tv",response.toString());
                    tvonairtodayList.clear();
                    ArrayList<TvShow> tvShowArrayList = new ArrayList<>(testclass.getResults());
                    tvonairtodayList.addAll(tvShowArrayList);
                    onairtodayRecyclerAdapter.notifyDataSetChanged();

//                    new TvShowAsyncTask(movieDatabase, Constants.UPCOMING).execute(tvShowArrayList.toArray(new TvShow[tvShowArrayList.size()]));
                }
            }

            @Override
            public void onFailure(Call<TvShow_testclass> call, Throwable t) {
                fetchTvShowsOnAirToday();
            }
        });

    }
}
