package com.example.hyc.movieshow;

import android.app.Application;
import android.app.TaskStackBuilder;

import com.squareup.leakcanary.LeakCanary;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by hyc on 16-11-5.
 */

public class App extends Application
{
    private static App                mApp;
    private static RealmConfiguration mConfiguration;

    public static Application getApp()
    {
        return mApp;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this))
        {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        mApp = this;
        initRealm();
    }

    private void initRealm()
    {
        Realm.init(this);
        mConfiguration = new RealmConfiguration.Builder()
            // 这里删除之前的Migration ,正式发布时需要进行修改
            .deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(mConfiguration);
    }

    public static RealmConfiguration getConfiguration()
    {
        return mConfiguration;
    }
}
