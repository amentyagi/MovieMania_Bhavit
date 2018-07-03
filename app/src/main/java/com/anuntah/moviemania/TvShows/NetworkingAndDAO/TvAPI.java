package com.anuntah.moviemania.TvShows.NetworkingAndDAO;

import com.anuntah.moviemania.Movies.Networking.GenreList;
import com.anuntah.moviemania.Movies.Networking.Movie;
import com.anuntah.moviemania.Movies.Networking.Movie_testclass;
import com.anuntah.moviemania.Movies.Networking.SessionToken;
import com.anuntah.moviemania.Movies.Networking.TrailersTestClass;
import com.anuntah.moviemania.RequestToken;
import com.anuntah.moviemania.SessionId;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface TvAPI {

    @GET("/tv/airing_today?api_key=4b4e67d5132e642d0f6bfc206d5e28d0&language=en-US")
    Call<TvShow_testclass> getTvAiringToday(@Query("page_no") int page);

    @GET("https://api.themoviedb.org/3/movie/{movie_id}?api_key=4b4e67d5132e642d0f6bfc206d5e28d0&language=en-US&append_to_response=videos")
    Call<Movie> getMovieDetail(@Path("movie_id") int id);

    @GET("/tv/on_the_air?api_key=4b4e67d5132e642d0f6bfc206d5e28d0&language=en-US")
    Call<TvShow_testclass> getTvOnAir(@Query("page_no") int page);

    @GET("/tv/popular?api_key=4b4e67d5132e642d0f6bfc206d5e28d0&language=en-US")
    Call<TvShow_testclass> getPopularTvShows(@Query("page_no") int page);

    @GET("movie/{movie_id}/videos?api_key=4b4e67d5132e642d0f6bfc206d5e28d0")
    Call<TrailersTestClass> getTrailerList(@Path("movie_id") String id);

    @GET("/tv/top_rated?api_key=4b4e67d5132e642d0f6bfc206d5e28d0&language=en-US\n")
    Call<TvShow_testclass> getTopRatedTvShow(@Query("page_no") int page);

    @GET("https://api.themoviedb.org/3/genre/movie/list?api_key=4b4e67d5132e642d0f6bfc206d5e28d0&language=en-US")
    Call<GenreList> getGenres();

    @GET("authentication/token/new?api_key=4b4e67d5132e642d0f6bfc206d5e28d0")
    Call<RequestToken> getRequestToken();
    @GET("authentication/session/new?api_key=4b4e67d5132e642d0f6bfc206d5e28d0")
    Call<SessionId> getSessionId(@Query("request_token") String req);

    @GET("https://api.themoviedb.org/3/discover/movie?api_key=4b4e67d5132e642d0f6bfc206d5e28d0&language=en-US&page=15")
    Call<Movie_testclass> getGenreMovie(@Query("with_genres") int id);

    @GET("https://api.themoviedb.org/3/movie/{movie_id}/similar?api_key=4b4e67d5132e642d0f6bfc206d5e28d0&language=en-US&page=1")
    Call<Movie_testclass> getSimilarMovie(@Path("movie_id") int id);

    @GET("https://api.themoviedb.org/3/authentication/session/new?api_key=4b4e67d5132e642d0f6bfc206d5e28d0")
    Call<SessionToken> getSessionToken(@Query("request_token") String token);

}
