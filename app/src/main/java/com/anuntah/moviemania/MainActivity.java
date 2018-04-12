package com.anuntah.moviemania;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    //private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    private ViewPager mSlideViewPager;
    private LinearLayout mDotsLayout;
    private SlideAdapter slideAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_movies);

        mSlideViewPager = findViewById(R.id.slideViewPages);
        mDotsLayout = findViewById(R.id.dotsLayout);

        /*bottomNavigationView = findViewById(R.id.mainNav);
        frameLayout = findViewById(R.id.mainFrame);*/

        mSlideViewPager = findViewById(R.id.slideViewPages);
        mDotsLayout = findViewById(R.id.dotsLayout);


        /*bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.movie_btn:
                        frameLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        return true;

                    case  R.id.tv_btn:
                        frameLayout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        return true;
                    case R.id.profile_btn:
                        frameLayout.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                        return true;
                }
                return true;
            }
        });*/


        /*slideAdapter = new SlideAdapter(this);
        mSlideViewPager.setAdapter(slideAdapter);*/


    }
}
