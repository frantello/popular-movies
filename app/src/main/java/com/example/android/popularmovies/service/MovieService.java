package com.example.android.popularmovies.service;

import com.example.android.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Movie service.
 */

public class MovieService {

    private static final String MOVIE_URL = "http://api.themoviedb.org/3/movie/%1$s?api_key=%2$s";
    private static final String POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";

    private String apiKey;

    public MovieService(String apiKey) {
        this.apiKey = apiKey;
    }

    public List<Movie> findAllSortByPopularity() {

        try {
            URL url = new URL(String.format(MOVIE_URL, POPULAR, apiKey));

            String response = getResponseFromHttpUrl(url);

            return parseResponse(response);

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

    private List<Movie> parseResponse(String response) {

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
            URL url = new URL(String.format(MOVIE_URL, TOP_RATED, apiKey));

            String response = getResponseFromHttpUrl(url);

            return parseResponse(response);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }
}
