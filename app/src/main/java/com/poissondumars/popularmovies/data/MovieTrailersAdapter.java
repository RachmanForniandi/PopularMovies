package com.poissondumars.popularmovies.data;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

public class MovieTrailersAdapter {

    class MovieTrailersAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {



        MovieTrailersAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

}
