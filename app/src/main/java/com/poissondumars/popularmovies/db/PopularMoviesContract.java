package com.poissondumars.popularmovies.db;

import android.provider.BaseColumns;


public class PopularMoviesContract {

    /* Inner class that defines the table contents of the weather table */
    public static final class FavoriteMovieEntry implements BaseColumns {

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
