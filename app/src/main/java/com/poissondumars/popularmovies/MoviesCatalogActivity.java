package com.poissondumars.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.poissondumars.popularmovies.API.MoviesDbJSONHelper;
import com.poissondumars.popularmovies.API.TheMoviesDbApiClient;
import com.poissondumars.popularmovies.data.Movie;
import com.poissondumars.popularmovies.data.MoviesListAdapter;

import org.json.JSONException;

public class MoviesCatalogActivity extends AppCompatActivity implements MoviesListAdapter.MoviesListAdapterOnClickHandler {

    private static final String TAG = MoviesCatalogActivity.class.getSimpleName();

    private MoviesListAdapter mMoviesListAdapter;

    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_catalog);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movies_list);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        // Setup adapter
        mMoviesListAdapter = new MoviesListAdapter(this);
        mRecyclerView.setAdapter(mMoviesListAdapter);

        int numberOfColumns = 3;
        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        loadMoviesList(TheMoviesDbApiClient.SORT_BY_POPULARITY);
    }

    private void loadMoviesList(String sorting) {
        new FetchMoviesTask().execute(sorting);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sorting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.sort_by_popularity_action) {
            mMoviesListAdapter.setMoviesData(null);
            setTitle(R.string.sort_by_popularity_title);
            loadMoviesList(TheMoviesDbApiClient.SORT_BY_POPULARITY);
        }

        if (itemId == R.id.sort_by_rating_action) {
            mMoviesListAdapter.setMoviesData(null);
            setTitle(R.string.sort_by_rating_title);
            loadMoviesList(TheMoviesDbApiClient.SORT_BY_VOTES);
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailAcivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
    }

    class FetchMoviesTask extends AsyncTask<String, Void, Movie[]> {

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
