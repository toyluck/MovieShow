package com.example.hyc.movieshow.utils;

import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by hyc on 16-11-5.
 */

public class SimpleCountingIdingResource implements IdlingResource
{
    private final String mName;
    AtomicInteger mAtomicInteger = new AtomicInteger(0);
    private ResourceCallback mResourceCallback;

    public SimpleCountingIdingResource(String name)
    {
        mName = name;
    }

    @Override
    public String getName()
    {

        return mName;
    }

    public void increment()
    {
        mAtomicInteger.getAndIncrement();
    }

    public void decrement()
    {
        int i = mAtomicInteger.decrementAndGet();
        if (i < 0)
        {
            throw new IllegalArgumentException("Counter has been corrupted!");
        }
        if (i == 0)
        {
            if (null != mResourceCallback)
            {
                mResourceCallback.onTransitionToIdle();
            }
        }
    }

    @Override
    public boolean isIdleNow()
    {
        return mAtomicInteger.get() == 0;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback)
    {
        mResourceCallback = callback;
    }
}
