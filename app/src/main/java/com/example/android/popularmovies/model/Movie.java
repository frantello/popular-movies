package com.example.android.popularmovies.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Movie
 */

public class Movie implements Serializable {

    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185/";

    private String originalTitle;
    private String posterThumbnail;
    private String synopsis;
    private Double rating;
    private String release;

    public static String getPosterBaseUrl() {
        return POSTER_BASE_URL;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPosterThumbnail() {
        return POSTER_BASE_URL + posterThumbnail;
    }

    public void setPosterThumbnail(String posterThumbnail) {
        this.posterThumbnail = posterThumbnail;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }
}
