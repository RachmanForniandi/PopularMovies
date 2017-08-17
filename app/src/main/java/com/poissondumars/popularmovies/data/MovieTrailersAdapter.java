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

public class MovieTrailersAdapter extends RecyclerView.Adapter<MovieTrailersAdapter.MovieTrailersAdapterViewHolder> {

    class MovieTrailersAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_trailer_name)
        TextView trailerNameTextView;

        MovieTrailersAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int index = getAdapterPosition();
            Trailer selectedTrailer = mTrailers[index];
            mClickHandler.onClick(selectedTrailer);
        }
    }

    public interface MovieTrailersAdapterOnClickHandler {
        void onClick(Trailer trailer);
    }

    private final MovieTrailersAdapterOnClickHandler mClickHandler;

    private Trailer[] mTrailers;

    public MovieTrailersAdapter(MovieTrailersAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public MovieTrailersAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.movie_trailer_item, parent, false);

        return new MovieTrailersAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieTrailersAdapterViewHolder holder, int position) {
        Trailer trailer = mTrailers[position];

        if (trailer == null) return;

        holder.trailerNameTextView.setText(trailer.name);
    }

    @Override
    public int getItemCount() {
        if (mTrailers == null) return 0;
        return mTrailers.length;
    }

    public void setTrailers(Trailer[] trailers) {
        mTrailers = trailers;
        notifyDataSetChanged();
    }
}
