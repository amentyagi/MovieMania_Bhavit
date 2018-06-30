package com.anuntah.moviemania;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UserProfile extends Fragment {

    public UserProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.user_profile,container,false);

        SharedPreferences sharedPreferences=getContext().getSharedPreferences("moviemania",Context.MODE_PRIVATE);

        final SharedPreferences.Editor editor=sharedPreferences.edit();

        TextView textView=v.findViewById(R.id.username_text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("Logged","false");
                editor.commit();
                ProfileFragment profileFragment=new ProfileFragment();
                FragmentManager fragmentManager=getFragmentManager();
                FragmentTransaction transaction=fragmentManager.beginTransaction();
                transaction.replace(R.id.mainFrame,profileFragment).commit();
            }
        });
        return v;

    }


}
