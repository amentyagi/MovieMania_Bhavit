package com.anuntah.moviemania.Movies.Networking;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.ArrayList;

@Dao
public interface TrailersDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTrailers(ArrayList<Trailers> trailers);

//    @Query("Select * from Trailers")
//    ArrayList<Trailers> getTrailers(int id);
}
