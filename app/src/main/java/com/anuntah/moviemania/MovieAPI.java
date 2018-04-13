package com.anuntah.moviemania;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface MovieAPI {

    @GET("discover/movie?api_key=4b4e67d5132e642d0f6bfc206d5e28d0")
    Call<Movie_testclass> getPopularMovieList(@QueryMap Map<String, String> pop);

    @GET("movie/upcoming?api_key=4b4e67d5132e642d0f6bfc206d5e28d0")
    Call<Movie_testclass> getUpcomingMovieList(@QueryMap Map<String, String> pop);

    @GET("movie/{movie_id}/videos?api_key=4b4e67d5132e642d0f6bfc206d5e28d0")
    Call<TrailersTestClass> getTrailerList(@Path("movie_id")String id);

    @GET("authentication/token/new?api_key=4b4e67d5132e642d0f6bfc206d5e28d0")
    Call<RequestToken> getRequestToken();
    @GET("authentication/session/new?api_key=4b4e67d5132e642d0f6bfc206d5e28d0")
    Call<SessionId> getSessionId(@Query("request_token") String req);

}
