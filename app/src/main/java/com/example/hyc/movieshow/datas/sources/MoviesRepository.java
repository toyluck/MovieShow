package com.example.hyc.movieshow.datas.sources;

import android.support.annotation.NonNull;

import com.example.hyc.movieshow.datas.MovieModel;

import org.reactivestreams.Publisher;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hyc on 16-11-5.
 * 用来处理 本地和 网络数据
 */

public class MoviesRepository implements MoviesDataSource {

    public static MoviesRepository INSTANCE;
    private final MoviesDataSource mRemoteDataSouce;
    private final MoviesDataSource mLocalDataSouce;
    private       boolean          mCacheIsDirty;

    private MoviesRepository(@NonNull MoviesDataSource moveisRemoteDataSouce, @NonNull MoviesDataSource
            moviesLocalDataSouce) {
        mRemoteDataSouce = moveisRemoteDataSouce;
        mLocalDataSouce = moviesLocalDataSouce;
    }

    public static MoviesRepository newInstance(@NonNull MoviesDataSource moveisRemoteDataSouce,
                                               @NonNull MoviesDataSource moviesLocalDataSouce) {
        if (INSTANCE == null) {
            INSTANCE = new MoviesRepository(moveisRemoteDataSouce, moviesLocalDataSouce);
        }

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Flowable<List<MovieModel>> getMovies(int page) {

        Flowable<List<MovieModel>> remoteMovies = getAndSaveRemoteMovies(page);
        if (!mCacheIsDirty) {
            return remoteMovies;
        } else {
            //
            return getAndSaveMomeMovies(page);
        }


    }

    private Flowable<List<MovieModel>> getAndSaveMomeMovies(int page) {

        return mLocalDataSouce.getMovies(page);
    }


    //
    private Flowable<List<MovieModel>> getAndSaveRemoteMovies(int page) {
        return mRemoteDataSouce.getMovies(page).flatMap(new Function<List<MovieModel>,
                Publisher<List<MovieModel>>>() {
            @Override
            public Publisher<List<MovieModel>> apply(List<MovieModel> movieModels) throws Exception {
                saveMovies(movieModels);
                Flowable<MovieModel> next = Flowable.fromIterable(movieModels)
                        .doOnNext(new Consumer<MovieModel>() {
                            @Override
                            public void accept(MovieModel movieModel) throws Exception {
                                // save data to local);

                            }
                        });
                Single<List<MovieModel>> single = next.toList();

                return single.toFlowable();
            }
        }).doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
                mCacheIsDirty = false;
            }
        });

    }

    @Override
    public  MovieModel getMovie() {

        return mRemoteDataSouce.getMovie()==null?mLocalDataSouce.getMovie():mRemoteDataSouce.getMovie();
    }

    @Override
    public void refreshMovies() {
        // 缓存被污染
        mCacheIsDirty = true;
    }

    @Override
    public void loadMoreMovies() {

    }

    @Override
    public void saveMovies(List<MovieModel> movieModels) {
        //保存
        mLocalDataSouce.saveMovies(movieModels);
    }

    @Override
    public void release() {

    }
}
