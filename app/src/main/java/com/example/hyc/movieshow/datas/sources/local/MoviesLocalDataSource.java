package com.example.hyc.movieshow.datas.sources.local;

import com.example.hyc.movieshow.datas.MovieModel;
import com.example.hyc.movieshow.datas.sources.MoviesDataSource;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmModel;

/**
 * Created by hyc on 16-11-5.
 */

public class MoviesLocalDataSource implements MoviesDataSource {

    private final Realm mRealm;

    @Override
    public Flowable<List<MovieModel>> getMovies() {
        return null;
    }

    @Override
    public Flowable<MovieModel> getMovie() {
        return null;
    }

    @Override
    public void refreshMovies() {

    }

    @Override
    public void loadMoreMovies() {

    }

    public MoviesLocalDataSource() {
        mRealm = Realm.getDefaultInstance();
    }


    @Override
    public void saveMovies(final List<MovieModel> movieModels) {
        Observable.fromIterable(movieModels).doOnNext(new Consumer<MovieModel>() {
            @Override
            public void accept(MovieModel movieModel) throws Exception {
//                mRealm.insertOrUpdate(movieModel);
            }
        });
    }

    @Override
    public void release() {
        mRealm.close();
    }
}
