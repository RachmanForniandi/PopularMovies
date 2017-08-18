package com.poissondumars.popularmovies;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poissondumars.popularmovies.api.TheMoviesDbApiClient;
import com.poissondumars.popularmovies.data.FavoritesManager;
import com.poissondumars.popularmovies.data.Movie;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieInfoFragment extends MovieFragment {

    @BindView(R.id.tv_title)
    TextView mTitleTextView;

    @BindView(R.id.iv_poster)
    ImageView mPosterImageView;

    @BindView(R.id.tv_overview)
    TextView mOverviewTextView;

    @BindView(R.id.tv_rating)
    TextView mRatingTextView;

    @BindView(R.id.tv_release_date)
    TextView mReleaseDateTextView;

    @BindView(R.id.fl_supplementary_container)
    FrameLayout mSupplementaryContainer;

    private static final Integer FAVORITE_BUTTON_STATE_OFF = 0;
    private static final Integer FAVORITE_BUTTON_STATE_ON = 1;

    private FavoritesManager mFavoritesManager;

    public MovieInfoFragment() { }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_info, container, false);
        ButterKnife.bind(this, view);

        mFavoritesManager = new FavoritesManager(this.getContext());

        restoreInstanceState(savedInstanceState);
        configureViewsWithMovieData(mMovie);

        return view;
    }

    @Override
    protected void configureViewsWithMovieData(Movie movie) {
        if (movie == null) return;

        mTitleTextView.setText(movie.title);
        mOverviewTextView.setText(movie.overview);
        String ratingViewText = getString(R.string.rating_tv_template, movie.popularity);
        mRatingTextView.setText(ratingViewText);

        if (movie.releaseDate != null) {
            String dateFormat = getString(R.string.detail_activity_date_format);
            DateFormat df = new SimpleDateFormat(dateFormat);
            String releasedDateViewText = getString(R.string.release_date_tv_template, df.format(movie.releaseDate));
            mReleaseDateTextView.setText(releasedDateViewText);
        }

        boolean isFavorite = mFavoritesManager.isFavorite(mMovie);
        updateFavoriteActionButton(isFavorite);

        Uri posterUri = TheMoviesDbApiClient.buildUriForImage(movie.backdropPath, "w342");
        Picasso.with(this.getContext()).load(posterUri)
                .error(R.drawable.no_image)
                .placeholder( R.drawable.progress_animation )
                .into(mPosterImageView);
    }

    public void onFavoriteActionButtonClick(View v) {
        boolean activated = ((Integer) v.getTag()).equals(FAVORITE_BUTTON_STATE_ON);
            updateFavoriteActionButton(!activated);
        if (activated) {
            mFavoritesManager.removeFromFavorites(mMovie.id);
        } else {
            mFavoritesManager.saveToFavorites(mMovie);
        }
    }

    private void updateFavoriteActionButton(boolean activate) {
        int buttonStyle;
        Integer buttonTag;
        if (activate) {
            buttonStyle = R.style.FavoriteButtonActivated;
            buttonTag = FAVORITE_BUTTON_STATE_ON;
        } else {
            buttonStyle = R.style.FavoriteButtonDeactivated;
            buttonTag = FAVORITE_BUTTON_STATE_OFF;
        }

        Button favoriteButton = new Button(this.getContext(), null, 0, buttonStyle);
        favoriteButton.setTag(buttonTag);
        favoriteButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFavoriteActionButtonClick(v);
            }
        });
        mSupplementaryContainer.removeAllViews();
        mSupplementaryContainer.addView(favoriteButton);
    }

}
