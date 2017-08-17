package com.poissondumars.popularmovies.data;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poissondumars.popularmovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.MovieReviewsAdapterViewHolder> {

    class MovieReviewsAdapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_review_author_name)
        TextView reviewAuthorNameTextView;

        @BindView(R.id.tv_review_text)
        TextView reviewTextView;

        MovieReviewsAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private Review[] mReviews;

    @Override
    public MovieReviewsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.movie_review_item, parent, false);

        return new MovieReviewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieReviewsAdapterViewHolder holder, int position) {
        Review review = mReviews[position];

        if (review == null) return;

        holder.reviewAuthorNameTextView.setText(review.author);
        holder.reviewTextView.setText(review.content);
    }

    @Override
    public int getItemCount() {
        if (mReviews == null) return 0;
        return mReviews.length;
    }

    public void setReviews(Review[] reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }

}
