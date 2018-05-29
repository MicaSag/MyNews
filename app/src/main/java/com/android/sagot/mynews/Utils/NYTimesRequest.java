package com.android.sagot.mynews.Utils;

import android.util.Log;

import com.android.sagot.mynews.Models.Criteria;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 *  Formatting Request for API NYTimes Article Search
 * */
public class NYTimesRequest {

    // For Debug
    private static final String TAG=NYTimesRequest.class.getSimpleName();

    // Filters following conditions
    Map<String, String> filters;

    // Create request
    public void createNYTimesRequest(Criteria criteria) {

        // Create filters
        filters = new HashMap<>();

        // --> Add Criteria <--
        // -- Results are sorted by newest to oldest
        filters.put("sort", "newest");

        // -- Query criteria
        if (criteria.getKeysWords() !="") filters.put("q", criteria.getKeysWords());

        // -- Sections criteria
        String sections = "news_desk:(";
        if (criteria.isArts()) sections += " \"Arts\"";
        if (criteria.isBusiness()) sections += " \"Business\"";
        if (criteria.isEntrepreneurs()) sections += " \"Entrepreneurs\"";
        if (criteria.isPolitics()) sections += " \"Politics\"";
        if (criteria.isSports()) sections += " \"Sports\"";
        if (criteria.isTravel()) sections += " \"Travel\"";
        sections += ")";
        filters.put("fq", sections);
    }

    // Add Date criteria to request
    public void addDateCriteriaToRequest(Date date, String criteriaName) {
        // -- Date criteria
        SimpleDateFormat criteriaDateFormatter = new SimpleDateFormat("yyyyMMdd", Locale.US);
        if (date != null) filters.put(criteriaName, criteriaDateFormatter.format(date));
    }

    public Map<String, String> getFilters() {
        return filters;
    }

    public void displayRequest() {
        Log.d(TAG, "displayRequest: ");
        for (String key: filters.keySet()) Log.d(TAG, key + " : "+filters.get(key));
    }
}
