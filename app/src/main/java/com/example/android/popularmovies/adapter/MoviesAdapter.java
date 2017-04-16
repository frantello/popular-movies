package com.example.android.popularmovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Movies adapter
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private List<Movie> mMovies;

    private OnClickMovieListener mListener;

    public MoviesAdapter(List<Movie> mMovies, OnClickMovieListener mListener) {
        this.mMovies = mMovies;
        this.mListener = mListener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(mMovies.get(position));
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Movie mMovie;
        View itemView;
        ImageView poster;

        public MovieViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.itemView = itemView;
            this.poster = (ImageView) itemView.findViewById(R.id.movie_poster);
        }

        public void bind(Movie mMovie) {
            this.mMovie = mMovie;
            Picasso.with(itemView.getContext()).load(mMovie.getPosterUrl()).into(poster);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(mMovie);
        }
    }

    public interface OnClickMovieListener {

        void onClick(Movie movie);
    }
}
