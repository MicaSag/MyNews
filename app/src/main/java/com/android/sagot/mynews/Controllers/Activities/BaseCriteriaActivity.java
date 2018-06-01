package com.android.sagot.mynews.Controllers.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.sagot.mynews.Models.Criteria;
import com.android.sagot.mynews.Models.DataModel;
import com.android.sagot.mynews.Models.Model;
import com.android.sagot.mynews.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/*
 * ABSTRACT Class BaseCriteriaActivity
 *
 */
public abstract class BaseCriteriaActivity extends AppCompatActivity {
    
    // Force developer implement those methods
    protected abstract int getActivityLayout(); // Layout of the Child Activity
    protected abstract Criteria getCriteria();  // Criteria of the Child Activity

    // For debugging Mode
    private static final String TAG = BaseCriteriaActivity.class.getSimpleName();

    // Adding @BindView in order to indicate to ButterKnife to get & serialise it
    // Of the keys Words
    @BindView(R.id.keys_words_edit) EditText mEditKeysWords;
    // Of the options
    @BindView(R.id.checkbox_arts) CheckBox mCheckBoxArts;
    @BindView(R.id.checkbox_business) CheckBox mCheckBoxBusiness;
    @BindView(R.id.checkbox_entrepreneurs) CheckBox mCheckBoxEntrepreneurs;
    @BindView(R.id.checkbox_politics) CheckBox mCheckBoxPolitics;
    @BindView(R.id.checkbox_sports) CheckBox mCheckBoxSports;
    @BindView(R.id.checkbox_travel) CheckBox mCheckBoxTravel;
    // Of the ToolBar
    @BindView(R.id.toolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayout());

        // Get Model of the App
        getModel();
        
        // Get & serialise all views
        ButterKnife.bind(this);

        // Update UI with Criteria
        this.updateUI(getCriteria());

        // Configuring Toolbar
        this.configureToolBar();
    }
    
    // --------------
    //    ( IN )
    // --------------
     // Model of the App
    protected DataModel getModel() {
        return Model.getInstance().getDataModel();
    }

    // --------------
    //      UI
    // --------------
    protected void updateUI(Criteria criteria){
        mEditKeysWords.setText(criteria.getKeysWords());
        mCheckBoxArts.setChecked(criteria.isArts());
        mCheckBoxBusiness.setChecked(criteria.isBusiness());
        mCheckBoxEntrepreneurs.setChecked(criteria.isEntrepreneurs());
        mCheckBoxPolitics.setChecked(criteria.isPolitics());
        mCheckBoxSports.setChecked(criteria.isSports());
        mCheckBoxTravel.setChecked(criteria.isTravel());
    }

    // --------------
    //    TOOLBAR
    // --------------
    protected void configureToolBar(){
        //Set the toolbar
        setSupportActionBar(mToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
    }

    // ------------------
    // ACTIONS CHECKBOX
    // ------------------
    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.checkbox_arts)
    protected void checkBoxArts(View view) {
        getCriteria().setArts(mCheckBoxArts.isChecked());
    }
    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.checkbox_business)
    protected void checkBoxBusiness(View view) {
        getCriteria().setBusiness(mCheckBoxBusiness.isChecked());
    }

    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.checkbox_entrepreneurs)
    protected void checkBoxEntrepreneurs(View view) {
        getCriteria().setEntrepreneurs(mCheckBoxEntrepreneurs.isChecked());
    }

    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.checkbox_politics)
    protected void checkBoxPolitics(View view) {
        getCriteria().setPolitics(mCheckBoxPolitics.isChecked());
    }

    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.checkbox_sports)
    protected void checkBoxSports(View view) {
        getCriteria().setSports(mCheckBoxSports.isChecked());
    }

    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.checkbox_travel)
    protected void checkBoxTravel(View view) {
        getCriteria().setTravel(mCheckBoxTravel.isChecked());
    }

    // -----------------------------
    // ACTIONS EDIT_TEXT KEYS_WORDS
    // -----------------------------
    @OnTextChanged(R.id.keys_words_edit)
    protected void afterTextChanged(Editable text){
        getCriteria().setKeysWords(text.toString());
    }
    
    // -------------------
    // HTTP (RxJAVA)
    // -------------------
    /**
     *  Formatting Request for Stream " NYTimesStreams.streamFetchArticleSearch "
     */
     protected  Map<String, String> formattingRequest() {

         // Create a new request and put criteria
         NYTimesRequest request = new NYTimesRequest();
         request.createNYTimesRequest(Model.getInstance().getDataModel().getSearchCriteria());
         request.addDateCriteriaToRequest(Model.getInstance().getDataModel().getSearchCriteria()
                                .getBeginDate(), "BeginDate");
         request.addDateCriteriaToRequest(Model.getInstance().getDataModel().getSearchCriteria()
                                .getEndDate(), "EndDate");
         // Display request
         request.displayRequest();

         return request.getFilters();
     }
    /**
     *  Execute Stream " NYTimesStreams.streamFetchArticleSearch "
     */
    @Override
    protected void executeHttpRequestWithRetrofit() {

        // Execute the stream subscribing to Observable defined inside NYTimesStreams
        mDisposable = NYTimesStreams.streamFetchArticleSearch(api_key,  this.formattingRequest())
                .subscribeWith(new DisposableObserver<NYTimesArticleSearch>() {
            @Override
            public void onNext(NYTimesArticleSearch articleSearch) {
                Log.d(TAG, "onNext: ");
            }

            @Override
            public void onError(Throwable e) {
                // Display a toast message
                updateUIWhenErrorHTTPRequest();
                Log.d(TAG, "onError: ");
            }
            @Override
            public void onComplete() { Log.d(TAG,"On Complete !!"); }
        });
    }
    
    // -----------
    //  ( OUT )    
    // -----------
    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: ");
        // SAVE MODEL IN THE SHARED_PREFERENCES
        // Create à SHARED_PREF_MODEL String with a Gson Object
        final Gson gson = new GsonBuilder()
                .serializeNulls()
                .disableHtmlEscaping()
                .create();
        String json = gson.toJson(getModel());

        // Add the Model in shared Preferences
       Model.getInstance().getSharedPreferences().edit()
                .putString(MainActivity.SHARED_PREF_MODEL, json).apply();
        super.onPause();
    }

    protected void displayCriteria(){
        Log.d(TAG, "displayCriteria: Query               = "+getCriteria().getKeysWords());
        Log.d(TAG, "displayCriteria: checkArts           = "+getCriteria().isArts());
        Log.d(TAG, "displayCriteria: checkBusiness       = "+getCriteria().isBusiness());
        Log.d(TAG, "displayCriteria: checkEntrepreneurs  = "+getCriteria().isEntrepreneurs());
        Log.d(TAG, "displayCriteria: checkPolitics       = "+getCriteria().isPolitics());
        Log.d(TAG, "displayCriteria: checkSports         = "+getCriteria().isSports());
        Log.d(TAG, "displayCriteria: checkTravels        = "+getCriteria().isTravel());
    }
}

