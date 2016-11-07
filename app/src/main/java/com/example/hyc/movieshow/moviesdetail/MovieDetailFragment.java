package com.example.hyc.movieshow.moviesdetail;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.hyc.movieshow.R;
import com.example.hyc.movieshow.databinding.FragMovieDetailBinding;
import com.example.hyc.movieshow.databinding.FragMoviesBinding;
import com.example.hyc.movieshow.datas.MovieModel;
import com.example.hyc.movieshow.utils.FragmentUtils;

/**
 * Created by hyc on 16-11-7.
 */

public class MovieDetailFragment extends Fragment implements RadioGroup.OnCheckedChangeListener
{

    private FragMovieDetailBinding mMovieDetailBinding;

    public static MovieDetailFragment newInstance(MovieModel movieModel)
    {

        Bundle args = new Bundle();
        args.putParcelable(MovieModel.KEY, movieModel);
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mMovieDetailBinding = FragMovieDetailBinding.inflate(inflater, container, false);
        init();
        return mMovieDetailBinding.getRoot();
    }

    private void init()
    {
        mMovieDetailBinding.rgMovieDetail.setOnCheckedChangeListener(this);
        MovieModel model = getArguments().getParcelable(MovieModel.KEY);
        FragmentUtils.addFragmentToActivity(getChildFragmentManager(), InfoFragment.newInstance(model), R.id
            .frame_movie_detail);
    }

    public void scrollToTop(boolean toTop)
    {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        switch (checkedId)
        {
            case R.id.rb_info:
                // 信息fragment
                break;
            case R.id.rb_relate:
                // relate fragment

                break;
        }
    }
}
