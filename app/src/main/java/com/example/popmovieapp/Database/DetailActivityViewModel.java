package com.example.popmovieapp.Database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.popmovieapp.Movie;

public class DetailActivityViewModel extends ViewModel {
    private LiveData<Movie>mMovie;

    public DetailActivityViewModel(MovieDatabase mDb,int id) {

        mMovie = mDb.movieDao().loadMovieById(id);
    }

    public LiveData<Movie> getmMovie() {
        return mMovie;
    }
}
