package com.android.sagot.mynews.Models;

import java.io.Serializable;

/**
 *  Data group used in the layout "activity_notifications.xml/category.xml"
 */

public class NotificationsCriteria extends Criteria implements Serializable {
                 
    private boolean mNotificationStatus = false;    // Saves the status of the notification switch

    public boolean isNotificationStatus() {
        return mNotificationStatus;
    }

    public void setNotificationStatus(boolean notificationStatus) {
        mNotificationStatus = notificationStatus;
    }
}
