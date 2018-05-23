package com.android.sagot.mynews.Controllers.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.android.sagot.mynews.Controllers.Fragments.ResultSearchFragment;
import com.android.sagot.mynews.Models.SearchCriteria;
import com.android.sagot.mynews.R;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.sagot.mynews.Utils.UIUtilities.changeStatusBarColor;

public class ResultSearchActivity extends AppCompatActivity {

    // Adding @BindView in order to indicate to ButterKnife to get & serialise it
    @BindView(R.id.toolbar) Toolbar mToolbar;

    // String containing the criteria of search serialized
    String mSearchCriteria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_search);

        // Get & serialise all views
        ButterKnife.bind(this);

        // Get back Intent send to parameter by the SearchActivity
        Intent intent = getIntent();
        mSearchCriteria = intent.getStringExtra(SearchActivity.BUNDLE_SEARCH_CRITERIA);

        // Configuring Toolbar
        this.configureToolbar();

        // Change the color of the Status Bar
        changeStatusBarColor(this,getResources().getColor(R.color.searchPrimaryDark));

        configureAndShowSearchFragment();
    }

    // --------------
    // TOOLBAR
    // --------------

    private void configureToolbar(){
         //Set the toolbar
        setSupportActionBar(mToolbar);
        // Change Color of the Toolbar
        mToolbar.setBackgroundColor(getResources().getColor(R.color.searchPrimary));
    }

    // --------------
    // FRAGMENTS
    // --------------

    private void configureAndShowSearchFragment(){
        // Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        ResultSearchFragment fragment = (ResultSearchFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activity_result_frame_layout);

        if (fragment == null) {
            // Create new main fragment
            fragment = ResultSearchFragment.newInstance(4,mSearchCriteria);
            // Add it to FrameLayout container
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_result_frame_layout, fragment)
                    .commit();
        }
    }
}
