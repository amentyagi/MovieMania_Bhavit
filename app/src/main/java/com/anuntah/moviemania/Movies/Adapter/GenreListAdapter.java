package com.anuntah.moviemania.Movies.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.anuntah.moviemania.Movies.Networking.Genre;
import com.anuntah.moviemania.R;

import java.util.ArrayList;

public class GenreListAdapter extends RecyclerView.Adapter<GenreListAdapter.GenreViewHolder> {

    Context context;
    ArrayList<Genre> genreArrayList;
    onGenreClickListener onGenreClickListener;

    public interface onGenreClickListener {
        void onGenreClicked(int adapterPosition);
    }

    public GenreListAdapter(Context context, ArrayList<Genre> genreArrayList, GenreListAdapter.onGenreClickListener onGenreClickListener) {
        this.context = context;
        this.genreArrayList = genreArrayList;
        this.onGenreClickListener = onGenreClickListener;
    }

    @Override
    public GenreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.genre_layout,parent,false);
        GenreViewHolder viewHolder=new GenreViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final GenreViewHolder holder, int position) {

        Toast.makeText(context,"genre",Toast.LENGTH_SHORT).show();
        Log.d("tage","genres");

        //if(genreArrayList.get(position).getDrawableid()!=null) {
            holder.poster.setImageResource(genreArrayList.get(position).getDrawableid());
            holder.poster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onGenreClickListener.onGenreClicked(holder.getAdapterPosition());
                }
            });


    }

    @Override
    public int getItemCount() {
        return genreArrayList.size();
    }

    class GenreViewHolder extends RecyclerView.ViewHolder{

        ImageView poster;

        public GenreViewHolder(View itemView) {
            super(itemView);
            poster=itemView.findViewById(R.id.genre_poster);
        }
    }
}
