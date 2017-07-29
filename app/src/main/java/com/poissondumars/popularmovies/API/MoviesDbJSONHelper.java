package com.poissondumars.popularmovies.API;

import android.content.Context;

import com.poissondumars.popularmovies.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 28.07.17.
 */

public class MoviesDbJSONHelper {

    private static final String OWN_ID = "id";
    private static final String OWN_POPULARITY = "vote_average";
    private static final String OWN_TITLE = "title";
    private static final String OWN_POSTER = "poster_path";
    private static final String OWN_OVERVIEW = "overview";

    public static Movie[] getMoviesFromJson(Context context, String moviesListJson) throws JSONException {

        final String OWN_RESULTS = "results";

        Movie[] movies = null;

        JSONObject jsonObject = new JSONObject(moviesListJson);

        if (jsonObject.has(OWN_RESULTS)) {
            JSONArray resultsArray = jsonObject.getJSONArray(OWN_RESULTS);
            movies = new Movie[resultsArray.length()];

            for(int i = 0; i < resultsArray.length(); i++) {
                movies[i] = getMovieFromJson(resultsArray.getString(i));
            }
        }

        return movies;
    }

    private static Movie getMovieFromJson(String movieJson) throws JSONException {

        Movie movie = null;
        JSONObject movieJsonObject = new JSONObject(movieJson);

        if (isMovieJsonObjectCorrect(movieJsonObject)) {
            movie = new Movie();
            movie.id = movieJsonObject.getInt(OWN_ID);
            movie.overview = movieJsonObject.getString(OWN_OVERVIEW);
            movie.title = movieJsonObject.getString(OWN_TITLE);
            movie.popularity = movieJsonObject.getDouble(OWN_POPULARITY);
            movie.posterPath = movieJsonObject.getString(OWN_POSTER);
        }

        return  movie;
    }

    private static boolean isMovieJsonObjectCorrect(JSONObject movieJsonObject) {
        return movieJsonObject.has(OWN_ID) &&
                movieJsonObject.has(OWN_OVERVIEW) &&
                movieJsonObject.has(OWN_POPULARITY) &&
                movieJsonObject.has(OWN_POSTER) &&
                movieJsonObject.has(OWN_TITLE);
    }
}
