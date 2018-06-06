package com.android.sagot.mynews.Models;

import android.content.SharedPreferences;

import java.util.List;

    /**
     *                   >>         SINGLETON         <<
     *
     * Class to share common data between different components of the application
     */

public class Model {

    private SharedPreferences mSharedPreferences;       // Reference backup of the SharedPreferences
    private SavedModel mSavedModel;                     // Model data saved in the SharedPreferences
    // Saving the list of news that are loaded when the application starts
    private List<NYTimesNews>  mListTopStoriesNews;     // List of news "TopsStories"    
    private List<NYTimesNews>  mListMostPopularNews;    // List of news "MostPopular"
    private List<NYTimesNews>  mListBusinessNews;       // List of news "Business"
    private List<NYTimesNews>  mListSportsNews;         // List of news "Sports
    private List<NYTimesNews>  mListSearchNews;         // List of Search news

    private static final Model MODEL = new Model();

    public static Model getInstance() {
        return MODEL;
    }

    public SavedModel getSavedModel() {
        return mSavedModel;
    }

    public void setSavedModel(SavedModel savedModel) {
        this.mSavedModel = savedModel;
    }

    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    public List<NYTimesNews> getListTopStoriesNews() {
        return mListTopStoriesNews;
    }

    public void setListTopStoriesNews(List<NYTimesNews> listTopStoriesNews) {
        mListTopStoriesNews = listTopStoriesNews;
    }

    public List<NYTimesNews> getListMostPopularNews() {
        return mListMostPopularNews;
    }

    public void setListMostPopularNews(List<NYTimesNews> listMostPopularNews) {
        mListMostPopularNews = listMostPopularNews;
    }

    public List<NYTimesNews> getListBusinessNews() {
        return mListBusinessNews;
    }

    public void setListBusinessNews(List<NYTimesNews> listBusinessNews) {
        mListBusinessNews = listBusinessNews;
    }

    public List<NYTimesNews> getListSportsNews() {
        return mListSportsNews;
    }

    public void setListSportsNews(List<NYTimesNews> listSportsNews) {
        mListSportsNews = listSportsNews;
    }

   public List<NYTimesNews> getListSearchNews() {
        return mListSearchNews;
    }

    public void setListSearchNews(List<NYTimesNews> listSearchNews) {
        mListSearchNews = listSearchNews;
    }
}
