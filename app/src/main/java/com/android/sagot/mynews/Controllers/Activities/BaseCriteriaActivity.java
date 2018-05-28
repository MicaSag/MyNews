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
 * Initialization to be made by inheritance ( search or notifications activity ):
 *      Criteria mCriteria    : Criteria of the Activity inheriting
 *      int mIdLayoutActivity : Id of the view of the Activity layout inheriting
 */
public abstract class BaseCriteriaActivity extends AppCompatActivity {

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

    // Initialization to be made by inheritance
    private Criteria mCriteria;     // Criteria ( search or notification )
    private int mIdLayoutActivity;  // Id of the view of the Activity layout inheriting

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mIdLayoutActivity);

        // Get & serialise all views
        ButterKnife.bind(this);

        // Update UI with Criteria
        this.updateUI(mCriteria);

        // Configuring Toolbar
        this.configureToolBar();
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
        mCriteria.setArts(mCheckBoxArts.isChecked());
    }
    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.checkbox_business)
    protected void checkBoxBusiness(View view) {
        mCriteria.setBusiness(mCheckBoxBusiness.isChecked());
    }

    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.checkbox_entrepreneurs)
    protected void checkBoxEntrepreneurs(View view) {
        mCriteria.setEntrepreneurs(mCheckBoxEntrepreneurs.isChecked());
    }

    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.checkbox_politics)
    protected void checkBoxPolitics(View view) {
        mCriteria.setPolitics(mCheckBoxPolitics.isChecked());
    }

    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.checkbox_sports)
    protected void checkBoxSports(View view) {
        mCriteria.setSports(mCheckBoxSports.isChecked());
    }

    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.checkbox_travel)
    protected void checkBoxTravel(View view) {
        mCriteria.setTravel(mCheckBoxTravel.isChecked());
    }

    // -----------------------------
    // ACTIONS EDIT_TEXT KEYS_WORDS
    // -----------------------------
    @OnTextChanged(R.id.keys_words_edit)
    protected void afterTextChanged(Editable text){
        mCriteria.setKeysWords(text.toString());
    }

    // Set criteria
    public void setCriteria(Criteria criteria) {
        mCriteria = criteria;
    }

    // Set Layout of the Activity
    public void setIdLayoutActivity(int idLayoutActivity) {
        mIdLayoutActivity = idLayoutActivity;
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
        super.onPause();
    }

    protected void displayCriteria(){
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
}

