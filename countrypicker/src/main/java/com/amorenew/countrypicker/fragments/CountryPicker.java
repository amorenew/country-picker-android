package com.amorenew.countrypicker.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.amorenew.countrypicker.R;
import com.amorenew.countrypicker.Util;
import com.amorenew.countrypicker.adapters.CountryListAdapter;
import com.amorenew.countrypicker.interfaces.CountryPickerListener;
import com.amorenew.countrypicker.models.Country;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

/**
 * Created by mukesh on 25/04/16.
 */
public class CountryPicker extends DialogFragment implements Comparator<Country> {

    private EditText searchEditText;
    private ListView countryListView;
    private CountryListAdapter adapter;
    private List<Country> allCountriesList;
    private List<Country> selectedCountriesList;
    private CountryPickerListener listener;
    private Context context;

    public static Currency getCurrencyCode(String countryCode) {
        try {
            return Currency.getInstance(new Locale("en", countryCode));
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * To support show as dialog
     */
    public static CountryPicker newInstance(String dialogTitle, List<Country> countries) {
        CountryPicker picker = new CountryPicker();
        if (countries != null)
            picker.setCountryList(countries);
        picker.getAllCountries();
        Bundle bundle = new Bundle();
        bundle.putString("dialogTitle", dialogTitle);
        picker.setArguments(bundle);
        return picker;
    }

    /**
     * To support show as dialog
     */
    public static CountryPicker newInstance(String dialogTitle) {
        return newInstance(dialogTitle, null);
    }

    public void setListener(CountryPickerListener listener) {
        this.listener = listener;
    }

    public EditText getSearchEditText() {
        return searchEditText;
    }

    public ListView getCountryListView() {
        return countryListView;
    }

    public void setCountryList(List<Country> allCountriesList) {
        this.allCountriesList = allCountriesList;
    }

    private List<Country> getAllCountries() {
        if (allCountriesList == null || allCountriesList.isEmpty()) {
            try {
                allCountriesList = new ArrayList<>();
                String allCountriesCode = Util.getRawString(context, R.raw.country_list);
                JSONArray countryArray = new JSONArray(allCountriesCode);
                for (int i = 0; i < countryArray.length(); i++) {
                    JSONObject jsonObject = countryArray.getJSONObject(i);
                    Country country = new Country();
                    country.setId(jsonObject.getInt("CountryId"));
                    country.setCode(jsonObject.getString("CountryISO2"));
                    country.setDialCode(jsonObject.getString("DialingCode"));
                    allCountriesList.add(country);
                }
                Collections.sort(allCountriesList, this);
                selectedCountriesList = new ArrayList<>();
                selectedCountriesList.addAll(allCountriesList);
                return allCountriesList;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Collections.sort(allCountriesList, this);
        selectedCountriesList = new ArrayList<>();
        selectedCountriesList.addAll(allCountriesList);
        return allCountriesList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.country_picker, null);
        Bundle args = getArguments();
        // remove title from dialog fragment
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (args != null) {
            String dialogTitle = args.getString("dialogTitle");
            TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvTitle.setText(dialogTitle);

            int width = getResources().getDimensionPixelSize(R.dimen.cp_dialog_width);
            int height = getResources().getDimensionPixelSize(R.dimen.cp_dialog_height);
            getDialog().getWindow().setLayout(width, height);
        }

        getAllCountries();
        searchEditText = (EditText) view.findViewById(R.id.country_code_picker_search);
        countryListView = (ListView) view.findViewById(R.id.country_code_picker_listview);

        adapter = new CountryListAdapter(getActivity(), selectedCountriesList);
        countryListView.setAdapter(adapter);

        countryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    Country country = selectedCountriesList.get(position);
                    listener.onSelectCountry(country);
                }
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                search(s.toString());
            }
        });

        return view;
    }

    @SuppressLint("DefaultLocale")
    private void search(String text) {
        if (selectedCountriesList != null)
            selectedCountriesList.clear();
        for (Country country : allCountriesList) {
            if (country.getName().toLowerCase(Locale.ENGLISH).contains(text.toLowerCase())) {
                selectedCountriesList.add(country);
            } else if (country.getDialCode().toLowerCase(Locale.ENGLISH).contains(text.toLowerCase())) {
                selectedCountriesList.add(country);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public int compare(Country lhs, Country rhs) {
        return lhs.getName().compareTo(rhs.getName());
    }

    public Country getUserCountryInfo(Context context) {
        this.context = context;
        getAllCountries();
        String countryIsoCode;
        TelephonyManager telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (!(telephonyManager.getSimState() == TelephonyManager.SIM_STATE_ABSENT)) {
            countryIsoCode = telephonyManager.getSimCountryIso();
            for (int i = 0; i < allCountriesList.size(); i++) {
                Country country = allCountriesList.get(i);
                if (country.getCode().equalsIgnoreCase(countryIsoCode)) {
                    country.setFlag(getFlagResId(country.getCode()));
                    return country;
                }
            }
        }
        return afghanistan();
    }

    private Country afghanistan() {
        Country country = new Country();
        country.setCode("AF");
        country.setDialCode("+93");
        country.setFlag(R.drawable.flag_af);
        return country;
    }

    private int getFlagResId(String drawable) {
        try {
            return context.getResources()
                    .getIdentifier("flag_" + drawable.toLowerCase(Locale.ENGLISH), "drawable",
                            context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
