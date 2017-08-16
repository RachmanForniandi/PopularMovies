package com.poissondumars.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.poissondumars.popularmovies.api.MoviesDbJSONHelper;
import com.poissondumars.popularmovies.api.TheMoviesDbApiClient;
import com.poissondumars.popularmovies.data.Movie;
import com.poissondumars.popularmovies.data.MovieTrailersAdapter;
import com.poissondumars.popularmovies.data.Trailer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieTrailersFragment extends Fragment {

    private MovieTrailersAdapter mTrailersAdapter;
    private Movie mMovie;

    @BindView(R.id.rv_trailers_list)
    RecyclerView mTrailersRecycleView;

    @BindView(R.id.pb_trailers_loading_indicator)
    ProgressBar mLoadingIndicator;

    public MovieTrailersFragment() { }

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

        mTrailersAdapter = new MovieTrailersAdapter();
        mTrailersRecycleView.setAdapter(mTrailersAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        mTrailersRecycleView.setLayoutManager(layoutManager);
        mTrailersRecycleView.setHasFixedSize(true);

        loadTrailers();

        return view;
    }

    private void loadTrailers() {
        new FetchTrailersTask().execute(mMovie.id);
    }

    private class FetchTrailersTask extends AsyncTask<Integer, Void, Trailer[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Trailer[] doInBackground(Integer... params) {
            if (params.length == 0) {
                return null;
            }

            Integer movieId = params[0];
            String response = TheMoviesDbApiClient.getTrailersForMovie(movieId);
            if (response != null) {
                return MoviesDbJSONHelper.parseTrailersFromJson(response);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Trailer[] trailers) {
            super.onPostExecute(trailers);
            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if (trailers != null) {
                mTrailersAdapter.setTrailers(trailers);
            }
        }
    }
}
