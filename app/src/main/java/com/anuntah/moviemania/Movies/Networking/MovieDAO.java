package com.anuntah.moviemania.Movies.Networking;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.anuntah.moviemania.Movies.Constants.Constants;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface MovieDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(ArrayList<Movie> movies);

    @Query("Select * from Movie where Tag=:tag ORDER BY :field DESC ")
    List<Movie> getMoviesList(String tag,String field);

    @Query("Select * from Movie where Tag=:tag ORDER BY popularity DESC ")
    List<Movie> getMoviesList1(String tag);

    @Query("Select * from Movie where Tag=:tag ORDER BY vote_average DESC ")
    List<Movie> getMoviesTopRated(String tag);

    @Query("Select * from Movie where Tag=:tag ORDER BY release_date DESC ")
    List<Movie> getMoviesUpcomingIntheatres(String tag);

    @Query("Select * from Movie where id=:id")
    Movie getMovies(int id);


}
