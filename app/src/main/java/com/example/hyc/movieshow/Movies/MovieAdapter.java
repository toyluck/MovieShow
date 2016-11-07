package com.example.hyc.movieshow.Movies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hyc.movieshow.databinding.ItemMovieBinding;
import com.example.hyc.movieshow.datas.MovieModel;

import java.util.List;

class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>
{

    private List<MovieModel>   mDataSource;
    private MovieClickListener mClickListener;

    public void setDataSource(List<MovieModel> dataSource)
    {
        mDataSource = dataSource;
        notifyDataSetChanged();
    }

    public MovieAdapter(MovieClickListener listener)
    {
        mClickListener = listener;
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
        return new MovieViewHolder(binding, mClickListener);
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

        if (getDataSource() != null)
        {
            return getDataSource().size();
        }
        return 0;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        private final ItemMovieBinding   mItemMovieBinding;
        private final MovieClickListener mClickListener;

        public MovieViewHolder(ItemMovieBinding itemView, MovieClickListener clickListener)
        {
            super(itemView.getRoot());
            mItemMovieBinding = itemView;
            itemView.getRoot().setOnClickListener(this);
            mClickListener = clickListener;
        }

        @Override
        public void onClick(View v)
        {
            mClickListener.click(mItemMovieBinding.getModel(), getAdapterPosition());
        }
    }

    public void setClickListener(MovieClickListener clickListener)
    {
        mClickListener = clickListener;
    }

    public interface MovieClickListener
    {
        void click(MovieModel model, int position);
    }
}