package com.example.hyc.movieshow.datas.sources.remote;

import com.example.hyc.movieshow.datas.MovieModel;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by hyc on 16-11-5.
 *
 */

public interface MoviesService
{
    @Headers("Cache-Control:public ,max-age=3600")
    @GET("/3/movie/popular")
    Flowable<List<MovieModel>> getMovies(@Query("api_key") String apikey, @Query("page") int page);
}
