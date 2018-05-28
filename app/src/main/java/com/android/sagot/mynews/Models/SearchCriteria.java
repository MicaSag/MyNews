package com.android.sagot.mynews.Models;

import java.io.Serializable;
import java.util.Date;

/**
 *  Data group used in the layout "activity_search.xml/category.xml"
 */
public class SearchCriteria extends Criteria implements Serializable{

    private Date mBeginDate;                    // Saving the begin date
    private Date mEndDate;                      // Saving the end date

    public Date getBeginDate() {
        return mBeginDate;
    }

    public void setBeginDate(Date beginDate) {
        mBeginDate = beginDate;
    }

    public Date getEndDate() {
        return mEndDate;
    }

    public void setEndDate(Date endDate) {
        mEndDate = endDate;
    }
}
