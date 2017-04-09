package com.example.android.popularmovies.data;

import android.provider.BaseColumns;

/**
 * Favorite Contract
 */

public class FavoriteContract implements BaseColumns {

    public static final String TABLE_NAME = "FAVORITE";

    public static final String COLUMN_MOVIE_ID = "MOVIE_ID";

    public static final String COLUMN_MOVIE_TITLE = "MOVIE_TITLE";
}
