package com.poissondumars.popularmovies.data;

/**
 * Created by admin on 14.08.17.
 */

public class ApiResponse<T> {

    public Integer id;
    public Integer page;
    public Integer totalPages;
    public Integer totalResults;
    public T[] results;


}
