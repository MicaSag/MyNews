package com.android.sagot.mynews.Controllers.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.android.sagot.mynews.Models.Criteria;
import com.android.sagot.mynews.Models.Model;
import com.android.sagot.mynews.Models.SearchCriteria;
import com.android.sagot.mynews.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseCriteriaActivity {

    // For debugging Mode
    private static final String TAG = SearchActivity.class.getSimpleName();
    
    // Adding @BindView in order to indicate to ButterKnife to get & serialise it
    // Of the SearchActivity
    @BindView(R.id.activity_search_begin_date) EditText mBeginDate;
    @BindView(R.id.activity_search_end_date) EditText mEndDate;
    @BindView(R.id.activity_search_button) Button mButton;
    // Of the ToolBar
    @BindView(R.id.toolbar) Toolbar mToolbar;

    // Declarations for management of the dates Fields with a DatePickerDialog
    private DatePickerDialog mBeginDatePickerDialog;
    private DatePickerDialog mEndDatePickerDialog;
    private SimpleDateFormat displayDateFormatter;
    private Calendar newCalendar;

    // Search Criteria
    SearchCriteria mSearchCriteria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Get back in the model the criteria of search
        mSearchCriteria = Model.getInstance().getDataModel().getSearchCriteria();

        // Put the inherited variables
        setIdLayoutActivity(R.layout.activity_search);
        setCriteria(mSearchCriteria);

        // Management of the date Fields
        this.manageDateFields();

        super.onCreate(savedInstanceState);
    }

    // --------------
    //   UPDATE UI
    // --------------
    @Override
    protected void updateUI(Criteria criteria) {
        super.updateUI(criteria);

        // Set BeginDate if present in Model
        if (mSearchCriteria.getBeginDate() != null)
            mBeginDate.setText(displayDateFormatter.format(mSearchCriteria.getBeginDate()));
        // Set EndDate if present in Model
        if (mSearchCriteria.getEndDate() != null)
            mEndDate.setText(displayDateFormatter.format(mSearchCriteria.getEndDate()));
    }

    // -----------------------
    // CONFIGURATION TOOLBAR
    // -----------------------
    @Override
    protected void configureToolBar(){
        super.configureToolBar();

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
    // Click on BeginDate Field
    @OnClick(R.id.activity_search_begin_date)
    public void onClickBeginDate(View v) {
        mBeginDatePickerDialog.show();
    }

    // Click on EndDate Field
    @OnClick(R.id.activity_search_end_date)
    public void onClickEndDate(View v) {
        mEndDatePickerDialog.show();
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
    protected void displayCriteria(){
        super.displayCriteria();
        Log.d(TAG, "displaySearchCriteria: Begin Date          = "+Model.getInstance().getDataModel().getSearchCriteria().getBeginDate());
        Log.d(TAG, "displaySearchCriteria: End Date            = "+Model.getInstance().getDataModel().getSearchCriteria().getEndDate());
    }
}
