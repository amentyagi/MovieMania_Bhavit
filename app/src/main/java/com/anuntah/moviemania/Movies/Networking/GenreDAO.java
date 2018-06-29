package com.anuntah.moviemania.Movies.Networking;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface GenreDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ArrayList<Genre> genreArrayList);

    @Query("Select * from Genre")
    List<Genre> getGenresList();
}
