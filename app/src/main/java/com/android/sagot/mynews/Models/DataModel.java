package com.android.sagot.mynews.Models;

public class DataModel {
    private SearchCriteria mSearchCriteria;
    private NotificationsCriteria mNotificationsCriteria;

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
}
