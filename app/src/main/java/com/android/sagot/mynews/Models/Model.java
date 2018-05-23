package com.android.sagot.mynews.Models;

public class Model {

    private Preferences mPreferences;

    private static final Model MODEL = new Model();

    public static Model getInstance() {
        return MODEL;
    }

    public Preferences getPreferences() {
        return mPreferences;
    }

    public void setPreferences(Preferences preferences) {
        this.mPreferences = preferences;
    }
}