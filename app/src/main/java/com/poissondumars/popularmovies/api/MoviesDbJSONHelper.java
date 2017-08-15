package com.poissondumars.popularmovies.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.poissondumars.popularmovies.data.ApiResponse;
import com.poissondumars.popularmovies.data.Movie;
import com.poissondumars.popularmovies.data.Review;
import com.poissondumars.popularmovies.data.Trailer;

import java.lang.reflect.Type;

public class MoviesDbJSONHelper {

    public static Movie[] parseMoviesFromJson(String moviesListJson) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        Type collectionType = new TypeToken<ApiResponse<Movie>>() {}.getType();
        ApiResponse<Movie> response = gson.fromJson(moviesListJson, collectionType);
        return response.results;
    }

    public static Trailer[] parseTrailersFromJson(String moviesListJson) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        Type collectionType = new TypeToken<ApiResponse<Trailer>>() {}.getType();
        ApiResponse<Trailer> response = gson.fromJson(moviesListJson, collectionType);
        return response.results;
    }

    public static Review[] parseReviewsFromJson(String moviesListJson) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        Type collectionType = new TypeToken<ApiResponse<Review>>() {}.getType();
        ApiResponse<Review> response = gson.fromJson(moviesListJson, collectionType);
        return response.results;
    }
}
