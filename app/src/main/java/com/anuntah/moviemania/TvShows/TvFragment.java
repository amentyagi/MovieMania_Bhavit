package com.anuntah.moviemania.TvShows;


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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.anuntah.moviemania.Movies.Adapter.TrailerRecyclerAdapter;
import com.anuntah.moviemania.Movies.Constants.Constants;
import com.anuntah.moviemania.Movies.Networking.Trailers;
import com.anuntah.moviemania.Movies.Networking.TrailersTestClass;
import com.anuntah.moviemania.Movies.TrailerActivity;
import com.anuntah.moviemania.R;
import com.anuntah.moviemania.TvShows.Adapter.TvShowRecyclerAdapter;
import com.anuntah.moviemania.TvShows.NetworkingAndDAO.TvAPI;
import com.anuntah.moviemania.TvShows.NetworkingAndDAO.TvShow;
import com.anuntah.moviemania.TvShows.NetworkingAndDAO.TvShow_testclass;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class TvFragment extends Fragment {

    Retrofit retrofit;
    TvAPI tvAPI;
    ArrayList<TvShow> tvonairtodayList=new ArrayList<>();
    ArrayList<TvShow> tvonairList=new ArrayList<>();
    ArrayList<TvShow> tvpopularList=new ArrayList<>();
    ArrayList<TvShow> tvtopratedList=new ArrayList<>();

    Dialog dialog;

    RecyclerView tvshowpopularReycler;
    RecyclerView tvshowonAirRecyler;
    TvShowRecyclerAdapter populartvshowAdapter;
     com.anuntah.moviemania.TvShows.Adapter.TrailerRecyclerAdapter tvOnAirTodayAdapter;


    public TvFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_tv, container, false);

        tvshowonAirRecyler=view.findViewById(R.id.recyclerViewMovies);
        tvshowpopularReycler=view.findViewById(R.id.recyclerPopular);

        retrofit=new Retrofit.Builder().baseUrl("https://api.themoviedb.org/").addConverterFactory(GsonConverterFactory.create()).build();
        tvAPI=retrofit.create(TvAPI.class);

        tvOnAirTodayAdapter=new com.anuntah.moviemania.TvShows.Adapter.TrailerRecyclerAdapter(getContext(), tvonairtodayList, new com.anuntah.moviemania.TvShows.Adapter.TrailerRecyclerAdapter.TrailerOnClickListener() {
            @Override
            public void OnTrailerClicked(int pos) {
                Intent intent = new Intent(getContext(), TrailerActivity.class);
                intent.putExtra(Constants.VIDEO, tvonairtodayList.get(pos).getTrailer_id());
                startActivity(intent);
            }

            @Override
            public void OnShareClicked(int pos) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");

                intent.putExtra(Intent.EXTRA_TEXT,"https://www.youtube.com/watch?v="+ tvonairtodayList.get(pos).getTrailer_id());
                startActivity(Intent.createChooser(intent,"Share Video"));

            }
        }, new com.anuntah.moviemania.TvShows.Adapter.TrailerRecyclerAdapter.setOnClickMoviePosterListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void OnTouchClicked(View v, MotionEvent e, final int pos) {
                if(e.getAction()==MotionEvent.ACTION_UP){
                    if(dialog!=null)
                        dialog.dismiss();
                }
                if(e.getAction()==MotionEvent.ACTION_DOWN){
                    LayoutInflater inflater=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view1=inflater.inflate(R.layout.preview_poster,container,false);
                    final ImageView imageView=view1.findViewById(R.id.poster_view);
                    imageView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            dialog.dismiss();
                            return true;
                        }
                    });
                    Picasso.get().load(Constants.IMAGE_URI + "w500" + tvonairtodayList.get(pos).getPoster_path()).networkPolicy(NetworkPolicy.OFFLINE).resize(800, 1200).into(imageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(Constants.IMAGE_URI + "w500" + tvonairtodayList.get(pos).getPoster_path()).resize(800, 1200).into(imageView);
                        }
                    });
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

        LinearLayoutManager tvtodaylayout=new LinearLayoutManager(getContext());
        tvtodaylayout.setOrientation(LinearLayoutManager.HORIZONTAL);
        SnapHelper snapHelpernow = new PagerSnapHelper();
        snapHelpernow.attachToRecyclerView(tvshowonAirRecyler);
        tvshowonAirRecyler.setLayoutManager(tvtodaylayout);
        tvshowonAirRecyler.setAdapter(tvOnAirTodayAdapter);

        populartvshowAdapter=new TvShowRecyclerAdapter(getContext(), tvpopularList, new TvShowRecyclerAdapter.setOnMovieClickListner() {
            @Override
            public void OnMovieClicked(int pos) {
                //
            }
        });
        LinearLayoutManager Manager=new LinearLayoutManager(getContext());
        Manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        tvshowpopularReycler.setLayoutManager(Manager);
        tvshowpopularReycler.setAdapter(populartvshowAdapter);


        setUpTvShowsFragment();

        return view;
    }

    void setUpTvShowsFragment() {

        fetchTvShowsOnAirToday();
        fetchTvShowsPopular();

    }

    private void fetchTvShowsPopular() {
        Call<TvShow_testclass> call=tvAPI.getPopularTvShows(1);
        call.enqueue(new Callback<TvShow_testclass>() {
            @Override
            public void onResponse(Call<TvShow_testclass> call, Response<TvShow_testclass> response) {
                TvShow_testclass testclass=response.body();

                if(testclass!=null){
                    tvpopularList.clear();
                    tvpopularList.addAll(testclass.getResults());
                    populartvshowAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<TvShow_testclass> call, Throwable t) {
                fetchTvShowsPopular();
            }
        });
    }

    void fetchTvShowsOnAirToday() {

        Call<TvShow_testclass> call=tvAPI.getTvAiringToday(1);
        call.enqueue(new Callback<TvShow_testclass>() {
            @Override
            public void onResponse(Call<TvShow_testclass> call, Response<TvShow_testclass> response) {
                TvShow_testclass testclass=response.body();
                Log.d("tv",response.toString());
                if (testclass != null) {
                    Log.d("tv",response.toString());
                    tvonairtodayList.clear();
                    tvOnAirTodayAdapter.notifyDataSetChanged();
                    ArrayList<TvShow> tvShowArrayList = new ArrayList<>(testclass.getResults());
                    getTvShowTrailer(tvShowArrayList);
                }
            }

            @Override
            public void onFailure(Call<TvShow_testclass> call, Throwable t) {
                fetchTvShowsOnAirToday();
            }
        });

    }

    private void getTvShowTrailer(ArrayList<TvShow> tvShowArrayList) {
        for(final TvShow tvShow:tvShowArrayList) {
            Call<TrailersTestClass> call = tvAPI.getTrailerList(tvShow.getId());
            call.enqueue(new Callback<TrailersTestClass>() {
                @Override
                public void onResponse(Call<TrailersTestClass> call, Response<TrailersTestClass> response) {
                    TrailersTestClass testClass=response.body();
                    if(testClass!=null){
                        for(Trailers trailers:testClass.getResults()){
                            if (trailers.getType().equals("Trailer")) {
                                TvShow tvShow1 = new TvShow(tvShow.getId(), tvShow.getName(), tvShow.getGenre_ids(), tvShow.getPopularity(), tvShow.getVote_count(), tvShow.getFirst_air_date(), tvShow.getBackdrop_path(), tvShow.getVote_average(), tvShow.getOverview(), tvShow.getPoster_path(), trailers.getKey());
                                tvonairtodayList.add(tvShow1);
                                break;
                            }
                        }
                        Log.d("TV",response.toString());
                        tvOnAirTodayAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<TrailersTestClass> call, Throwable t) {
                    fetchTvShowsOnAirToday();
                }
            });
        }
    }

}
