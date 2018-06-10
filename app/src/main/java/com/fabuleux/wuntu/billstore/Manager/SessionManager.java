package com.fabuleux.wuntu.billstore.Manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SessionManager
{
    static SharedPreferences pref;
    static SharedPreferences.Editor editor;
    Context _context;

    private String GST_SLAB_LIST = "gst_slab_list";
    private String UNITS_LIST = "units_list";
    private String IS_INTERNET_AVAILABLE ="isinternetavailable";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String LANGUAGE_PREFERENCE = "language_preference";

    // Shared pref mode
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "com.example.wuntu.billstore";

    // Constructor
    public SessionManager(Context context) {
        this._context = context.getApplicationContext();
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void clearSharedPref() {
        editor.clear();
        editor.commit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void saveGstSlabLists(String arraylistString) {
        editor.putString(GST_SLAB_LIST,arraylistString);
        editor.commit();
    }
    public ArrayList<String> getGstSlabList() {
        Gson gson = new Gson();
        String storedHashMapString = pref.getString(GST_SLAB_LIST,"");
        Type type = new TypeToken<ArrayList<String>>(){}.getType();

        ArrayList<String> gstSlabList = gson.fromJson(storedHashMapString, type);
        return gstSlabList;
    }

    public void saveUnitList(String arraylistString) {
        editor.putString(UNITS_LIST,arraylistString);
        editor.commit();
    }
    public ArrayList<String> getUnitList() {
        Gson gson = new Gson();
        String storedHashMapString = pref.getString(UNITS_LIST,"");
        Type type = new TypeToken<ArrayList<String>>(){}.getType();

        ArrayList<String> unitList = gson.fromJson(storedHashMapString, type);
        return unitList;
    }

    public void setInternetAvailable(boolean b) {
        editor.putBoolean(IS_INTERNET_AVAILABLE,b);
        editor.commit();
    }

    public Boolean isInternetAvailable() {
        return pref.getBoolean(IS_INTERNET_AVAILABLE,true);
    }

    public void saveLanguagePreference(String language)
    {
        editor.putString(LANGUAGE_PREFERENCE, language);
        editor.commit();
    }

    public String getLanguagePreference()
    {
        return pref.getString(LANGUAGE_PREFERENCE, "en");
    }
}
