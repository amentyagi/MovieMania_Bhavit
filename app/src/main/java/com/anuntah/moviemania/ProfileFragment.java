package com.anuntah.moviemania;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.anuntah.moviemania.Movies.Constants.Constants;
import com.anuntah.moviemania.Movies.Networking.MovieAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    Button loginbutton;
    CustomTabsServiceConnection connection;

    private Retrofit retrofit;
    MovieAPI movieAPI;
    CustomTabsClient clients;
    CustomTabsSession customTabsSession;
    boolean islogged;

    public ProfileFragment() {

        connection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient client) {
                clients=client;
                clients.warmup(0L);
                customTabsSession=clients.newSession(null);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                clients=null;
            }
        };
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


            View v = inflater.inflate(R.layout.login_layout, container, false);
            retrofit = new Retrofit.Builder().baseUrl(Constants.base_url).addConverterFactory(GsonConverterFactory.create()).build();
            movieAPI = retrofit.create(MovieAPI.class);
            loginbutton = v.findViewById(R.id.button);
            loginbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userAuth();
                }
            });
            return v;

    }

    private void userAuth() {
        Call<RequestToken> call=movieAPI.getRequestToken();
        call.enqueue(new Callback<RequestToken>() {
            @Override
            public void onResponse(Call<RequestToken> call, Response<RequestToken> response) {
                RequestToken requestToken=response.body();
                if (requestToken != null) {

                    final String token=requestToken.getRequest_token();
                    final String url="https://www.themoviedb.org/authenticate/"+token+"?redirect_to=anything://www.moviemania.com";
                    if(customTabsSession!=null)
                    customTabsSession.mayLaunchUrl(Uri.parse(url),null,null);
//
// Intent intent=new Intent(getContext(),WebView.class);
//                    intent.putExtra("token",token);
//                    startActivity(intent);

                    connection = new CustomTabsServiceConnection() {
                        @Override
                        public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient client) {
                            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                            CustomTabsIntent intent = builder.build();
                            intent.intent.setPackage("com.android.chrome");
                            intent.intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            Log.d("token",token);
                            intent.intent.putExtra("token",token);
                            intent.launchUrl(getContext(), Uri.parse(url));//pass the url you need to open
                        }

                        @Override
                        public void onServiceDisconnected(ComponentName name) {

                        }
                    };
                    CustomTabsClient.bindCustomTabsService(getContext(), "com.android.chrome", connection);
                }
            }

            @Override
            public void onFailure(Call<RequestToken> call, Throwable t) {

            }
        });
    }


}
