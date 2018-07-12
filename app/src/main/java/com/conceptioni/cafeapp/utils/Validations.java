package com.conceptioni.cafeapp.utils;

import android.widget.EditText;

/**
 * Created by Admin on 8/10/2015.
 */
public class Validations {

    public boolean isEmpty(EditText editText) {
        return editText.getText().toString().equals("");
    }

    public boolean isValidPhoneNumber(EditText phoneNumber) {
        return phoneNumber.getText().toString().length() == 10;
    }


}
