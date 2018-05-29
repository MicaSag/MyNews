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

import com.android.sagot.mynews.Models.Criteria;
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

public class NotificationsActivity extends BaseCriteriaActivity {

    // FOR TRACES
    private static final String TAG = NotificationsActivity.class.getSimpleName();

    // Adding @BindView in order to indicate to ButterKnife to get & serialise it
    // Of the ToolBar
    @BindView(R.id.toolbar) Toolbar mToolbar;
    // Of the Switch
    @BindView(R.id.activity_notifications_switch) Switch mSwitch;

    // Creating an intent to execute our broadcast
    private PendingIntent pendingIntent;

    // Notification Criteria
    NotificationsCriteria mNotificationsCriteria;
    
    // --------------
    // BASE METHODS
    // --------------
    @Override
    protected int getActivityLayout() { 
        return R.layout.activity_notifications; 
    }
    
    @Override
    protected int getCriteria() { 
        return (Criteria)getModel().getNotificationsCriteria(); 
    }

    // --------------
    //      UI
    //---------------
    @Override
    protected void updateUI(Criteria criteria){
        super.updateUI(criteria);

        // Set the state of the present switch in the Model
        mSwitch.setChecked(getModel().getNotificationsCriteria().isNotificationStatus());
    }

    // -----------------------
    // CONFIGURATION TOOLBAR
    // -----------------------
    @Override
    protected void configureToolBar() {
        super.configureToolBar();

        // Change Color of the Toolbar
        mToolbar.setBackgroundColor(getResources().getColor(R.color.notificationsPrimary));
    }

    // ---------------
    // ACTIONS SWITCH
    // ---------------
    @OnCheckedChanged(R.id.activity_notifications_switch)
    public void OnCheckedChanged(CompoundButton cb, boolean isChecked){
        Log.d(TAG, "OnCheckedChanged: isChecked = "+isChecked);

        // If checked and not already checked in the model, Start Alarm
        if (isChecked && !(getModel().getNotificationsCriteria().isNotificationStatus())) this.startAlarm();
        // If not checked and pendingIntent <> null , Stop alarm
        if (!isChecked && pendingIntent != null) this.stopAlarm();

        // Save the state of the switch in the model
        getModel().getNotificationsCriteria().setNotificationStatus(isChecked);
    }

    // ------------------------------
    // SCHEDULE TASK  : AlarmManager
    // ------------------------------
    // Start Alarm
    private void startAlarm() {
        this.configureAlarmManager();
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,0,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
        Toast.makeText(this, "Notifications set !", Toast.LENGTH_SHORT).show();
    }

    // Start Alarm
    private void stopAlarm() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        Toast.makeText(this, "Notifications Canceled !", Toast.LENGTH_SHORT).show();
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
    protected void displayCriteria(){
        super.displayCriteria();

        Log.d(TAG, "displaySearchCriteria: switch              = "+getModel()
              .getNotificationsCriteria().isNotificationStatus());
    }
}
