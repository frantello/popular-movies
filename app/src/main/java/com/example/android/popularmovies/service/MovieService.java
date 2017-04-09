package com.example.android.popularmovies.service;

import android.net.Uri;

import com.example.android.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
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
    private static final String PATH_MOVIE = "movie";
    private static final String PATH_VIDEOS = "videos";
    private static final String PATH_REVIEWS = "reviews";
    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String FAVORITE = "favorite";

    private String apiKey;

    public MovieService(String apiKey) {
        this.apiKey = apiKey;
    }

    public List<Movie> findAllSortByPopularity() {

        try {

            String response = getResponseFromHttpUrl(getPopularMovieUrl());

            return parseFindAllResponse(response);

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

    private List<Movie> parseFindAllResponse(String response) {

        try {
            JSONObject object = new JSONObject(response);

            JSONArray array = object.getJSONArray("results");

            List<Movie> movies = new ArrayList<>();

            for(int i = 0; i < array.length(); i++) {

                JSONObject item = array.getJSONObject(i);

                Movie movie = new Movie();
                movie.setOriginalTitle(item.getString("original_title"));
                movie.setSynopsis(item.getString("overview"));
                movie.setRating(item.getDouble("vote_average"));
                movie.setRelease(item.getString("release_date"));
                movie.setPosterThumbnail(item.getString("poster_path"));

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

            return parseFindAllResponse(response);

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

        return Collections.emptyList();
    }

    public List<String> findVideosByMovieId(long movieId) {

        return null;
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

    public List<String> findReviewsByMovieId(long movieId) {

        return null;
    }

    private URL getMovieReviewsUrl(long movieId) throws MalformedURLException {
        Uri.Builder builder = Uri.parse(BASE_URL).buildUpon()
                .appendPath(PATH_MOVIE)
                .appendPath(String.valueOf(movieId))
                .appendPath(POPULAR)
                .appendQueryParameter(PARAM_API_KEY, apiKey);

        String moviesReviewUrl = builder.build().toString();

        return new URL(moviesReviewUrl);
    }

    public Movie findById(long movieId) {

        return null;
    }
}
