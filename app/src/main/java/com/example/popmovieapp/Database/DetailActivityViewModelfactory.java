package com.example.popmovieapp.Database;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class DetailActivityViewModelfactory extends ViewModelProvider.NewInstanceFactory {
    private final MovieDatabase mDb;
    private final int mMovieId;

    public DetailActivityViewModelfactory(MovieDatabase mDb, int mMovieId) {
        this.mDb = mDb;
        this.mMovieId = mMovieId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailActivityViewModel(mDb,mMovieId);


    }
}
