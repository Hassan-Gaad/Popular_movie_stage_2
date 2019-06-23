package com.example.popmovieapp;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(tableName = "preferred_movies")
public class Movie implements Parcelable {
     private String title,release,poster,overview,rate;
     @PrimaryKey
     @NonNull
     private int id;


    public Movie(int id,String title,String release,String poster,String overview,String rate){
        this.overview=overview;
        this.poster=poster;
        this.rate=rate;
        this.release=release;
        this.title=title;
        this.id=id;
    }

    public Movie(){}



    //////////////
    protected Movie(Parcel in) {
        title = in.readString();
        release = in.readString();
        poster = in.readString();
        overview = in.readString();
        rate = in.readString();
        id = in.readInt();
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

    ////////////////////////

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(release);
        dest.writeString(poster);
        dest.writeString(overview);
        dest.writeString(rate);
        dest.writeInt(id);
    }
}
