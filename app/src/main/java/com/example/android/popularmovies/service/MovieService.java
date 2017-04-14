package com.example.android.popularmovies.service;

import android.net.Uri;

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

    private String apiKey;

    public MovieService(String apiKey) {
        this.apiKey = apiKey;
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
                movie.setOriginalTitle(item.getString(Movie.ORIGINAL_TITLE));
                movie.setSynopsis(item.getString(Movie.OVERVIEWº));
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

        return Collections.emptyList();
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

    public Movie findById(long movieId) {

        return null;
    }
}
