package com.alghazal.mohammed.hussein.project6newapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewer extends AppCompatActivity {

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Bundle extras = getIntent().getExtras();

        String url= extras.getString("URL");
        webView=  findViewById(R.id.webView);
        webView.loadUrl(url);
    }
}
