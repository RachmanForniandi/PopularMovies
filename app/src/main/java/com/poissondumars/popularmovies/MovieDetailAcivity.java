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

/**
 * Created by aleksejsobolevskij on 29.07.17.
 */

public class MovieDetailAcivity extends AppCompatActivity {

    private TextView mTitleTextView;
    private ImageView mPosterImageView;
    private TextView mOverviewTextView;
    private TextView mRatingTextView;
    private TextView mReleaseDateTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mTitleTextView = (TextView) findViewById(R.id.tv_title);
        mPosterImageView = (ImageView) findViewById(R.id.iv_poster);
        mOverviewTextView = (TextView) findViewById(R.id.tv_overview);
        mRatingTextView = (TextView) findViewById(R.id.tv_rating);
        mReleaseDateTextView = (TextView) findViewById(R.id.tv_release_date);

        Intent intentThatOpenedThisActivity = getIntent();
        if (intentThatOpenedThisActivity.hasExtra("movie")) {
            Movie selectedMovie = (Movie) intentThatOpenedThisActivity.getSerializableExtra("movie");
            setUpViewsWithMovieData(selectedMovie);
        }
    }

    private void setUpViewsWithMovieData(Movie movie) {
        mTitleTextView.setText(movie.title);
        mOverviewTextView.setText(movie.overview);
        mRatingTextView.setText("Rating: " + String.valueOf(movie.popularity));

        if (movie.releaseDate != null) {
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            mReleaseDateTextView.setText("Released at " + df.format(movie.releaseDate));
        }

        Uri posterUri = TheMoviesDbApiClient.buildUriForImage(movie.backdropPath, "w342");
        Picasso.with(this).load(posterUri)
                .error(R.drawable.no_image)
                .placeholder( R.drawable.progress_animation )
                .fit()
                .into(mPosterImageView);
    }
}
