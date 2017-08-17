package com.poissondumars.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.poissondumars.popularmovies.data.Movie;

public class MovieFragment extends Fragment {

    protected Movie mMovie;

    public MovieFragment() { }

    public static MovieFragment instanseWith(Class instanceClass, Movie movie) {
        try {
            MovieFragment instance = (MovieFragment) instanceClass.newInstance();
            instance.mMovie = movie;
            return instance;
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnsupportedOperationException("Cannot instantiate movie fragment");
        }
    }

    protected void restoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mMovie = savedInstanceState.getParcelable(getString(R.string.movie_extra_key));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(getString(R.string.movie_extra_key), mMovie);
        super.onSaveInstanceState(outState);
    }

    protected void configureViewsWithMovieData(Movie movie) {
        throw new UnsupportedOperationException("Method configureViewsWithMovieData is not implemented");
    }
}
