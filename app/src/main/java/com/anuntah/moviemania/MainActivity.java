package com.anuntah.moviemania;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.anuntah.moviemania.Movies.MoviesFragment;
import com.anuntah.moviemania.TvShows.TvFragment;
import com.google.android.youtube.player.YouTubeThumbnailView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    private ViewPager mSlideViewPager;
    private LinearLayout mDotsLayout;
    private SlideAdapter slideAdapter;
    private YouTubeThumbnailView ytThubnailView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String logged="loggednot";
    CustomTabsServiceConnection connection;
    CustomTabsClient clients;
    CustomTabsSession customTabsSession;

    @SuppressLint("ApplySharedPref")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout=findViewById(R.id.mainFrame);

        sharedPreferences=getSharedPreferences("moviemania",MODE_PRIVATE);
        Intent intent=getIntent();
        String action=intent.getAction();
        if(Intent.ACTION_VIEW.equals(action)){
            String data=intent.getDataString();
            assert data != null;
            if(data.contains("approved")) {
                editor = sharedPreferences.edit();
                editor.putString("Logged", "true");
                String token=data.substring(44,data.indexOf("&"));
                Log.d("token", intent.getDataString());
                Log.d("token", token);
                editor.commit();
            }
        }




        bottomNavigationView=findViewById(R.id.mainNav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.movie_btn:
                        setFragment(new MoviesFragment());
                        return true;

                    case  R.id.tv_btn:
                        setFragment(new TvFragment());
                        return true;
                    case R.id.profile_btn:
                        String islogged=sharedPreferences.getString("Logged","false");
                        Log.d("nj",islogged);
                        if(islogged.equals("true"))
                            setFragment(new UserProfile());
                        else
                        setFragment(new ProfileFragment());
                        return true;
                }
                return true;
            }
        });


//        MoviesFragment fragment=new MoviesFragment();
//        FragmentManager fragmentManager=getSupportFragmentManager();
//        FragmentTransaction transaction=fragmentManager.beginTransaction();
//        transaction.add(R.id.mainFrame,fragment).commit();
        setFragment(new MoviesFragment());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String action = intent.getAction();
        String data = intent.getDataString();
//        if (Intent.ACTION_VIEW.equals(action)) {
//            Toast.makeText(this,"logged",Toast.LENGTH_SHORT).show();
//        }
        editor=sharedPreferences.edit();
        editor.putString("Logged","true");
        Log.d("nj","1234true");
        editor.commit();
        setFragment(new ProfileFragment());
    }

    private void setFragment(Fragment fragment) {
        frameLayout.removeAllViews();
        Bundle b=new Bundle();
        b.putString("Logged",logged);
        fragment.setArguments(b);
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.add(R.id.mainFrame,fragment).commit();
    }

}
