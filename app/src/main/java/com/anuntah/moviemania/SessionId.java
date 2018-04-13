package com.anuntah.moviemania;

public class SessionId {
    private String success;
    private String session_id;

    public SessionId(String success, String session_id) {
        this.success = success;
        this.session_id = session_id;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }
}
