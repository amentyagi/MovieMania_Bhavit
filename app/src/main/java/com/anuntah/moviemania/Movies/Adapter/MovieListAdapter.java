package com.anuntah.moviemania.Movies.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.anuntah.moviemania.Movies.Networking.Movie;
import com.anuntah.moviemania.Movies.Constants.Constants;
import com.anuntah.moviemania.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieHolder> {

    public interface setOnMovieClickListner{
        void OnMovieClicked(int pos);
    }

    private MovieListAdapter.setOnMovieClickListner listner;
    private Context context;
    private ArrayList<Movie> movies;

    public MovieListAdapter(setOnMovieClickListner listner, Context context, ArrayList<Movie> movies) {
        this.listner = listner;
        this.context = context;
        this.movies = movies;
    }

    @Override
    public MovieListAdapter.MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.listview_movies_content,parent,false);
        MovieListAdapter.MovieHolder viewHolder=new MovieListAdapter.MovieHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final MovieListAdapter.MovieHolder holder, final int position) {


        holder.moviename.setText(movies.get(position).getTitle());
        holder.moviegenre.setText(movies.get(position).getTitle());
        holder.ratingBar.setRating(movies.get(position).getVote_average()/2);
        holder.ratings.setText(movies.get(position).getVote_average()/2+"");
        Picasso.get().load(Constants.IMAGE_URI+"/w154"+movies.get(position).getPoster_path()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.poster, new Callback() {
            @Override
            public void onSuccess() {
                Log.d("OFFLINE",Constants.IMAGE_URI+"w154"+movies.get(position).getPoster_path());
            }

            @Override
            public void onError(Exception e) {
                Picasso.get().load(Constants.IMAGE_URI+"/w154"+movies.get(position).getPoster_path()).into(holder.poster);

            }
        });

        holder.poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.OnMovieClicked(holder.getAdapterPosition());
            }
        });


        StringBuffer genre=new StringBuffer("");
        ArrayList<Integer> genreid=new ArrayList<>();
        if(movies.get(position).getGenre_ids()!=null) {
            genreid.addAll(movies.get(position).getGenre_ids());
            genre = new StringBuffer();
            for (int j = 0; j < genreid.size(); j++) {

                genre.append(genrematcher(genreid.get(j)));
                genre.append(",");

            }
            if(genre.length()!=0)
            genre.deleteCharAt(genre.length() - 1);
        }
        holder.moviegenre.setText(genre.toString());

        if(!movies.get(position).getRelease_date().equals("")) {
            StringBuffer s = new StringBuffer(movies.get(position).getRelease_date());
            String[] s1 = s.toString().split("-");
            String s2 = "";
            s2 = s2.concat(s1[2]);
            s2 = s2.concat(" ");
            s2 = s2.concat(s1[1]);
            s2 = s2.concat(" ");
            s2 = s2.concat(s1[0]);
            Log.d("bhavit", s2);


            String date = setDate(s1[1]);
            if (!date.equals("0"))
                holder.releasedate.setText(s1[2].concat(" " + date).concat(" " + s1[0]));
        }
    }

    private String setDate(String s) {
        switch (s){
            case "01":return "Jan";
            case "02":return "Feb";
            case "03":return "Mar";
            case "04":return "Apr";
            case "05":return "May";
            case "06":return "Jun";
            case "07":return "Jul";
            case "08":return "Aug";
            case "09":return "Sep";
            case "10":return "Oct";
            case "11":return "Nov";
            case "12":return "Dec";
        }
        return "0";
    }

    private String genrematcher(int pos) {
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

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieHolder extends RecyclerView.ViewHolder {

        TextView moviename;
        TextView moviegenre;
        TextView releasedate;
        RatingBar ratingBar;
        TextView ratings;

        ImageView poster;


        public MovieHolder(View itemView) {
            super(itemView);
            moviename=itemView.findViewById(R.id.movie_name);
            moviegenre=itemView.findViewById(R.id.movie_genre);
            releasedate=itemView.findViewById(R.id.movie_release);
            ratingBar=itemView.findViewById(R.id.movie_rating_icon);
            ratings=itemView.findViewById(R.id.movie_rating);
            poster=itemView.findViewById(R.id.movie_thumbnail);
        }
    }
}
