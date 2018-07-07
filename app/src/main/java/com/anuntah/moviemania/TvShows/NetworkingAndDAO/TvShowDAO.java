package com.anuntah.moviemania.TvShows.NetworkingAndDAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.anuntah.moviemania.Movies.Networking.Movie;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface TvShowDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTvShow(ArrayList<TvShow> tvShows);

//    @Query("Select * from TvShow where Tag=:tag ORDER BY :field DESC ")
//    List<Movie> getMoviesList(String tag, String field);

    @Query("Select * from TvShow where Tag=:tag ORDER BY popularity DESC ")
    List<TvShow> getTvPopular(String tag);

    @Query("Select * from TvShow where Tag=:tag ORDER BY vote_average DESC ")
    List<TvShow> getTvTopRated(String tag);

    @Query("Select * from TvShow where Tag=:tag")
    List<TvShow> getMoviesUpcomingIntheatres(String tag);

//    @Query("Select * from Movie where id=:id")
//    Movie getMovies(int id);


}
