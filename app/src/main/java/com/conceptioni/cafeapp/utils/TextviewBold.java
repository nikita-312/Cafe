package com.conceptioni.cafeapp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class TextviewBold extends TextView {

    public TextviewBold(Context context) {
        super(context);
        init();
    }

    public TextviewBold(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextviewBold(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/POPPINS-BOLD_0.TTF");
        setTypeface(tf);
    }
}
