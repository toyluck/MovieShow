package com.example.hyc.movieshow;

import android.graphics.Movie;

import java.util.List;

/**
 * Created by hyc on 16-11-5.
 * 开发计划
 */

public class ScheduClass

{
    //分析需求

    /**
     * 1 . 主页面
     *    保存数据
     *    横版模式
     *
     */

    interface MoviesFragment
    {
        // 获取
        List<Movie> getMovies();

        //  展示
        void showMovies();

        // 排序
        void Sort();

        // 跳转
        void click();

    }

}
