package com.anuntah.moviemania.Movies.AsyncTask;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.anuntah.moviemania.MovieDatabase;
import com.anuntah.moviemania.Movies.Adapter.MoviesRecyclerAdapter;
import com.anuntah.moviemania.Movies.Constants.Constants;
import com.anuntah.moviemania.Movies.Networking.Movie;

import java.util.ArrayList;

public class MovieFragmentAsyncTask extends AsyncTask<Void,Void,ArrayList<Movie>> {

    private MovieDatabase movieDatabase;
    MoviesRecyclerAdapter moviesRecyclerAdapter;
    ArrayList<Movie> popularmovie;

    public MovieFragmentAsyncTask(MovieDatabase movieDatabase,MoviesRecyclerAdapter moviesRecyclerAdapter,ArrayList<Movie> popularmovie) {
        this.movieDatabase = movieDatabase;
        this.moviesRecyclerAdapter=moviesRecyclerAdapter;
        this.popularmovie=popularmovie;
    }


    @Override
    protected ArrayList<Movie> doInBackground(Void... voids) {

        if(movieDatabase.getMoviesDAO().getMoviesList1(Constants.pOPULARS)!=null) {

            popularmovie.addAll(movieDatabase.getMoviesDAO().getMoviesList1(Constants.pOPULARS));
        }
        return popularmovie;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        super.onPostExecute(movies);
        if(movies!=null)
            Log.d("bhavit","doneeeeeeee");
        moviesRecyclerAdapter.notifyDataSetChanged();
    }
}
