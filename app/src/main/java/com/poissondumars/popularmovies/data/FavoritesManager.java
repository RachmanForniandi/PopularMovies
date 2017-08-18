package com.poissondumars.popularmovies.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.poissondumars.popularmovies.db.PopularMoviesContract.FavoriteMovieEntry;
import com.poissondumars.popularmovies.db.PopularMoviesDbHelper;

import java.util.Date;

public class FavoritesManager {

    private Context mContext;
    private PopularMoviesDbHelper mDbHelper;

    private final static String[] MOVIE_COLUMNS = {
            FavoriteMovieEntry.COLUMN_OUTER_ID,
            FavoriteMovieEntry.COLUMN_POPULARITY,
            FavoriteMovieEntry.COLUMN_TITLE,
            FavoriteMovieEntry.COLUMN_BACKDROP_PATH,
            FavoriteMovieEntry.COLUMN_POSTER_PATH,
            FavoriteMovieEntry.COLUMN_OVERVIEW,
            FavoriteMovieEntry.COLUMN_RELEASED_DATE
    };

    public FavoritesManager(Context context) {
        mContext = context;
        mDbHelper = new PopularMoviesDbHelper(mContext);
    }

    public Movie[] getFavoriteMovies() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = db.query(FavoriteMovieEntry.TABLE_NAME, MOVIE_COLUMNS, null, null, null, null, null);
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

            long dateInMills = cursor.getLong(releasedDateIdx);
            favMovie.releaseDate = new Date(dateInMills);

            result[cursor.getPosition()] = favMovie;
        }
        cursor.close();

        return result;
    }

    public void saveToFavorites(Movie movie) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(FavoriteMovieEntry.COLUMN_OUTER_ID, movie.id);
        cv.put(FavoriteMovieEntry.COLUMN_POPULARITY, movie.popularity);
        cv.put(FavoriteMovieEntry.COLUMN_TITLE, movie.title);
        cv.put(FavoriteMovieEntry.COLUMN_BACKDROP_PATH, movie.backdropPath);
        cv.put(FavoriteMovieEntry.COLUMN_POSTER_PATH, movie.posterPath);
        cv.put(FavoriteMovieEntry.COLUMN_OVERVIEW, movie.overview);
        cv.put(FavoriteMovieEntry.COLUMN_RELEASED_DATE, movie.releaseDate.getTime());

        db.insert(FavoriteMovieEntry.TABLE_NAME, null, cv);
    }

    public boolean isFavorite(Movie movie) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql ="SELECT _ID FROM " +
                FavoriteMovieEntry.TABLE_NAME + " WHERE "  +
                FavoriteMovieEntry.COLUMN_OUTER_ID + " = " + movie.id + ";";

        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();
        cursor.close();

        return count > 0;
    }

    public void removeFromFavorites(int movieId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String where = "outer_id = ?";
        String[] whereArgs = {"" + movieId};
        db.delete(FavoriteMovieEntry.TABLE_NAME, where, whereArgs);
    }

}
