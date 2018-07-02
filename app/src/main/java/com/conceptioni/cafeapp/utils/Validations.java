package com.conceptioni.cafeapp.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Admin on 8/10/2015.
 */
public class Validations {
    Pattern pattern;
    private Matcher matcher;
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public boolean isEmpty(EditText editText) {
        if (editText.getText().toString().equals("")) return true;
        else return false;
    }

    public boolean isEmpty(TextView textView) {
        if (textView.getText().toString().equals("")) return true;
        else return false;
    }

    public boolean isSamePassword(EditText password, EditText confirmPassword) {
        if (password.getText().toString().equals(confirmPassword.getText().toString())) return true;
        else return false;
    }

    public boolean isValidPassword(EditText password) {
        if (password.getText().toString().length() >= 6) return true;
        else return false;
    }

    public boolean isPassword(EditText password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password.getText().toString().trim());

        return matcher.matches();

    }

    public <object> boolean isNull(object abc) {
        if (abc == null) return true;
        else return false;
    }

    public boolean isEmail(EditText email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email.getText().toString());
        if (matcher.matches()) return true;
        else return false;
    }

    public boolean isEmail(TextView email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email.getText().toString());
        if (matcher.matches()) return true;
        else return false;
    }

    public boolean isValidPhoneNumber(EditText phoneNumber) {
        if (phoneNumber.getText().toString().length() == 10) return true;
        else return false;
    }

    public boolean isChecked(CheckBox checkBox){
        if (checkBox.isChecked()) return true;
        else return false;
    }

    public String ImageToBase64(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public String ImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}
