package com.example.hyc.movieshow.moviesdetail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hyc.movieshow.R;
import com.example.hyc.movieshow.ScrollingActivity;
import com.example.hyc.movieshow.databinding.ActTemplateBinding;
import com.example.hyc.movieshow.datas.MovieModel;
import com.example.hyc.movieshow.utils.FragmentUtils;


/**
 * Created by hyc on 16-11-7.
 */

public class MovieDetailActivity extends AppCompatActivity
{

    private ButtonBarLayout         mBtnbLayout;
    private CollapsingToolbarLayout mCollaToolbar;
    private MovieDetailFragment     mMovieDetailFragment;
    private ImageView               mImgPlay;
    private TextView                mTvToolTitle;
    private MovieModel              mMovieModel;
    private Toolbar                 mToolBar;

    public static void start(Context context, MovieModel model)
    {
        Intent starter = new Intent(context, MovieDetailActivity.class);
        starter.putExtra("MovieModel", model);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ActTemplateBinding dataBinding = DataBindingUtil.setContentView(this, R.layout.act_template);
        mBtnbLayout = dataBinding.btnbLayout;
        mCollaToolbar = dataBinding.collaToolbar;
        mImgPlay = dataBinding.imgPlay;
        mTvToolTitle = dataBinding.tvToolTitle;
        mToolBar = dataBinding.toolBar;
        Intent intent = getIntent();
        mMovieModel = intent.getParcelableExtra("MovieModel");
        dataBinding.setModel(mMovieModel);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mMovieDetailFragment = (MovieDetailFragment) fragmentManager.findFragmentById(R.id
            .act_template_frame);
        if (mMovieDetailFragment == null)
        {
            mMovieDetailFragment = MovieDetailFragment.newInstance(mMovieModel);
            FragmentUtils.addFragmentToActivity(fragmentManager, mMovieDetailFragment, R.id.act_template_frame);
        }

        addActBarListener(dataBinding);
        initToolbar();
    }

    private void initToolbar()
    {
        mCollaToolbar.setTitle(mMovieModel.getTitle());
        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mCollaToolbar.setTitleEnabled(true);
        mCollaToolbar.setCollapsedTitleTextColor(Color.RED);

        actionBar.setTitle(mMovieModel.getTitle());

    }


    private void addActBarListener(final ActTemplateBinding actTemplateBinding)
    {
        if (actTemplateBinding == null) return;
        final AppBarLayout appbarLayout = actTemplateBinding.appbarLayout;

        appbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener()
        {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset)
            {
                int totalScrollRange = appBarLayout.getTotalScrollRange();
                Log.d(TAG, totalScrollRange + "onOffsetChanged: " + verticalOffset);

                int offset = Math.abs(verticalOffset);
                if (offset == 0)
                {

                    changeStatus(AppBarOffSetType.EXPANDED);
                } else if (offset >= appbarLayout.getTotalScrollRange())
                {
                    changeStatus(AppBarOffSetType.COLLAPSED);
                } else
                {

                    changeStatus(AppBarOffSetType.SCROOLING);
                }

            }
        });
        mBtnbLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                appbarLayout.performClick();

            }
        });

        appbarLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                appbarLayout.setExpanded(true, true);
                changeStatus(AppBarOffSetType.EXPANDED);
                mMovieDetailFragment.scrollToTop(true);
            }
        });
    }

    private AppBarOffSetType mCurrentType;

    private void changeStatus(AppBarOffSetType type)
    {
        mCurrentType = type;

        switch (type)
        {

            case COLLAPSED:
//                mBtnbLayout.setVisibility(View.VISIBLE);
                mCollaToolbar.setTitle("COLLAPSED");
                mImgPlay.setVisibility(View.VISIBLE);
                mTvToolTitle.setText(getString(R.string.play));
                break;

            case SCROOLING:
                mImgPlay.setVisibility(View.GONE);
                mCollaToolbar.setTitle("SCROOLING");
                mTvToolTitle.setText(mMovieModel.getTitle());
                break;

            case EXPANDED:
                mCollaToolbar.setTitle("EXPANDED");
                mImgPlay.setVisibility(View.GONE);
                mTvToolTitle.setText(mMovieModel.getTitle());

                break;
        }

    }


    private static final String TAG = "MovieDetailActivity";
}
