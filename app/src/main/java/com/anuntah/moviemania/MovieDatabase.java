package com.anuntah.moviemania;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.anuntah.moviemania.Movies.Networking.Genre;
import com.anuntah.moviemania.Movies.Networking.GenreDAO;
import com.anuntah.moviemania.Movies.Networking.Movie;
import com.anuntah.moviemania.Movies.Networking.MovieDAO;
import com.anuntah.moviemania.Movies.Networking.Trailers;
import com.anuntah.moviemania.Movies.Networking.TrailersDAO;
import com.anuntah.moviemania.TvShows.NetworkingAndDAO.TvShow;
import com.anuntah.moviemania.TvShows.NetworkingAndDAO.TvShowDAO;

@Database(entities = {Movie.class, Trailers.class, Genre.class, TvShow.class},version = 1 )
@TypeConverters({MovieTypeConverter.class})

public abstract class MovieDatabase extends RoomDatabase {

    private static MovieDatabase INSTANCE;

    public static MovieDatabase getInstance(Context context){

        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),MovieDatabase.class,"github_databases")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

    public abstract MovieDAO getMoviesDAO();
    public abstract TrailersDAO getTrailersDAO();
    public abstract GenreDAO genreDAO();
    public abstract TvShowDAO getTvDAO();
}
