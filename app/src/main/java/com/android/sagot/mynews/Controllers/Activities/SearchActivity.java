package com.android.sagot.mynews.Controllers.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.sagot.mynews.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    // Adding @BindView in order to indicate to ButterKnife to get & serialise it
    @BindView(R.id.activity_search_text) TextView mTextview;
    @BindView(R.id.activity_search_spinner_date_begin) Spinner mSpinnerDateBegin;
    @BindView(R.id.activity_search_spinner_date_end) Spinner mSpinnerDateEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Get & serialise all views
        ButterKnife.bind(this);

        // Configuring Toolbar
        this.configureToolbar();
    }

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
