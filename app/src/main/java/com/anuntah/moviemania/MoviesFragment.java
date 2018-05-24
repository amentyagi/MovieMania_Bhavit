package com.anuntah.moviemania;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment implements TrailerRecyclerAdapter.TrailerOnClickListener, TrailerRecyclerAdapter.setOnClickMoviePosterListener, MoviesRecyclerAdapter.setOnMovieClickListner, View.OnClickListener {


    private Retrofit retrofit=new Retrofit.Builder().baseUrl(Constants.base_url).addConverterFactory(GsonConverterFactory.create()).build();
    MovieAPI movieAPI=retrofit.create(MovieAPI.class);
    ArrayList<Movie> trailer_movielist=new ArrayList<>();

    public MoviesFragment() {
        // Required empty public constructor
    }

    MoviesRecyclerAdapter topratedRecyclerAdapter;

    ArrayList<Movie> topratedmovies=new ArrayList<>();

    ArrayList<Movie> upcomingmovie=new ArrayList<>();

    ArrayList<Movie> intheatres=new ArrayList<>();

    Dialog dialog;
    TextView upcomingsee,popularsee,intheatresee,topratedsee;
    ViewGroup viewGroup;
    LinearLayout linearLayout;
    RecyclerView poprecycler,toprecycler,upcomingrecycler;
    ArrayList<Movie> popular_movie=new ArrayList<>();
    MoviesRecyclerAdapter moviesRecyclerAdapter;

    GestureDetector detector;
    TrailerRecyclerAdapter upcomingRecyclerAdapter;
    RecyclerView recyclerView;
    TrailerRecyclerAdapter trailerRecyclerAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_movies, container, false);
        recyclerView=view.findViewById(R.id.recyclerInTheatre);
        poprecycler=view.findViewById(R.id.recyclerPopular);
        toprecycler=view.findViewById(R.id.recyclerTopRated);
        upcomingrecycler=view.findViewById(R.id.recyclerUpcoming);
        upcomingsee=view.findViewById(R.id.in_theatre_text_seeall);
        popularsee=view.findViewById(R.id.popular_see_all);
        intheatresee=view.findViewById(R.id.upcoming_see_all);
        topratedsee=view.findViewById(R.id.topRated_see_all);


        upcomingsee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),MovieListView.class);
                intent.putExtra(Constants.UPCOMING,"upcoming");
                startActivity(intent);
            }
        });

        popularsee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),MovieListView.class);
                intent.putExtra(Constants.UPCOMING,"popular");
                startActivity(intent);

            }
        });

        intheatresee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),MovieListView.class);
                intent.putExtra(Constants.UPCOMING,"intheatres");
                startActivity(intent);
            }
        });

        topratedsee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),MovieListView.class);
                intent.putExtra(Constants.UPCOMING,"toprated");
                startActivity(intent);
            }
        });



        viewGroup=container;

        fetchTrailers();

        fetchPopularMovies();
        fetchTopRatedMovies();
        fetchInTheatres();

        trailerRecyclerAdapter=new TrailerRecyclerAdapter(getContext(), trailer_movielist, this,this);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        moviesRecyclerAdapter=new MoviesRecyclerAdapter(getContext(),popular_movie,this);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        poprecycler.setLayoutManager(layoutManager);


        topratedRecyclerAdapter=new MoviesRecyclerAdapter(getContext(), topratedmovies, new MoviesRecyclerAdapter.setOnMovieClickListner() {
            @Override
            public void OnMovieClicked(int pos) {
                Intent intent=new Intent(getContext(),MovieDetail.class);
                ArrayList<Integer> integerArrayList=new ArrayList<>();
                for(Movie movie:topratedmovies)
                    integerArrayList.add(movie.getId());
                intent.putExtra(Constants.ID,integerArrayList);
                intent.putExtra(Constants.POS,pos);
                startActivity(intent);
            }
        });
        LinearLayoutManager Manager=new LinearLayoutManager(getContext());
        Manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        toprecycler.setLayoutManager(Manager);


        upcomingRecyclerAdapter=new TrailerRecyclerAdapter(getContext(), intheatres, new TrailerRecyclerAdapter.TrailerOnClickListener() {
            @Override
            public void OnTrailerClicked(int pos) {
                Intent intent = new Intent(getContext(), TrailerActivity.class);
                intent.putExtra(Constants.VIDEO, intheatres.get(pos).getTrailerid());
                startActivity(intent);
            }

            @Override
            public void OnShareClicked(int pos) {

                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");

                intent.putExtra(Intent.EXTRA_TEXT,"https://www.youtube.com/watch?v="+trailer_movielist.get(pos).getTrailerid());
                startActivity(Intent.createChooser(intent,"Share Video"));

            }
        }, new TrailerRecyclerAdapter.setOnClickMoviePosterListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void OnTouchClicked(View v, final MotionEvent e,int pos) {
                if(e.getAction()==MotionEvent.ACTION_UP){
                    if(dialog!=null)
                        dialog.dismiss();
                }
                if(e.getAction()==MotionEvent.ACTION_DOWN){
                    LayoutInflater inflater=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view1=inflater.inflate(R.layout.preview_poster,container,false);
                    ImageView imageView=view1.findViewById(R.id.poster_view);
                    imageView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            dialog.dismiss();
                            return true;
                        }
                    });
                    Picasso.get().load(Constants.IMAGE_URI+""+intheatres.get(pos).getPoster_path()).resize(800,1200).into(imageView);
                    dialog=new Dialog(getContext());
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(
                            new ColorDrawable(Color.TRANSPARENT));
                    dialog.setContentView(view1);
                    dialog.show();
                }
            }
        });

        LinearLayoutManager upcominglayout=new LinearLayoutManager(getContext());
        upcominglayout.setOrientation(LinearLayoutManager.HORIZONTAL);
        SnapHelper snapHelpernow = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        upcomingrecycler.setLayoutManager(upcominglayout);


        snapHelpernow.attachToRecyclerView(upcomingrecycler);
        upcomingrecycler.setAdapter(upcomingRecyclerAdapter);
        toprecycler.setAdapter(topratedRecyclerAdapter);
        poprecycler.setAdapter(moviesRecyclerAdapter);
        recyclerView.setAdapter(trailerRecyclerAdapter);


        return view;
    }

    private void fetchInTheatres() {
        Call<Movie_testclass> call=movieAPI.getNowShowing(1);
        call.enqueue(new Callback<Movie_testclass>() {
            @Override
            public void onResponse(Call<Movie_testclass> call, Response<Movie_testclass> response) {
                Log.d("inon",response.toString());
                intheatres.clear();
                Movie_testclass testclass=response.body();
                ArrayList<Movie> list= new ArrayList<>();
                if (testclass != null) {
                    list = (testclass.getResults());
                }
                fetchInTheatresTrailers(list);
            }

            @Override
            public void onFailure(Call<Movie_testclass> call, Throwable t) {

            }
        });
    }

    private void fetchInTheatresTrailers(ArrayList<Movie> videoslist) {
        for (final Movie movie:videoslist) {
            Call<TrailersTestClass> call = movieAPI.getTrailerList(Integer.toString(movie.getId()));
            call.enqueue(new Callback<TrailersTestClass>() {
                @Override
                public void onResponse(Call<TrailersTestClass> call, Response<TrailersTestClass> response) {
                    TrailersTestClass results = response.body();

                    ArrayList<Trailers> trailersArrayList = null;


                    if (results != null) {
                        trailersArrayList = results.getResults();
                        for (Trailers trailers : trailersArrayList) {
                            if (trailers.getType().equals("Trailer")) {
                                intheatres.add(new Movie(movie.getId(), movie.getTitle(), movie.getRelease_date(), movie.getPoster_path(), movie.getGenre_ids(), trailers.getKey(), movie.getBackdrop_path()));
//                                Toast.makeText(getContext(), intheatres.get(0).getTrailerid(), Toast.LENGTH_SHORT).show();

                                break;

                            }
                            upcomingRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(Call<TrailersTestClass> call, Throwable t) {
                    Toast.makeText(getContext(),"nokey",Toast.LENGTH_SHORT).show();
                    Log.d("error",t.toString());
                }
            });

        }
    }

    private void fetchTopRatedMovies() {
        Call<Movie_testclass> call=movieAPI.getTopRated(1);
        call.enqueue(new Callback<Movie_testclass>() {
            @Override
            public void onResponse(Call<Movie_testclass> call, Response<Movie_testclass> response) {
                topratedmovies.clear();
                Log.d("result",response.toString());
                Movie_testclass movie_testclass=response.body();
                if (movie_testclass != null) {
                    topratedmovies.addAll(movie_testclass.getResults());
                }
                topratedRecyclerAdapter.notifyDataSetChanged();
//                Toast.makeText(getContext(),topratedmovies.size()+"",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Movie_testclass> call, Throwable t) {

            }
        });
    }

    private void fetchPopularMovies() {
        Map<String,String> query=new HashMap<>();
        query.put(Constants.LANG,Constants.LANGUAGE);
        query.put(Constants.SORT_BY,Constants.POPULAR);
        Call<Movie_testclass> call=movieAPI.getPopularMovieList(1);
        call.enqueue(new Callback<Movie_testclass>() {
            @Override
            public void onResponse(Call<Movie_testclass> call, Response<Movie_testclass> response) {
                popular_movie.clear();
                Log.d("result",response.toString());
                Movie_testclass movie_testclass=response.body();
                if (movie_testclass != null) {
                    popular_movie.addAll(movie_testclass.getResults());
                }
                moviesRecyclerAdapter.notifyDataSetChanged();
//                Toast.makeText(getContext(),popular_movie.size()+"",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Movie_testclass> call, Throwable t) {
                Toast.makeText(getContext(),"error",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void fetchTrailers() {
        Map<String,String> query=new HashMap<>();
        query.put(Constants.LANG,Constants.LANGUAGE);

        Call<Movie_testclass> call=movieAPI.getUpcomingMovieList(query,1);
        call.enqueue(new Callback<Movie_testclass>() {
            @Override
            public void onResponse(Call<Movie_testclass> call, Response<Movie_testclass> response) {
                upcomingmovie.clear();
                Movie_testclass results=response.body();
                Log.d("result",response.toString());
                ArrayList<Movie> upcoming_list= null;
                if (results != null) {
                    upcoming_list = results.getResults();
                    upcomingmovie.addAll(upcoming_list);
                    upcomingRecyclerAdapter.notifyDataSetChanged();
                    trailer_movielist = fetchVideos(upcoming_list);
                }
//                trailerRecyclerAdapter.notifyDataSetChanged();//                trailerRecyclerAdapter.notifyDataSetChanged();
//                Toast.makeText(getContext(),String.valueOf(trailer_movielist.size()),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Movie_testclass> call, Throwable t) {

            }
        });
    }

    private ArrayList<Movie> fetchVideos( ArrayList<Movie> videoslist) {
        for (final Movie movie:videoslist) {
            Call<TrailersTestClass> call = movieAPI.getTrailerList(Integer.toString(movie.getId()));
            call.enqueue(new Callback<TrailersTestClass>() {
                @Override
                public void onResponse(Call<TrailersTestClass> call, Response<TrailersTestClass> response) {
                    TrailersTestClass results = response.body();
                    ArrayList<Trailers> trailersArrayList = null;
                    if (results != null) {
                        trailersArrayList = results.getResults();
                        for (Trailers trailers : trailersArrayList) {
                            if (trailers.getType().equals("Trailer")) {
                                trailer_movielist.add(new Movie(movie.getId(), movie.getTitle(), movie.getRelease_date(), movie.getPoster_path(), movie.getGenre_ids(), trailers.getKey(), movie.getBackdrop_path()));
                                break;
                            }
                        }
                        trailerRecyclerAdapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onFailure(Call<TrailersTestClass> call, Throwable t) {
                    Toast.makeText(getContext(),"nokey",Toast.LENGTH_SHORT).show();
                    Log.d("error",t.toString());
                }
            });

        }
        return trailer_movielist;
    }

    @Override
    public void OnTrailerClicked(int pos) {
        Intent intent=new Intent(getContext(),TrailerActivity.class);
        intent.putExtra(Constants.VIDEO,trailer_movielist.get(pos).getTrailerid());
        startActivity(intent);
    }

    @Override
    public void OnShareClicked(int pos) {

        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        intent.putExtra(Intent.EXTRA_TEXT,"https://www.youtube.com/watch?v="+trailer_movielist.get(pos).getTrailerid());
        startActivity(Intent.createChooser(intent,"Share Video"));

    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void OnTouchClicked(View v, MotionEvent e, int pos) {
        if(e.getAction()==MotionEvent.ACTION_UP){
            if(dialog!=null)
                dialog.dismiss();
        }
        if(e.getAction()==MotionEvent.ACTION_DOWN) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view1 = inflater.inflate(R.layout.preview_poster, viewGroup, false);
            ImageView imageView = view1.findViewById(R.id.poster_view);
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    dialog.dismiss();
                    return true;
                }
            });
            Picasso.get().load(Constants.IMAGE_URI + "" + trailer_movielist.get(pos).getPoster_path()).resize(800, 1200).into(imageView);
            dialog = new Dialog(getContext());
            dialog.setCanceledOnTouchOutside(true);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(view1);
            dialog.show();
        }
    }

    @Override
    public void OnMovieClicked(int pos) {
        Intent intent=new Intent(getContext(),MovieDetail.class);
        ArrayList<Integer> integerArrayList=new ArrayList<>();
        for(Movie movie:popular_movie)
            integerArrayList.add(movie.getId());
        intent.putExtra(Constants.ID,integerArrayList);
        intent.putExtra(Constants.POS,pos);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(),"Impressed",Toast.LENGTH_SHORT).show();
    }
}
