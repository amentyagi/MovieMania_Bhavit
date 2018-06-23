package com.anuntah.moviemania.Movies.Networking;

import com.anuntah.moviemania.Movies.Networking.Trailers;

import java.util.ArrayList;

public class TrailersTestClass {
    private ArrayList<Trailers> results;

    public TrailersTestClass(ArrayList<Trailers> results) {
        this.results = results;
    }

    public ArrayList<Trailers> getResults() {
        return results;
    }

    public void setResults(ArrayList<Trailers> results) {
        this.results = results;
    }
}
