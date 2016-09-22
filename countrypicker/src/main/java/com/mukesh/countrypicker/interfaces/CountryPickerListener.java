package com.mukesh.countrypicker.interfaces;

/**
 * Created by mukesh on 25/04/16.
 */
public interface CountryPickerListener {
    void onSelectCountry(int id, String name, String code, String dialCode, int flagDrawableResID);
}
