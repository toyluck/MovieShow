package com.example.hyc.movieshow.Movies;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hyc.movieshow.R;
import com.example.hyc.movieshow.databinding.FragMoviesBinding;
import com.example.hyc.movieshow.datas.MovieModel;
import com.example.hyc.movieshow.moviesdetail.MovieDetailActivity;
import com.example.hyc.movieshow.utils.DividerItemDecoration;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.realm.Realm;
import io.realm.RealmChangeListener;

/**
 * Created by hyc on 16-11-2.
 * 展示电影页面
 */

public class MoviesFragment extends Fragment implements MoviesContract.View, MovieAdapter.MovieClickListener
{

    private MoviesContract.Presenter   mPresenter;
    private FragMoviesBinding          mDataBinding;
    private MovieAdapter               mMovieAdapter;
    private RealmChangeListener<Realm> mRealmChangeListener;

    public static MoviesFragment newInstance()
    {
        Bundle         args     = new Bundle();
        MoviesFragment fragment = new MoviesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mDataBinding = FragMoviesBinding.inflate(inflater, container, false);

        // setup ReyclerView
        RecyclerView recyclerView = mDataBinding.recyclerView;
        RecyclerView.LayoutManager manager = new GridLayoutManager(this.getContext(), 2, LinearLayoutManager
            .VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mMovieAdapter = new MovieAdapter(this);
        recyclerView.setAdapter(mMovieAdapter);
        setHasOptionsMenu(true);

        mRealmChangeListener = new RealmChangeListener<Realm>()
        {
            @Override
            public void onChange(Realm element)
            {
                Log.d("Movie", "Realm ChangeListener" + Thread.currentThread());
                System.out.println("element = " + element);
                mMovieAdapter.notifyDataSetChanged();
            }
        };
        Realm.getDefaultInstance().addChangeListener(mRealmChangeListener);
        mPresenter.subscribe();
        return mDataBinding.getRoot();
    }

    @Override
    public void onResume()
    {
        super.onResume();

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        // 删除realm实例  防止内存泄露
        Realm instance = Realm.getDefaultInstance();
        instance.removeChangeListener(mRealmChangeListener);
        instance.close();
        instance = null;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        ObservableTransformer<Boolean, Permission> ensureEach = RxPermissions.getInstance(this.getActivity())
            .ensureEach(Manifest.permission
                .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
        Observable.just(true).compose(ensureEach)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<Permission>()
            {
                @Override
                public void accept(Permission permission) throws Exception
                {
                    System.out.println("permission = " + permission);
                    if (!permission.granted)
                    {
                        ActivityCompat.requestPermissions(MoviesFragment.this.getActivity(), new
                            String[]{permission.name}, 12);
                    }
                }
            }, new Consumer<Throwable>()
            {
                @Override
                public void accept(Throwable throwable) throws Exception
                {
                    System.out.println("throwable.getMessage() = " + throwable.getMessage());
                }
            });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12)
        {
            Toast.makeText(getActivity(),
                data + "", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onPause()
    {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_main_1:
                mPresenter.loadTaks(true);
                return true;
            case R.id.menu_main_2:
                // sort
                mPresenter.setFiltering(MoviesFilterType.Hot);
                break;
            case R.id.menu_main_3:
                mPresenter.setFiltering(MoviesFilterType.Loved);
                break;
        }
        mPresenter.loadTaks(false);

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void bindPresenter(MoviesContract.Presenter presenter)
    {
        mPresenter = presenter;
    }

    @Override
    public void setLoadingIndicator(boolean showLoadingUi)
    {
        //展示loading页面
        if (showLoadingUi)
            Toast.makeText(MoviesFragment.this.getActivity(), "开始loading", Toast.LENGTH_SHORT).show();
        else
        {

        }
    }

    @Override
    public void showMovies(List<MovieModel> movies)
    {

        mMovieAdapter.setDataSource(movies);
    }

    @Override
    public void showLoadingMoviesError()
    {
        Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean isActive()
    {
        return isAdded();
    }

    @Override
    public void showFiltingPopUpMenu()
    {

    }

    @Override
    public void showMovieDetailUi(MovieModel model, int position)
    {
        MovieDetailActivity.start(getActivity(), model);
    }


    @Override
    public void click(MovieModel model, int position)
    {
        mPresenter.openMovieDetail(model, position);
    }
}
