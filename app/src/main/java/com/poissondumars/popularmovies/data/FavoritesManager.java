package com.poissondumars.popularmovies.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.poissondumars.popularmovies.db.PopularMoviesContract;
import com.poissondumars.popularmovies.db.PopularMoviesContract.FavoriteMovieEntry;
import com.poissondumars.popularmovies.db.PopularMoviesDbHelper;

import java.util.Date;

public class FavoritesManager {

    private final static String[] MOVIE_COLUMNS = {
            FavoriteMovieEntry.COLUMN_OUTER_ID,
            FavoriteMovieEntry.COLUMN_POPULARITY,
            FavoriteMovieEntry.COLUMN_TITLE,
            FavoriteMovieEntry.COLUMN_BACKDROP_PATH,
            FavoriteMovieEntry.COLUMN_POSTER_PATH,
            FavoriteMovieEntry.COLUMN_OVERVIEW,
            FavoriteMovieEntry.COLUMN_RELEASED_DATE
    };

    private Context mContext;
    private FavoritesManagerListener mListener;
    private Handler mMainHandler;

    public interface FavoritesManagerListener {
        void loadFavorites(@Nullable Movie[] favorites);
    }

    public FavoritesManager(Context context, @Nullable FavoritesManagerListener listener) {
        mContext = context;
        mListener = listener;
        mMainHandler = new Handler(context.getMainLooper());
    }

    public void requestFavorites() {
        Thread requestFavorites = new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor favoritesCursor = mContext.getContentResolver().query(
                        FavoriteMovieEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        FavoriteMovieEntry.COLUMN_TITLE + " ASC");

                final Movie[] favorites = parseFavoriteMoviesFromCursor(favoritesCursor);
                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mListener != null) {
                            mListener.loadFavorites(favorites);
                        }
                    }
                });
            }
        });
        requestFavorites.start();
    }

    public void saveToFavorites(Movie movie) {
        ContentValues cv = new ContentValues();
        cv.put(FavoriteMovieEntry.COLUMN_OUTER_ID, movie.id);
        cv.put(FavoriteMovieEntry.COLUMN_POPULARITY, movie.popularity);
        cv.put(FavoriteMovieEntry.COLUMN_TITLE, movie.title);
        cv.put(FavoriteMovieEntry.COLUMN_BACKDROP_PATH, movie.backdropPath);
        cv.put(FavoriteMovieEntry.COLUMN_POSTER_PATH, movie.posterPath);
        cv.put(FavoriteMovieEntry.COLUMN_OVERVIEW, movie.overview);
        cv.put(FavoriteMovieEntry.COLUMN_RELEASED_DATE, movie.releaseDate.getTime());

        mContext.getContentResolver().bulkInsert(FavoriteMovieEntry.CONTENT_URI, new ContentValues[]{cv});
    }

    @Nullable
    private Movie[] parseFavoriteMoviesFromCursor(Cursor cursor) {
        if (cursor == null) return null;

        int outerIdIdx = cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_OUTER_ID);
        int popularityIdx = cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_POPULARITY);
        int titleIdx = cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_TITLE);
        int backdropPathIdx = cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_BACKDROP_PATH);
        int posterPathIdx = cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_POSTER_PATH);
        int overviewIdx = cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_OVERVIEW);
        int releasedDateIdx = cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_RELEASED_DATE);

        Movie[] result = new Movie[cursor.getCount()];
        while (cursor.moveToNext()) {
            Movie favMovie = new Movie();
            favMovie.id = cursor.getInt(outerIdIdx);
            favMovie.popularity = cursor.getFloat(popularityIdx);
            favMovie.title = cursor.getString(titleIdx);
            favMovie.backdropPath = cursor.getString(backdropPathIdx);
            favMovie.posterPath = cursor.getString(posterPathIdx);
            favMovie.overview = cursor.getString(overviewIdx);
            favMovie.isFavorite = true;

            long dateInMills = cursor.getLong(releasedDateIdx);
            favMovie.releaseDate = new Date(dateInMills);

            result[cursor.getPosition()] = favMovie;
        }
        cursor.close();

        return result;
    }

    public void removeFromFavorites(int movieId) {
        Uri deleteFavoriteUri = FavoriteMovieEntry.CONTENT_URI.buildUpon()
                .appendPath("" + movieId)
                .build();
        mContext.getContentResolver().delete(deleteFavoriteUri, null, null);
    }
}
