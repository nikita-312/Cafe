package com.dineore.app.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class TextviewRegular extends TextView {

    public TextviewRegular(Context context) {
        super(context);
        init();
    }

    public TextviewRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextviewRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/POPPINS-REGULAR_0.TTF");
        setTypeface(tf);
    }
}
