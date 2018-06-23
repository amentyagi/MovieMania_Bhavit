package com.anuntah.moviemania.Movies;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anuntah.moviemania.MovieDatabase;
import com.anuntah.moviemania.Movies.Adapter.VideosPagerAdapter;
import com.anuntah.moviemania.Movies.Constants.Constants;
import com.anuntah.moviemania.Movies.Networking.Genre;
import com.anuntah.moviemania.Movies.Networking.Movie;
import com.anuntah.moviemania.Movies.Networking.MovieAPI;
import com.anuntah.moviemania.Movies.Networking.Trailers;
import com.anuntah.moviemania.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetail extends AppCompatActivity {

   /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    ArrayList<Movie> movieArrayList;
    ArrayList<Integer> idlist=new ArrayList<>();
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private int listpos;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        Intent intent=getIntent();
        idlist=intent.getIntegerArrayListExtra(Constants.ID);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        Log.d("list",String.valueOf(idlist.get(0)));
        listpos=intent.getIntExtra(Constants.POS,-1);

        Bundle b=new Bundle();
        b.putIntegerArrayList(Constants.ID,idlist);
        b.putString("sucess","sucess");
        PlaceholderFragment placeholderFragment=new PlaceholderFragment();
        placeholderFragment.setArguments(b);
        // Set up the ViewPager with the sections adapter.
        mViewPager =  findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements Callback<Movie>,VideosPagerAdapter.setOnTrailerClicked {

        private Retrofit retrofit=new Retrofit.Builder().baseUrl(Constants.base_url).addConverterFactory(GsonConverterFactory.create()).build();
        MovieAPI movieAPI=retrofit.create(MovieAPI.class);

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        ViewPager mviewpager;
        TextView moviename,releaseyear,moviegenre,overview;
        ImageView poster;
        TextView rating,runtime;

        VideosPagerAdapter pagerAdapter;
        ArrayList<Trailers>
                trailers=new ArrayList<>();
        ArrayList<Integer> idlist=new ArrayList<>();
        int pos;

        public PlaceholderFragment() {
        }




        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        Movie movie;
        public static PlaceholderFragment newInstance(int sectionNumber,ArrayList<Integer> idlist,int listpos) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putIntegerArrayList(Constants.ID,idlist);
            args.putInt(Constants.POS,listpos);
            fragment.setArguments(args);
            return fragment;
        }

        MovieDatabase movieDatabase;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.movie_detail_activity, container, false);

            movieDatabase=MovieDatabase.getInstance(getContext());

            Toast.makeText(getContext(),"called",Toast.LENGTH_SHORT).show();
            Bundle b;
            b=getArguments();
            int listpos=b.getInt(Constants.POS);
            Log.d("list",String.valueOf(listpos));
            pos=b.getInt(ARG_SECTION_NUMBER);
            idlist=b.getIntegerArrayList(Constants.ID);
            moviename=rootView.findViewById(R.id.movie_name);
            releaseyear=rootView.findViewById(R.id.release_year);
            moviegenre=rootView.findViewById(R.id.movie_genre);
            overview=rootView.findViewById(R.id.description);
            poster=rootView.findViewById(R.id.movie_poster);
            rating=rootView.findViewById(R.id.rating_number);
            runtime=rootView.findViewById(R.id.run_time);
            mviewpager=rootView.findViewById(R.id.trailer_placeholder);

            pagerAdapter=new VideosPagerAdapter(trailers,getContext(),this);

            mviewpager.setAdapter(pagerAdapter);

            Movie movie1=movieDatabase.getMoviesDAO().getMovies(idlist.get(listpos+pos-1));

            if(movie1!=null){
                setUpMovie(movie1);
            }

            Call<Movie> call=movieAPI.getMovieDetail(idlist.get(listpos+pos-1));     //listpos+pos-1
            call.enqueue(this);

            return rootView;
        }

        @Override
        public void onResponse(Call<Movie> call, Response<Movie> response) {
            movie=response.body();
            if (movie != null) {
                Log.d("movie", movie.getTitle());
                setUpMovie(movie);
            }
        }

        private void setUpMovie(Movie movie) {
            String genres = "";
            moviename.setText(movie.getTitle());
            releaseyear.setText(movie.getRelease_date());

            for (Genre genre : movie.getGenres()) {
                genres = genres.concat(genre.getName() + ",");
            }
            genres = genres.substring(0, genres.length() - 1);
            moviegenre.setText(genres);

            overview.setText(movie.getOverview());
            Picasso.get().load(Constants.IMAGE_URI + "" + movie.getPoster_path()).resize(100, 140).into(poster);
            rating.setText(String.valueOf(movie.getVote_average()));
            if(runtime!=null)
            runtime.setText(movie.getRuntime() + "mins");
            if(movie.getVideos()!=null)
                trailers.addAll(movie.getVideos().getResults());
            pagerAdapter.notifyDataSetChanged();

            final int[] currentPage = new int[1];
            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (currentPage[0] == trailers.size()) {
                        currentPage[0] = 0;
                    }
                    mviewpager.setCurrentItem(currentPage[0]++, true);
                }
            };
            Timer swipeTimer = new Timer();
            swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, 2500, 2500);
        }

        @Override
        public void onFailure(Call<Movie> call, Throwable t) {

        }

        @Override
        public void OnVideoClicked(int pos) {
            Intent intent=new Intent(getContext(),TrailerActivity.class);
            intent.putExtra(Constants.VIDEO,trailers.get(pos).getKey());
            startActivity(intent);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment fragment= PlaceholderFragment.newInstance(position + 1,idlist,listpos);
            View view=fragment.getView();
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return idlist.size();
        }
    }
}
