package com.anuntah.moviemania;

import android.arch.persistence.room.TypeConverter;
import android.util.Log;

import com.anuntah.moviemania.Movies.Networking.Genre;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MovieTypeConverter {

    @TypeConverter
    public static ArrayList<Genre> fromString(String value){
        Type type=new TypeToken<ArrayList<Genre>>(){}.getType();
        Log.d("bhavit1",value);
        return new Gson().fromJson(value,type);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<Genre> genres){
        Gson gson=new Gson();
        String json= gson.toJson(genres,new TypeToken<ArrayList<Genre>>(){}.getType());
        if(genres==null){
            Log.d("bhavit1","nill");
        }
        return json;
    }

    @TypeConverter
    public static ArrayList<Integer> fromStringInt(String value){
        Type type=new TypeToken<ArrayList<Integer>>(){}.getType();
        Log.d("bhavit1",value);
        return new Gson().fromJson(value,type);
    }

    @TypeConverter
    public static String fromArrayListInt(ArrayList<Integer> genres){
        Gson gson=new Gson();
        String json= gson.toJson(genres,new TypeToken<ArrayList<Integer>>(){}.getType());
        if(genres==null){
            Log.d("bhavit1","nill");
        }
        return json;
    }
}
