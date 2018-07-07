package com.anuntah.moviemania.TvShows.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anuntah.moviemania.Movies.Constants.Constants;
import com.anuntah.moviemania.R;
import com.anuntah.moviemania.TvShows.NetworkingAndDAO.TvShow;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TvShowRecyclerAdapter extends RecyclerView.Adapter<TvShowRecyclerAdapter.MovieViewHolder> {

    public interface setOnMovieClickListner{
        void OnMovieClicked(int pos);
    }

    setOnMovieClickListner listner;
    Context context;
    ArrayList<TvShow> movies;

    public TvShowRecyclerAdapter(Context context, ArrayList<TvShow> movies, setOnMovieClickListner listner) {
        this.context = context;
        this.movies = movies;
        this.listner=listner;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.genre_poster,parent,false);
        MovieViewHolder viewHolder=new MovieViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, final int position) {
//        Toast.makeText(context,position+"",Toast.LENGTH_SHORT).show();
        holder.movie_poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.OnMovieClicked(holder.getAdapterPosition());
            }
        });
        holder.textView.setText(String.valueOf(movies.get(position).getVote_average()));
        holder.movie_name.setText(movies.get(position).getName());
        holder.movie_year.setText(movies.get(position).getFirst_air_date());
//        if(movies.get(position).getImage()!=null) {
//            Log.d("bhavit","upload");
//            byte[] blob = movies.get(position).getImage();
//            Bitmap bmp = BitmapFactory.decodeByteArray(blob, 0, blob.length);
//            ImageView image = new ImageView(context);
//            image.setImageBitmap(bmp);
//        }
        //Picasso.get().load(Constants.IMAGE_URI+""+movies.get(position).getPoster_path()).into(holder.movie_poster);

        Picasso.get().load(Constants.IMAGE_URI+"/w342"+movies.get(position).getPoster_path()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.movie_poster, new Callback() {
            @Override
            public void onSuccess() {
                Log.d("offline",Constants.IMAGE_URI+""+movies.get(position).getPoster_path());
            }

            @Override
            public void onError(Exception e) {
                Picasso.get().load(Constants.IMAGE_URI+"/w342"+movies.get(position).getPoster_path()).into(holder.movie_poster);

            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView movie_poster;
        TextView movie_name;
        TextView movie_year;
        TextView textView;
        ImageView like;

        public MovieViewHolder(View itemView) {
            super(itemView);
            movie_poster=itemView.findViewById(R.id.movie_poster);
            movie_name=itemView.findViewById(R.id.movie_name);
            movie_year=itemView.findViewById(R.id.movie_year);
            textView=itemView.findViewById(R.id.textView);
            like=itemView.findViewById(R.id.like);
        }
    }
}
