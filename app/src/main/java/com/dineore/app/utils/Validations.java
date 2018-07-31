package com.dineore.app.utils;

import android.widget.EditText;

public class Validations {

    public boolean isEmpty(EditText editText) {
        return editText.getText().toString().equals("");
    }

    public boolean isValidPhoneNumber(EditText phoneNumber) {
        return phoneNumber.getText().toString().length() == 10;
    }


}
