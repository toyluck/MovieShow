package com.example.hyc.movieshow;

import com.example.hyc.movieshow.Movies.MoviesContract;
import com.example.hyc.movieshow.Movies.MoviesFilterType;
import com.example.hyc.movieshow.Movies.MoviesPresenter;
import com.example.hyc.movieshow.datas.MovieModel;
import com.example.hyc.movieshow.datas.sources.MoviesRepository;
import com.example.hyc.movieshow.utils.schedulers.BaseSchedulersProvider;
import com.example.hyc.movieshow.utils.schedulers.SchedulerProvider;
import com.example.hyc.movieshow.utils.schedulers.TrampolineSchedulerProvider;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import io.reactivex.Flowable;

import static com.example.hyc.movieshow.utils.GsonHelper.buildForRealm;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by hyc on 16-11-8.
 */

public class MoviesPresenterTest
{
    @Mock
    private MoviesContract.View mMoviesView;

    @Mock
    private MoviesRepository mMoviesRepository;
    private MoviesPresenter  mMoviesPresenter;

    Gson mGson;
    private List<MovieModel>       MOVIES;
    private BaseSchedulersProvider mSchedulerProvider;

    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        mGson = buildForRealm();
        mSchedulerProvider = new TrampolineSchedulerProvider();

        mMoviesPresenter = new MoviesPresenter(mMoviesRepository, mMoviesView, mSchedulerProvider);

        when(mMoviesView.isActive()).thenReturn(true);

        MOVIES = mGson.fromJson("[{\"poster_path\": \"/xfWac8MTYDxujaxgPVcRD9yZaul.jpg\",\n" +
            "            \"adult\": false,\n" +
            "            \"overview\": \"After his career is destroyed, a brilliant but arrogant surgeon gets a new lease on life when a sorcerer takes him under his wing and trains him to defend the world against evil.\",\n" +
            "            \"release_date\": \"2016-10-25\",\n" +
            "            \"genre_ids\": [\n" +
            "                28,\n" +
            "                12,\n" +
            "                14,\n" +
            "                878\n" +
            "            ],\n" +
            "            \"id\": 284052,\n" +
            "            \"original_title\": \"Doctor Strange\",\n" +
            "            \"original_language\": \"en\",\n" +
            "            \"title\": \"Doctor Strange\",\n" +
            "            \"backdrop_path\": \"/hETu6AxKsWAS42tw8eXgLUgn4Lo.jpg\",\n" +
            "            \"popularity\": 72.035408,\n" +
            "            \"vote_count\": 536,\n" +
            "            \"video\": false,\n" +
            "            \"vote_average\": 7\n" +
            "        },\n" +
            "        {\n" +
            "            \"poster_path\": \"/mLrQMqyZgLeP8FrT5LCobKAiqmK.jpg\",\n" +
            "            \"adult\": false,\n" +
            "            \"overview\": \"The USS Enterprise crew explores the furthest reaches of uncharted space, where they encounter a mysterious new enemy who puts them and everything the Federation stands for to the test.\",\n" +
            "            \"release_date\": \"2016-07-07\",\n" +
            "            \"genre_ids\": [\n" +
            "                28,\n" +
            "                12,\n" +
            "                878,\n" +
            "                53\n" +
            "            ],\n" +
            "            \"id\": 188927,\n" +
            "            \"original_title\": \"Star Trek Beyond\",\n" +
            "            \"original_language\": \"en\",\n" +
            "            \"title\": \"Star Trek Beyond\",\n" +
            "            \"backdrop_path\": \"/6uBlEXZCUHM15UNZqNig17VdN4m.jpg\",\n" +
            "            \"popularity\": 29.184544,\n" +
            "            \"vote_count\": 1063,\n" +
            "            \"video\": false,\n" +
            "            \"vote_average\": 6.33\n" +
            "        },\n" +
            "        {\n" +
            "            \"poster_path\": \"/z09QAf8WbZncbitewNk6lKYMZsh.jpg\",\n" +
            "            \"adult\": false,\n" +
            "            \"overview\": \"\\\"Finding Dory\\\" reunites Dory with friends Nemo and Marlin on a search for answers about her past. What can she remember? Who are her parents? And where did she learn to speak Whale?\",\n" +
            "            \"release_date\": \"2016-06-16\",\n" +
            "            \"genre_ids\": [\n" +
            "                16,\n" +
            "                10751\n" +
            "            ],\n" +
            "            \"id\": 127380,\n" +
            "            \"original_title\": \"Finding Dory\",\n" +
            "            \"original_language\": \"en\",\n" +
            "            \"title\": \"Finding Dory\",\n" +
            "            \"backdrop_path\": \"/iWRKYHTFlsrxQtfQqFOQyceL83P.jpg\",\n" +
            "            \"popularity\": 29.013015,\n" +
            "            \"vote_count\": 1253,\n" +
            "            \"video\": false,\n" +
            "            \"vote_average\": 6.67\n" +
            "        }]", new TypeToken<List<MovieModel>>()
        {
        }.getType());
    }

    @Test
    public void loadAllMoviesFromRepositoryAndLoadInfoView()
    {
        when(mMoviesRepository.getMovies(1)).thenReturn(Flowable.<List<MovieModel>>fromArray(MOVIES));
        mMoviesPresenter.setFiltering(MoviesFilterType.DefaultType);
        mMoviesPresenter.loadTaks(true);

        Mockito.verify(mMoviesView).setLoadingIndicator(true);
        Mockito.verify(mMoviesView).setLoadingIndicator(false);
    }


    @Test
    public void loadSortHotMoviesFromRepositoryAndLoadInfoView()
    {
        when(mMoviesRepository.getMovies(1)).thenReturn(Flowable.<List<MovieModel>>fromArray(MOVIES));
        mMoviesPresenter.setFiltering(MoviesFilterType.Hot);
        mMoviesPresenter.loadTaks(true);

        Mockito.verify(mMoviesView).setLoadingIndicator(true);
        Mockito.verify(mMoviesView).setLoadingIndicator(false);
    }

    @Test
    public void loadSortLovedMoviesFromRepositoryAndLoadInfoView()
    {
        when(mMoviesRepository.getMovies(1)).thenReturn(Flowable.<List<MovieModel>>fromArray(MOVIES));
        mMoviesPresenter.setFiltering(MoviesFilterType.Loved);
        mMoviesPresenter.loadTaks(true);

        Mockito.verify(mMoviesView).setLoadingIndicator(true);
        Mockito.verify(mMoviesView).setLoadingIndicator(false);
    }

    @Test
    public void errorLoadingMovies_ShowsError()
    {
        when(mMoviesRepository.getMovies(1)).thenReturn(Flowable.<List<MovieModel>>error(new Exception()));
        mMoviesPresenter.setFiltering(MoviesFilterType.DefaultType);
        mMoviesPresenter.loadTaks(true);

        verify(mMoviesView).showLoadingMoviesError();
    }

    @Test
    public void clickOnMovie_showDetailUi()
    {
        MovieModel model = MOVIES.get(0);

        mMoviesPresenter.openMovieDetail(model, 0);

        verify(mMoviesView).showMovieDetailUi(any(MovieModel.class), any(Integer.class));

    }
}
