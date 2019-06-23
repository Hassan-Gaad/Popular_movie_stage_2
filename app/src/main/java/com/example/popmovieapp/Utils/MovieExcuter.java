package com.example.popmovieapp.Utils;

import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MovieExcuter {
    private static  final Object LOCK=new Object();
    private static MovieExcuter sInstance;
    private final Executor diskIO;


    public MovieExcuter(Executor diskIO) {
        this.diskIO = diskIO;
    }

    public static MovieExcuter getInstance(){
        if (sInstance==null){
            synchronized (LOCK){
                sInstance=new MovieExcuter(Executors.newSingleThreadExecutor());
            }
        }
        return sInstance;
    }

    public Executor diskIO(){return diskIO;}

}
