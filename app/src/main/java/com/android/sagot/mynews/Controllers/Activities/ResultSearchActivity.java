package com.android.sagot.mynews.Controllers.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.android.sagot.mynews.Controllers.Fragments.SearchFragment;
import com.android.sagot.mynews.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.sagot.mynews.Utils.UIUtilities.changeStatusBarColor;

public class ResultSearchActivity extends AppCompatActivity {

    // Adding @BindView in order to indicate to ButterKnife to get & serialise it
    @BindView(R.id.toolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_search);

        // Get & serialise all views
        ButterKnife.bind(this);


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
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        // Change Color of the Toolbar
        mToolbar.setBackgroundColor(getResources().getColor(R.color.searchPrimary));
    }

    // --------------
    // FRAGMENTS
    // --------------

    private void configureAndShowSearchFragment(){
        // Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        SearchFragment fragment = (SearchFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activity_result_frame_layout);

        if (fragment == null) {
            // Create new main fragment
            fragment = SearchFragment.newInstance(4);
            // Add it to FrameLayout container
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_result_frame_layout, fragment)
                    .commit();
        }
    }
}
