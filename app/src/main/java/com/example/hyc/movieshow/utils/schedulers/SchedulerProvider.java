package com.example.hyc.movieshow.utils.schedulers;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hyc on 16-11-8.
 */

public class SchedulerProvider implements BaseSchedulersProvider
{
    private static SchedulerProvider INSTANCE;
    static Object obj = new Object();

    public static SchedulerProvider getInstance()
    {
        if (INSTANCE == null)
        {
            synchronized (obj)
            {
                if (INSTANCE == null)
                {
                    INSTANCE = new SchedulerProvider();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Scheduler io()
    {
        return Schedulers.io();
    }

    @Override
    public Scheduler worker()
    {
        return Schedulers.computation();
    }

    @Override
    public Scheduler ui()
    {
        return AndroidSchedulers.mainThread();
    }

    @Override
    public Scheduler newThread()
    {
        return Schedulers.newThread();
    }
}
