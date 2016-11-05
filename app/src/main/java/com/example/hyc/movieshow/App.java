package com.example.hyc.movieshow;

import android.app.Application;

/**
 * Created by hyc on 16-11-5.
 */

public class App extends Application
{
    private static App mApp;

    public static Application getApp()
    {
        return mApp;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        mApp = this;
    }
}
