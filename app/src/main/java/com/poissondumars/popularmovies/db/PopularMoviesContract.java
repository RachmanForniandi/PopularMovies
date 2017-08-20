package com.poissondumars.popularmovies.db;

import android.net.Uri;
import android.provider.BaseColumns;


public class PopularMoviesContract {

    public static final String CONTENT_AUTHORITY = "com.poissondumars.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVORITES = "favorites";

    /* Inner class that defines the table contents of the weather table */
    public static final class FavoriteMovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITES)
                .build();

        /* Used internally as the name of our weather table. */
        public static final String TABLE_NAME = "favorite_movies";

        public static final String COLUMN_OUTER_ID = "outer_id";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASED_DATE = "released_date";

    }

}
