package com.android.sagot.mynews.Models;

import java.io.Serializable;

/**
 * Criteria group common to search and notification features
 */

public class Criteria implements Serializable {

    private String mKeysWords;                      // Backing up keywords
    private boolean mArts = false;                  // CheckBox Arts status
    private boolean mBusiness = false;              // CheckBox Business status
    private boolean mEntrepreneurs = false;         // CheckBox Entrepreneurs status
    private boolean mPolitics = false;              // CheckBox Politics status
    private boolean mSports = false;                // CheckBox Sports status
    private boolean mTravel = false;                // CheckBox Travel status

    public String getKeysWords() {
        return mKeysWords;
    }

    public void setKeysWords(String keysWords) {
        mKeysWords = keysWords;
    }

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

    public boolean isSports() {
        return mSports;
    }

    public void setSports(boolean sports) {
        mSports = sports;
    }

    public boolean isTravel() {
        return mTravel;
    }

    public void setTravel(boolean travel) {
        mTravel = travel;
    }
}

