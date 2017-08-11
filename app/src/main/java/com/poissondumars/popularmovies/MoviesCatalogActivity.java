package com.poissondumars.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.poissondumars.popularmovies.api.MoviesDbJSONHelper;
import com.poissondumars.popularmovies.api.TheMoviesDbApiClient;
import com.poissondumars.popularmovies.data.Movie;
import com.poissondumars.popularmovies.data.MoviesListAdapter;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesCatalogActivity extends AppCompatActivity implements MoviesListAdapter.MoviesListAdapterOnClickHandler {

    private static final String TAG = MoviesCatalogActivity.class.getSimpleName();

    private MoviesListAdapter mMoviesListAdapter;

    @BindView(R.id.rv_movies_list) RecyclerView mRecyclerView;
    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_catalog);

        ButterKnife.bind(this);

        // Setup adapter
        mMoviesListAdapter = new MoviesListAdapter(this);
        mRecyclerView.setAdapter(mMoviesListAdapter);

        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        loadMoviesList(TheMoviesDbApiClient.POPULAR_MOVIES_LIST);
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

    private void loadMoviesList(String sorting) {
        new FetchMoviesTask().execute(sorting);
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
            loadMoviesList(TheMoviesDbApiClient.POPULAR_MOVIES_LIST);
        }

        if (itemId == R.id.sort_by_rating_action) {
            mMoviesListAdapter.setMoviesData(null);
            loadMoviesList(TheMoviesDbApiClient.TOP_RATED_MOVIES_LIST);
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
                try {
                    return MoviesDbJSONHelper.getMoviesFromJson(MoviesCatalogActivity.this, response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
