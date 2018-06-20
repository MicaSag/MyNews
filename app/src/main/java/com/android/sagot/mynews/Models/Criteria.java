package com.android.sagot.mynews.Models;

import java.io.Serializable;

/**
 * Criteria group common to search and notification features
 */

public class Criteria implements Serializable {

    private String mKeysWords;              // Backing up keywords
    private boolean mArts;                  // CheckBox Arts status
    private boolean mBusiness;              // CheckBox Business status
    private boolean mEntrepreneurs;         // CheckBox Entrepreneurs status
    private boolean mPolitics;              // CheckBox Politics status
    private boolean mSports;                // CheckBox Sports status
    private boolean mTravel;                // CheckBox Travel status

    // Default constructor
    public Criteria() {
        this.mKeysWords     = "";           // Not KeyWords by default
        this.mArts          = false;        // Not cheked by default
        this.mBusiness      = false;        // Not cheked by default
        this.mEntrepreneurs = false;        // Not cheked by default
        this.mPolitics      = false;        // Not cheked by default
        this.mSports        = false;        // Not cheked by default
        this.mtravel        = false;        // Not cheked by default
    }
    
    // Getter an Setter Methods
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

