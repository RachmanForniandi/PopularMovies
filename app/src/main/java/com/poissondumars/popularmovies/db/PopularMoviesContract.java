package com.poissondumars.popularmovies.db;

import android.provider.BaseColumns;


public class PopularMoviesContract {

    /* Inner class that defines the table contents of the weather table */
    public static final class FavoriteMovieEntry implements BaseColumns {

        /* Used internally as the name of our weather table. */
        public static final String TABLE_NAME = "favorite_movies";

        public static final String COLUMN_OUTER_ID = "outer_id";
        public static final String COLUMN_DATE = "created";

    }

}
