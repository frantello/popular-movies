package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class MovieActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movie> {

    ImageView mPoster;
    TextView mOriginalTitle;
    TextView mSynopsis;
    TextView mRelease;
    TextView mRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        mPoster = (ImageView) findViewById(R.id.movie_poster);
        mOriginalTitle = (TextView) findViewById(R.id.movie_original_title);
        mSynopsis = (TextView) findViewById(R.id.movie_synopsis);
        mRelease = (TextView) findViewById(R.id.movie_release);
        mRating = (TextView) findViewById(R.id.movie_rating);

        Intent intent = getIntent();

        if (intent != null) {

            if (intent.hasExtra(Intent.EXTRA_TEXT)) {

                Movie movie = (Movie) intent.getSerializableExtra(Intent.EXTRA_TEXT);
                Picasso.with(this).load(movie.getPosterThumbnail()).into(mPoster);
                mOriginalTitle.setText(movie.getOriginalTitle());
                mSynopsis.setText(movie.getSynopsis());
                mRelease.setText(movie.getRelease());
                mRating.setText(String.valueOf(movie.getRating()));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.movie, menu);

        return true;
    }

    @Override
    public Loader<Movie> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Movie> loader, Movie data) {

    }

    @Override
    public void onLoaderReset(Loader<Movie> loader) {

    }
}
