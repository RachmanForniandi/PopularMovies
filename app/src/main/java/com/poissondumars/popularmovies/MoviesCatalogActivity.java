package com.poissondumars.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.poissondumars.popularmovies.api.MoviesDbJSONHelper;
import com.poissondumars.popularmovies.api.TheMoviesDbApiClient;
import com.poissondumars.popularmovies.data.FavoritesManager;
import com.poissondumars.popularmovies.data.Movie;
import com.poissondumars.popularmovies.data.MoviesListAdapter;


import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesCatalogActivity extends AppCompatActivity
        implements MoviesListAdapter.MoviesListAdapterOnClickHandler, FavoritesManager.FavoritesManagerListener {

    private static final String TAG = MoviesCatalogActivity.class.getSimpleName();

    private MoviesListAdapter mMoviesListAdapter;

    private final static String MOVIE_LIST_TYPE_KEY = "list_type";
    private final static String LAYOUT_STATE_KEY = "layout_state";

    private final static int POPULAR_MOVIE_LIST_TYPE = 0;
    private final static int TOP_RATED_MOVIE_LIST_TYPE = 1;
    private final static int FAVORITES_MOVIE_LIST_TYPE = 2;

    private final static int DEFAULT_MOVIE_LIST_TYPE = POPULAR_MOVIE_LIST_TYPE;

    private String DEFAULT_TITLE;

    private int mCurrentMovieListType = DEFAULT_MOVIE_LIST_TYPE;

    @BindView(R.id.rv_movies_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;

    @BindView(R.id.tv_empty_list_text)
    TextView mEmptyListText;

    private Movie[] mLoadedMovies;
    private boolean mNeedReload;
    private LayoutManager mLayoutManager;
    private Parcelable mLayoutState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_catalog);

        ButterKnife.bind(this);
        DEFAULT_TITLE = getString(R.string.app_name);

        // Setup adapter
        mMoviesListAdapter = new MoviesListAdapter(this);
        mRecyclerView.setAdapter(mMoviesListAdapter);

        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns());
        mLayoutManager = layoutManager;
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadMoviesList(mCurrentMovieListType);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            mLayoutState = savedInstanceState.getParcelable(LAYOUT_STATE_KEY);
            setCurrentMovieListType(savedInstanceState.getInt(MOVIE_LIST_TYPE_KEY, DEFAULT_MOVIE_LIST_TYPE));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        onSaveState(outState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        onSaveState(outState);
    }

    private void onSaveState(Bundle state) {
        mLayoutState = mLayoutManager.onSaveInstanceState();
        state.putParcelable(LAYOUT_STATE_KEY, mLayoutState);
        state.putInt(MOVIE_LIST_TYPE_KEY, mCurrentMovieListType);
    }

    private void restoreLayoutPosition() {
        if (mLayoutState != null) {
            mLayoutManager.onRestoreInstanceState(mLayoutState);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.catalog_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        mNeedReload = true;
        mLoadedMovies = null;

        if (itemId == R.id.sort_by_popularity_action) {
            mMoviesListAdapter.setMoviesData(null);
            setCurrentMovieListType(POPULAR_MOVIE_LIST_TYPE);
        }

        if (itemId == R.id.sort_by_rating_action) {
            mMoviesListAdapter.setMoviesData(null);
            setCurrentMovieListType(TOP_RATED_MOVIE_LIST_TYPE);
        }

        if (itemId == R.id.show_favorites_action) {
            mMoviesListAdapter.setMoviesData(null);
            setCurrentMovieListType(FAVORITES_MOVIE_LIST_TYPE);
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        String movieExtraKey = getString(R.string.movie_extra_key);
        intent.putExtra(movieExtraKey, movie);
        startActivity(intent);
    }

    private void setCurrentMovieListType(int listType) {
        mCurrentMovieListType = listType;
        loadMoviesList(mCurrentMovieListType);
    }

    private void showLoading() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mEmptyListText.setVisibility(View.INVISIBLE);
        mMoviesListAdapter.setMoviesData(null);
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) {
            return 2;
        }
        return nColumns;
    }

    private void loadMoviesList(int listType) {
        updateTitleForListType(listType);

        if (!mNeedReload && mLoadedMovies != null) {
            updateList(mLoadedMovies);
            return;
        }

        mNeedReload = false;

        switch (listType) {
            case POPULAR_MOVIE_LIST_TYPE:
                new FetchMoviesTask().execute(TheMoviesDbApiClient.POPULAR_MOVIES);
                break;
            case TOP_RATED_MOVIE_LIST_TYPE:
                new FetchMoviesTask().execute(TheMoviesDbApiClient.TOP_RATED_MOVIES);
                break;
            case FAVORITES_MOVIE_LIST_TYPE:
                requestFavoriteMovies();
                break;
            default:
                throw new UnsupportedOperationException("Unknown list type: " + listType);
        }
    }

    private void updateTitleForListType(int listType) {
        String listTypeName;

        switch (listType) {
            case FAVORITES_MOVIE_LIST_TYPE:
                listTypeName = getString(R.string.favorites);
                break;
            case POPULAR_MOVIE_LIST_TYPE:
                listTypeName = getString(R.string.popular_movies);
                break;
            case TOP_RATED_MOVIE_LIST_TYPE:
                listTypeName = getString(R.string.top_rated_movies);
                break;
            default:
                throw new UnsupportedOperationException("Unknown list type: " + listType);
        }

        setTitle(DEFAULT_TITLE + ": " + listTypeName);
    }

    private void requestFavoriteMovies() {
        showLoading();
        new FavoritesManager(this, this).requestFavorites();
    }

    private void updateList(Movie[] movies) {
        mLoadedMovies = movies;
        if (movies != null && movies.length > 0) {
            mMoviesListAdapter.setMoviesData(movies);
            restoreLayoutPosition();
        } else {
            mEmptyListText.setVisibility(View.VISIBLE);
            mMoviesListAdapter.setMoviesData(null);
        }
    }

    @Override
    public void loadFavorites(Movie[] favorites) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        updateList(favorites);
    }

    private class FetchMoviesTask extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
            mEmptyListText.setVisibility(View.INVISIBLE);
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

            updateList(movies);
        }
    }
}
