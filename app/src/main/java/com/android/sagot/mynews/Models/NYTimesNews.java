package com.android.sagot.mynews.Models;

/***************************************************************
 *  All data for use in the itemFragments of the RecyclerView
 */
public class NYTimesNews {

    String mDate;       // It's created date of the news
    String mSection;    // It's the "section > subSection" label
    String mTitle;      // It's Title of the news
    String mNewsURL;    // It's the Url of the news page
    String mImageURL;   // It's the Url where the image is

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
