package com.android.sagot.mynews.Models;

import java.util.Date;
import java.util.List;

/**
 *  Data group used in the application and saved in the SharedPreferences
 */

public class SavedModel {
    private SearchCriteria mSearchCriteria;               // Data group used in the layout "activity_search.xml/category.xml"
    private NotificationsCriteria mNotificationsCriteria; // Data group used in the layout "activity_notifications.xml/category.xml"
    private List<String> mListUrlArticleRead;             // List of urls of articles already read

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

    public List<String> getListUrlArticleRead() {
        return mListUrlArticleRead;
    }

    public void setListUrlArticleRead(List<String> listUrlArticleRead) {
        mListUrlArticleRead = listUrlArticleRead;
    }
}
