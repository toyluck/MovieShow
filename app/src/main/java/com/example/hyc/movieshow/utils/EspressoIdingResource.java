package com.example.hyc.movieshow.utils;

import android.support.test.espresso.IdlingResource;

/**
 *
 * Created by hyc on 16-11-5.
 */

public class EspressoIdingResource
{
    private static final String RESOURCE = "GLOBAL";

    private static SimpleCountingIdingResource mCountingIdlingResource =
        new SimpleCountingIdingResource(RESOURCE);

    public static void increment() {
        mCountingIdlingResource.increment();
    }

    public static void decrement() {
        mCountingIdlingResource.decrement();
    }

    public static IdlingResource getIdlingResource() {
        return mCountingIdlingResource;
    }
}
