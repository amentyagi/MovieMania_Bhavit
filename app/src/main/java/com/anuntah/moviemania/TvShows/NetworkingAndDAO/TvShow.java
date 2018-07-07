package com.anuntah.moviemania.TvShows.NetworkingAndDAO;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.anuntah.moviemania.Movies.Networking.Genre;

import java.util.ArrayList;

@Entity
public class TvShow {

    @PrimaryKey@NonNull
    private String id;
    private String name;
    private ArrayList<Integer> genre_ids;
    private String popularity;
    private String vote_count;
    private String first_air_date;
    private String backdrop_path;
    private String vote_average;
    private String overview;
    private String poster_path;
    private String trailer_id;
    private String Tag;
    private ArrayList<Genre> Genres;



    public TvShow(String id, String name, ArrayList<Integer> genre_ids, String popularity, String vote_count, String first_air_date, String backdrop_path, String vote_average, String overview, String poster_path) {
        this.id = id;
        this.name = name;
        this.genre_ids = genre_ids;
        this.popularity = popularity;
        this.vote_count = vote_count;
        this.first_air_date = first_air_date;
        this.backdrop_path = backdrop_path;
        this.vote_average = vote_average;
        this.overview = overview;
        this.poster_path = poster_path;
    }

    public TvShow(String id, String name, ArrayList<Integer> genre_ids, String popularity, String vote_count, String first_air_date, String backdrop_path, String vote_average, String overview, String poster_path, String trailer_id) {
        this.id = id;
        this.name = name;
        this.genre_ids = genre_ids;
        this.popularity = popularity;
        this.vote_count = vote_count;
        this.first_air_date = first_air_date;
        this.backdrop_path = backdrop_path;
        this.vote_average = vote_average;
        this.overview = overview;
        this.poster_path = poster_path;
        this.trailer_id = trailer_id;
    }

    public TvShow() {
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    public ArrayList<Genre> getGenres() {
        return Genres;
    }

    public void setGenres(ArrayList<Genre> genres) {
        Genres = genres;
    }

    public String getTrailer_id() {
        return trailer_id;
    }

    public void setTrailer_id(String trailer_id) {
        this.trailer_id = trailer_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Integer> getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(ArrayList<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getVote_count() {
        return vote_count;
    }

    public void setVote_count(String vote_count) {
        this.vote_count = vote_count;
    }

    public String getFirst_air_date() {
        return first_air_date;
    }

    public void setFirst_air_date(String first_air_date) {
        this.first_air_date = first_air_date;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }
}
