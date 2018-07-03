package com.anuntah.moviemania;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfile extends Fragment {

    private GoogleSignInClient mGoogleSignInClient;

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

                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);


                final MainActivity mainActivity=(MainActivity)getActivity();


                final FirebaseAuth mAuth=FirebaseAuth.getInstance();
                mGoogleSignInClient.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mAuth.signOut();
                        FirebaseAuth.getInstance().signOut();

                        if (mainActivity != null) {
                            mainActivity.setFragment(new LoginFragment(),"replace");
                        }
                        Log.d("tag","signout");
                    }
                });
                editor.putString("Logged","false");
                editor.commit();
//                LoginFragment loginFragment =new LoginFragment();
//                FragmentManager fragmentManager=getFragmentManager();
//                FragmentTransaction transaction=fragmentManager.beginTransaction();
//                transaction.replace(R.id.mainFrame, loginFragment).commit();
            }
        });
        return v;

    }


}
