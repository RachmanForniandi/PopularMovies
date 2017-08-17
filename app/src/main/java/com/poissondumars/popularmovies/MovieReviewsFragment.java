package com.poissondumars.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.poissondumars.popularmovies.api.MoviesDbJSONHelper;
import com.poissondumars.popularmovies.api.TheMoviesDbApiClient;
import com.poissondumars.popularmovies.data.Movie;
import com.poissondumars.popularmovies.data.MovieReviewsAdapter;
import com.poissondumars.popularmovies.data.Review;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MovieReviewsFragment extends MovieFragment {

    private MovieReviewsAdapter mReviewsAdapter;

    @BindView(R.id.rv_reviews_list)
    RecyclerView mReviewsRecycleView;

    @BindView(R.id.pb_reviews_loading_indicator)
    ProgressBar mLoadingIndicator;

    public MovieReviewsFragment() { }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_reviews, container, false);
        ButterKnife.bind(this, view);

        mReviewsAdapter = new MovieReviewsAdapter();
        mReviewsRecycleView.setAdapter(mReviewsAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        mReviewsRecycleView.setLayoutManager(layoutManager);
        mReviewsRecycleView.setHasFixedSize(true);

        restoreInstanceState(savedInstanceState);
        configureViewsWithMovieData(mMovie);

        return view;
    }

    @Override
    protected void configureViewsWithMovieData(Movie movie) {
        if (movie == null) return;

        loadReviews();
    }

    private void loadReviews() {
        if (mMovie == null) return;

        new FetchReviewsTask().execute(mMovie.id);
    }

    private class FetchReviewsTask extends AsyncTask<Integer, Void, Review[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Review[] doInBackground(Integer... params) {
            if (params.length == 0) {
                return null;
            }

            Integer movieId = params[0];
            String response = TheMoviesDbApiClient.getReviewsForMovie(movieId);
            if (response != null) {
                return MoviesDbJSONHelper.parseReviewsFromJson(response);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Review[] reviews) {
            super.onPostExecute(reviews);
            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if (reviews != null) {
                mReviewsAdapter.setReviews(reviews);
            }
        }
    }
}
