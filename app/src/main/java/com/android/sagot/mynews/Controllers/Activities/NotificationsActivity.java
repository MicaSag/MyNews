package com.android.sagot.mynews.Controllers.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.android.sagot.mynews.Models.Model;
import com.android.sagot.mynews.Models.NotificationsCriteria;
import com.android.sagot.mynews.Models.SearchCriteria;
import com.android.sagot.mynews.R;
import com.android.sagot.mynews.Utils.NotificationsAlarmReceiver;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class NotificationsActivity extends AppCompatActivity{

    // FOR TRACES
    private static final String TAG = NotificationsActivity.class.getSimpleName();

    // Adding @BindView in order to indicate to ButterKnife to get & serialise it
    // Of the ToolBar
    @BindView(R.id.toolbar) Toolbar mToolbar;
    // Of the Keys words
    @BindView(R.id.activity_notifications_keys_words_text) EditText mEditKeysWords;
    // Of the category
    @BindView(R.id.checkbox_arts) CheckBox mCheckBoxArts;
    @BindView(R.id.checkbox_business) CheckBox mCheckBoxBusiness;
    @BindView(R.id.checkbox_entrepreneurs) CheckBox mCheckBoxEntrepreneurs;
    @BindView(R.id.checkbox_politics) CheckBox mCheckBoxPolitics;
    @BindView(R.id.checkbox_sports) CheckBox mCheckBoxSports;
    @BindView(R.id.checkbox_travel) CheckBox mCheckBoxTravel;
    // Of the Switch
    @BindView(R.id.activity_notifications_switch) Switch mSwitch;

    // Creating an intent to execute our broadcast
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        // Get & serialise all views
        ButterKnife.bind(this);

        // Update Notifications UI with NotificationsCriteria of the Model
        this.displayNotificationsCriteria();
        this.updateUI();

        // Toolbar Configuration
        this.configureToolBar();
    }

    // --------------
    //      UI
    // --------------
    private void updateUI(){
        NotificationsCriteria notificationsCriteria = Model.getInstance().getDataModel().getNotificationsCriteria();
        mEditKeysWords.setText(notificationsCriteria.getKeysWords());
        mCheckBoxArts.setChecked(notificationsCriteria.isArts());
        mCheckBoxBusiness.setChecked(notificationsCriteria.isBusiness());
        mCheckBoxEntrepreneurs.setChecked(notificationsCriteria.isEntrepreneurs());
        mCheckBoxPolitics.setChecked(notificationsCriteria.isPolitics());
        mCheckBoxSports.setChecked(notificationsCriteria.isSports());
        mCheckBoxTravel.setChecked(notificationsCriteria.isTravel());
        mSwitch.setChecked(notificationsCriteria.isNotificationStatus());
    }

    // -----------------------
    // CONFIGURATION TOOLBAR
    // -----------------------
    private void configureToolBar() {
        Log.d(TAG, "configureToolBar: ");
        // Get the toolbar view inside the activity layout

        //Set the toolbar
        setSupportActionBar(mToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        // Change Color of the Toolbar
        mToolbar.setBackgroundColor(getResources().getColor(R.color.notificationsPrimary));
    }

    // ------------------
    // ACTIONS CHECKBOX
    // ------------------
    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.checkbox_arts)
    public void checkBoxArts(View view) {
        Model.getInstance().getDataModel().getNotificationsCriteria()
                .setArts(mCheckBoxArts.isChecked());
    }
    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.checkbox_business)
    public void checkBoxBusiness(View view) {
        Model.getInstance().getDataModel().getNotificationsCriteria()
                .setBusiness(mCheckBoxBusiness.isChecked());
    }

    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.checkbox_entrepreneurs)
    public void checkBoxEntrepreneurs(View view) {
        Model.getInstance().getDataModel().getNotificationsCriteria()
                .setEntrepreneurs(mCheckBoxEntrepreneurs.isChecked());
    }

    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.checkbox_politics)
    public void checkBoxPolitics(View view) {
        Model.getInstance().getDataModel().getNotificationsCriteria()
                .setPolitics(mCheckBoxPolitics.isChecked());
    }

    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.checkbox_sports)
    public void checkBoxSports(View view) {
        Model.getInstance().getDataModel().getNotificationsCriteria()
                .setSports(mCheckBoxSports.isChecked());
    }

    // click on Search Button and call ResultSearchActivity
    @OnClick(R.id.checkbox_travel)
    public void checkBoxTravel(View view) {
        Model.getInstance().getDataModel().getNotificationsCriteria()
                .setTravel(mCheckBoxTravel.isChecked());
    }

    // -----------------------------
    // ACTIONS EDIT_TEXT KEYS_WORDS
    // -----------------------------
    @OnTextChanged(R.id.activity_notifications_keys_words_text)
    public void onTextChanged(Editable text){
        Model.getInstance().getDataModel().getNotificationsCriteria().setKeysWords(text.toString());
    }

    // ---------------
    // ACTIONS SWITCH
    // ---------------
    @OnCheckedChanged(R.id.activity_notifications_switch)
    public void OnCheckedChanged(CompoundButton cb, boolean isChecked){
        Log.d(TAG, "OnCheckedChanged: isChecked = "+isChecked);
        Model.getInstance().getDataModel().getNotificationsCriteria().setNotificationStatus(isChecked);

        // If checked, Start Alarm
        if (isChecked) this.startAlarm();
        // If not checked, Stop alarm
        if (!isChecked) this.stopAlarm();
    }

    // ------------------------------
    // SCHEDULE TASK  : AlarmManager
    // ------------------------------
    // Start Alarm
    private void startAlarm() {
        Log.d(TAG, "startAlarm: ");
        this.configureAlarmManager();
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,0,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
        Toast.makeText(this, "Alarm set !", Toast.LENGTH_SHORT).show();
    }

    // Start Alarm
    private void stopAlarm() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm Canceled !", Toast.LENGTH_SHORT).show();
    }

    // ----------------------------
    // CONFIGURATION ALARM MANAGER
    // ----------------------------
    private void configureAlarmManager(){
        Log.d(TAG, "configureAlarmManager: ");

        Intent alarmIntent = new Intent(NotificationsActivity.this,
                NotificationsAlarmReceiver.class);

        pendingIntent = PendingIntent.getBroadcast(NotificationsActivity.this, 0,
                alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
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
        this.displayNotificationsCriteria();
        super.onPause();
    }

    private void displayNotificationsCriteria(){
        Log.d(TAG, "displaySearchCriteria: Query               = "+Model.getInstance().getDataModel().getNotificationsCriteria().getKeysWords());
        Log.d(TAG, "displaySearchCriteria: checkArts           = "+Model.getInstance().getDataModel().getNotificationsCriteria().isArts());
        Log.d(TAG, "displaySearchCriteria: checkBusiness       = "+Model.getInstance().getDataModel().getNotificationsCriteria().isBusiness());
        Log.d(TAG, "displaySearchCriteria: checkEntrepreneurs  = "+Model.getInstance().getDataModel().getNotificationsCriteria().isEntrepreneurs());
        Log.d(TAG, "displaySearchCriteria: checkPolitics       = "+Model.getInstance().getDataModel().getNotificationsCriteria().isPolitics());
        Log.d(TAG, "displaySearchCriteria: checkSports         = "+Model.getInstance().getDataModel().getNotificationsCriteria().isSports());
        Log.d(TAG, "displaySearchCriteria: checkTravels        = "+Model.getInstance().getDataModel().getNotificationsCriteria().isTravel());
        Log.d(TAG, "displaySearchCriteria: switch              = "+Model.getInstance().getDataModel().getNotificationsCriteria().isNotificationStatus());
    }
}
