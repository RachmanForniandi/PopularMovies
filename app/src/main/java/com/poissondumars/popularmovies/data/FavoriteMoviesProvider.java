package com.poissondumars.popularmovies.data;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.poissondumars.popularmovies.db.PopularMoviesContract;
import com.poissondumars.popularmovies.db.PopularMoviesContract.FavoriteMovieEntry;
import com.poissondumars.popularmovies.db.PopularMoviesDbHelper;

public class FavoriteMoviesProvider extends ContentProvider {

    public static final int CODE_FAVORITES = 100;
    public static final int CODE_FAVORITES_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private PopularMoviesDbHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        mOpenHelper = new PopularMoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case CODE_FAVORITES:
                cursor = mOpenHelper.getReadableDatabase().query(
                        FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_FAVORITES_WITH_ID:
                String favoriteIdString = uri.getLastPathSegment();
                String[] selectionArguments = new String[] {favoriteIdString};

                cursor = mOpenHelper.getReadableDatabase().query(
                        FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        FavoriteMovieEntry.COLUMN_OUTER_ID + " = ?",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case CODE_FAVORITES:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(FavoriteMovieEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;

            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted;

        switch (sUriMatcher.match(uri)) {
            case CODE_FAVORITES_WITH_ID:
                String favoriteIdString = uri.getLastPathSegment();
                String[] selectionArguments = new String[] {favoriteIdString};

                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        FavoriteMovieEntry.TABLE_NAME,
                        FavoriteMovieEntry.COLUMN_OUTER_ID + " = ?",
                        selectionArguments);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PopularMoviesContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, PopularMoviesContract.PATH_FAVORITES, CODE_FAVORITES);
        matcher.addURI(authority, PopularMoviesContract.PATH_FAVORITES + "/#", CODE_FAVORITES_WITH_ID);

        return matcher;
    }

}
