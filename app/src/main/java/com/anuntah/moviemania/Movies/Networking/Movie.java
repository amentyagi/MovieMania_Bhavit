package com.anuntah.moviemania.Movies.Networking;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import com.anuntah.moviemania.MovieTypeConverter;

import java.util.ArrayList;

@Entity
public class Movie  {
    @PrimaryKey
    private int id;
    private String title;
    private String overview;
    private String release_date;
    private String backdrop_path;
    private String poster_path;
    private int vote_count;
    private float vote_average;
    private double popularity;
    private String original_language;
    @Ignore
    private ArrayList<Integer> genre_ids;
    private String trailerid;
    private int runtime;
    @Ignore
    private com.anuntah.moviemania.Movies.Networking.videos videos;
    @TypeConverters(MovieTypeConverter.class)
    private ArrayList<Genre> genres;
    private String Tag;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image;

    public Movie(){}

    public Movie(int id, String title, String overview, String release_date, String backdrop_path, String poster_path, int vote_count, int vote_average, int popularity, String original_language, ArrayList<Integer> genre_ids, int runtime, com.anuntah.moviemania.Movies.Networking.videos videos, ArrayList<Genre> genres) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.release_date = release_date;
        this.backdrop_path = backdrop_path;
        this.poster_path = poster_path;
        this.vote_count = vote_count;
        this.vote_average = vote_average;
        this.popularity = popularity;
        this.original_language = original_language;
        this.genre_ids = genre_ids;
        this.runtime=runtime;
        this.videos = videos;
        this.genres=genres;

    }

    public Movie(int id, String title, String release_date, String poster_path, ArrayList<Integer> genre_ids, String trailerid,String backdrop_path) {
        this.id = id;
        this.title = title;
        this.release_date = release_date;
        this.poster_path = poster_path;
        this.genre_ids = genre_ids;
        this.trailerid = trailerid;
        this.backdrop_path=backdrop_path;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getTrailerid() {
        return trailerid;
    }

    public void setTrailerid(String trailerid) {
        this.trailerid = trailerid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public ArrayList<Integer> getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(ArrayList<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }


    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public com.anuntah.moviemania.Movies.Networking.videos getVideos() {
        return videos;
    }

    public void setVideos(com.anuntah.moviemania.Movies.Networking.videos videos) {
        this.videos = videos;
    }

    @TypeConverters(MovieTypeConverter.class)
    public ArrayList<Genre> getGenres() {
        return genres;
    }

    @TypeConverters(MovieTypeConverter.class)
    public void setGenres(ArrayList<Genre> genres) {
        this.genres = genres;
    }
}
