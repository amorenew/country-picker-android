package com.mukesh.countrypicker.interfaces;

import com.mukesh.countrypicker.models.Country;

/**
 * Created by mukesh on 25/04/16.
 */
public interface CountryPickerListener {
    void onSelectCountry(Country country);
}
