package com.example.popmovieapp.Database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.popmovieapp.MainActivity;
import com.example.popmovieapp.Movie;

public class MainViewModel extends AndroidViewModel {
  String TAG= MainActivity.class.getSimpleName();

  private LiveData<Movie[]> movies;

    public MainViewModel(@NonNull Application application) {
        super(application);
         MovieDatabase database=MovieDatabase.getInstance(this.getApplication());
        Log.d(TAG,"Retrieving movies from the database");

         movies=database.movieDao().loadAllMovie();
    }

    public LiveData<Movie[]> getMovies() {
        return movies;
    }
}
