package com.anuntah.moviemania.Movies.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anuntah.moviemania.Movies.Networking.Movie;
import com.anuntah.moviemania.Movies.Constants.Constants;
import com.anuntah.moviemania.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import static java.lang.String.format;

public class TrailerRecyclerAdapter extends RecyclerView.Adapter<TrailerRecyclerAdapter.TrailerViewHolder> {

    Date date=new Date();
    private final int UNINITIALIZED = 1;
    private final int INITIALIZING = 2;
    private final int INITIALIZED = 3;
    private boolean readyForLoadingYoutubeThumbnail = true;
    private int blackColor = Color.parseColor("#FF000000");
    private int transparentColor = Color.parseColor("#00000000");


    public interface setOnClickMoviePosterListener{
        void OnTouchClicked(View v, MotionEvent e,int pos);
    }


    public interface TrailerOnClickListener{
        void OnTrailerClicked(int pos);
        void OnShareClicked(int pos);
    }

    private setOnClickMoviePosterListener moviePosterListener;

    private TrailerOnClickListener trailerOnClickListener;

    private Context context;
    private ArrayList<Movie> movies=new ArrayList<>();

    public TrailerRecyclerAdapter(Context context,ArrayList<Movie> movies,TrailerOnClickListener trailerOnClickListener,setOnClickMoviePosterListener moviePosterListener) {
        this.context = context;
        this.movies=movies;
        this.trailerOnClickListener=trailerOnClickListener;
        this.moviePosterListener=moviePosterListener;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.trailer_poster,parent,false);
        TrailerViewHolder viewHolder=new TrailerViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final TrailerViewHolder holder, final int position) {

//        Toast.makeText(context, movies.get(position).getTitle(), Toast.LENGTH_SHORT).show();


        holder.ytThubnailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trailerOnClickListener.OnTrailerClicked(holder.getAdapterPosition());
            }
        });


        holder.poster.setLongClickable(true);
        holder.poster.setClickable(true);


        holder.poster.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                moviePosterListener.OnTouchClicked(v,event,holder.getAdapterPosition());
                return true;
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trailerOnClickListener.OnShareClicked(holder.getAdapterPosition());
            }
        });


        if(movies.get(position).getTrailerid()!=null) {
            if (readyForLoadingYoutubeThumbnail) {
                readyForLoadingYoutubeThumbnail = false;
                holder.ytThubnailView.initialize(Constants.Youtube_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(final YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {

                        Picasso.get().load(Constants.YTIMAGE_URI + "" + movies.get(position).getTrailerid() + "/maxresdefault.jpg").networkPolicy(NetworkPolicy.OFFLINE).fit().into(holder.ytThubnailView, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError(Exception e) {

                                Picasso.get().load(Constants.YTIMAGE_URI + "" + movies.get(position).getTrailerid() + "/maxresdefault.jpg").fit().into(holder.ytThubnailView, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Log.d("offlineee",Constants.YTIMAGE_URI + "" + movies.get(position).getTrailerid() + "/0.jpg");
                                        Picasso.get().load(Constants.YTIMAGE_URI + "" + movies.get(position).getTrailerid() + "/0.jpg").fit().centerCrop().into(holder.ytThubnailView);

                                    }
                                });
                            }
                        });


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
        }
        Picasso.get().load(Constants.IMAGE_URI+"/w185"+movies.get(position).getPoster_path()).networkPolicy(NetworkPolicy.OFFLINE).fit().into(holder.poster, new Callback() {
            @Override
            public void onSuccess() {
                Log.d("OFFLINE",Constants.IMAGE_URI+"w154"+movies.get(position).getPoster_path());
            }

            @Override
            public void onError(Exception e) {
                Picasso.get().load(Constants.IMAGE_URI+"/w185"+movies.get(position).getPoster_path()).fit().into(holder.poster);

            }
        });
        holder.title.setText(movies.get(position).getTitle());
        StringBuffer s=new StringBuffer(movies.get(position).getRelease_date());
        String[] s1=s.toString().split("-");
        String s2="";
        s2=s2.concat(s1[2]);
        s2=s2.concat(" ");
        s2=s2.concat(s1[1]);
        s2=s2.concat(" ");
        s2=s2.concat(s1[0]);
        Log.d("bhavit",s2);



        String date=setDate(s1[1]);
        if(!date.equals("0"))
        holder.releaseyear.setText(s1[2].concat(" "+date));

        ArrayList<Integer> genreid=new ArrayList<>();
        genreid.addAll(movies.get(position).getGenre_ids());
        StringBuffer genre=new StringBuffer();

        for(int j=0;j<genreid.size();j++){
            if(j==2)
                break;
            else {
                genre.append(genrematcher(genreid.get(j)));
                genre.append(",");
              }
            }
            if(genre.length()!=0)
        genre.deleteCharAt(genre.length()-1);
        holder.genre.setText(genre.toString());
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

    class TrailerViewHolder extends RecyclerView.ViewHolder{
        YouTubeThumbnailView ytThubnailView;
        ImageView poster;
        TextView title;
        TextView releaseyear;
        TextView genre;
        ImageView share;


        public TrailerViewHolder(View itemView) {
            super(itemView);
            share=itemView.findViewById(R.id.share);
            ytThubnailView=itemView.findViewById(R.id.trailer_placeholder);
            poster=itemView.findViewById(R.id.poster_placeholder);
            title=itemView.findViewById(R.id.movie_name);
            releaseyear=itemView.findViewById(R.id.release_year);
            genre=itemView.findViewById(R.id.movie_genre);
        }

    }
}
