package com.example.hyc.movieshow.datas.sources.local;

import android.support.annotation.WorkerThread;
import android.util.Log;

import com.example.hyc.movieshow.App;
import com.example.hyc.movieshow.datas.MovieModel;
import com.example.hyc.movieshow.datas.sources.MoviesDataSource;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.hyc.movieshow.utils.RealmHelper.getCurrentThreadRealm;

/**
 * Created by hyc on 16-11-5.
 */

public class MoviesLocalDataSource implements MoviesDataSource
{

//    private Realm mRealm;

    @WorkerThread
    @Override
    public Flowable<List<MovieModel>> getMovies(final int page)
    {
        //todo doesn't work
        return getCurrentThreadRealm().concatMap(new Function<Realm, Publisher<List<MovieModel>>>()
        {
            @Override
            public Publisher<List<MovieModel>> apply(Realm realm) throws Exception
            {
                RealmResults<MovieModel> movieModels = realm.where(MovieModel.class).lessThanOrEqualTo
                    ("page", page).findAll();
                final List<MovieModel> models = new ArrayList<>();
                for (MovieModel model : movieModels)
                {
                    models.add(model);
                }
                System.out.println("models = " + models);
//                realm.close();
                return Flowable.fromArray(models).doOnError(new Consumer<Throwable>()
                {
                    @Override
                    public void accept(Throwable throwable) throws Exception
                    {
                        Log.e(TAG, throwable.getMessage());
                    }
                }).doOnComplete(new Action()
                {
                    @Override
                    public void run() throws Exception
                    {
                        Log.e(TAG, "run: Complete");
                    }
                });
            }
        }).doOnNext(new Consumer<List<MovieModel>>()
        {
            @Override
            public void accept(List<MovieModel> movieModels) throws Exception
            {
                Log.e(TAG, "22accept:11 +" + movieModels.toString());
            }
        });
    }

    private static final String TAG = "MoviesLocalDataSource";


    @Override
    public MovieModel getMovie()
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

    public MoviesLocalDataSource()
    {

    }


    @Override
    @WorkerThread
    public void saveMovies(final List<MovieModel> movieModels)
    {
        final Realm realm = Realm.getInstance(App.getConfiguration());
        realm.executeTransactionAsync(new Realm.Transaction()
        {
            @Override
            public void execute(final Realm realm)
            {
                Observable.fromIterable(movieModels).doOnNext(new Consumer<MovieModel>()
                {
                    @Override
                    public void accept(MovieModel movieModel) throws Exception
                    {
                        realm.insertOrUpdate(movieModel);
                    }
                }).doOnTerminate(new Action()
                {
                    @Override
                    public void run() throws Exception
                    {
                        realm.close();
                        Log.d("Movie", "Realm Close()" + Thread.currentThread());
                    }
                }).subscribe();
            }
        });
    }

    @Override
    public void release()
    {

    }
}
