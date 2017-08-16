package com.poissondumars.popularmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poissondumars.popularmovies.data.Movie;
import com.poissondumars.popularmovies.data.Trailer;

import butterknife.ButterKnife;

public class MovieTrailersFragment extends Fragment {

    private Trailer[] mTrailers;
    private Movie mMovie;

    public MovieTrailersFragment() {}

    public static MovieTrailersFragment instanseWith(Movie movie) {
        MovieTrailersFragment instance = new MovieTrailersFragment();
        instance.mMovie = movie;
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_trailers, container, false);
        ButterKnife.bind(this, view);

        return view;
    }
}
