package com.anuntah.moviemania.TvShows.NetworkingAndDAO;

import java.util.ArrayList;

public class TvShow_testclass {
    private ArrayList<TvShow> results;

    public TvShow_testclass(ArrayList<TvShow> results) {
        this.results = results;
    }

    public ArrayList<TvShow> getResults() {
        return results;
    }

    public void setResults(ArrayList<TvShow> results) {
        this.results = results;
    }
}
