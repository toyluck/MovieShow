package com.example.hyc.movieshow;

/**
 * Created by hyc on 16-11-5.
 *
 */

public interface BaseView<T>
{
    void bindPresenter(T presenter);
}
