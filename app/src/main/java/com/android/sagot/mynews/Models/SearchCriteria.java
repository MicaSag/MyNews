package com.android.sagot.mynews.Models;

import java.io.Serializable;
import java.util.Date;

/**
 *  Data group used in the layout "activity_search.xml/category.xml"
 */
public class SearchCriteria implements Serializable{

    private boolean mArts = false;              // CheckBox Arts status
    private boolean mBusiness = false;          // CheckBox Business status
    private boolean mEntrepreneurs = false;     // CheckBox Entrepreneurs status
    private boolean mPolitics = false;          // CheckBox Politics status
    private boolean mSports = false;            // CheckBox Sports status
    private boolean mTravel = false;            // CheckBox Travel status
    private String mKeysWords;                  // Backing up keywords
    private Date mBeginDate;                    // Saving the begin date
    private Date mEndDate;                      // Saving the end date

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

    public String getKeysWords() {
        return mKeysWords;
    }

    public void setKeysWords(String keysWords) {
        mKeysWords = keysWords;
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

    public boolean isSports() {
        return mSports;
    }

    public void setSports(boolean sports) {
        mSports = sports;
    }
}
