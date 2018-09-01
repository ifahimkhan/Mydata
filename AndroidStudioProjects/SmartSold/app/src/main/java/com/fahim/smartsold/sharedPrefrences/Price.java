package com.fahim.smartsold.sharedPrefrences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by root on 13/3/18.
 */

public class Price {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "PricePref";
    private static final String KEY_DEDUCTION = "deduction_amount";
    private static final String KEY_MARKET_PRICE = "market_Price";
    private static final String KEY_MARKET_DISPLAY_PRICE = "display_price";
    private static final String DESCRIPTION_PREF = "desc_pref";
    private static final String Name_PREF = "name_pref";

    public Price(Context context) {
        this.context = context;
        sharedPreferences = this.context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }


    public void setDeductionAmount(int value) {
        editor.putInt(KEY_DEDUCTION, value);
        editor.commit();
    }

    public int getDeductionAmount() {

        int deduction_amount = sharedPreferences.getInt(KEY_DEDUCTION, 0);
        return deduction_amount;

    }

    public String getDescription() {
        return sharedPreferences.getString(DESCRIPTION_PREF, "null");
    }

    public void setDescriptionPref(String descriptionPref) {
        editor.putString(DESCRIPTION_PREF, descriptionPref);
        editor.commit();
    }

    public String getPrefName() {
        return sharedPreferences.getString(Name_PREF, "null");
    }

    public void setPrefName(String descriptionPref) {
        editor.putString(Name_PREF, descriptionPref);
        editor.commit();
    }

    public void setKeyMarketDisplayPrice(int value) {
        editor.putInt(KEY_MARKET_DISPLAY_PRICE, value);
        editor.commit();
    }

    public int getMarketDisplayPrice() {

        int amount = sharedPreferences.getInt(KEY_MARKET_DISPLAY_PRICE, 0);
        return amount;

    }

    public void setMarketPrice(int value) {
        editor.putInt(KEY_MARKET_PRICE, value);
        editor.commit();
    }

    public int getMarketPrice() {

        int amount = sharedPreferences.getInt(KEY_MARKET_PRICE, 0);
        return amount;

    }

    public void refresh() {
        editor.clear();
        editor.commit();
    }


}
