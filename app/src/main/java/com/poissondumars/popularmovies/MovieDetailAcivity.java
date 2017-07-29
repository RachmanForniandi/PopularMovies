package com.poissondumars.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.poissondumars.popularmovies.API.TheMoviesDbApiClient;
import com.poissondumars.popularmovies.data.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by aleksejsobolevskij on 29.07.17.
 */

public class MovieDetailAcivity extends AppCompatActivity {

    @BindView(R.id.tv_title) TextView mTitleTextView;
    @BindView(R.id.iv_poster) ImageView mPosterImageView;
    @BindView(R.id.tv_overview) TextView mOverviewTextView;
    @BindView(R.id.tv_rating) TextView mRatingTextView;
    @BindView(R.id.tv_release_date) TextView mReleaseDateTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);

        String movieExtraKey = getString(R.string.movie_extra_key);
        Intent intentThatOpenedThisActivity = getIntent();
        if (intentThatOpenedThisActivity.hasExtra(movieExtraKey)) {
            Movie selectedMovie = (Movie) intentThatOpenedThisActivity.getParcelableExtra(movieExtraKey);
            setUpViewsWithMovieData(selectedMovie);
        }
    }

    private void setUpViewsWithMovieData(Movie movie) {
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

        Uri posterUri = TheMoviesDbApiClient.buildUriForImage(movie.backdropPath, "w342");
        Picasso.with(this).load(posterUri)
                .error(R.drawable.no_image)
                .placeholder( R.drawable.progress_animation )
                .fit()
                .into(mPosterImageView);
    }
}
