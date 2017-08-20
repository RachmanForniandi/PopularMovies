package com.poissondumars.popularmovies;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
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

public class MovieTrailersFragment extends MovieFragment implements MovieTrailersAdapter.MovieTrailersAdapterOnClickHandler {

    private final static String LAYOUT_STATE_KEY = "layout_state";

    @BindView(R.id.rv_trailers_list)
    RecyclerView mTrailersRecycleView;

    @BindView(R.id.pb_trailers_loading_indicator)
    ProgressBar mLoadingIndicator;

    private LayoutManager mLayoutManager;
    private Parcelable mLayoutPosition;
    private MovieTrailersAdapter mTrailersAdapter;

    public MovieTrailersFragment() { }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_trailers, container, false);
        ButterKnife.bind(this, view);

        mTrailersAdapter = new MovieTrailersAdapter(this);
        mTrailersRecycleView.setAdapter(mTrailersAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        mLayoutManager = layoutManager;
        mTrailersRecycleView.setLayoutManager(layoutManager);
        mTrailersRecycleView.setHasFixedSize(true);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        configureViewsWithMovieData(mMovie);
    }

    @Override
    protected void configureViewsWithMovieData(Movie movie) {
        if (mMovie == null) return;

        loadTrailers();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mLayoutPosition = savedInstanceState.getParcelable(LAYOUT_STATE_KEY);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Parcelable layoutPosition = mLayoutManager.onSaveInstanceState();
        outState.putParcelable(LAYOUT_STATE_KEY, layoutPosition);
    }

    private void restoreLayoutPosition() {
        if (mLayoutPosition != null) {
            mLayoutManager.onRestoreInstanceState(mLayoutPosition);
        }
    }

    private void loadTrailers() {
        if (mMovie == null) return;

        new FetchTrailersTask().execute(mMovie.id);
    }

    @Override
    public void onClick(Trailer trailer) {
        if (trailer.site.equals("YouTube")) {
            openYouTubeVideo(trailer.key);
        }
    }

    private void openYouTubeVideo(String videoId) {
        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vdn.youtube:" + videoId));
        PackageManager packageManager = getActivity().getPackageManager();

        if (youtubeIntent.resolveActivity(packageManager) != null) {
            startActivity(youtubeIntent);
        } else {
            Intent viewVideoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videoId));
            startActivity(viewVideoIntent);
        }
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
                restoreLayoutPosition();
            }
        }
    }
}
