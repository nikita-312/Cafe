package com.conceptioni.cafeapp;

import android.app.Application;
import android.content.Context;

/**
 * Created by 123 on 21-02-2017.
 */

public class CafeApp extends Application {

    private static CafeApp mInstance;

    public CafeApp() {
        mInstance = this;
    }

    public static Context getContext() {
        return mInstance;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);

    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

}
