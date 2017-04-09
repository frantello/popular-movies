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

        final String SQL_CREATE_TABLE_FAVORITE = "CRATE TABLE " + FavoriteContract.TABLE_NAME +
                "(" +
                FavoriteContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavoriteContract.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
                FavoriteContract.COLUMN_MOVIE_TITLE + " TEXT NOT NULL" +
                ")";
        db.execSQL(SQL_CREATE_TABLE_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        final String SQL_DROP_TABLE_FAVORITE =
                "DROP TABLE IF EXISTS " + FavoriteContract.TABLE_NAME;
        db.execSQL(SQL_DROP_TABLE_FAVORITE);

        onCreate(db);
    }
}
