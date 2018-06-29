package com.anuntah.moviemania.Movies.AsyncTask;

import android.os.AsyncTask;

import com.anuntah.moviemania.MovieDatabase;
import com.anuntah.moviemania.Movies.Networking.Movie;

import java.util.ArrayList;

public class MovieDetailAsyncTask extends AsyncTask<Movie,Void, Void> {

    private MovieDatabase movieDatabase;
    private ArrayList<Integer> idlist;
    private int listpos,pos;

    public MovieDetailAsyncTask(MovieDatabase movieDatabase, ArrayList<Integer> idlist, int listpos, int pos) {
        this.movieDatabase = movieDatabase;
        this.idlist = idlist;
        this.listpos = listpos;
        this.pos = pos;
    }

    @Override
    protected Void doInBackground(Movie... movies) {
        for(Movie movie:movies) {
            movieDatabase.getMoviesDAO().getMovies(idlist.get(listpos + pos - 1)).setGenres(movie.getGenres());
            movieDatabase.getMoviesDAO().getMovies(idlist.get(listpos + pos - 1)).setRuntime(movie.getRuntime());
        }
        return null;
    }
}
