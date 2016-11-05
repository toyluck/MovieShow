package com.example.hyc.movieshow.Movies;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.hyc.movieshow.R;
import com.example.hyc.movieshow.datas.sources.MoviesRepository;
import com.example.hyc.movieshow.datas.sources.local.MoviesLocalDataSource;
import com.example.hyc.movieshow.datas.sources.remote.MoviesRemotoDataSource;
import com.example.hyc.movieshow.utils.FragmentUtils;

public class MoviesActivity extends AppCompatActivity
{

    public static final int    FRAME               = R.id.activity_main_frame;
    public static final String CURRENT_FILETER_KEY = "current_filter_key";
    private MoviesPresenter mMoviesPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DataBindingUtil.setContentView(this, R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        findViewById(R.id.activity_main_frame);

        FragmentManager fragmentManager = getSupportFragmentManager();
        MoviesFragment  fragment        = (MoviesFragment) fragmentManager.findFragmentById(FRAME);

        if (fragment == null)
        {
            fragment = MoviesFragment.newInstance();
            FragmentUtils.addFragmentToActivity(fragmentManager, fragment, FRAME);
        }

        mMoviesPresenter = new MoviesPresenter(MoviesRepository.newInstance(new MoviesRemotoDataSource(),
            new MoviesLocalDataSource()), fragment);

        if (savedInstanceState != null)
        {
            MoviesFilterType currentFiltering = (MoviesFilterType) savedInstanceState.getSerializable
                (CURRENT_FILETER_KEY);
            mMoviesPresenter.setFiltering(currentFiltering);
        }

    }

}
