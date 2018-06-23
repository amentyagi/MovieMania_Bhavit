package com.anuntah.moviemania.Movies.Networking;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Trailers {
    @PrimaryKey@NonNull
    private String id;
    private String name;
    private String type;
    private String key;

    public Trailers(String id, String name, String type,String key) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.key=key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
