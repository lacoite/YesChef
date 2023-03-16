package com.example.yeschef;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class WebViewActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Bundle extras = getIntent().getExtras();
        String value = extras.getString("key");
        //The key argument here must match that used in the other activity
        WebView myWebView = new WebView(this);
        setContentView(myWebView);
        myWebView.loadUrl(value);

    }
}