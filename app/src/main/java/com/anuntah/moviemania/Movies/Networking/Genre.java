package com.anuntah.moviemania.Movies.Networking;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Genre {
    @PrimaryKey
    private int id;
    private String name;
    private int drawableid;

    public Genre() {
    }

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Genre(int id, String name, int drawableid) {
        this.id = id;
        this.name = name;
        this.drawableid = drawableid;
    }

    public int getDrawableid() {
        return drawableid;
    }

    public void setDrawableid(int drawableid) {
        this.drawableid = drawableid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
