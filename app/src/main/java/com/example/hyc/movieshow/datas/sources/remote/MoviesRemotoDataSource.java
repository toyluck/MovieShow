package com.example.hyc.movieshow.datas.sources.remote;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v4.net.ConnectivityManagerCompat;

import com.example.hyc.movieshow.BuildConfig;
import com.example.hyc.movieshow.datas.MovieModel;
import com.example.hyc.movieshow.datas.sources.BaseModel;
import com.example.hyc.movieshow.datas.sources.MoviesDataSource;
import com.example.hyc.movieshow.datas.sources.RealmInt;
import com.example.hyc.movieshow.utils.FileUtils;
import com.example.hyc.movieshow.utils.GsonHelper;
import com.example.hyc.movieshow.utils.InternetUtils;
import com.example.hyc.movieshow.utils.StringConverterFactory;
import com.example.hyc.movieshow.utils.UIUtil;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hyc on 16-11-5.
 * 从网络拿取数据
 */

public class MoviesRemotoDataSource implements MoviesDataSource {

    public static final String BASE_URL = "https://api.themoviedb.org/";

    public static final int CACHE_SIZE = 1024 * 1024 * 20;//20mb

    private final MoviesService mMoviesService;
    private       MovieModel    _movieModel;

    public MoviesRemotoDataSource() {
        OkHttpClient           client             = new OkHttpClient();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        String storagePath = FileUtils.getExternalStoragePath(UIUtil.getContext());
        File   netCache    = new File(storagePath, "NetCache");
        Cache  cache       = new Cache(netCache, CACHE_SIZE);

        client = client.newBuilder()
                // log
                .addInterceptor(loggingInterceptor)
                // 数据格式化
//                .addInterceptor(new ConverIntercapter())
                // cache
                .addInterceptor(new CacheIntercapter()).cache(cache).build();

        Gson gson = GsonHelper.buildForRealm();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(StringConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        mMoviesService = retrofit.create(MoviesService.class);
    }


    private class CacheIntercapter implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!InternetUtils.isConnected()) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
            }
            Response response = chain.proceed(request);
            if (!InternetUtils.isConnected()) {
                int max = 3600 * 10;//10分钟
                // 服务器会返回一些无效的信息,需要清除请求头
                return response.newBuilder().removeHeader("Pragma")
                        // 这是list集合
                        .header("Cache-Control", "Public , Only-if-cached ,max-stale=" + max)
                        .build();
            }
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", request.cacheControl().toString())
                    .build();
        }
    }

    private class ConverIntercapter implements Interceptor {
        final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        Gson mGson = new Gson();

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request            request  = chain.request();
            Response           response = chain.proceed(request);
            final ResponseBody body     = response.body();
            BaseModel          model    = mGson.fromJson(body.string(), BaseModel.class);
            body.close();
            List results = model.getResults();
            String json = mGson.toJson(results, new TypeToken<List>() {
            }.getType());
            //java.lang.IllegalStateException: closed
            // reading the response body 2x? You can only call string() once.
//            return response.newBuilder().body(ResponseBody.create(JSON, model.toString())).build();
            return response.newBuilder().build();
        }
    }

    public MoviesService getMoviesService() {
        return mMoviesService;
    }

    @Override
    public Flowable<List<MovieModel>> getMovies(int page) {

        return mMoviesService.getMoviesFromOne(BuildConfig.Movie_Key, page)
                .map(new Function<MovieModel, List<MovieModel>>() {
                    @Override
                    public List<MovieModel> apply(MovieModel movieModel) throws Exception {
                        _movieModel = movieModel;
                        return movieModel.getResults();
                    }
                });
    }

    @Override
    public  MovieModel getMovie() {

        return _movieModel;
    }

    @Override
    public void refreshMovies() {
        /**
         * {@link com.example.hyc.movieshow.datas.sources.MoviesRepository} does it
         */
    }

    @Override
    public void loadMoreMovies() {

    }

    @Override
    public void saveMovies(List<MovieModel> movieModels) {

    }

    @Override
    public void release() {

    }
}
