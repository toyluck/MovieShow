package com.example.hyc.movieshow.Movies;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hyc.movieshow.databinding.FragMoviesBinding;
import com.example.hyc.movieshow.datas.MovieModel;
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
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by hyc on 16-11-2.
 * 展示电影页面
 */

public class MoviesFragment extends Fragment implements MoviesContract.View {

    private MoviesContract.Presenter mPresenter;
    private FragMoviesBinding        mDataBinding;
    private MovieAdapter             mMovieAdapter;

    public static MoviesFragment newInstance() {
        Bundle         args     = new Bundle();
        MoviesFragment fragment = new MoviesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDataBinding = FragMoviesBinding.inflate(inflater, container, false);

        // setup ReyclerView
        RecyclerView recyclerView = mDataBinding.recyclerView;
        RecyclerView.LayoutManager manager = new GridLayoutManager(this.getContext(), 2, LinearLayoutManager
                .VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mMovieAdapter = new MovieAdapter();
        recyclerView.setAdapter(mMovieAdapter);
        return mDataBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();

    }

    @Override
    public void onStart() {
        super.onStart();
        ObservableTransformer<Boolean, Permission> ensureEach = RxPermissions.getInstance(this.getActivity())
                .ensureEach(Manifest.permission
                        .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
        Observable.just(true).compose(ensureEach)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        System.out.println("permission = " + permission);
                        if (!permission.granted) {
                            ActivityCompat.requestPermissions(MoviesFragment.this.getActivity(), new
                                    String[]{permission.name}, 12);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("throwable.getMessage() = " + throwable.getMessage());
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12) {
            Toast.makeText(getActivity(),
                    data + "", Toast.LENGTH_SHORT).show();
        }
    }

    private void testRxjava2() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {

            }
        }, BackpressureStrategy.ERROR);
        final List<MovieModel> models = new ArrayList<>();

        Flowable<MovieModel> movieModelFlowable = Flowable.fromIterable(models).onBackpressureBuffer().observeOn(AndroidSchedulers.mainThread());
        movieModelFlowable.flatMap(new Function<MovieModel, Publisher<MovieModel>>() {
            @Override
            public Publisher<MovieModel> apply(MovieModel movieModel) throws Exception {
                System.out.println("movieModel = " + movieModel);

                return Flowable.just(movieModel);
            }
        }).subscribe(new Consumer<MovieModel>() {
            @Override
            public void accept(MovieModel movieModel) throws Exception {
                System.out.println("movieModel 2= " + movieModel);
            }

        });

        movieModelFlowable.flatMapSingle(new Function<MovieModel, SingleSource<?>>() {
            @Override
            public SingleSource<?> apply(MovieModel movieModel) throws Exception {
                return null;
            }
        }).subscribe(new Subscriber<Object>() {
            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("s = " + s);
            }

            @Override
            public void onNext(Object o) {
                System.out.println("MoviesFragment.onNext");
                System.out.println("o = " + o);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("MoviesFragment.onError");
                System.out.println("t = " + t);
            }

            @Override
            public void onComplete() {
                System.out.println("MoviesFragment.onComplete");
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }


    @Override
    public void bindPresenter(MoviesContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setLoadingIndicator(boolean showLoadingUi) {
        //展示loading页面
        Toast.makeText(MoviesFragment.this.getActivity(), "开始loading", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMovies(List<MovieModel> movies) {
        mMovieAdapter.setDataSource(movies);
    }


}
