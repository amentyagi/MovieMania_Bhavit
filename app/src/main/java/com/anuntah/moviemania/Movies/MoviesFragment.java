package com.anuntah.moviemania.Movies;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import com.anuntah.moviemania.MovieDatabase;
import com.anuntah.moviemania.Movies.Adapter.GenreListAdapter;
import com.anuntah.moviemania.Movies.AsyncTask.MovieFragmentAsyncTask;
import com.anuntah.moviemania.Movies.AsyncTask.MoviePosterAsyncTask;
import com.anuntah.moviemania.Movies.Adapter.MoviesRecyclerAdapter;
import com.anuntah.moviemania.Movies.Adapter.TrailerRecyclerAdapter;
import com.anuntah.moviemania.Movies.Constants.Constants;
import com.anuntah.moviemania.Movies.Constants.Genre_constants;
import com.anuntah.moviemania.Movies.Networking.Genre;
import com.anuntah.moviemania.Movies.Networking.GenreList;
import com.anuntah.moviemania.Movies.Networking.Movie;
import com.anuntah.moviemania.Movies.Networking.MovieAPI;
import com.anuntah.moviemania.Movies.Networking.Movie_testclass;
import com.anuntah.moviemania.Movies.Networking.Trailers;
import com.anuntah.moviemania.Movies.Networking.TrailersTestClass;
import com.anuntah.moviemania.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
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

    MovieDatabase movieDatabase;

    private Retrofit retrofit=new Retrofit.Builder().baseUrl(Constants.base_url).addConverterFactory(GsonConverterFactory.create()).build();
    MovieAPI movieAPI=retrofit.create(MovieAPI.class);
    ArrayList<Movie> upcomingmovielist =new ArrayList<>();

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
    RecyclerView poprecycler,toprecycler, intheatresrecycler;
    ArrayList<Movie> popular_movie=new ArrayList<>();
    MoviesRecyclerAdapter popularmoviesRecyclerAdapter;

    RecyclerView genreRecyclerview;
    GenreListAdapter genreListAdapter;
    ArrayList<Genre> genreArrayList=new ArrayList<>();
    GestureDetector detector;
    TrailerRecyclerAdapter intheatresRecyclerAdapter;
    RecyclerView upcomingRecyclerView;
    TrailerRecyclerAdapter upcomingRecyclerAdapter;
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_movies, container, false);
        upcomingRecyclerView =view.findViewById(R.id.recyclerUpcoming);
        poprecycler=view.findViewById(R.id.recyclerPopular);
        toprecycler=view.findViewById(R.id.recyclerTopRated);
        intheatresrecycler =view.findViewById(R.id.recyclerInTheatre);
        upcomingsee=view.findViewById(R.id.upcoming_see_all);
        popularsee=view.findViewById(R.id.popular_see_all);
        intheatresee=view.findViewById(R.id.in_theatre_text_seeall);
        topratedsee=view.findViewById(R.id.topRated_see_all);
        genreRecyclerview=view.findViewById(R.id.recyclerGenres);

        movieDatabase=MovieDatabase.getInstance(getContext());



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

        genreListAdapter=new GenreListAdapter(getContext(), genreArrayList, new GenreListAdapter.onGenreClickListener() {
            @Override
            public void onGenreClicked(int position) {
                Intent intent=new Intent(getContext(),MovieListView.class);
                intent.putExtra("id",genreArrayList.get(position).getId());
                startActivity(intent);
            }
        });

        LinearLayoutManager genrelayout=new LinearLayoutManager(getContext());
        genrelayout.setOrientation(LinearLayoutManager.HORIZONTAL);


        genreRecyclerview.setLayoutManager(genrelayout);

        upcomingRecyclerAdapter =new TrailerRecyclerAdapter(getContext(), upcomingmovielist, this,this);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        upcomingRecyclerView.setLayoutManager(linearLayoutManager);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(upcomingRecyclerView);

        popularmoviesRecyclerAdapter =new MoviesRecyclerAdapter(getContext(),popular_movie,this);
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


        intheatresRecyclerAdapter =new TrailerRecyclerAdapter(getContext(), intheatres, new TrailerRecyclerAdapter.TrailerOnClickListener() {
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

                intent.putExtra(Intent.EXTRA_TEXT,"https://www.youtube.com/watch?v="+ upcomingmovielist.get(pos).getTrailerid());
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
        snapHelper.attachToRecyclerView(upcomingRecyclerView);
        intheatresrecycler.setLayoutManager(upcominglayout);
        snapHelpernow.attachToRecyclerView(intheatresrecycler);


        genreRecyclerview.setAdapter(genreListAdapter);
        upcomingRecyclerView.setAdapter(upcomingRecyclerAdapter);
        poprecycler.setAdapter(popularmoviesRecyclerAdapter);
        intheatresrecycler.setAdapter(intheatresRecyclerAdapter);
        toprecycler.setAdapter(topratedRecyclerAdapter);



        MovieFragmentAsyncTask asyncTask=new MovieFragmentAsyncTask(movieDatabase, popularmoviesRecyclerAdapter,popular_movie);
        asyncTask.execute();

        if(movieDatabase.getMoviesDAO().getMoviesUpcomingIntheatres(Constants.UPCOMING)!=null) {

            upcomingmovielist.addAll(movieDatabase.getMoviesDAO().getMoviesUpcomingIntheatres(Constants.UPCOMING));
            Toast.makeText(getContext(), "yes", Toast.LENGTH_SHORT).show();
            Log.d("mohit","1toprated");
            upcomingRecyclerAdapter.notifyDataSetChanged();

        }if(movieDatabase.getMoviesDAO().getMoviesTopRated(Constants.TOPRATED)!=null) {

            topratedmovies.addAll(movieDatabase.getMoviesDAO().getMoviesTopRated(Constants.TOPRATED));
            Log.d("mohit","toprated");
            topratedRecyclerAdapter.notifyDataSetChanged();
        }if(movieDatabase.getMoviesDAO().getMoviesUpcomingIntheatres(Constants.INTHEATRES)!=null) {

            intheatres.addAll(movieDatabase.getMoviesDAO().getMoviesUpcomingIntheatres(Constants.INTHEATRES));
            intheatresRecyclerAdapter.notifyDataSetChanged();
        }


        fetchUpcomingMovies();
        fetchPopularMovies();
        fetchTopRatedMovies();
        fetchInTheatresMovies();

        fetchGenres();


//            popular_movie.addAll(movieDatabase.getMoviesDAO().getMoviesList(Constants.pOPULARS));
//            Log.d("bhavit",popular_movie.size()+"");
//            popularmoviesRecyclerAdapter.notifyDataSetChanged();



        return view;
    }


    private void fetchGenres() {
        if(movieDatabase.genreDAO().getGenresList().size()==0) {
            Call<GenreList> call = movieAPI.getGenres();

            call.enqueue(new Callback<GenreList>() {
                @Override
                public void onResponse(Call<GenreList> call, Response<GenreList> response) {
                    GenreList genreList = response.body();
                    ArrayList<Genre> genres = genreList.getGenres();
                    Log.d("tage", genreArrayList.size() + "");

                    for (Genre genre : genres) {
                        switch (genre.getId() + "") {
                            case Genre_constants.ACTION:
                                genre.setDrawableid(R.drawable.action);
                                genreArrayList.add(genre);
                                break;
                            case Genre_constants.ADVENTURE:
                                genre.setDrawableid(R.drawable.adventure);
                                genreArrayList.add(genre);
                                break;
                            case Genre_constants.ANIMATION:
                                genre.setDrawableid(R.drawable.animation);
                                genreArrayList.add(genre);
                                break;
                            case Genre_constants.COMEDY:
                                genre.setDrawableid(R.drawable.comedy);
                                genreArrayList.add(genre);
                                break;
                            case Genre_constants.DRAMA:
                                genre.setDrawableid(R.drawable.drama);
                                genreArrayList.add(genre);
                                break;
                            case Genre_constants.FANTASY:
                                genre.setDrawableid(R.drawable.fantasy);
                                genreArrayList.add(genre);
                                break;
                            case Genre_constants.HORROR:
                                genre.setDrawableid(R.drawable.horror);
                                genreArrayList.add(genre);
                                break;
                            case Genre_constants.ROMANCE:
                                genre.setDrawableid(R.drawable.romance);
                                genreArrayList.add(genre);
                                break;
                            case Genre_constants.THRILLER:
                                genre.setDrawableid(R.drawable.thriller);
                                genreArrayList.add(genre);
                                break;
                            case Genre_constants.SCI_FICTION:
                                genre.setDrawableid(R.drawable.scifi);
                                genreArrayList.add(genre);
                        }

                    }
                    genreListAdapter.notifyDataSetChanged();
                    movieDatabase.genreDAO().insert(genreArrayList);

                }

                @Override
                public void onFailure(Call<GenreList> call, Throwable t) {
                    Log.d("tag", "ok");

                }
            });
        }
        else {
            genreArrayList.addAll(movieDatabase.genreDAO().getGenresList());
            genreListAdapter.notifyDataSetChanged();
        }
    }

    private void fetchInTheatresMovies() {
        Call<Movie_testclass> call=movieAPI.getNowShowing(1);
        call.enqueue(new Callback<Movie_testclass>() {
            @Override
            public void onResponse(Call<Movie_testclass> call, Response<Movie_testclass> response) {
                Log.d("inon",response.toString());
                intheatres.clear();
                Movie_testclass testclass=response.body();
                ArrayList<Movie> list= new ArrayList<>();
                if (testclass != null) {
                    list.addAll(testclass.getResults());
                }
                new MoviePosterAsyncTask(movieDatabase,Constants.INTHEATRES).execute(list.toArray(new Movie[list.size()]));
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
                            intheatresRecyclerAdapter.notifyDataSetChanged();
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
                Log.d("result",response.toString());
                Movie_testclass movie_testclass=response.body();
                if (movie_testclass != null) {
                    topratedmovies.clear();
                    topratedmovies.addAll(movie_testclass.getResults());
                    for(Movie movie:movie_testclass.getResults()){
                        popular_movie.add(movie);
                    }
                    new MoviePosterAsyncTask(movieDatabase,Constants.TOPRATED).execute(topratedmovies.toArray(new Movie[topratedmovies.size()]));

                }
                topratedRecyclerAdapter.notifyDataSetChanged();
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
                Log.d("result",response.toString());
                Movie_testclass movie_testclass=response.body();
                if (movie_testclass != null) {
                    popular_movie.clear();
                    for(Movie movie:movie_testclass.getResults()){
                        popular_movie.add(movie);
                    }
                    new MoviePosterAsyncTask(movieDatabase,Constants.pOPULARS).execute(popular_movie.toArray(new Movie[popular_movie.size()]));
                }
                popularmoviesRecyclerAdapter.notifyDataSetChanged();
//                Toast.makeText(getContext(),popular_movie.size()+"",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Movie_testclass> call, Throwable t) {
                Toast.makeText(getContext(),"error",Toast.LENGTH_SHORT).show();
            }
        });

    }

//    private ArrayList<Genre> idToGenre(ArrayList<Integer> genreid) {
//        ArrayList<Genre> genreArrayList=new ArrayList<>();
//        for(int id:genreid){
//            String genre=genreMatcher(id);
//            Genre genre1=new Genre(id,genre);
//            genreArrayList.add(genre1);
//        }
//        return genreArrayList;
//    }

    private void fetchUpcomingMovies() {
        Map<String,String> query=new HashMap<>();
        query.put(Constants.LANG,Constants.LANGUAGE);

        Call<Movie_testclass> call=movieAPI.getUpcomingMovieList(query,1);
        call.enqueue(new Callback<Movie_testclass>() {
            @Override
            public void onResponse(Call<Movie_testclass> call, Response<Movie_testclass> response) {

                Movie_testclass results=response.body();
                Log.d("result",response.toString());
                ArrayList<Movie> upcoming_list= null;
                if (results != null) {
                    upcomingmovielist.clear();
                    upcoming_list = results.getResults();
                    upcomingmovie.addAll(upcoming_list);
                    upcomingmovielist=fetchVideos(upcoming_list);
                }
                new MoviePosterAsyncTask(movieDatabase,Constants.UPCOMING).execute(upcoming_list.toArray(new Movie[upcoming_list.size()]));
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
                                upcomingmovielist.add(new Movie(movie.getId(), movie.getTitle(), movie.getRelease_date(), movie.getPoster_path(), movie.getGenre_ids(), trailers.getKey(), movie.getBackdrop_path()));
                                break;
                            }
                        }
                        upcomingRecyclerAdapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onFailure(Call<TrailersTestClass> call, Throwable t) {
                    Toast.makeText(getContext(),"nokey",Toast.LENGTH_SHORT).show();
                    Log.d("error",t.toString());
                }
            });

        }
        return upcomingmovielist;
    }

    @Override
    public void OnTrailerClicked(int pos) {
        Intent intent=new Intent(getContext(),TrailerActivity.class);
        intent.putExtra(Constants.VIDEO, upcomingmovielist.get(pos).getTrailerid());
        startActivity(intent);
    }

    @Override
    public void OnShareClicked(int pos) {

        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        intent.putExtra(Intent.EXTRA_TEXT,"https://www.youtube.com/watch?v="+ upcomingmovielist.get(pos).getTrailerid());
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
            Picasso.get().load(Constants.IMAGE_URI + "" + upcomingmovielist.get(pos).getPoster_path()).resize(800, 1200).into(imageView);
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

//    private String genreMatcher(int pos) {
//        switch (pos){
//            case 28:return "Action";
//            case 12 :return "Adventure";
//            case 16 :return "Animation";
//            case 35 :return  "Comedy";
//            case 80 :return "Crime";
//            case 99 :return "Documentary";
//            case 18 :return "Drama";
//            case 14 :return "Fantasy";
//            case 27 :return  "Horror";
//            case 10749:return "Romance";
//            case 878:return  "Sci-Fic";
//            case 53 :return "Thriller";
//            case 10751: return "Family";
//            case 36:return "History";
//            case 10402:return "Music";
//            case 9648:return "Mystery";
//            case 10752:return "War";
//            case 37:return "Western";
//        }
//        return "k";
//    }
}
