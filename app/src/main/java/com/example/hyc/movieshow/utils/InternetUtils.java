package com.example.hyc.movieshow.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by hyc on 2016/11/6.
 */

public class InternetUtils {

    public static boolean isConnected() {
        ConnectivityManager manager = (ConnectivityManager) UIUtil.getContext().getSystemService
                (Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        return info != null && info.isConnected();
    }

    public static NetworkInfo getNerWorkInfo() {
        ConnectivityManager manager = (ConnectivityManager) UIUtil.getContext().getSystemService
                (Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo();
    }

    public static boolean isWifiConnected() {

        return getNerWorkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }
}
