/*package com.anuntah.moviemania;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebViewClient;

public class WebView extends AppCompatActivity {

    android.webkit.WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Intent intent=getIntent();
        String url=intent.getStringExtra("token");
        webView=findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://www.themoviedb.org/authenticate/"+url+"?redirect_to=http://www.google.com");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
                super.shouldOverrideUrlLoading(view, url);
                view.loadUrl(url);
                view.setWebViewClient(new WebViewClient(){
                    @Override
                    public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
                         super.shouldOverrideUrlLoading(view, url);
                         finish();
                         return true;
                    }
                });
                return true;
            }

        });
    }
}*/
