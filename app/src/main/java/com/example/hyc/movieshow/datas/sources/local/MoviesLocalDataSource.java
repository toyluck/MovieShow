package com.example.hyc.movieshow.datas.sources.local;

import android.os.Looper;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.example.hyc.movieshow.App;
import com.example.hyc.movieshow.datas.MovieModel;
import com.example.hyc.movieshow.datas.sources.MoviesDataSource;

import org.antlr.v4.codegen.model.Loop;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmResults;

import static java.lang.Math.E;

/**
 * Created by hyc on 16-11-5.
 */

public class MoviesLocalDataSource implements MoviesDataSource {

//    private Realm mRealm;

    @WorkerThread
    @Override
    public Flowable<List<MovieModel>> getMovies(final int page) {

        return getCurrentThreadRealm().concatMap(new Function<Realm, Publisher<List<MovieModel>>>() {
            @Override
            public Publisher<List<MovieModel>> apply(Realm realm) throws Exception {

                RealmResults<MovieModel> movieModels = realm.where(MovieModel.class).lessThanOrEqualTo
                        ("page", page).findAll();
                List<MovieModel> models = new ArrayList<>();
                for (MovieModel model : movieModels) {
                    models.add(model);
                }
                System.out.println("models = " + models);
                return Flowable.fromArray(models).doOnNext(new Consumer<List<MovieModel>>() {
                    @Override
                    public void accept(List<MovieModel> movieModels) throws Exception {
                        System.out.println("MoviesLocalDataSource.accept");
                    }
                });
            }
        });
//
//        return Flowable.just(page).map(new Function<Integer, List<MovieModel>>() {
//            @Override
//            public List<MovieModel> apply(Integer integer) throws Exception {
//                System.out.println("Thread.currentThread()  apply= " + Thread.currentThread());
//
//
//                RealmResults<MovieModel> movieModels = mRealm.where(MovieModel.class).lessThanOrEqualTo
//                        ("page", integer).findAll();
//
//                List<MovieModel> models = new ArrayList<>();
//                for (MovieModel model : movieModels) {
//                    models.add(model);
//                }
////                mRealm.close();
//                return models;
//
//            }
//        }).doOnSubscribe(new Consumer<Subscription>() {
//            @Override
//            public void accept(Subscription subscription) throws Exception {
//                System.out.println("Thread.currentThread()  subscription= " + Thread.currentThread());
//                mRealm = Realm.getInstance(App.getConfiguration());
//            }
//        }).doOnComplete(new Action() {
//            @Override
//            public void run() throws Exception {
//                mRealm.close();
//            }
//        });
    }

    public static Flowable<Realm> getCurrentThreadRealm() {
        return Flowable.create(new FlowableOnSubscribe<Realm>() {
            @Override
            public void subscribe(FlowableEmitter<Realm> e) throws Exception {
                final Realm realm = Realm.getDefaultInstance();
//                e.setDisposable(new Disposable() {
//                    @Override
//                    public void dispose() {
//                        realm.close();
//                    }
//
//                    @Override
//                    public boolean isDisposed() {
//                        return false;
//                    }
//                });

                e.onNext(realm);
            }
        }, BackpressureStrategy.BUFFER);
    }

    private Realm geneRealm() {
//        if (mRealm == null || mRealm.isClosed()) {
//            mRealm = Realm.getDefaultInstance();
//        }
//        return mRealm;
        return null;
    }

    @Override
    public MovieModel getMovie() {

        return null;
    }

    @Override
    public void refreshMovies() {

    }

    @Override
    public void loadMoreMovies() {

    }

    public MoviesLocalDataSource() {
        geneRealm();
    }


    @Override
    @WorkerThread
    public void saveMovies(final List<MovieModel> movieModels) {
        final Realm realm = Realm.getInstance(App.getConfiguration());
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(final Realm realm) {
                Observable.fromIterable(movieModels).doOnNext(new Consumer<MovieModel>() {
                    @Override
                    public void accept(MovieModel movieModel) throws Exception {
                        realm.insertOrUpdate(movieModel);
                    }
                }).doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
//                        geneRealm();
                    }
                }).doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        realm.close();
                        Log.d("Movie", "Realm Close()" + Thread.currentThread());
                    }
                }).subscribe();
            }
        });
    }

    @Override
    public void release() {

    }
}
