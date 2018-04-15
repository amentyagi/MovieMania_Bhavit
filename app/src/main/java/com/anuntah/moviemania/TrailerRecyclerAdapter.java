package com.anuntah.moviemania;

import android.content.Context;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.text.UnicodeSetSpanner;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import static android.provider.Contacts.SettingsColumns.KEY;
import static java.lang.String.format;

public class TrailerRecyclerAdapter extends RecyclerView.Adapter<TrailerRecyclerAdapter.TrailerViewHolder> {

    private final int UNINITIALIZED = 1;
    private final int INITIALIZING = 2;
    private final int INITIALIZED = 3;
    private boolean readyForLoadingYoutubeThumbnail = true;
    private int blackColor = Color.parseColor("#FF000000");
    private int transparentColor = Color.parseColor("#00000000");


    private Context context;
    private ArrayList<Movie> movies=new ArrayList<>();

    public TrailerRecyclerAdapter(Context context,ArrayList<Movie> movies) {
        this.context = context;
        this.movies=movies;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.trailer_poster,parent,false);
        TrailerViewHolder viewHolder=new TrailerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final TrailerViewHolder holder, final int position) {
        if (readyForLoadingYoutubeThumbnail) {
            Log.d("TAG", "initializing for youtube thumbnail view...");
            readyForLoadingYoutubeThumbnail = false;
            holder.ytThubnailView.initialize(Constants.Youtube_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(final YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {

                    Picasso.get().load(Constants.YTIMAGE_URI+""+movies.get(position).getTrailerid()+"/maxresdefault.jpg").resize(500,210).into(holder.ytThubnailView);
                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {

                        @Override
                        public void onThumbnailLoaded(YouTubeThumbnailView childYouTubeThumbnailView, String s) {
                            youTubeThumbnailLoader.release(); // spy ga memory lick
                        }

                        @Override
                        public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                            youTubeThumbnailLoader.release(); // spy ga memory lick
                        }
                    });

                    readyForLoadingYoutubeThumbnail = true;
                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                    //do nohing.. ada error, tambahin method ini jalan, error-nya lupa...
                    readyForLoadingYoutubeThumbnail = true;
                }
            });
        }
        Picasso.get().load(Constants.IMAGE_URI+""+movies.get(position).getPoster_path()).resize(100,130).into(holder.poster);
        holder.title.setText(movies.get(position).getTitle());
        holder.releaseyear.setText(movies.get(position).getRelease_date());
    }


    @Override
    public int getItemCount() {
        return movies.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder{
        YouTubeThumbnailView ytThubnailView;
        ImageView poster;
        TextView title;
        TextView releaseyear;
        TextView genre;


        public TrailerViewHolder(View itemView) {
            super(itemView);
            ytThubnailView=itemView.findViewById(R.id.trailer_placeholder);
            poster=itemView.findViewById(R.id.poster_placeholder);
            title=itemView.findViewById(R.id.movie_name);
            releaseyear=itemView.findViewById(R.id.release_year);
            genre=itemView.findViewById(R.id.movie_genre);
        }

    }
}
