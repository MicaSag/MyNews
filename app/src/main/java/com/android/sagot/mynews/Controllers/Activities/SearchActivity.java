package com.android.sagot.mynews.Controllers.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.android.sagot.mynews.Models.Criteria;
import com.android.sagot.mynews.Models.Model;
import com.android.sagot.mynews.Models.NYTimesNews;
import com.android.sagot.mynews.Models.NYTimesStreams.ArticleSearch.NYTimesArticleSearch;
import com.android.sagot.mynews.R;
import com.android.sagot.mynews.Utils.NYTimesNewsList;
import com.android.sagot.mynews.Utils.NYTimesRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    @BindView(R.id.activity_search_progress_bar) ProgressBar mProgressBar;
    // Of the ToolBar
    @BindView(R.id.toolbar) Toolbar mToolbar;

    // Declarations for management of the dates Fields with a DatePickerDialog
    private DatePickerDialog mBeginDatePickerDialog;
    private DatePickerDialog mEndDatePickerDialog;
    private SimpleDateFormat displayDateFormatter;
    private Calendar newCalendar;


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
                getModel().getSearchCriteria().setBeginDate(newDate.getTime());
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
                getModel().getSearchCriteria().setEndDate(newDate.getTime());
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
        // Deactivate the button of search the time to question the API of the NYTimes
        mButton.setEnabled(false);
        // Check if the required search criteria are filled
        if ( validateCriteria() ) {
            // Display ProgressBar
            progressBar.setVisibility(View.VISIBLE);   
            // CALL BASE METHOD : HTTP (RxJAVA) : Execute the request of research on the API of the NYTimes
            executeHttpRequestWithRetrofit();
        }
        // Revive the button of search when the interrogation of the API of the NYTimes is ended
        mButton.setEnabled(true);
    }

    // -------------------------
    // DECLARATION BASE METHODS
    // -------------------------
    // BASE METHOD Implementation
    // Analyze the answer of HttpRequestWithRetrofit
    // CALLED BY BASE METHOD 'executeHttpRequestWithRetrofit()'
    @Override
    protected void responseHttpRequestAnalyze(NYTimesArticleSearch articleSearch){
        Log.d(TAG, "responseHTTPRequestAnalyze: ");
        // Hidden ProgressBar
        progressBar.setVisibility(View.GONE);
        if (articleSearch.getResponse().getDocs().size() != 0) {
            // Create List of Articles search in the Model
            createListArticleSearch(articleSearch);
            // Create Intent and add it some data
            Intent intentResultSearchActivity =
                    new Intent(SearchActivity.this, ResultSearchActivity.class);
            // Call ResultSearchActivity
            startActivity(intentResultSearchActivity);
        } else {
            Snackbar.make(findViewById(getCoordinatorLayout()),
                    "No article found for these criteria of searches",
                    Snackbar.LENGTH_LONG)
                    .show();
        }
    }
    
    // Create list of Article search and save it in the Model
    protected void createListArticleSearch(NYTimesArticleSearch articleSearch) {

        // Create list of articles
        List<NYTimesNews> listNYTimesNews = new ArrayList<>();
        NYTimesNewsList.createListArticleSearch(listNYTimesNews, articleSearch);
        // Save the News in the Model
        Model.getInstance().setListSearchNews(listNYTimesNews);
    }

    // BASE METHOD Implementation
    // Formatting Request for Stream " NYTimesStreams.streamFetchArticleSearch "
    // CALLED BY BASE METHOD 'executeHttpRequestWithRetrofit(...)'
    @Override
    protected Map<String, String> formattingRequest() {

        // Create a new request and put criteria
        NYTimesRequest request = new NYTimesRequest();
        request.createQuery(Model.getInstance().getSavedModel().getSearchCriteria());
        request.addDateCriteriaToQuery(Model.getInstance().getSavedModel().getSearchCriteria()
                .getBeginDate(), "BeginDate");
        request.addDateCriteriaToQuery(Model.getInstance().getSavedModel().getSearchCriteria()
                .getEndDate(), "EndDate");
        // Display request
        request.displayQuery();

        return request.getQuery();
    }

    // BASE METHOD Implementation
    // Get the activity layout
    // CALLED BY BASE METHOD 'onCreate(...)'
    @Override
    protected int getActivityLayout() {
        return R.layout.activity_search;
    }

    // BASE METHOD Implementation
    // Get the coordinator layout
    // CALLED BY BASE METHOD 'validateCriteria()' and 'updateUIWhenErrorHTTPRequest()'
    @Override
    protected int getCoordinatorLayout() {
        return R.id.activity_search_coordinatorLayout;
    }

    // BASE METHOD Implementation
    // Get the search criteria List of the Model
    // CALLED BY BASE METHOD 'onCreate(...)'
    @Override
    protected Criteria getCriteria() {
        return (Criteria)getModel().getSearchCriteria();
    }

    // ----------------------
    // OVERRIDE BASE METHODS
    // ----------------------
    // OVERRIDE BASE METHOD : UpdateUI(Criteria criteria)
    // To add the date criteria to the UI
    @Override
    protected void updateUI(Criteria criteria) {
        super.updateUI(criteria);

        // Management of the date Fields
        this.manageDateFields();

        // Set BeginDate if present in Model
        if (getModel().getSearchCriteria().getBeginDate() != null)
            mBeginDate.setText(displayDateFormatter.format(getModel().getSearchCriteria().getBeginDate()));
        // Set EndDate if present in Model
        if (getModel().getSearchCriteria().getEndDate() != null)
            mEndDate.setText(displayDateFormatter.format(getModel().getSearchCriteria().getEndDate()));

        // For Debug
        displayCriteria();
    }

    // OVERRIDE BASE METHOD : ConfigurationToolBar()
    // To change ToolBar color
    @Override
    protected void configureToolBar(){
        super.configureToolBar();
        // Change Color of the Toolbar
        mToolbar.setBackgroundColor(getResources().getColor(R.color.searchPrimary));
    }
    
    // OVERRIDE BASE METHOD : updateUIWhenErrorHTTPRequest()
    // To Hide the ProgressBar
    protected void updateUIWhenErrorHTTPRequest(){
        super.updateUIWhenErrorHTTPRequest();
        // Hidden ProgressBar
        progressBar.setVisibility(View.GONE);
    }

    // OVERRIDE BASE METHOD : displayCriteria()
    // To add the date criteria to the display debug
    @Override
    protected void displayCriteria(){
        super.displayCriteria();
        Log.d(TAG, "displaySearchCriteria: Begin Date          = "+getModel().getSearchCriteria().getBeginDate());
        Log.d(TAG, "displaySearchCriteria: End Date            = "+getModel().getSearchCriteria().getEndDate());
    }
}
