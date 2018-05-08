package com.android.sagot.mynews.Models;

public class NYTimesNews {

    String mTitle;
    String mNewsURL;


    public NYTimesNews(String title, String newsURL) {
        this.mTitle = title;
        this.mNewsURL = newsURL;
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
