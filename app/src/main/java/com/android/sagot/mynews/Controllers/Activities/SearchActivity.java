package com.android.sagot.mynews.Controllers.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
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
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import icepick.Icepick;
import icepick.State;

public class SearchActivity extends AppCompatActivity {

    // For debugging Mode
    private static final String TAG = SearchActivity.class.getSimpleName();
    
    // Adding @BindView in order to indicate to ButterKnife to get & serialise it
    // Of the SearchActivity
    @BindView(R.id.activity_search_keys_words_text) EditText mEditKeysWords;
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

    // Declarations for management of the dates Fields with a DatePickerDialog
    private DatePickerDialog mBeginDatePickerDialog;
    private DatePickerDialog mEndDatePickerDialog;
    private SimpleDateFormat displayDateFormatter;
    private Calendar newCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Get & serialise all views
        ButterKnife.bind(this);

        // Management of the date Fields
        this.manageDateFields();

        // Update Search UI with SearchCriteria of the Model
        this.displaySearchCriteria();
        this.updateUI();

        // Configuring Toolbar
        this.configureToolbar();
    }

    // --------------
    //      UI
    // --------------
    private void updateUI(){
        SearchCriteria searchCriteria = Model.getInstance().getDataModel().getSearchCriteria();
        mEditKeysWords.setText(searchCriteria.getKeysWords());
        if (searchCriteria.getBeginDate() != null)
            mBeginDate.setText(displayDateFormatter.format(searchCriteria.getBeginDate()));
        if (searchCriteria.getEndDate() != null)
            mEndDate.setText(displayDateFormatter.format(searchCriteria.getEndDate()));
        mCheckBoxArts.setChecked(searchCriteria.isArts());
        mCheckBoxBusiness.setChecked(searchCriteria.isBusiness());
        mCheckBoxEntrepreneurs.setChecked(searchCriteria.isEntrepreneurs());
        mCheckBoxPolitics.setChecked(searchCriteria.isPolitics());
        mCheckBoxSports.setChecked(searchCriteria.isSports());
        mCheckBoxTravel.setChecked(searchCriteria.isTravel());
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
        displayDateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
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

                // Save date selected in the Model
                Model.getInstance().getDataModel().getSearchCriteria()
                         .setBeginDate(newDate.getTime());
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
                Model.getInstance().getDataModel().getSearchCriteria()
                        .setEndDate(newDate.getTime());
                // .setEndDate(criteriaDateFormatter.format(newDate.getTime()));
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
        Model.getInstance().getDataModel().getSearchCriteria()
                .setArts(mCheckBoxArts.isChecked());
    }
    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.checkbox_business)
    public void checkBoxBusiness(View view) {
        Model.getInstance().getDataModel().getSearchCriteria()
                .setBusiness(mCheckBoxBusiness.isChecked());
    }

    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.checkbox_entrepreneurs)
    public void checkBoxEntrepreneurs(View view) {
        Model.getInstance().getDataModel().getSearchCriteria()
                .setEntrepreneurs(mCheckBoxEntrepreneurs.isChecked());
    }

    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.checkbox_politics)
    public void checkBoxPolitics(View view) {
        Model.getInstance().getDataModel().getSearchCriteria()
                .setPolitics(mCheckBoxPolitics.isChecked());
    }

    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.checkbox_sports)
    public void checkBoxSports(View view) {
        Model.getInstance().getDataModel().getSearchCriteria()
                .setSports(mCheckBoxSports.isChecked());
    }

    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.checkbox_travel)
    public void checkBoxTravel(View view) {
        Model.getInstance().getDataModel().getSearchCriteria()
                .setTravel(mCheckBoxTravel.isChecked());
    }

    // -----------------------------
    // ACTIONS EDIT_TEXT KEYS_WORDS
    // -----------------------------
    @OnTextChanged(R.id.activity_search_keys_words_text)
    public void afterTextChanged(Editable text){
        Model.getInstance().getDataModel().getSearchCriteria().setKeysWords(text.toString());
    }

    // ----------------------
    // ACTIONS SEARCH BUTTON
    // ----------------------

    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.activity_search_button)
    public void submit(View view) {
        Log.d(TAG, "submit: ");

        // Create Intent and add it some data
        Intent intentResultSearchActivity = new Intent(SearchActivity.this, ResultSearchActivity.class);

        // Call ResultSearchActivity
        startActivity(intentResultSearchActivity);
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: ");
        // SAVE MODELS PREFERENCES IN THE SHARED_PREFERENCES
        // Create à SHARED_PREF_MODEL String with a Gson Object
        final Gson gson = new GsonBuilder()
                .serializeNulls()
                .disableHtmlEscaping()
                .create();
        String json = gson.toJson(Model.getInstance().getDataModel());

        // Add the Model in shared Preferences
        Model.getInstance().getSharedPreferences().edit()
                .putString(MainActivity.SHARED_PREF_MODEL, json).apply();
        this.displaySearchCriteria();
        super.onPause();
    }

    private void displaySearchCriteria(){
        Log.d(TAG, "displaySearchCriteria: Query               = "+Model.getInstance().getDataModel().getSearchCriteria().getKeysWords());
        Log.d(TAG, "displaySearchCriteria: checkArts           = "+Model.getInstance().getDataModel().getSearchCriteria().isArts());
        Log.d(TAG, "displaySearchCriteria: checkBusiness       = "+Model.getInstance().getDataModel().getSearchCriteria().isBusiness());
        Log.d(TAG, "displaySearchCriteria: checkEntrepreneurs  = "+Model.getInstance().getDataModel().getSearchCriteria().isEntrepreneurs());
        Log.d(TAG, "displaySearchCriteria: checkPolitics       = "+Model.getInstance().getDataModel().getSearchCriteria().isPolitics());
        Log.d(TAG, "displaySearchCriteria: checkSports         = "+Model.getInstance().getDataModel().getSearchCriteria().isSports());
        Log.d(TAG, "displaySearchCriteria: checkTravels        = "+Model.getInstance().getDataModel().getSearchCriteria().isTravel());
        Log.d(TAG, "displaySearchCriteria: Begin Date          = "+Model.getInstance().getDataModel().getSearchCriteria().getBeginDate());
        Log.d(TAG, "displaySearchCriteria: End Date            = "+Model.getInstance().getDataModel().getSearchCriteria().getEndDate());
    }

    // --------
    //  OUTPUT
    // --------
    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}
