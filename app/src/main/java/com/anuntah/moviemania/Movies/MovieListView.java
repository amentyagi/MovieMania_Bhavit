package com.anuntah.moviemania.Movies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.anuntah.moviemania.MovieDatabase;
import com.anuntah.moviemania.Movies.Adapter.MovieListAdapter;
import com.anuntah.moviemania.Movies.Constants.Constants;
import com.anuntah.moviemania.Movies.Networking.Movie;
import com.anuntah.moviemania.Movies.Networking.MovieAPI;
import com.anuntah.moviemania.Movies.Networking.Movie_testclass;
import com.anuntah.moviemania.R;

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
    ArrayList<Movie> genremovie=new ArrayList<>();
    MovieDatabase movieDatabase;

    int genreid;
    MovieListAdapter upcomingRecyclerAdapter,popularRecyclerAdapter,intheatresRecyclerAdapter,topratedRecyclerAdapter,genreListAdapter;

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

        if(genreid!=-1){
            genreListAdapter=new MovieListAdapter(new MovieListAdapter.setOnMovieClickListner() {
                @Override
                public void OnMovieClicked(int pos) {
                    ArrayList<Integer> idlist=new ArrayList<>();
                    for(Movie movie:genremovie){
                        idlist.add(movie.getId());
                    }

                    Intent intent1=new Intent(MovieListView.this,MovieDetail.class);
                    intent1.putIntegerArrayListExtra(Constants.ID,idlist);
                    intent1.putExtra(Constants.POS,pos);
                    startActivity(intent1);
                }
            },this,genremovie);
            recyclerView.setAdapter(genreListAdapter);
            fetchGenreMovie();
        }

        if(type.equals("upcoming")){
            upcomingRecyclerAdapter=new MovieListAdapter(new MovieListAdapter.setOnMovieClickListner() {
                @Override
                public void OnMovieClicked(int pos) {
                    ArrayList<Integer> idlist=new ArrayList<>();
                    for(Movie movie:upcomingmovie){
                        idlist.add(movie.getId());
                    }
                    Intent intent1=new Intent(MovieListView.this,MovieDetail.class);
                    intent1.putIntegerArrayListExtra(Constants.ID,idlist);
                    intent1.putExtra(Constants.POS,pos);

                    startActivity(intent1);
                }
            }, this, upcomingmovie);
             recyclerView.setAdapter(upcomingRecyclerAdapter);

            upcomingmovie.addAll(movieDatabase.getMoviesDAO().getMoviesUpcomingIntheatres(Constants.UPCOMING));
            upcomingRecyclerAdapter.notifyDataSetChanged();
            fetchUpcomingMovies();

        }
        if(type.equals("popular")){

            popularRecyclerAdapter=new MovieListAdapter(new MovieListAdapter.setOnMovieClickListner() {
                @Override
                public void OnMovieClicked(int pos) {
                    ArrayList<Integer> idlist=new ArrayList<>();
                    for(Movie movie:popular_movie){
                        idlist.add(movie.getId());
                    }
                    Intent intent1=new Intent(MovieListView.this,MovieDetail.class);
                    intent1.putIntegerArrayListExtra(Constants.ID,idlist);
                    intent1.putExtra(Constants.POS,pos);

                    startActivity(intent1);
                }
            }, this, popular_movie);
            recyclerView.setAdapter(popularRecyclerAdapter);

            popular_movie.addAll(movieDatabase.getMoviesDAO().getMoviesList(Constants.pOPULARS,Constants.popularity));
            popularRecyclerAdapter.notifyDataSetChanged();
            fetchPopularMovies();

        }
        if(type.equals("intheatres")){

            intheatresRecyclerAdapter=new MovieListAdapter(new MovieListAdapter.setOnMovieClickListner() {
                @Override
                public void OnMovieClicked(int pos) {
                    ArrayList<Integer> idlist=new ArrayList<>();
                    for(Movie movie:intheatres){
                        idlist.add(movie.getId());
                    }
                    Intent intent1=new Intent(MovieListView.this,MovieDetail.class);
                    intent1.putIntegerArrayListExtra(Constants.ID,idlist);
                    intent1.putExtra(Constants.POS,pos);
                    startActivity(intent1);

                }
            }, this, intheatres);
            recyclerView.setAdapter(intheatresRecyclerAdapter);

            fetchNowShowingMovies();
            intheatres.addAll(movieDatabase.getMoviesDAO().getMoviesUpcomingIntheatres(Constants.INTHEATRES));
            intheatresRecyclerAdapter.notifyDataSetChanged();
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
                    intent1.putExtra(Constants.POS,pos);
                    startActivity(intent1);
                }
            }, this, topratedmovies);
            recyclerView.setAdapter(topratedRecyclerAdapter);

            topratedmovies.addAll(movieDatabase.getMoviesDAO().getMoviesUpcomingIntheatres(Constants.TOPRATED));
            topratedRecyclerAdapter.notifyDataSetChanged();
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

    private void fetchGenreMovie() {
        Call<Movie_testclass> call=movieAPI.getGenreMovie(genreid);
        call.enqueue(new Callback<Movie_testclass>() {
            @Override
            public void onResponse(Call<Movie_testclass> call, Response<Movie_testclass> response) {
                genremovie.clear();
                Log.d("tag",response.toString());
                Movie_testclass movie_testclass=response.body();
                if (movie_testclass != null) {
                    for(Movie movie:movie_testclass.getResults())
                    genremovie.add(movie);
                }
                if(movie_testclass==null)
                    fetchGenreMovie();
                genreListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Movie_testclass> call, Throwable t) {
                Log.d("tag","error");
                fetchGenreMovie();
            }
        });
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
                if(movie_testclass==null)
                    fetchTopratedMovies();
                topratedRecyclerAdapter.notifyDataSetChanged();
//                Toast.makeText(getContext(),topratedmovies.size()+"",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Movie_testclass> call, Throwable t) {
                fetchTopratedMovies();
            }
        });
    }

    private void fetchNowShowingMovies() {
        Call<Movie_testclass> call=movieAPI.getNowShowing(20);
        call.enqueue(new Callback<Movie_testclass>() {
            @Override
            public void onResponse(Call<Movie_testclass> call, Response<Movie_testclass> response) {
                intheatres.clear();
                Movie_testclass testclass=response.body();
                if (testclass != null) {
                    intheatres.addAll(testclass.getResults());
                }
                if(testclass==null)
                    fetchNowShowingMovies();
                intheatresRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Movie_testclass> call, Throwable t) {
                fetchNowShowingMovies();
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
                    for(Movie movie:movie_testclass.getResults())
                        popular_movie.add(movie);
                }
                if(movie_testclass==null)
                    fetchPopularMovies();
                popularRecyclerAdapter.notifyDataSetChanged();
//                Toast.makeText(getContext(),popular_movie.size()+"",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Movie_testclass> call, Throwable t) {
                fetchPopularMovies();
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
                upcomingmovie.clear();
                if (testclass != null) {
                    for(Movie movie:testclass.getResults())
                        upcomingmovie.add(movie);
                }
                if(testclass==null)
                    fetchUpcomingMovies();
                upcomingRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Movie_testclass> call, Throwable t) {
                fetchUpcomingMovies();
            }
        });
    }
}
