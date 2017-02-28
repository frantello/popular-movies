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

import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.OnClickMovieListener {

    MovieService mMovieService;
    RecyclerView mMovies;
    MoviesAdapter mMoviesAdapter;
    ProgressBar mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieService = new MovieService(getString(R.string.the_movie_database_api_key));

        mMovies = (RecyclerView) findViewById(R.id.movies);
        mMovies.setHasFixedSize(true);
        mMovies.setLayoutManager(new GridLayoutManager(this, 2));
        mLoading = (ProgressBar) findViewById(R.id.loading);
        new MoviesTask().execute(MovieService.TOP_RATED);
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
                new MoviesTask().execute(MovieService.POPULAR);
                return true;
            case R.id.action_sort_by_rating:
                new MoviesTask().execute(MovieService.TOP_RATED);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, MovieActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, movie);
        startActivity(intent);
    }

    class MoviesTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(String... strings) {

            List<Movie> movies;

            if (strings[0].equals("popular")) {
                movies = mMovieService.findAllSortByPopularity();
            } else {
                movies = mMovieService.findAllSortByRating();
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
