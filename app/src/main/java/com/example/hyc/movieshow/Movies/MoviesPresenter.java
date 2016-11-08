package com.example.hyc.movieshow.Movies;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.hyc.movieshow.datas.MovieModel;
import com.example.hyc.movieshow.datas.sources.MoviesRepository;
import com.example.hyc.movieshow.utils.EspressoIdingResource;
import com.example.hyc.movieshow.utils.schedulers.BaseSchedulersProvider;
import com.example.hyc.movieshow.utils.schedulers.SchedulerProvider;
import com.squareup.haha.guava.collect.Collections2;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.realm.Sort;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by hyc on 16-11-5.
 * 电影页面的Presenter 层
 */

public class MoviesPresenter implements MoviesContract.Presenter
{

    private final MoviesContract.View    mMovieView;
    private final MoviesRepository       mMoviesRepository;
    private final BaseSchedulersProvider mSchedulersProvider;
    private       CompositeDisposable    mSubscriptions;

    public MoviesPresenter(@NonNull MoviesRepository moviesRepository, MoviesContract.View view,
                           @NonNull BaseSchedulersProvider
                               schedulerProvider)
    {
        mMovieView = view;
        mMovieView.bindPresenter(this);
        mMoviesRepository = moviesRepository;
        mSchedulersProvider = schedulerProvider;
        mSubscriptions = new CompositeDisposable();
    }

    @Override
    public void subscribe()
    {
        loadTaks(true);
    }

    @Override
    public void unsubscribe()
    {
        mSubscriptions.clear();
    }

    private MoviesFilterType mFilterType = MoviesFilterType.DefaultType;

    @Override
    public void setFiltering(MoviesFilterType type)
    {
        this.mFilterType = type;
    }

    @Override
    public MoviesFilterType getFiltering()
    {
        return null;
    }

    @Override
    public void loadTaks(boolean forceUpdate)
    {
        int page = 1;
        if (forceUpdate)
        {
            mMoviesRepository.refreshMovies();
        } else
        {
            page = mMoviesRepository.getMovie().getPage();
            mMovieView.setLoadingIndicator(true);
        }
        // 非静态
        mSubscriptions.clear();

        Flowable<List<MovieModel>> movies = mMoviesRepository.getMovies(page);
        movies.subscribeOn(mSchedulersProvider.newThread())
            .single(new ArrayList<MovieModel>())
            .map(new Function<List<MovieModel>,
                List<MovieModel>>()
            {
                @Override
                public List<MovieModel> apply(List<MovieModel> movieModels) throws Exception
                {
                    switch (mFilterType)
                    {
                        case Hot:
                            Collections.sort(movieModels, new Comparator<MovieModel>()
                            {
                                @Override
                                public int compare(MovieModel o1, MovieModel o2)
                                {
                                    return (int) (o1.getPopularity() - o2.getPopularity());
                                }
                            });
                            break;
                        case Loved:
                            Collections.sort(movieModels, new Comparator<MovieModel>()
                            {
                                @Override
                                public int compare(MovieModel o1, MovieModel o2)
                                {

                                    return o1.getVote_count() - o2.getVote_count();
                                }
                            });
                            break;
                        case DefaultType:
                    }
                    return movieModels;
                }
            })
            .observeOn(mSchedulersProvider.ui())
            .subscribe(new SingleObserver<List<MovieModel>>()
            {
                @Override
                public void onSubscribe(Disposable d)
                {
                    System.out.println("d = " + d);
                    EspressoIdingResource.increment();
                    mMovieView.setLoadingIndicator(true);
                }

                @Override
                public void onSuccess(List<MovieModel> value)
                {
                    EspressoIdingResource.decrement();

                    mMovieView.showMovies(value);
                    mMovieView.setLoadingIndicator(false);
                }

                @Override
                public void onError(Throwable e)
                {
                    System.err.println(e.getMessage() + "-----");
                    mMovieView.showLoadingMoviesError();
                }
            });


    }

    @Override
    public void openMovieDetail(MovieModel model, int position)
    {
        checkNotNull(model);
        mMovieView.showMovieDetailUi(model,position);
    }
}
