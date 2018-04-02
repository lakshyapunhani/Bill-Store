package com.example.wuntu.billstore.Manager;

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
    private String ISINTERNETAVAILABLE="isinternetavailable";

    // Shared pref mode
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "com.example.wuntu.billstore";

    // Constructor
    public SessionManager(Context context) {
        this._context = context.getApplicationContext();
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
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
        editor.putBoolean(ISINTERNETAVAILABLE,b);
        editor.commit();
    }

    public Boolean isInternetAvailable() {
        return pref.getBoolean(ISINTERNETAVAILABLE,true);
    }
}
