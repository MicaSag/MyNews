package com.android.sagot.mynews.Controllers.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.android.sagot.mynews.Models.Criteria;
import com.android.sagot.mynews.R;
import com.android.sagot.mynews.Utils.NotificationsAlarmReceiver;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnTouch;

import static com.android.sagot.mynews.Utils.UIUtilities.hideKeyboardFrom;

public class NotificationsActivity extends BaseCriteriaActivity {

    // FOR TRACES
    private static final String TAG = NotificationsActivity.class.getSimpleName();

    // Adding @BindView in order to indicate to ButterKnife to get & serialise it
    // Of the Switch
    @BindView(R.id.activity_notifications_coordinatorLayout) CoordinatorLayout mCoordinatorLayout;
    // Of the Switch
    @BindView(R.id.activity_notifications_switch) Switch mSwitch;
    // Of the ToolBar
    @BindView(R.id.toolbar) Toolbar mToolbar;

    // Creating an intent to execute our broadcast
    private PendingIntent mPendingIntent;
    // Creating alarmManager
    private AlarmManager mAlarmManager;

    // -------------------------
    // DECLARATION BASE METHODS
    // -------------------------
    // BASE METHOD Implementation
    // Get the activity layout
    // CALLED BY BASE METHOD 'validateCriteria()' and 'updateUIWhenErrorHTTPRequest()'
    @Override
    protected int getActivityLayout() { 
        return R.layout.activity_notifications; 
    }

    // BASE METHOD Implementation
    // Get the coordinator layout
    // CALLED BY BASE METHOD 'onCreate(...)'
    @Override
    protected int getCoordinatorLayout() {
        return R.id.activity_notifications_coordinatorLayout;
    }

    // BASE METHOD Implementation
    // Get the search criteria List of the Model
    // CALLED BY BASE METHOD 'onCreate(...)'
    @Override
    protected Criteria getCriteria() {
        return (Criteria)getModel().getNotificationsCriteria(); 
    }

    // ----------------------
    // OVERRIDE BASE METHODS
    // ----------------------
    // OVERRIDE BASE METHOD : onCreate(Bundle savedInstanceState)
    // To add the call to configureAlarmManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Configuring The AlarmManager
        this.configureAlarmManager();
    }
    
    // OVERRIDE BASE METHOD : UpdateUI(Criteria criteria)
    // To add the notification status to the UI
    @Override
    protected void updateUI(Criteria criteria){
        super.updateUI(criteria);

        // Set the state of the present switch in the Model
        mSwitch.setChecked(getModel().getNotificationsCriteria().isNotificationStatus());
    }

    // OVERRIDE BASE METHOD : ConfigurationToolBar()
    // To change ToolBar color
    @Override
    protected void configureToolBar() {
        super.configureToolBar();

        // Change Color of the Toolbar
        mToolbar.setBackgroundColor(getResources().getColor(R.color.notificationsPrimary));
    }
       
    // OVERRIDE BASE METHOD : displayCriteria()
    // To add the date criteria to the display debug
    @Override
    protected void displayCriteria(){
        super.displayCriteria();

        Log.d(TAG, "displayCriteria: switch              = "+getModel()
              .getNotificationsCriteria().isNotificationStatus());
    }

    // -------------------
    //   ACTION LAYOUT
    // -------------------
    // Hide the keyboard when the editText loses the focus and coordinator layout obtains it
    @OnTouch(R.id.activity_notifications_coordinatorLayout)
    public boolean onTouchLayout(View v, MotionEvent event){
        hideKeyboardFrom(this);
        return false;
    }

    // ---------------
    // ACTIONS SWITCH
    // ---------------
    // Hide the keyboard when the editText loses the focus and notifications switch obtains it
    @OnTouch(R.id.activity_notifications_switch)
    public boolean onTouchSwitch(View v, MotionEvent event){
        hideKeyboardFrom(this);
        return false;
    }

    @OnCheckedChanged(R.id.activity_notifications_switch)
    public void OnCheckedChanged(CompoundButton cb, boolean isChecked){
        Log.d(TAG, "OnCheckedChanged: isChecked = "+isChecked);
        Log.d(TAG, "OnCheckedChanged: isChecked on Model Before = "
                +getModel().getNotificationsCriteria().isNotificationStatus());

        // If check switch and old state is not checked
        if ( isChecked && !getModel().getNotificationsCriteria().isNotificationStatus()) {
            // Check if the required criteria are filled
            if (validateCriteria() ) {
                // It's Ok, start alarm and Save the state of the switch in the model
                getModel().getNotificationsCriteria().setNotificationStatus(true);
                this.startAlarm();
            }else { // It's KO, Check False for the state of the switch ans save in the model
                cb.setChecked(false);
                getModel().getNotificationsCriteria().setNotificationStatus(false);
            }
        }else { // Not Checked  and old state is checked
            if ( !isChecked && getModel().getNotificationsCriteria().isNotificationStatus()) {
                // Stop Alarm and save the state of the switch in the model
                getModel().getNotificationsCriteria().setNotificationStatus(false);
                this.stopAlarm();
            }
        }
        Log.d(TAG, "OnCheckedChanged: isChecked on Model After = " +
                ""+ getModel().getNotificationsCriteria().isNotificationStatus());
    }

    // ----------------------------
    // CONFIGURATION ALARM MANAGER
    // ----------------------------
    private void configureAlarmManager(){
        Log.d(TAG, "configureAlarmManager: ");
        // Create the Intent to destination of the BroadcastReceiver
        Intent alarmIntent = new Intent(NotificationsActivity.this,
                NotificationsAlarmReceiver.class);
        mPendingIntent = PendingIntent.getBroadcast(NotificationsActivity.this, 0,
                alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    // ------------------------------
    // SCHEDULE TASK  : AlarmManager
    // ------------------------------
    // @Return The hour it milliseconds of release of the first alarm
    private long nextNotification() {
        Log.d(TAG, "nextNotification: ");

        Calendar cal = Calendar.getInstance();
        // If it is after noon then we add one day to the meter of release of the alarm
        //if (cal.get(Calendar.HOUR_OF_DAY) > 12 ) cal.add(Calendar.DATE, 1);
        // The alarm next one will thus be at 12:00 am tomorrow
        cal.set(Calendar.HOUR_OF_DAY, 20);
        cal.set(Calendar.MINUTE, 54);
        cal.set(Calendar.SECOND, 00);
        cal.set(Calendar.MILLISECOND, 0);
        //The hour it milliseconds of release of the first alarm
        return cal.getTimeInMillis();
    }

    // Start Alarm
    private void startAlarm() {
        Log.d(TAG, "startAlarm: ");
        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.setInexactRepeating( AlarmManager.RTC_WAKEUP,     // which will wake up the device when it goes off
                                    nextNotification(),          // First start at 12:00
                                    mAlarmManager.INTERVAL_DAY,  // Will trigger every day
                                    mPendingIntent);
        Snackbar.make(mCoordinatorLayout,"Notifications set !",Snackbar.LENGTH_LONG).show();
    }
    // Stop Alarm
    private void stopAlarm() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(mPendingIntent);
        Snackbar.make(mCoordinatorLayout,"Notifications canceled !",Snackbar.LENGTH_LONG).show();
    }
}
