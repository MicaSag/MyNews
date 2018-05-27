package com.android.sagot.mynews.Models;

import android.content.SharedPreferences;

import java.util.List;

public class Model {

    private SharedPreferences mSharedPreferences;
    private DataModel mDataModel;
    private List<NYTimesNews>  mListTopStoriesNews;
    private List<NYTimesNews>  mListMostPopularNews;
    private List<NYTimesNews>  mListBusinessNews;
    private List<NYTimesNews>  mListSportsNews;
    private List<NYTimesNews>  mListSearchNews;

    private static final Model MODEL = new Model();

    public static Model getInstance() {
        return MODEL;
    }

    public DataModel getDataModel() {
        return mDataModel;
    }

    public void setDataModel(DataModel dataModel) {
        this.mDataModel = dataModel;
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