package com.anuntah.moviemania.Movies.Networking;

public class SessionToken {
    private String sucess;
    private String session_id;

    public SessionToken(String sucess, String session_id) {
        this.sucess = sucess;
        this.session_id = session_id;
    }

    public String getSucess() {
        return sucess;
    }

    public void setSucess(String sucess) {
        this.sucess = sucess;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }
}
