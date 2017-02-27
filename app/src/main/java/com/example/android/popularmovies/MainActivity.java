package com.example.android.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.popularmovies.adapter.MoviesAdapter;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.service.MovieService;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.OnClickMovieListener {

    MovieService mMovieService;
    RecyclerView mMovies;
    MoviesAdapter mMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieService = new MovieService(getString(R.string.the_movie_database_api_key));

        mMovies = (RecyclerView) findViewById(R.id.movies);
        mMovies.setLayoutManager(new GridLayoutManager(this, 3));
        new MoviesTask().execute();
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
                new MoviesTask().execute(item.getItemId());
                return true;
            case R.id.action_sort_by_rating:
                new MoviesTask().execute(item.getItemId());
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

    class MoviesTask extends AsyncTask<Integer, Void, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(Integer... integers) {

            Integer id = integers.length == 1 ? integers[0] : -1;

            List<Movie> movies;

            if (id == R.id.action_sort_by_rating) {
                movies = mMovieService.findAllSortByPopularity();
            } else {
                movies = mMovieService.findAllSortByRating();
            }

            return movies;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            mMoviesAdapter = new MoviesAdapter(movies, MainActivity.this);
            mMovies.setAdapter(mMoviesAdapter);
        }
    }
}
