package com.anuntah.moviemania;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface MovieAPI {

    @GET("movie/popular?api_key=4b4e67d5132e642d0f6bfc206d5e28d0")
    Call<Movie_testclass> getPopularMovieList(@Query("page_no") int page);

    @GET("https://api.themoviedb.org/3/movie/{movie_id}?api_key=4b4e67d5132e642d0f6bfc206d5e28d0&language=en-US&append_to_response=videos")
    Call<Movie> getMovieDetail(@Path("movie_id")int id);

    @GET("movie/now_playing?api_key=4b4e67d5132e642d0f6bfc206d5e28d0&language=en-US&region=IN")
    Call<Movie_testclass> getNowShowing(@Query("page_no") int page);

    @GET("movie/upcoming?api_key=4b4e67d5132e642d0f6bfc206d5e28d0")
    Call<Movie_testclass> getUpcomingMovieList(@QueryMap Map<String, String> pop,@Query("page_no") int page);

    @GET("movie/{movie_id}/videos?api_key=4b4e67d5132e642d0f6bfc206d5e28d0")
    Call<TrailersTestClass> getTrailerList(@Path("movie_id")String id);

    @GET("movie/top_rated?api_key=4b4e67d5132e642d0f6bfc206d5e28d0")
    Call<Movie_testclass> getTopRated(@Query("page_no") int page);

    @GET("authentication/token/new?api_key=4b4e67d5132e642d0f6bfc206d5e28d0")
    Call<RequestToken> getRequestToken();
    @GET("authentication/session/new?api_key=4b4e67d5132e642d0f6bfc206d5e28d0")
    Call<SessionId> getSessionId(@Query("request_token") String req);

}
