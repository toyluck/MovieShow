package com.example.hyc.movieshow.Movies;

import android.support.annotation.NonNull;

import com.example.hyc.movieshow.datas.MovieModel;
import com.example.hyc.movieshow.datas.sources.MoviesRepository;
import com.example.hyc.movieshow.utils.EspressoIdingResource;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hyc on 16-11-5.
 * 电影页面的Presenter 层
 */

public class MoviesPresenter implements MoviesContract.Presenter
{

    private final MoviesContract.View mMovieView;
    private final MoviesRepository    mMoviesRepository;
    private       CompositeDisposable mSubscriptions;

    public MoviesPresenter(@NonNull MoviesRepository moviesRepository, MoviesContract.View view)
    {
        mMovieView = view;
        mMovieView.bindPresenter(this);
        mMoviesRepository = moviesRepository;
        mSubscriptions = new CompositeDisposable();
    }

    @Override
    public void subscribe()
    {
        loadTaks(false);
    }

    @Override
    public void unsubscribe()
    {

    }

    @Override
    public void setFiltering(MoviesFilterType type)
    {

    }

    @Override
    public MoviesFilterType getFiltering()
    {
        return null;
    }

    @Override
    public void loadTaks(boolean forceUpdate)
    {
        if (forceUpdate)
        {
            mMovieView.setLoadingIndicator(true);
        } else
        {
            mMoviesRepository.refreshMovies();
        }
        // 非静态
        EspressoIdingResource.increment();
        mSubscriptions.clear();

        Flowable<List<MovieModel>> movies = mMoviesRepository.getMovies();
        movies.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
//                subscribe(new Subscriber<List<MovieModel>>()
//            {
//                @Override
//                public void onSubscribe(Subscription s)
//                {
//                    System.out.println("MoviesPresenter.onSubscribe");
//                    System.out.println("s = [" + s + "]");
//                }
//
//                @Override
//                public void onNext(List<MovieModel> movieModels)
//                {
//                    System.out.println("movieModels = [" + movieModels + "]");
//                    System.out.println("MoviesPresenter.onNext");
//                    System.out.println("movieModels = " + movieModels);
//                }
//
//                @Override
//                public void onError(Throwable t)
//                {
//                    System.out.println("t.getMessage() = " + t.getMessage());
//                }
//
//                @Override
//                public void onComplete()
//                {
//                    System.out.println("MoviesPresenter.onComplete");
//                }
//            });
    single(new ArrayList<MovieModel>()).subscribe(new SingleObserver<List<MovieModel>>()
        {
            @Override
            public void onSubscribe(Disposable d)
            {

            }

            @Override
            public void onSuccess(List<MovieModel> value)
            {
                EspressoIdingResource.decrement();

                mMovieView.showMovies(value);
            }

            @Override
            public void onError(Throwable e)
            {

            }
        });


    }
}
