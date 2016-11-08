package com.example.hyc.movieshow.Movies;

import android.graphics.Movie;

import com.example.hyc.movieshow.BasePresenter;
import com.example.hyc.movieshow.BaseView;
import com.example.hyc.movieshow.datas.MovieModel;

import java.util.List;

/**
 * Created by hyc on 16-11-5.
 * 我的电影页面接口
 */

public interface MoviesContract
{
    interface View extends BaseView<Presenter>
    {

        void setLoadingIndicator(boolean showLoadingUi);

        void showMovies(List<MovieModel> movies);

        void showLoadingMoviesError();

        boolean isActive();

        void showFiltingPopUpMenu();


        void showMovieDetailUi(MovieModel model, int position);
    }

    interface Presenter extends BasePresenter
    {
        void setFiltering(MoviesFilterType type);

        MoviesFilterType getFiltering();

        void loadTaks(boolean forceUpdate);

        void openMovieDetail(MovieModel model, int position);
    }


}
