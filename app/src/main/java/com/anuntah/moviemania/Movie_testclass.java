package com.anuntah.moviemania;

import java.util.ArrayList;

public class Movie_testclass {
    private ArrayList<Movie> results;

    public Movie_testclass(ArrayList<Movie> results) {
        this.results = results;
    }

    public ArrayList<Movie> getResults() {
        return results;
    }

    public void setResults(ArrayList<Movie> results) {
        this.results = results;
    }
}
