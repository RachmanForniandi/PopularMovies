package com.poissondumars.popularmovies.API;


import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

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
    private static final String MOVIES_LIST = "discover/movie";

    //    Params names
    private static final String SORT_BY_PARAM = "sort_by";
    private static final String API_KEY_PARAM = "api_key";
    private static final String PAGE_PARAM = "page";

    //    Params values
    public static final String SORT_BY_POPULARITY = "popularity.desc";
    public static final String SORT_BY_VOTES = "vote_average.desc";
    private static final String API_KEY = "5c3de77d7bb8e1a6cc06c3cc11b8909f";

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

    public static String getMoviesList(String sortedBy) {
        Uri moviesListUri = buildBaseUri()
                .appendEncodedPath(MOVIES_LIST)
                .appendQueryParameter(SORT_BY_PARAM, sortedBy)
                .build();

        String response = "";
        URL requestUrl = buildUrlFromUri(moviesListUri);
        Log.d("TheMoviesDbApiClient", requestUrl.toString());
        if (requestUrl != null) {
            try {
                response = getResponseFromUrl(requestUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return response;
    }

    @Nullable
    public static Uri buildUriForImage(String path) {
        String imageSize = "w185";

        return Uri.parse(IMAGE_BASE).buildUpon()
                .appendEncodedPath(imageSize)
                .appendEncodedPath(path)
                .build();
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

}
