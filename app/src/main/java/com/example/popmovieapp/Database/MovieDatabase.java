package com.example.popmovieapp.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.example.popmovieapp.Movie;

@Database(entities = {Movie.class},version = 3,exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {
    private static final String TAG =MovieDatabase.class.getSimpleName();
    private static final Object LOCK=new Object();
    private static final String DATABASE_NAME="movielist";
    private static MovieDatabase sInstance;


    public static MovieDatabase getInstance(Context context){
        if (sInstance==null){
            synchronized (LOCK){
                Log.d(TAG,"Creating a new database instance");
                sInstance= Room.databaseBuilder(context.getApplicationContext(),
                        MovieDatabase.class,MovieDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();

            }
        }
        Log.d(TAG, "get the database Instance");
        return sInstance;
    }

public abstract MovieDao movieDao();
}
