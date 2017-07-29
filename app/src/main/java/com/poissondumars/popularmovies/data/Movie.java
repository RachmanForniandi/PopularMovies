package com.poissondumars.popularmovies.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by admin on 28.07.17.
 */

public class Movie implements Serializable {

    public int id;
    public double popularity;
    public String title;
    public String thumbnailPath;
    public String posterPath;
    public String overview;
    public Date releaseDate;

}
