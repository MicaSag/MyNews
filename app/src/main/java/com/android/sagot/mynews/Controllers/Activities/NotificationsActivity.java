package com.android.sagot.mynews.Controllers.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.android.sagot.mynews.Models.Criteria;
import com.android.sagot.mynews.Models.Model;
import com.android.sagot.mynews.R;
import com.android.sagot.mynews.Utils.NYTimesRequest;
import com.android.sagot.mynews.Utils.NotificationsAlarmReceiver;

import java.util.Calendar;
import java.util.Map;

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

    // Static variables for intent parameters
    public static final String BUNDLE_NBR_ARTICLE_FOUND = "BUNDLE_NBR_ARTICLE_FOUND";

    // Creating an intent to execute our broadcast
    private PendingIntent mPendingIntent;
    // Creating alarmManager
    private AlarmManager mAlarmManager;
    
    // Number of articles meeting the criteria of the notification
    int mNbrArticleFound;
    
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
    private Calendar createCalendar() {
        /* Set the alarm to start at 11:00 AM */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DAY_OF_YEAR,1);
        calendar.set(Calendar.MILLISECOND, 1);
         return calendar;
    }
    // Start Alarm
    private void startAlarm() {
        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.setInexactRepeating(  AlarmManager.RTC_WAKEUP,   // which will wake up the device when it goes off
                                            createCalendar().getTimeInMillis(),      // First start at 11:00
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
