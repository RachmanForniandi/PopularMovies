package com.poissondumars.popularmovies.api;


import android.net.Uri;
import android.support.annotation.Nullable;

import com.poissondumars.popularmovies.BuildConfig;
import com.poissondumars.popularmovies.data.Review;
import com.poissondumars.popularmovies.data.Trailer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by admin on 28.07.17.
 */

public class TheMoviesDbApiClient {

    private static final String API_BASE = "https://api.themoviedb.org/3/";
    private static final String IMAGE_BASE = "http://image.tmdb.org/t/p/";

    //    Api routes
    public static final String POPULAR_MOVIES = "movie/popular";
    public static final String TOP_RATED_MOVIES = "movie/top_rated";
    public static final String MOVIE_TRAILERS= "movie/{id}/videos";
    public static final String MOVIE_REVIEWS= "movie/{id}/reviews";

    //    Params names
    private static final String API_KEY_PARAM = "api_key";

    //    Params values
    private static final String API_KEY = BuildConfig.API_KEY;

    private static Uri.Builder buildBaseUri() {
        return Uri.parse(API_BASE).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY);
    }

    @Nullable
    private static URL buildUrlFromUri(Uri uri) {
        URL requestUrl = null;
        try {
            requestUrl = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return requestUrl;
    }

    public static String getMoviesList(String listPath) {
        return performRequestWithPath(listPath);
    }

    @Nullable
    public static <T> String getMovieEntities(Class<T> tClass, int id) {
        if (tClass.equals(Trailer.class)) {
            return getTrailersForMovie(id);
        } else if (tClass.equals(Review.class)) {
            return getReviewsForMovie(id);
        }

        throw new UnsupportedOperationException("Unsupported entity class: " + tClass.getSimpleName());
    }

    @Nullable
    public static String getReviewsForMovie(int id) {
        String trailersPath = MOVIE_REVIEWS.replace("{id}", "" + id);
        return performRequestWithPath(trailersPath);
    }

    @Nullable
    public static String getTrailersForMovie(int id) {
        String trailersPath = MOVIE_TRAILERS.replace("{id}", "" + id);
        return performRequestWithPath(trailersPath);
    }

    @Nullable
    public static Uri buildUriForImage(String path, String imageSize) {
        return Uri.parse(IMAGE_BASE).buildUpon()
                .appendEncodedPath(imageSize)
                .appendEncodedPath(path)
                .build();
    }

    @Nullable
    public static Uri buildUriForImage(String path) {
        String imageSize = "w185";
        return buildUriForImage(path, imageSize);
    }

    @Nullable
    private static String getResponseFromUrl(URL requestUrl) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) requestUrl.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }

    }

    @Nullable
    private static String performRequestWithPath(String path) {
        Uri uri = buildBaseUri()
                .appendEncodedPath(path)
                .build();

        String response = null;
        URL requestUrl = buildUrlFromUri(uri);
        if (requestUrl != null) {
            try {
                response = getResponseFromUrl(requestUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return response;
    }

}
