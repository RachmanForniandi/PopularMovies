package com.poissondumars.popularmovies.data;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.poissondumars.popularmovies.API.TheMoviesDbApiClient;
import com.poissondumars.popularmovies.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by admin on 28.07.17.
 */

public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.MoviesListAdapterViewHolder> {

    class MoviesListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mPosterView;

        MoviesListAdapterViewHolder(View view) {
            super(view);

            view.setOnClickListener(this);
            mPosterView = (ImageView) view.findViewById(R.id.iv_poster);
        }

        @Override
        public void onClick(View v) {
            int selectedItemIdx = getAdapterPosition();
            Movie selectedMovie = mMoviesData[selectedItemIdx];
            mClickHandler.onClick(selectedMovie);
        }
    }

    public interface MoviesListAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    private final MoviesListAdapterOnClickHandler mClickHandler;

    private Movie[] mMoviesData;

    public MoviesListAdapter(MoviesListAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public MoviesListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_grid_item, parent, false);

        MoviesListAdapterViewHolder viewHolder = new MoviesListAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MoviesListAdapterViewHolder holder, int position) {
        Movie movie = mMoviesData[position];

        if (movie == null) return;

        Uri imageUri = TheMoviesDbApiClient.buildUriForImage(movie.posterPath);

        if (imageUri != null) {
            Context context = holder.itemView.getContext();
            final ImageView imageView = holder.mPosterView;
            Picasso.with(context).load(imageUri)
                    .error(R.drawable.no_image)
                    .placeholder( R.drawable.progress_animation )
                    .fit()
                    .into(imageView);
        }
    }

    @Override
    public int getItemCount() {
        if (null == mMoviesData) return 0;
        return mMoviesData.length;
    }

    public void setMoviesData(Movie[] moviesData) {
        mMoviesData = moviesData;
        notifyDataSetChanged();
    }

}
