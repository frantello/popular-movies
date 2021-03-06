package com.example.android.popularmovies.service;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.data.MovieProvider;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.model.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Movie service.
 */

public class MovieService {

    private static final String BASE_URL = "http://api.themoviedb.org/3";
    private static final String PARAM_API_KEY = "api_key";
    private static final String PARAM_LANG = "language";
    private static final String PATH_MOVIE = "movie";
    private static final String PATH_VIDEOS = "videos";
    private static final String PATH_REVIEWS = "reviews";
    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String FAVORITE = "favorite";

    private Context context;

    private ContentResolver resolver;

    private String apiKey;

    public MovieService(Context context, String apiKey) {
        this.context = context;
        this.apiKey = apiKey;

        resolver = context.getContentResolver();
    }

    public List<Movie> findAllSortByPopularity() {

        try {

            String response = getResponseFromHttpUrl(getPopularMovieUrl());

            return parseMoviesResponse(response);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    private String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    private  URL getPopularMovieUrl() throws MalformedURLException {
        Uri.Builder builder = Uri.parse(BASE_URL).buildUpon()
                .appendPath(PATH_MOVIE)
                .appendPath(POPULAR)
                .appendQueryParameter(PARAM_API_KEY, apiKey);

        String popularMovieUrl = builder.build().toString();

        return new URL(popularMovieUrl);
    }

    private List<Movie> parseMoviesResponse(String response) {

        try {
            JSONObject object = new JSONObject(response);

            JSONArray array = object.getJSONArray(Video.RESULTS);

            List<Movie> movies = new ArrayList<>();

            for(int i = 0; i < array.length(); i++) {

                JSONObject item = array.getJSONObject(i);

                Movie movie = new Movie();
                movie.setId(item.getInt(Movie.ID));
                movie.setOriginalTitle(item.getString(Movie.ORIGINAL_TITLE));
                movie.setSynopsis(item.getString(Movie.OVERVIEW));
                movie.setRating(item.getDouble(Movie.VOTE_AVERAGE));
                movie.setRelease(item.getString(Movie.RELEASE_DATE));
                movie.setPosterThumbnail(item.getString(Movie.POSTER_PATH));

                movies.add(movie);
            }

            return movies;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    public List<Movie> findAllSortByRating() {

        try {

            String response = getResponseFromHttpUrl(getTopRatedMovieUrl());

            return parseMoviesResponse(response);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    private  URL getTopRatedMovieUrl() throws MalformedURLException {
        Uri.Builder builder = Uri.parse(BASE_URL).buildUpon()
                .appendPath(PATH_MOVIE)
                .appendPath(TOP_RATED)
                .appendQueryParameter(PARAM_API_KEY, apiKey);

        String topRatedMovieUrl = builder.build().toString();

        return new URL(topRatedMovieUrl);
    }

    public List<Movie> findAllSortByFavourite() {

        Cursor cursor = resolver.query(MovieContract.FavoriteEntry.CONTENT_URI,
                null, null, null, null);

        if (cursor == null || cursor.getCount() < 1) {
            if (cursor != null) {
                cursor.close();
            }
            return Collections.emptyList();
        }

        int count = cursor.getCount();

        List<Movie> movies = new ArrayList<>(count);

        cursor.moveToFirst();
        do {
            Movie movie = new Movie();
            movie.setId(cursor.getInt(cursor.getColumnIndex(MovieContract.FavoriteEntry._ID)));
            movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(
                    MovieContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE)));
            movie.setPosterThumbnail(cursor.getString(cursor.getColumnIndex(
                    MovieContract.FavoriteEntry.COLUMN_POSTER_THUMBNAIL)));
            movie.setSynopsis(cursor.getString(cursor.getColumnIndex(
                    MovieContract.FavoriteEntry.COLUMN_SYNOPSIS)));
            movie.setRating(cursor.getDouble(cursor.getColumnIndex(
                    MovieContract.FavoriteEntry.COLUMN_RATING)));
            movie.setRelease(cursor.getString(cursor.getColumnIndex(
                    MovieContract.FavoriteEntry.COLUMN_RELEASE)));
            movie.setFavorite(true);

            movies.add(movie);

        } while (cursor.moveToNext());

        cursor.close();

        return movies;
    }

    public List<Video> findVideosByMovieId(long movieId) {

        try {

            String response = getResponseFromHttpUrl(getMovieVideosUrl(movieId));

            return parseVideosResponse(response);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    private URL getMovieVideosUrl(long movieId) throws MalformedURLException {
        Uri.Builder builder = Uri.parse(BASE_URL).buildUpon()
                .appendPath(PATH_MOVIE)
                .appendPath(String.valueOf(movieId))
                .appendPath(PATH_VIDEOS)
                .appendQueryParameter(PARAM_API_KEY, apiKey);

        String movieVideosUrl = builder.build().toString();

        return new URL(movieVideosUrl);
    }

    private List<Video> parseVideosResponse(String response) {

        List<Video> videos = new ArrayList<>();

        try {

            JSONObject object = new JSONObject(response);

            JSONArray array = object.getJSONArray(Video.RESULTS);

            for (int i = 0; i < array.length(); i++) {

                JSONObject item = array.getJSONObject(i);

                Video video = new Video();
                video.setId(item.getString(Video.ID));
                video.setKey(item.getString(Video.KEY));
                video.setName(item.getString(Video.NAME));
                video.setSite(item.getString(Video.SITE));
                video.setSize(item.getInt(Video.SIZE));
                video.setType(item.getString(Video.TYPE));

                videos.add(video);
            }

            return videos;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    public List<Review> findReviewsByMovieId(long movieId) {

        try {

            String response = getResponseFromHttpUrl(getMovieReviewsUrl(movieId));

            return parseReviewsResponse(response);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    private URL getMovieReviewsUrl(long movieId) throws MalformedURLException {
        Uri.Builder builder = Uri.parse(BASE_URL).buildUpon()
                .appendPath(PATH_MOVIE)
                .appendPath(String.valueOf(movieId))
                .appendPath(PATH_REVIEWS)
                .appendQueryParameter(PARAM_API_KEY, apiKey);

        String moviesReviewUrl = builder.build().toString();

        return new URL(moviesReviewUrl);
    }

    private List<Review> parseReviewsResponse(String response) {

        List<Review> reviews = new ArrayList<>();

        try {

            JSONObject object = new JSONObject(response);

            JSONArray array = object.getJSONArray(Review.RESULTS);

            for (int i = 0; i < array.length(); i++) {

                JSONObject item = array.getJSONObject(i);

                Review review = new Review();
                review.setId(item.getString(Review.ID));
                review.setAuthor(item.getString(Review.AUTHOR));
                review.setContent(item.getString(Review.CONTENT));
                review.setUrl(item.getString(Review.URL));

                reviews.add(review);
            }

            return reviews;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    public boolean isFavorite(Movie movie) {

        Uri uri = MovieContract.FavoriteEntry.CONTENT_URI.buildUpon()
                .appendPath(String.valueOf(movie.getId())).build();

        Cursor cursor = resolver.query(uri, null, null, null, null);

        boolean favorite = cursor.getCount() > 0;

        cursor.close();

        return favorite;
    }

    public boolean addFavorite(Movie movie) {

        ContentValues values = new ContentValues();
        values.put(MovieContract.FavoriteEntry._ID, movie.getId());
        values.put(MovieContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE,
                movie.getOriginalTitle());
        values.put(MovieContract.FavoriteEntry.COLUMN_POSTER_THUMBNAIL,
                movie.getPosterThumbnail());
        values.put(MovieContract.FavoriteEntry.COLUMN_SYNOPSIS,
                movie.getSynopsis());
        values.put(MovieContract.FavoriteEntry.COLUMN_RATING,
                movie.getRating());
        values.put(MovieContract.FavoriteEntry.COLUMN_RELEASE,
                movie.getRelease());

        Uri uri = resolver.insert(MovieContract.FavoriteEntry.CONTENT_URI, values);

        if (uri != null) {
            movie.setFavorite(true);
            return true;
        }

        return false;
    }

    public boolean removeFavorite(Movie movie) {

        Uri uri = MovieContract.FavoriteEntry.CONTENT_URI.buildUpon()
                .appendPath(String.valueOf(movie.getId())).build();

        int rows = resolver.delete(uri, null, null);

        if (rows > 0) {
            movie.setFavorite(false);
            return true;
        }

        return false;
    }
}
