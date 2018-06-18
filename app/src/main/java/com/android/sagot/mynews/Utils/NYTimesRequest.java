package com.android.sagot.mynews.Utils;

import android.util.Log;

import com.android.sagot.mynews.Models.Criteria;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 *  This Library is a set of functions to query the NYT APIs
 * */
public class NYTimesRequest {

    // For Debug
    private static final String TAG=NYTimesRequest.class.getSimpleName();

    // Criteria of the query
    private Map<String, String> mQuery;

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

    public Map<String, String> getQuery() {
        return mQuery;
    }
}
