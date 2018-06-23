package com.anuntah.moviemania;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.util.Log;

import com.anuntah.moviemania.Movies.Constants.Constants;
import com.anuntah.moviemania.Movies.Networking.Genre;
import com.anuntah.moviemania.Movies.Networking.Movie;
import com.anuntah.moviemania.Movies.Networking.MovieDAO;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MoviePosterAsyncTask extends AsyncTask<Movie,String,String> {


    private MovieDatabase movieDatabase;

    public MoviePosterAsyncTask(MovieDatabase movieDatabase) {
        this.movieDatabase = movieDatabase;
    }

    @Override
    protected String doInBackground(Movie... movies) {

        ArrayList<Movie> movieArrayList=new ArrayList<>();
        for(Movie movie:movies){
            movie.setTag(Constants.pOPULARS);
            movie.setGenres(idToGenre(movie.getGenre_ids()));
            try {
                Bitmap bitmap=Picasso.get().load(Constants.IMAGE_URI+""+movie.getPoster_path()).get();
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                float scaleWidth = ((float) 480) / width;
                float scaleHeight = ((float) 640) / height;
                // CREATE A MATRIX FOR THE MANIPULATION
                Matrix matrix = new Matrix();
                // RESIZE THE BIT MAP
                matrix.postScale(scaleWidth, scaleHeight);

                // "RECREATE" THE NEW BITMAP
                bitmap=Bitmap.createBitmap(
                        bitmap, 0, 0, width, height, matrix, false);
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                byte[] bytes=stream.toByteArray();
                Log.d("byte",bytes.length/1024+"");
                movie.setImage(bytes);
                Log.d("mohit",Constants.IMAGE_URI+""+movie.getPoster_path());
            } catch (IOException e) {
                e.printStackTrace();
            }
            movieArrayList.add(movie);

            MovieDAO movieDAO= movieDatabase.getMoviesDAO();
            movieDAO.insertMovie(movieArrayList);
        }
        return "Stored";
    }

    private ArrayList<Genre> idToGenre(ArrayList<Integer> genreid) {
        ArrayList<Genre> genreArrayList=new ArrayList<>();
        for(int id:genreid){
            String genre=genreMatcher(id);
            Genre genre1=new Genre(id,genre);
            genreArrayList.add(genre1);
        }
        return genreArrayList;
    }

    private String genreMatcher(int pos) {
        switch (pos){
            case 28:return "Action";
            case 12 :return "Adventure";
            case 16 :return "Animation";
            case 35 :return  "Comedy";
            case 80 :return "Crime";
            case 99 :return "Documentary";
            case 18 :return "Drama";
            case 14 :return "Fantasy";
            case 27 :return  "Horror";
            case 10749:return "Romance";
            case 878:return  "Sci-Fic";
            case 53 :return "Thriller";
            case 10751: return "Family";
            case 36:return "History";
            case 10402:return "Music";
            case 9648:return "Mystery";
            case 10752:return "War";
            case 37:return "Western";
        }
        return "k";
    }

}
