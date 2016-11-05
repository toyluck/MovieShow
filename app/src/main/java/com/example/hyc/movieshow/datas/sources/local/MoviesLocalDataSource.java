package com.example.hyc.movieshow.datas.sources.local;

import com.example.hyc.movieshow.datas.MovieModel;
import com.example.hyc.movieshow.datas.sources.MoviesDataSource;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by hyc on 16-11-5.
 */

public class MoviesLocalDataSource implements MoviesDataSource
{
    @Override
    public Flowable<List<MovieModel>> getMovies()
    {
        return null;
    }

    @Override
    public Flowable<MovieModel> getMovie()
    {
        return null;
    }

    @Override
    public void refreshMovies()
    {

    }

    @Override
    public void loadMoreMovies()
    {

    }
}
