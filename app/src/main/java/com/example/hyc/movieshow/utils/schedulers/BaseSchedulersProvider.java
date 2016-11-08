package com.example.hyc.movieshow.utils.schedulers;

import io.reactivex.Scheduler;

/**
 * Created by hyc on 16-11-8.
 */

public interface BaseSchedulersProvider
{
    Scheduler io();
    Scheduler worker();
    Scheduler ui();
    Scheduler newThread();
}
