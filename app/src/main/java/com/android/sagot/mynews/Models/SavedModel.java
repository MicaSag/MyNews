package com.android.sagot.mynews.Models;

import java.util.ArrayList;
import java.util.List;

/**
 *  Data group used in the application and saved in the SharedPreferences
 */

public class SavedModel {
    // Data group used in the layout "activity_search.xml/category.xml"
    private SearchCriteria mSearchCriteria = new SearchCriteria();
    // Data group used in the layout "activity_notifications.xml/category.xml"
    private NotificationsCriteria mNotificationsCriteria = new NotificationsCriteria();
    // List of urls of articles already read
    private List<String> mListUrlArticleRead = new ArrayList<>();

    public SearchCriteria getSearchCriteria() {
        return mSearchCriteria;
    }

    public NotificationsCriteria getNotificationsCriteria() {
        return mNotificationsCriteria;
    }

    public List<String> getListUrlArticleRead() {
        return mListUrlArticleRead;
    }
}
