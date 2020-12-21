package com.dss.swmusic.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.dss.swmusic.R;


/**
 * 网页视图，是一个activity，页面中只有一个WebView控件
 */
public class MyWebView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_web_view);

        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        webView.loadUrl(url);
    }

    public static void start(Activity activity,String url){
        Intent intent = new Intent(activity,MyWebView.class);
        intent.putExtra("url",url);
        activity.startActivity(intent);
    }

}