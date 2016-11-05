package com.example.hyc.movieshow.datas.sources;

import com.example.hyc.movieshow.datas.MovieModel;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by hyc on 16-11-5.
 */

public interface MoviesDataSource
{
    Flowable<List<MovieModel>> getMovies();

    Flowable<MovieModel> getMovie();

    void refreshMovies();

    void loadMoreMovies();
}
