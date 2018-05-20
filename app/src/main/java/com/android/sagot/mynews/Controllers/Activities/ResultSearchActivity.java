package com.android.sagot.mynews.Controllers.Activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

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

        // Change Color of the Toolbar
        mToolbar.setBackgroundColor(getResources().getColor(R.color.searchPrimary));
    }

    // --------------
    // TOOLBAR
    // --------------

    private void configureToolbar(){
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
