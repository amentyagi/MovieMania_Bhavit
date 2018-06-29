package com.anuntah.moviemania.Movies.Networking;

import java.util.ArrayList;

public class GenreList {

    private ArrayList<Genre> genres;

    public GenreList(ArrayList<Genre> genres) {
        this.genres = genres;
    }

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<Genre> genres) {
        this.genres = genres;
    }
}
