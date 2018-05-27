package com.android.sagot.mynews.Models;

import java.util.Date;

public class NotificationsCriteria {

    private String mKeysWords;
    private boolean mArts = false;
    private boolean mBusiness = false;
    private boolean mEntrepreneurs = false;
    private boolean mPolitics = false;
    private boolean mSports = false;
    private boolean mTravel = false;
    private boolean mNotificationStatus = false;

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

    public boolean isNotificationStatus() {
        return mNotificationStatus;
    }

    public void setNotificationStatus(boolean notificationStatus) {
        mNotificationStatus = notificationStatus;
    }
}
