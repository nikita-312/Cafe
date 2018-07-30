package com.conceptioni.cafeapp;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.util.List;
import java.util.Objects;

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
