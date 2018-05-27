package com.android.sagot.mynews.Models;

import android.content.SharedPreferences;

public class Model {

    private SharedPreferences mSharedPreferences;
    private DataModel mDataModel;

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
}