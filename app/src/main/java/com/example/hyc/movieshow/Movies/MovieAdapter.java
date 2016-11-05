package com.example.hyc.movieshow.Movies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.hyc.movieshow.databinding.ItemMovieBinding;
import com.example.hyc.movieshow.datas.MovieModel;

import java.util.List;

class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>
{

    private List<MovieModel> mDataSource;

    public void setDataSource(List<MovieModel> dataSource)
    {
        mDataSource = dataSource;
        notifyDataSetChanged();
    }

    public MovieAdapter()
    {

    }

    public MovieAdapter(List<MovieModel> dataSource)
    {
        this.mDataSource = dataSource;
    }

    public List<MovieModel> getDataSource()
    {
        return mDataSource;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater   inflater = LayoutInflater.from(parent.getContext());
        ItemMovieBinding binding  = ItemMovieBinding.inflate(inflater, parent, false);
        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position)
    {
        holder.mItemMovieBinding.setModel(getItem(position));
    }

    public MovieModel getItem(int position)
    {
        return getDataSource().get(position);
    }

    @Override
    public int getItemCount()
    {

        if (getDataSource()!=null){
            return getDataSource().size();
        }
        return 0;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder
    {

        private final ItemMovieBinding mItemMovieBinding;

        public MovieViewHolder(ItemMovieBinding itemView)
        {
            super(itemView.getRoot());
            mItemMovieBinding = itemView;
        }
    }
}