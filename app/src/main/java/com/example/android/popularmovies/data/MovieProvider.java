package com.example.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Movie provider.
 */

public class MovieProvider extends ContentProvider {

    public static final int FAVORITES = 100;

    public static final int FAVORITES_WITH_ID = 101;

    private UriMatcher uriMatcher;

    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {

        uriMatcher = buildUriMatcher();
        db = new MovieDbHelper(getContext()).getWritableDatabase();

        return true;
    }

    private UriMatcher buildUriMatcher() {

        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_FAVORITES, FAVORITES);
        matcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_FAVORITES + "/#", FAVORITES_WITH_ID);

        return matcher;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;

        switch (uriMatcher.match(uri)) {
            case FAVORITES:

                cursor = db.query(MovieContract.FavoriteEntry.TABLE_NAME,
                        null, null, null, null, null, null);

                break;

            case FAVORITES_WITH_ID:

                selection = MovieContract.FavoriteEntry._ID + "=?";
                selectionArgs = new String[] {uri.getLastPathSegment()};

                cursor = db.query(MovieContract.FavoriteEntry.TABLE_NAME,
                        null, selection, selectionArgs, null, null, null);

                break;

            default:
                throw new UnsupportedOperationException("Bad URI");
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        switch (buildUriMatcher().match(uri)) {
            case FAVORITES:

                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                        MovieContract.AUTHORITY + "/" + MovieContract.PATH_FAVORITES;
            case FAVORITES_WITH_ID:

                return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                        MovieContract.AUTHORITY + "/" + MovieContract.PATH_FAVORITES;

            default:

                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        switch (uriMatcher.match(uri)) {
            case FAVORITES:

                long id = db.insert(MovieContract.FavoriteEntry.TABLE_NAME, null, values);

                if (id == -1) {
                    return null;
                }

                getContext().getContentResolver().notifyChange(uri, null);

                return MovieContract.FavoriteEntry.CONTENT_URI.buildUpon()
                        .appendPath(String.valueOf(id)).build();

            default:

                throw new UnsupportedOperationException("Bad URI");
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        switch (uriMatcher.match(uri)) {
            case FAVORITES_WITH_ID:

                selection = MovieContract.FavoriteEntry._ID + "=?";

                String id = uri.getLastPathSegment();

                selectionArgs = new String[] { id };

                int deleteCount =
                        db.delete(MovieContract.FavoriteEntry.TABLE_NAME, selection, selectionArgs);

                if (deleteCount > 0) {
                    getContext().getContentResolver()
                            .notifyChange(MovieContract.FavoriteEntry.CONTENT_URI, null);
                }

                return deleteCount;

            default:
                throw new UnsupportedOperationException("Bad URI");
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        throw new UnsupportedOperationException("Not yet implemented");
    }
}
