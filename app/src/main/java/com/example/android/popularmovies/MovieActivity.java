package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.adapter.ReviewsAdapter;
import com.example.android.popularmovies.adapter.VideosAdapter;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.model.Video;
import com.example.android.popularmovies.service.MovieService;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieActivity extends AppCompatActivity implements
        VideosAdapter.OnClickVideoListener,
        ReviewsAdapter.OnClickReviewListener,
        LoaderManager.LoaderCallbacks<Movie> {

    private static final int MOVIE_LOADER = 0;

    ImageView mPoster;
    TextView mOriginalTitle;
    TextView mSynopsis;
    TextView mRelease;
    TextView mRating;
    RecyclerView mVideos;
    RecyclerView mReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        mPoster = (ImageView) findViewById(R.id.movie_poster);
        mOriginalTitle = (TextView) findViewById(R.id.movie_original_title);
        mSynopsis = (TextView) findViewById(R.id.movie_synopsis);
        mRelease = (TextView) findViewById(R.id.movie_release);
        mRating = (TextView) findViewById(R.id.movie_rating);
        mVideos = (RecyclerView) findViewById(R.id.movie_videos);
        mReviews = (RecyclerView) findViewById(R.id.movie_reviews);

        mVideos.setLayoutManager(new LinearLayoutManager(this));
        mVideos.setHasFixedSize(true);
        mReviews.setLayoutManager(new LinearLayoutManager(this));
        mReviews.setHasFixedSize(true);

        Intent intent = getIntent();

        if (intent != null) {

            if (intent.hasExtra(Movie.MOVIE)) {

                Movie movie = (Movie) intent.getSerializableExtra(Movie.MOVIE);
                Picasso.with(this).load(movie.getPosterThumbnail()).into(mPoster);
                mOriginalTitle.setText(movie.getOriginalTitle());
                mSynopsis.setText(movie.getSynopsis());
                mRelease.setText(movie.getRelease());
                mRating.setText(String.valueOf(movie.getRating()));

                Bundle movieBundle = new Bundle();
                movieBundle.putSerializable(Movie.MOVIE, movie);
                getSupportLoaderManager().initLoader(MOVIE_LOADER, movieBundle, this);
            }
        }
    }

    private void onClickFavourite() {

    }

    @Override
    public void playVideo(Video video) {

        Intent launchYoutube = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + video.getKey()));

        if (launchYoutube.resolveActivity(getPackageManager()) != null) {

            String title = getResources().getString(R.string.chooser_video_title);

            Intent chooser = Intent.createChooser(launchYoutube, title);

            startActivity(chooser);
        }
    }

    @Override
    public void readReview(Review review) {
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(review.getUrl()));

        if (launchBrowser.resolveActivity(getPackageManager()) != null) {

            startActivity(launchBrowser);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.movie, menu);

        return true;
    }

    @Override
    public Loader<Movie> onCreateLoader(int id, Bundle args) {
        Movie movie = (Movie) args.getSerializable(Movie.MOVIE);

        return new MovieLoader(this, movie);
    }

    @Override
    public void onLoadFinished(Loader<Movie> loader, Movie movie) {

        mVideos.setAdapter(new VideosAdapter(movie.getVideos(), this));
        mVideos.getAdapter().notifyDataSetChanged();

        mReviews.setAdapter(new ReviewsAdapter(movie.getReviews(), this));
        mReviews.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Movie> loader) {

    }

    static class MovieLoader extends AsyncTaskLoader<Movie> {

        private Movie movie;

        private String apiKey;

        private MovieService movieService;

        public MovieLoader(Context context, Movie movie) {
            super(context);
            this.apiKey = context.getString(R.string.the_movie_database_api_key);
            movieService = new MovieService(apiKey);
            this.movie = movie;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Override
        public Movie loadInBackground() {

            List<Video> videos = movieService.findVideosByMovieId(movie.getId());
            List<Review> reviews = movieService.findReviewsByMovieId(movie.getId());

            movie.setVideos(videos);
            movie.setReviews(reviews);

            return movie;
        }
    }
}
