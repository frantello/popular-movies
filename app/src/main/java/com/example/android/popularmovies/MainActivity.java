package com.example.android.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.popularmovies.adapter.MoviesAdapter;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.service.MovieService;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.OnClickMovieListener {

    private static final String SORT_BY_KEY = "sortBy";

    private MovieService mMovieService;

    private String sortBy = MovieService.TOP_RATED;

    RecyclerView mMovies;
    MoviesAdapter mMoviesAdapter;
    ProgressBar mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            sortBy = savedInstanceState.getString(SORT_BY_KEY);
        }


        mMovieService = new MovieService(this, getString(R.string.the_movie_database_api_key));

        mMovies = (RecyclerView) findViewById(R.id.movies);
        mMovies.setHasFixedSize(true);
        mMovies.setLayoutManager(new GridLayoutManager(this, 2));
        mLoading = (ProgressBar) findViewById(R.id.loading);
    }

    @Override
    protected void onResume() {
        super.onResume();

        new MoviesTask().execute(sortBy);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString(SORT_BY_KEY, sortBy);

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_by_popularity:
                sortBy = MovieService.POPULAR;
                new MoviesTask().execute(sortBy);
                return true;
            case R.id.action_sort_by_rating:
                sortBy = MovieService.TOP_RATED;
                new MoviesTask().execute(sortBy);
                return true;
            case R.id.action_sort_by_favorite:
                sortBy = MovieService.FAVORITE;
                new MoviesTask().execute(sortBy);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, MovieActivity.class);
        intent.putExtra(Movie.MOVIE, movie);
        startActivity(intent);
    }

    class MoviesTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(String... strings) {

            List<Movie> movies;

            String sortBy = strings[0];
            if (MovieService.POPULAR.equals(sortBy)) {
                movies = mMovieService.findAllSortByPopularity();
            } else if (MovieService.TOP_RATED.equals(sortBy)) {
                movies = mMovieService.findAllSortByRating();
            } else {
                movies = mMovieService.findAllSortByFavourite();
            }

            return movies;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            mLoading.setVisibility(View.GONE);
            mMoviesAdapter = new MoviesAdapter(movies, MainActivity.this);
            mMovies.setAdapter(mMoviesAdapter);
        }
    }
}
