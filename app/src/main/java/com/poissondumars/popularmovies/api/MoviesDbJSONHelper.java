package com.poissondumars.popularmovies.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.poissondumars.popularmovies.R;
import com.poissondumars.popularmovies.data.ApiResponse;
import com.poissondumars.popularmovies.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MoviesDbJSONHelper {

    // Movie fields
    private static final String OWN_ID = "id";
    private static final String OWN_POPULARITY = "vote_average";
    private static final String OWN_TITLE = "title";
    private static final String OWN_POSTER = "poster_path";
    private static final String OWN_BACKDROP = "backdrop_path";
    private static final String OWN_OVERVIEW = "overview";
    private static final String OWN_RELEASE_DATE = "release_date";

    public static Movie[] getMoviesFromJson(Context context, String moviesListJson) throws JSONException {
        Gson gson = new Gson();
        Type collectionType = new TypeToken<ApiResponse<Movie>>() {}.getType();
        ApiResponse<Movie> response = gson.fromJson(moviesListJson, collectionType);

        // TODO: Parse response

        final String OWN_RESULTS = "results";

        Movie[] movies = null;

        JSONObject jsonObject = new JSONObject(moviesListJson);

        if (jsonObject.has(OWN_RESULTS)) {
            JSONArray resultsArray = jsonObject.getJSONArray(OWN_RESULTS);
            movies = new Movie[resultsArray.length()];

            for(int i = 0; i < resultsArray.length(); i++) {
                movies[i] = getMovieFromJson(context, resultsArray.getString(i));
            }
        }

        return movies;
    }

    private static Movie getMovieFromJson(Context context, String movieJson) throws JSONException {

        Movie movie = null;
        JSONObject movieJsonObject = new JSONObject(movieJson);

        if (isMovieJsonObjectCorrect(movieJsonObject)) {
            movie = new Movie();
            movie.id = movieJsonObject.getInt(OWN_ID);
            movie.overview = movieJsonObject.getString(OWN_OVERVIEW);
            movie.title = movieJsonObject.getString(OWN_TITLE);
            movie.popularity = movieJsonObject.getDouble(OWN_POPULARITY);
            movie.posterPath = movieJsonObject.getString(OWN_POSTER);
            movie.backdropPath = movieJsonObject.getString(OWN_BACKDROP);

            try {
                String dateFormat = context.getString(R.string.api_date_format);
                DateFormat df = new SimpleDateFormat(dateFormat);
                String releaseDateString = movieJsonObject.getString(OWN_RELEASE_DATE);
                Date releaseDate = df.parse(releaseDateString);
                String newDateString = df.format(releaseDate);
                movie.releaseDate = releaseDate;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return  movie;
    }

    private static boolean isMovieJsonObjectCorrect(JSONObject movieJsonObject) {
        return movieJsonObject.has(OWN_ID) &&
                movieJsonObject.has(OWN_OVERVIEW) &&
                movieJsonObject.has(OWN_POPULARITY) &&
                movieJsonObject.has(OWN_POSTER) &&
                movieJsonObject.has(OWN_BACKDROP) &&
                movieJsonObject.has(OWN_RELEASE_DATE) &&
                movieJsonObject.has(OWN_TITLE);
    }
}
