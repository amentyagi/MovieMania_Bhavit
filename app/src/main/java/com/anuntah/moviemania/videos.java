package com.anuntah.moviemania;

import java.util.ArrayList;

public class videos {
    private ArrayList<Trailers> results;

    public videos(ArrayList<Trailers> results) {
        this.results = results;
    }

    public ArrayList<Trailers> getResults() {
        return results;
    }

    public void setResults(ArrayList<Trailers> results) {
        this.results = results;
    }
}
