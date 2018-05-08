package com.android.sagot.mynews.Models;

public class NYTimesNews {

    String mTitle;
    String mNewsURL;
    String mImageURL;
    String mSection;
    String mDate;


    public NYTimesNews(String title, String imageURL, String newsURL, String date, String section) {
        this.mTitle = title;
        this.mNewsURL = newsURL;
        this.mImageURL = imageURL;
        this.mDate = date;
        this.mSection = section;
    }

    public String getSection() {
        return mSection;
    }

    public void setSection(String section) {
        this.mSection = section;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public String getImageURL() {
        return mImageURL;
    }

    public void setImageURL(String mImageURL) {
        this.mImageURL = mImageURL;
    }

    public String getNewsURL() {
        return mNewsURL;
    }

    public void setNewsURL(String newsURL) {
        this.mNewsURL = newsURL;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

}
