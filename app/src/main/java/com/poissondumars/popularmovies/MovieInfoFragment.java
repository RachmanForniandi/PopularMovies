package com.poissondumars.popularmovies;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.poissondumars.popularmovies.api.TheMoviesDbApiClient;
import com.poissondumars.popularmovies.data.Movie;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieInfoFragment extends Fragment {

    public static final String MOVIE_ARGUMENT = "movie";

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

    private Movie mMovie;

    public MovieInfoFragment() { }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_info, container, false);
        ButterKnife.bind(view);
        return view;
    }

    public void setMovie(Movie movie) {
        if (mMovie != null) {
            return;
        }

        mMovie = movie;
        setUpViewsWithMovieData(mMovie);
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
        Picasso.with(this.getContext()).load(posterUri)
                .error(R.drawable.no_image)
                .placeholder( R.drawable.progress_animation )
                .fit()
                .into(mPosterImageView);
    }
}
