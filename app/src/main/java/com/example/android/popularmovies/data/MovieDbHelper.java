package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database helper.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "movie.db";
    private static final int DB_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_FAVORITE_TABLE =
                "CREATE TABLE " + MovieContract.FavoriteEntry.TABLE_NAME +
                        "(" +
                        MovieContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY," +
                        MovieContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL," +
                        MovieContract.FavoriteEntry.COLUMN_POSTER_THUMBNAIL + " TEXT NOT NULL," +
                        MovieContract.FavoriteEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL," +
                        MovieContract.FavoriteEntry.COLUMN_RATING + " REAL NOT NULL," +
                        MovieContract.FavoriteEntry.COLUMN_RELEASE + " TEXT NOT NULL" +
                        ")";

        db.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        final String SQL_DROP_FAVORITE_TABLE =
                "DROP TABLE IF EXISTS " + MovieContract.FavoriteEntry.TABLE_NAME;

        db.execSQL(SQL_DROP_FAVORITE_TABLE);

        onCreate(db);
    }
}
