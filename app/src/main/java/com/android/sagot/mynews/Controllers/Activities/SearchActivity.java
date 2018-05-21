package com.android.sagot.mynews.Controllers.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.sagot.mynews.Controllers.Fragments.ResultSearchFragment;
import com.android.sagot.mynews.Models.SearchCriteria;
import com.android.sagot.mynews.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.sagot.mynews.Utils.UIUtilities.changeStatusBarColor;

public class SearchActivity extends AppCompatActivity {

    // For debugging Mode
    private static final String TAG = SearchActivity.class.getSimpleName();
    
    // Adding @BindView in order to indicate to ButterKnife to get & serialise it
    @BindView(R.id.activity_search_text) EditText mEditText;
    @BindView(R.id.activity_search_spinner_date_begin) Spinner mSpinnerDateBegin;
    @BindView(R.id.activity_search_spinner_date_end) Spinner mSpinnerDateEnd;
    @BindView(R.id.checkbox_arts) CheckBox mCheckBoxArts;
    @BindView(R.id.checkbox_business) CheckBox mCheckBoxBusiness;
    @BindView(R.id.checkbox_entrepreneurs) CheckBox mCheckBoxEntrepreneurs;
    @BindView(R.id.checkbox_politics) CheckBox mCheckBoxPolitics;
    @BindView(R.id.checkbox_sports) CheckBox mCheckBoxSports;
    @BindView(R.id.checkbox_travel) CheckBox mCheckBoxTravel;
    @BindView(R.id.activity_search_button) Button mButton;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    // Declare search fragment
    private ResultSearchFragment mSearchFragment;

    // Object containing the criteria of search
    SearchCriteria mSearchCriteria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Get & serialise all views
        ButterKnife.bind(this);

        // Create searchCriteria Object
        mSearchCriteria = new SearchCriteria();

        // Configuring Toolbar
        this.configureToolbar();
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
        // Change Color of the Toolbar
        mToolbar.setBackgroundColor(getResources().getColor(R.color.searchPrimary));
    }

    // ---------------------
    // ACTIONS
    // ---------------------

    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.activity_search_button)
    public void submit(View view) {
        Log.d(TAG, "submit: ");

        Intent intentResultSearchActivity = new Intent(SearchActivity.this, ResultSearchActivity.class);
        startActivity(intentResultSearchActivity);

    }

    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.checkbox_arts)
    public void checkBoxArts(View view) {
        mSearchCriteria.setArts(mCheckBoxArts.isChecked()?true:false);
    }
    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.checkbox_business)
    public void checkBoxBusiness(View view) {
        mSearchCriteria.setBusiness(mCheckBoxBusiness.isChecked()?true:false);
    }

    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.checkbox_entrepreneurs)
    public void checkBoxEntrepreneurs(View view) {
        mSearchCriteria.setEntrepreneurs(mCheckBoxEntrepreneurs.isChecked()?true:false);
    }

    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.checkbox_politics)
    public void checkBoxPolitics(View view) {
        mSearchCriteria.setPolitics(mCheckBoxPolitics.isChecked()?true:false);
    }

    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.checkbox_sports)
    public void checkBoxSports(View view) {
        mSearchCriteria.setSports(mCheckBoxSports.isChecked()?true:false);
    }

    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.checkbox_travel)
    public void checkBoxTravel(View view) {
        mSearchCriteria.setTravel(mCheckBoxTravel.isChecked()?true:false);
    }
}
