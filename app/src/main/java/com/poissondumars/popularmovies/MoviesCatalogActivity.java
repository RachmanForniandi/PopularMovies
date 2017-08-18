package com.poissondumars.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.poissondumars.popularmovies.api.MoviesDbJSONHelper;
import com.poissondumars.popularmovies.api.TheMoviesDbApiClient;
import com.poissondumars.popularmovies.data.FavoritesManager;
import com.poissondumars.popularmovies.data.Movie;
import com.poissondumars.popularmovies.data.MoviesListAdapter;


import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesCatalogActivity extends AppCompatActivity implements MoviesListAdapter.MoviesListAdapterOnClickHandler {

    private static final String TAG = MoviesCatalogActivity.class.getSimpleName();

    private MoviesListAdapter mMoviesListAdapter;

    private final static String MOVIE_LIST_TYPE_KEY = "list_type";

    private final static int POPULAR_MOVIE_LIST_TYPE = 0;
    private final static int TOP_RATED_MOVIE_LIST_TYPE = 1;
    private final static int FAVORITES_MOVIE_LIST_TYPE = 2;

    private final static int DEFAULT_MOVIE_LIST_TYPE = POPULAR_MOVIE_LIST_TYPE;

    private int mCurrentMovieListType;

    @BindView(R.id.rv_movies_list) RecyclerView mRecyclerView;
    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_catalog);

        ButterKnife.bind(this);

        mCurrentMovieListType = DEFAULT_MOVIE_LIST_TYPE;
        if (savedInstanceState != null) {
            mCurrentMovieListType = savedInstanceState.getInt(MOVIE_LIST_TYPE_KEY, DEFAULT_MOVIE_LIST_TYPE);
        }

        // Setup adapter
        mMoviesListAdapter = new MoviesListAdapter(this);
        mRecyclerView.setAdapter(mMoviesListAdapter);

        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        loadMoviesList(mCurrentMovieListType);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putInt(MOVIE_LIST_TYPE_KEY, mCurrentMovieListType);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.catalog_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.sort_by_popularity_action) {
            mMoviesListAdapter.setMoviesData(null);
            loadMoviesList(POPULAR_MOVIE_LIST_TYPE);
        }

        if (itemId == R.id.sort_by_rating_action) {
            mMoviesListAdapter.setMoviesData(null);
            loadMoviesList(TOP_RATED_MOVIE_LIST_TYPE);
        }

        if (itemId == R.id.show_favorites_action) {
            mMoviesListAdapter.setMoviesData(null);
            loadMoviesList(FAVORITES_MOVIE_LIST_TYPE);
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailAcivity.class);
        String movieExtraKey = getString(R.string.movie_extra_key);
        intent.putExtra(movieExtraKey, movie);
        startActivity(intent);
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) { return 2; }
        return nColumns;
    }

    private void loadMoviesList(int listType) {
        switch (listType) {
            case POPULAR_MOVIE_LIST_TYPE:
                new FetchMoviesTask().execute(TheMoviesDbApiClient.POPULAR_MOVIES);
                break;
            case TOP_RATED_MOVIE_LIST_TYPE:
                new FetchMoviesTask().execute(TheMoviesDbApiClient.TOP_RATED_MOVIES);
                break;
            case FAVORITES_MOVIE_LIST_TYPE:
                mMoviesListAdapter.setMoviesData(new FavoritesManager(this).getFavoriteMovies());
                break;
            default:
                throw new UnsupportedOperationException("Unknown list type: " + listType);
        }
    }

    private class FetchMoviesTask extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String sorting = params[0];
            String response = TheMoviesDbApiClient.getMoviesList(sorting);
            if (response != null) {
                return MoviesDbJSONHelper.parseMoviesFromJson(response);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            super.onPostExecute(movies);
            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if (movies != null) {
                mMoviesListAdapter.setMoviesData(movies);
            }
        }
    }
}
