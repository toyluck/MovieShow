package com.example.hyc.movieshow.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by hyc on 16-11-5.
 */

public class FragmentUtils
{

    public static void addFragmentToActivity(FragmentManager fragmentManager, Fragment fragment, @IdRes int id)
    {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        checkNotNull(id);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(id, fragment);
        transaction.commitAllowingStateLoss();

    }

    public static Fragment switchFragment(FragmentManager fragmentManager, Fragment from, Fragment to,
                                          @IdRes int container)
    {
        checkNotNull(fragmentManager);
        checkNotNull(from);
        checkNotNull(to);

        if (from == to) return from;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (!to.isAdded())
        {
            transaction.hide(from).add(container, to);
        } else
        {
            transaction.hide(from).show(to);
        }

        transaction.commitAllowingStateLoss();

        return to;

    }
}
