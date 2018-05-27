package com.android.sagot.mynews.Models;

import java.util.Date;

public class DataModel {
    private SearchCriteria mSearchCriteria;  // Save the search Criteria
    private NotificationsCriteria mNotificationsCriteria; // Save the notifications criteria
    // Save the publication date of the most recent news found matching the notification criteria
    private Date mLastNewsDate;


    public SearchCriteria getSearchCriteria() {
        return mSearchCriteria;
    }

    public void setSearchCriteria(SearchCriteria searchCriteria) {
        mSearchCriteria = searchCriteria;
    }

    public NotificationsCriteria getNotificationsCriteria() {
        return mNotificationsCriteria;
    }

    public void setNotificationsCriteria(NotificationsCriteria notificationsCriteria) {
        mNotificationsCriteria = notificationsCriteria;
    }

    public Date getLastNewsDate() {
        return mLastNewsDate;
    }

    public void setLastNewsDate(Date lastNewsDate) {
        mLastNewsDate = lastNewsDate;
    }
}
