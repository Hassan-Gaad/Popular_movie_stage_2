package com.example.popmovieapp.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.popmovieapp.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM preferred_movies")
    LiveData<Movie[]> loadAllMovie() ;

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

    @Query("SELECT * FROM preferred_movies WHERE id=:ID")
    LiveData<Movie> loadMovieById(int ID);
}
