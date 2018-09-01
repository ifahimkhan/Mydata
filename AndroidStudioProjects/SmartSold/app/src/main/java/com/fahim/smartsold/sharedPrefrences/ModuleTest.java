package com.fahim.smartsold.sharedPrefrences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by root on 13/3/18.
 */

public class ModuleTest {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "ModulePref";
    private static final String KEY_WIFI = "wifi";
    private static final String KEY_BLUETOOTH = "bluetooth";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_HOTSPOT = "hotspot";
    private static final String KEY_MIC = "mic";
    private static final String KEY_BATTERY = "battery";
    private static final String KEY_INFO = "info";

    public ModuleTest(Context context) {
        this.context = context;
        sharedPreferences = this.context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void refresh() {
        editor.clear();
        editor.commit();
    }

    public void setKeyInfo(String keyInfo) {
        editor.putString(KEY_INFO, keyInfo);
        editor.commit();
    }

    public String getInfo() {
        return sharedPreferences.getString(KEY_INFO, "null");
    }

    public void setKeyWifi(boolean bool) {

        editor.putBoolean(KEY_WIFI, bool);
        editor.commit();
    }

    public void setKeyBluetooth(boolean bool) {
        editor.putBoolean(KEY_BLUETOOTH, bool);
        editor.commit();
    }

    public void setKeyLocation(boolean bool) {
        editor.putBoolean(KEY_LOCATION, bool);
        editor.commit();
    }

    public void setKeyHotspot(boolean bool) {
        editor.putBoolean(KEY_HOTSPOT, bool);
        editor.commit();
    }

    public void setKeyMic(boolean bool) {
        editor.putBoolean(KEY_MIC, bool);
        editor.commit();
    }

    public void setKeyBattery(boolean bool) {
        editor.putBoolean(KEY_BATTERY, bool);
        editor.commit();
    }

    public boolean getWifi() {
        return sharedPreferences.getBoolean(KEY_WIFI, false);
    }

    public boolean getBluetooth() {
        return sharedPreferences.getBoolean(KEY_BLUETOOTH, false);
    }

    public boolean getLocation() {
        return sharedPreferences.getBoolean(KEY_LOCATION, false);
    }

    public boolean getHotspot() {
        return sharedPreferences.getBoolean(KEY_HOTSPOT, false);
    }

    public boolean getMic() {
        return sharedPreferences.getBoolean(KEY_MIC, false);
    }

    public boolean getBattery() {
        return sharedPreferences.getBoolean(KEY_BATTERY, false);
    }

}
