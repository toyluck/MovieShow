package com.example.hyc.movieshow.utils.schedulers;

import android.provider.BaseColumns;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hyc on 16-11-8.
 */

public class TrampolineSchedulerProvider implements BaseSchedulersProvider
{
    @Override
    public Scheduler io()
    {
        return Schedulers.trampoline();
    }

    @Override
    public Scheduler worker()
    {
        return Schedulers.trampoline();

    }

    @Override
    public Scheduler ui()
    {
        return Schedulers.trampoline();

    }

    @Override
    public Scheduler newThread()
    {
        return Schedulers.trampoline();

    }
}
