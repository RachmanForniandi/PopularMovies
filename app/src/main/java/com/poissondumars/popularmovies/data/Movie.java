package com.poissondumars.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by admin on 28.07.17.
 */

public class Movie implements Parcelable {

    public int id;
    public double popularity;
    public String title;
    public String backdropPath;
    public String posterPath;
    public String overview;
    public Date releaseDate;

    public Movie() {}

    public Movie(Parcel in) {
        id = in.readInt();
        popularity = in.readDouble();
        title = in.readString();
        backdropPath = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        // TODO: Convert date properly

    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeDouble(popularity);
        dest.writeString(title);
        dest.writeString(backdropPath);
        dest.writeString(posterPath);
        dest.writeString(overview);
    }
}
