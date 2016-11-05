package com.example.hyc.movieshow.utils;

import android.content.Context;
import android.content.res.Resources;

import com.example.hyc.movieshow.App;

public class UIUtil {

    /**
     * 获取全局上下文
     * @return
     */
    public static Context getContext() {
        return App.getApp();
    }

    /**
     * 获取资源
     */
    public static Resources getResources()
    {
        return getContext().getResources();
    }

    public static String getString(int resId)
    {
        return getResources().getString(resId);
    }

    public static String getString(int resId, Object... formatArgs)
    {
        return getResources().getString(resId, formatArgs);
    }

}