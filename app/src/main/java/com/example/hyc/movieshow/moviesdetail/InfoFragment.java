package com.example.hyc.movieshow.moviesdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hyc.movieshow.databinding.FragMovieInfoBinding;
import com.example.hyc.movieshow.datas.MovieModel;

/**
 * Created by hyc on 16-11-7.
 */

public class InfoFragment extends Fragment
{

    private FragMovieInfoBinding mMovieInfoBinding;


    public static InfoFragment newInstance(MovieModel model)
    {

        Bundle args = new Bundle();
        args.putParcelable(MovieModel.KEY, model);
        InfoFragment fragment = new InfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mMovieInfoBinding = FragMovieInfoBinding.inflate(inflater, container, false);
        mMovieInfoBinding.setModel((MovieModel) getArguments().getParcelable(MovieModel.KEY));

        return mMovieInfoBinding.getRoot();
    }


}
