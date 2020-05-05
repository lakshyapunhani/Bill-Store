package com.fabuleux.wuntu.billstore.Manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SessionManager
{
    public static final int FCM_NOTIFICATION_ID = 123456;
    static SharedPreferences pref;
    static SharedPreferences.Editor editor;
    Context _context;

    private String GST_SLAB_LIST = "gst_slab_list";
    private String UNITS_LIST = "units_list";
    private String IS_INTERNET_AVAILABLE ="isinternetavailable";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String LANGUAGE_PREFERENCE = "language_preference";

    private String DEVICE_TOKEN = "device_token";
    private String NAME = "name";
    private String SHOP_ADDRESS= "shop_address";
    private String SHOP_GST = "shop_gst";
    private String SHOP_NAME = "shop_name";
    private String SHOP_PAN = "shop_pan";
    private static final String DARK_THEME = "dark_theme";

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

    public void setDarkThemeEnabled (boolean isEnabled) {
        editor.putBoolean(DARK_THEME, isEnabled).commit();
    }

    public boolean isDarkThemeEnabled () {
        return pref.getBoolean(DARK_THEME, false);
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

    public String getDeviceToken()
    {
        return pref.getString(DEVICE_TOKEN,"");
    }

    public void setDeviceToken(String token)
    {
        editor.putString(DEVICE_TOKEN,token);
        editor.commit();
    }

    public String getName() {
        return pref.getString(NAME, "");
    }

    public void setName(String name) {
        editor.putString(NAME, name);
        editor.commit();
    }

    public String getShop_address() {
        return pref.getString(SHOP_ADDRESS, "");
    }

    public void setShop_address(String shop_address) {
        editor.putString(SHOP_ADDRESS, shop_address);
        editor.commit();
    }

    public String getShop_gst() {
        return pref.getString(SHOP_GST, "");
    }

    public void setShop_gst(String shop_gst) {
        editor.putString(SHOP_GST, shop_gst);
        editor.commit();
    }

    public String getShop_name() {
        return pref.getString(SHOP_NAME, "");
    }

    public void setShop_name(String shop_name) {
        editor.putString(SHOP_NAME, shop_name);
        editor.commit();
    }

    public String getShop_pan() {
        return pref.getString(SHOP_PAN, "");
    }

    public void setShop_pan(String shop_pan) {
        editor.putString(SHOP_PAN, shop_pan);
        editor.commit();
    }
}
