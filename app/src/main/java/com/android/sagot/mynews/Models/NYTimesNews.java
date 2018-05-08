package com.android.sagot.mynews.Models;

public class NYTimesNews {

    String mTitle;
    String mNewsURL;
    String mImageURL;


    public NYTimesNews(String title, String imageURL, String newsURL) {
        this.mTitle = title;
        this.mNewsURL = newsURL;
        this.mImageURL = imageURL;
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
