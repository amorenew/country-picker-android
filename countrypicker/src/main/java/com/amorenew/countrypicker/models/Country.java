package com.amorenew.countrypicker.models;

import android.text.TextUtils;

import java.util.Locale;

/**
 * Created by mukesh on 25/04/16.
 */

public class Country {
    private int id;
    private String code;
    private String name;
    private String dialCode;
    private int flag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDialCode() {
        return dialCode;
    }

    public void setDialCode(String dialCode) {
        this.dialCode = dialCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
        if (TextUtils.isEmpty(name)) {
            name = new Locale("", code).getDisplayName(Locale.getDefault());
        }
    }

    public String getName() {
        return name;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}