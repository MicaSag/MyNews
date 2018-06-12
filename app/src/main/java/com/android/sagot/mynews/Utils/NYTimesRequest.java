package com.android.sagot.mynews.Utils;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.sagot.mynews.Models.Criteria;
import com.android.sagot.mynews.Models.Model;
import com.android.sagot.mynews.Models.NYTimesNews;
import com.android.sagot.mynews.Models.NYTimesStreams.ArticleSearch.NYTimesArticleSearch;
import com.android.sagot.mynews.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 *  This Library is a set of functions to query the NYT APIs
 * */
public class NYTimesRequest {

    // For Debug
    private static final String TAG=NYTimesRequest.class.getSimpleName();

    // Criteria of the query
    private Map<String, String> mQuery;

    // Context
    Context mContext;
    SwipeRefreshLayout mSwipeRefreshLayout;
    NYTimesArticleSearch mArticleSearch;

     // Create criteria for request of NYTimes APIs
    public void createQuery(Criteria criteria) {

        // Create filters
        mQuery = new HashMap<>();

        // --> Add Criteria <--
        // -- Results are sorted by newest to oldest
        mQuery.put("sort", "newest");

        // -- Query criteria
        if (criteria.getKeysWords() !="") mQuery.put("q", criteria.getKeysWords());

        // -- Sections criteria
        String sections = "news_desk:(";
        if (criteria.isArts()) sections += " \"Arts\"";
        if (criteria.isBusiness()) sections += " \"Business\"";
        if (criteria.isEntrepreneurs()) sections += " \"Entrepreneurs\"";
        if (criteria.isPolitics()) sections += " \"Politics\"";
        if (criteria.isSports()) sections += " \"Sports\"";
        if (criteria.isTravel()) sections += " \"Travel\"";
        sections += ")";
        mQuery.put("fq", sections);
    }

    // Add Date criteria to request
    public void addDateCriteriaToQuery(Date date, String criteriaName) {
        // -- Date criteria
        SimpleDateFormat criteriaDateFormatter = new SimpleDateFormat("yyyyMMdd", Locale.US);
        if (date != null) mQuery.put(criteriaName, criteriaDateFormatter.format(date));
    }

    public void displayQuery() {
        Log.d(TAG, "displayQuery: ");
        for (String key: mQuery.keySet()) Log.d(TAG, key + " : "+mQuery.get(key));
    }

    // Generate a toast Message if error during Downloading
    protected void updateUIWhenErrorHTTPRequest(){
        // Stop refreshing
        mSwipeRefreshLayout.setRefreshing(false);
        Toast.makeText(mContext, "Error during Downloading", Toast.LENGTH_LONG).show();
    }

    public void setArticleSearch(NYTimesArticleSearch articleSearch) {
        mArticleSearch = articleSearch;
    }

    public Map<String, String> getQuery() {
        return mQuery;
    }
}
