package com.example.hyc.movieshow.datas.sources;

import android.support.annotation.NonNull;

import com.example.hyc.movieshow.datas.MovieModel;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

/**
 * Created by hyc on 16-11-5.
 * 用来处理 本地和 网络数据
 */

public class MoviesRepository implements MoviesDataSource
{

    public static MoviesRepository INSTANCE;
    private final MoviesDataSource mRemoteDataSouce;
    private final MoviesDataSource mLocalDataSouce;
    private       boolean          mCacheIsDirty;
    private LinkedHashMap<Integer, MovieModel> mMovieCached = new LinkedHashMap<>();

    private MoviesRepository(@NonNull MoviesDataSource moveisRemoteDataSouce, @NonNull MoviesDataSource
        moviesLocalDataSouce)
    {
        mRemoteDataSouce = moveisRemoteDataSouce;
        mLocalDataSouce = moviesLocalDataSouce;
    }

    public static MoviesRepository newInstance(@NonNull MoviesDataSource moveisRemoteDataSouce,
                                               @NonNull MoviesDataSource moviesLocalDataSouce)
    {
        if (INSTANCE == null)
        {
            INSTANCE = new MoviesRepository(moveisRemoteDataSouce, moviesLocalDataSouce);
        }

        return INSTANCE;
    }

    public static void destroyInstance()
    {
        INSTANCE = null;
    }

    @Override
    public Flowable<List<MovieModel>> getMovies(int page)
    {
        if (mMovieCached != null && !mCacheIsDirty)
        {
//            return getAndSaveMomeMovies(page);
            return Flowable.fromIterable(mMovieCached.values()).toList().toFlowable();
        }
        Flowable<List<MovieModel>> remoteMovies = getAndSaveRemoteMovies(page);
        if (mCacheIsDirty)
        {
            return remoteMovies;
        } else
        {

            return getAndSaveMomeMovies(page);
        }


    }

    private Flowable<List<MovieModel>> getAndSaveMomeMovies(int page)
    {

        return mLocalDataSouce.getMovies(page);
    }


    private Flowable<List<MovieModel>> getAndSaveRemoteMovies(int page)
    {
        return mRemoteDataSouce.getMovies(page).map(new Function<List<MovieModel>, List<MovieModel>>()
        {
            @Override
            public List<MovieModel> apply(List<MovieModel> movieModels) throws Exception
            {
                saveMovies(movieModels);
                for (MovieModel movieModel : movieModels)
                {
                    mMovieCached.put(movieModel.getId(), movieModel);
                }
                return movieModels;
            }
        }).doOnComplete(new Action()
        {
            @Override
            public void run() throws Exception
            {
                mCacheIsDirty = false;
            }
        });

    }

    @Override
    public MovieModel getMovie()
    {

        return mRemoteDataSouce.getMovie() == null ? mLocalDataSouce.getMovie() : mRemoteDataSouce.getMovie();
    }

    @Override
    public void refreshMovies()
    {
        // 缓存被污染
        mCacheIsDirty = true;
    }

    @Override
    public void loadMoreMovies()
    {

    }

    @Override
    public void saveMovies(List<MovieModel> movieModels)
    {
        //保存
        mLocalDataSouce.saveMovies(movieModels);
    }

    @Override
    public void release()
    {

    }
}
