package com.poissondumars.popularmovies.data;

import com.google.gson.annotations.SerializedName;

public class ApiResponse<T> {

    public Integer id;

    public Integer page;

    @SerializedName("total_pages")
    public Integer totalPages;

    @SerializedName("total_results")
    public Integer totalResults;

    public T[] results;


}
