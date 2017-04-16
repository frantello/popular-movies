package com.example.android.popularmovies.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Favorite Contract
 */

public final class MovieContract {

    public static final String AUTHORITY = "com.example.android.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_FAVORITES = "favorites";

    private MovieContract() {
        // Non instantiable.
    }

    public static final class FavoriteEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        public static final String TABLE_NAME = "FAVORITE";

        public static final String COLUMN_ORIGINAL_TITLE = "COLUMN_ORIGINAL_TITLE";

        public static final String COLUMN_POSTER_THUMBNAIL = "COLUMN_POSTER_THUMBNAIL";

        public static final String COLUMN_SYNOPSIS = "COLUMN_SYNOPSIS";

        public static final String COLUMN_RATING = "COLUMN_RATING";

        public static final String COLUMN_RELEASE = "COLUMN_RELEASE";
    }
}
