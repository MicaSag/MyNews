package com.android.sagot.mynews.Controllers.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.sagot.mynews.Controllers.Fragments.BaseNewsFragment;
import com.android.sagot.mynews.Controllers.Fragments.TopStoriesFragment;
import com.android.sagot.mynews.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.sagot.mynews.Utils.UIUtilities.changeStatusBarColor;

public class ItemSearchActivity extends AppCompatActivity {

    // For Debug
    private static final String TAG = ItemActivity.class.getSimpleName();

    // Adding @BindView in order to indicate to ButterKnife to get & serialise it
    @BindView(R.id.activity_search_item_webView) WebView mWebView;
    @BindView(R.id.toolbar) Toolbar mToolBar;

    // URL of the news
    private String mNewURL;

    // Tab Position
    int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);

        // Get & serialise all views
        ButterKnife.bind(this);

        // Get back the data intent in the activity
        Intent i = getIntent();
        mNewURL = i.getStringExtra(TopStoriesFragment.BUNDLE_NEWS_URL);
        mPosition = i.getIntExtra(BaseNewsFragment.BUNDLE_TAB_LAYOUT_POSITION,0);

        // Change the color of the status bar and of the toolbar
        applyColorTheme();

        // Configuring Toolbar
        this.configureToolbar();

        // Set emulator View
        mWebView.setWebViewClient(new WebViewClient());

        // Show the page of the news
        mWebView.loadUrl(mNewURL);
    }

    private void configureToolbar(){
        Log.d(TAG, "configureToolbar: ");
        //Set the toolbar
        setSupportActionBar(mToolBar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void applyColorTheme() {
        // Change Color of the Toolbar
        this.mToolBar.setBackgroundColor(getResources()
                .obtainTypedArray(R.array.primary_colors).getColor(mPosition, 0));

        // Change Color of the Status Toolbar
        changeStatusBarColor(this, getResources()
                .obtainTypedArray(R.array.primaryDark_colors).getColor(mPosition, 0));
    }
}
