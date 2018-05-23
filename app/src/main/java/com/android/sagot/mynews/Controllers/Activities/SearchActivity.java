package com.android.sagot.mynews.Controllers.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import com.android.sagot.mynews.Models.Model;
import com.android.sagot.mynews.Models.SearchCriteria;
import com.android.sagot.mynews.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;

public class SearchActivity extends AppCompatActivity {

    // For debugging Mode
    private static final String TAG = SearchActivity.class.getSimpleName();
    
    // Adding @BindView in order to indicate to ButterKnife to get & serialise it
    // Of the SearchActivity
    @BindView(R.id.activity_search_query_text) EditText mEditQuery;
    @BindView(R.id.activity_search_begin_date) EditText mBeginDate;
    @BindView(R.id.activity_search_end_date) EditText mEndDate;
    @BindView(R.id.activity_search_button) Button mButton;
    // Of the options
    @BindView(R.id.checkbox_arts) CheckBox mCheckBoxArts;
    @BindView(R.id.checkbox_business) CheckBox mCheckBoxBusiness;
    @BindView(R.id.checkbox_entrepreneurs) CheckBox mCheckBoxEntrepreneurs;
    @BindView(R.id.checkbox_politics) CheckBox mCheckBoxPolitics;
    @BindView(R.id.checkbox_sports) CheckBox mCheckBoxSports;
    @BindView(R.id.checkbox_travel) CheckBox mCheckBoxTravel;
    // Of the ToolBar
    @BindView(R.id.toolbar) Toolbar mToolbar;

    // Object containing the criteria of search
    @State SearchCriteria mSearchCriteria;

    // Declarations for management of the dates Fields with a DatePickerDialog
    private DatePickerDialog mBeginDatePickerDialog;
    private DatePickerDialog mEndDatePickerDialog;
    private SimpleDateFormat displayDateFormatter;
    private SimpleDateFormat criteriaDateFormatter;
    private Calendar newCalendar;

    // Create the key Mood
    public static final String BUNDLE_SEARCH_CRITERIA = "BUNDLE_SEARCH_CRITERIA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Get & serialise all views
        ButterKnife.bind(this);

        // Update searchCriteria with the Model
        mSearchCriteria = Model.getInstance().getPreferences().getSearchCriteria();
        displaySearchCriteria();

        // Update Search UI with Search Criteria
        this.updateUI();

        // Restore all @State annotation variables in Bundle
        // Useful in the case of the rotation of the device
        if (savedInstanceState != null) Icepick.restoreInstanceState(this, savedInstanceState);

        // Configuring Toolbar
        this.configureToolbar();

        // Management of the date Fields
        this.manageDateFields();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        super.onSaveInstanceState(outState);
        // Save all @State annotation variables in Bundle
        // Useful in the case of the rotation of the device
        Icepick.saveInstanceState(this, outState);
    }

    // --------------
    //      UI
    // --------------
    private void updateUI(){
        mEditQuery.setText(mSearchCriteria.getSearchQueryTerm());
        mBeginDate.setText(mSearchCriteria.getBeginDate());
        mEndDate.setText(mSearchCriteria.getEndDate());
        mCheckBoxArts.setChecked(mSearchCriteria.isArts());
        mCheckBoxBusiness.setChecked(mSearchCriteria.isBusiness());
        mCheckBoxEntrepreneurs.setChecked(mSearchCriteria.isEntrepreneurs());
        mCheckBoxPolitics.setChecked(mSearchCriteria.isPolitics());
        mCheckBoxSports.setChecked(mSearchCriteria.isSports());
        mCheckBoxTravel.setChecked(mSearchCriteria.isTravel());
    }

    // --------------
    //    TOOLBAR
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

    // -------------------
    // MANAGE DATE FIELDS
    // -------------------

    private void manageDateFields() {

        newCalendar = Calendar.getInstance();
        displayDateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        criteriaDateFormatter = new SimpleDateFormat("yyyyMMdd", Locale.FRANCE);
        setBeginDateField();
        setEndDateField();
    }

    // Manage BeginDate Field
    private void setBeginDateField() {

         // Set the default date of the day
         //mBeginDate.setText(dateFormatter.format(newCalendar.getTime()));

        // Create a DatePickerDialog and manage it
         mBeginDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                // Display date selected
                mBeginDate.setText(displayDateFormatter.format(newDate.getTime()));

                // Save date selected
                mSearchCriteria.setBeginDate(criteriaDateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    // Manage EndDate Field
    private void setEndDateField() {

        // Set the default date of the day
        //mEndDate.setText(dateFormatter.format(newCalendar.getTime()));

        // Create a DatePickerDialog and manage it
        mEndDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                // Display date selected
                mEndDate.setText(displayDateFormatter.format(newDate.getTime()));

                // Save date selected
                mSearchCriteria.setEndDate(criteriaDateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }
    // --------------------
    // ACTIONS DATE FIELDS
    // --------------------

    @OnClick(R.id.activity_search_begin_date)
    public void onClickBeginDate(View v) {
        mBeginDatePickerDialog.show();
    }

    @OnClick(R.id.activity_search_end_date)
    public void onClickEndDate(View v) {
        mEndDatePickerDialog.show();
    }

    // ------------------
    // ACTIONS CHECKBOX
    // ------------------

    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.checkbox_arts)
    public void checkBoxArts(View view) {
        mSearchCriteria.setArts(mCheckBoxArts.isChecked());
    }
    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.checkbox_business)
    public void checkBoxBusiness(View view) {
        mSearchCriteria.setBusiness(mCheckBoxBusiness.isChecked());
    }

    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.checkbox_entrepreneurs)
    public void checkBoxEntrepreneurs(View view) {
        mSearchCriteria.setEntrepreneurs(mCheckBoxEntrepreneurs.isChecked());
    }

    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.checkbox_politics)
    public void checkBoxPolitics(View view) {
        mSearchCriteria.setPolitics(mCheckBoxPolitics.isChecked());
    }

    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.checkbox_sports)
    public void checkBoxSports(View view) {
        mSearchCriteria.setSports(mCheckBoxSports.isChecked());
    }

    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.checkbox_travel)
    public void checkBoxTravel(View view) {
        mSearchCriteria.setTravel(mCheckBoxTravel.isChecked());
    }

    // ----------------------
    // ACTIONS SEARCH BUTTON
    // ----------------------

    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.activity_search_button)
    public void submit(View view) {
        Log.d(TAG, "submit: ");

        // Save Search query term
        mSearchCriteria.setSearchQueryTerm(mEditQuery.getText().toString());
        this.displaySearchCriteria();

        // Save the searchCriteria in the Model of the application
        this.saveSearchCriteriaPreferences(mSearchCriteria);

        // Create Intent and add it some data
        Intent intentResultSearchActivity = new Intent(SearchActivity.this, ResultSearchActivity.class);
                final Gson gson = new GsonBuilder()
                .serializeNulls()
                .disableHtmlEscaping()
                .create();
        String json = gson.toJson(mSearchCriteria);
        intentResultSearchActivity.putExtra(BUNDLE_SEARCH_CRITERIA, json);

        // Call ResultSearchActivity
        startActivity(intentResultSearchActivity);
    }
    // -----------------------------
    //  SAVE CRITERIA IN THE MODEL
    // -----------------------------
    // Method for save the searchCriteria in Model Preferences of the application
    private void saveSearchCriteriaPreferences(SearchCriteria searchCriteria) {
        // Add the searchCriteria in Preferences
        Model.getInstance().getPreferences().setSearchCriteria(searchCriteria);
    }

    private void displaySearchCriteria(){
        Log.d(TAG, "displaySearchCriteria: Query               = "+mSearchCriteria.getSearchQueryTerm());
        Log.d(TAG, "displaySearchCriteria: checkArts           = "+mSearchCriteria.isArts());
        Log.d(TAG, "displaySearchCriteria: checkBusiness       = "+mSearchCriteria.isBusiness());
        Log.d(TAG, "displaySearchCriteria: checkEntrepreneurs  = "+mSearchCriteria.isEntrepreneurs());
        Log.d(TAG, "displaySearchCriteria: checkPolitics       = "+mSearchCriteria.isPolitics());
        Log.d(TAG, "displaySearchCriteria: checkSports         = "+mSearchCriteria.isSports());
        Log.d(TAG, "displaySearchCriteria: checkTravels        = "+mSearchCriteria.isTravel());
        Log.d(TAG, "displaySearchCriteria: Begin Date          = "+mSearchCriteria.getBeginDate());
        Log.d(TAG, "displaySearchCriteria: End Date            = "+mSearchCriteria.getEndDate());
    }
}
