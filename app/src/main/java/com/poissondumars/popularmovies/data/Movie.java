package com.poissondumars.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Movie implements Parcelable {

    public int id;

    @SerializedName("vote_average")
    public double popularity;

    public String title;

    @SerializedName("backdrop_path")
    public String backdropPath;

    @SerializedName("poster_path")
    public String posterPath;

    public String overview;

    @SerializedName("release_date")
    public Date releaseDate;

    public boolean isFavorite;

    public Movie() {}

    public Movie(Parcel in) {
        id = in.readInt();
        popularity = in.readDouble();
        title = in.readString();
        backdropPath = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        releaseDate = (Date) in.readSerializable();
        isFavorite = in.readInt() == 1;
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
        dest.writeSerializable(releaseDate);
        dest.writeInt(isFavorite ? 1 : 0);
    }
}
