package com.android.sagot.mynews.Models;

import java.util.Date;

public class SearchCriteria {

    private boolean mArts;
    private boolean mBusiness;
    private boolean mEntrepreneurs;
    private boolean mPolitics;
    private boolean mSports;
    private boolean mTravel;
    private String mSearchQueryTerm;
    private Date mBeginDate;

    public boolean isArts() {
        return mArts;
    }

    public void setArts(boolean arts) {
        mArts = arts;
    }

    public boolean isBusiness() {
        return mBusiness;
    }

    public void setBusiness(boolean business) {
        mBusiness = business;
    }

    public boolean isEntrepreneurs() {
        return mEntrepreneurs;
    }

    public void setEntrepreneurs(boolean entrepreneurs) {
        mEntrepreneurs = entrepreneurs;
    }

    public boolean isPolitics() {
        return mPolitics;
    }

    public void setPolitics(boolean politics) {
        mPolitics = politics;
    }

    public boolean isTravel() {
        return mTravel;
    }

    public void setTravel(boolean travel) {
        mTravel = travel;
    }

    public String getSearchQueryTerm() {
        return mSearchQueryTerm;
    }

    public void setSearchQueryTerm(String searchQueryTerm) {
        mSearchQueryTerm = searchQueryTerm;
    }

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

    private Date mEndDate;

    public boolean isSports() {
        return mSports;
    }

    public void setSports(boolean sports) {
        mSports = sports;
    }
}
