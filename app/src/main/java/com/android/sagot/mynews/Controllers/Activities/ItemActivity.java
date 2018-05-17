package com.android.sagot.mynews.Controllers.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.sagot.mynews.Controllers.Fragments.TopStoriesFragment;
import com.android.sagot.mynews.R;

public class ItemActivity extends AppCompatActivity {

    private static final String TAG = ItemActivity.class.getSimpleName();
    
    private String mNewURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        // Configuring Toolbar
        this.configureToolbar();

        Intent i = getIntent();
        mNewURL = i.getStringExtra(TopStoriesFragment.BUNDLE_NEWS_URL);
        WebView webView = (WebView) findViewById(R.id.activity_item_webView);

        // Set emulator View
        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl(mNewURL);
    }

    private void configureToolbar(){
        Log.d(TAG, "configureToolbar: ");
        //Get the toolbar (Serialise)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //Set the toolbar
        setSupportActionBar(toolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
    }
}
