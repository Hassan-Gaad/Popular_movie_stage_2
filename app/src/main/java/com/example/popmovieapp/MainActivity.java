package com.example.popmovieapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.popmovieapp.Database.MainViewModel;
import com.example.popmovieapp.Database.MovieDatabase;
import com.example.popmovieapp.Utils.JSONUtlis;
import com.example.popmovieapp.Utils.MovieExcuter;
import com.example.popmovieapp.Utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements movieAdapter.itemClickListener {

    private movieAdapter movieAdapter;
    private RecyclerView recyclerView;
    ProgressBar progressBar;
    String defaultQuery="popular";
    Movie[] movies;
    private static final String TAG = "MainActivity";
    MovieDatabase mDB;
    private static final String BUNDLE_RECYCLER_LAYOUT = "ManiActivityRecyclerView";
    Parcelable savedRecyclerLayoutState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int span_count = spanCount(getApplicationContext());
        setTitle(defaultQuery);
        recyclerView=findViewById(R.id.rv_recycleView);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,span_count);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        if (savedInstanceState != null) {
            savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            Objects.requireNonNull(recyclerView.getLayoutManager()).onRestoreInstanceState(savedRecyclerLayoutState);

            if (savedInstanceState.containsKey("movies")) {
                movies = (Movie[]) savedInstanceState.getParcelableArray("movies");
            }
            defaultQuery=savedInstanceState.getString("defaultQuery");
        }

        movieAdapter=new movieAdapter(movies,MainActivity.this) ;
        recyclerView.setAdapter(movieAdapter);
        progressBar=findViewById(R.id.progressBar3);
        getMovieData();




    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
        outState.putParcelableArray("movies",movies);
        outState.putString("defaultQuery",defaultQuery);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, Objects.requireNonNull(recyclerView.getLayoutManager()).onSaveInstanceState());

    }



    private void actionCheck(){
        setTitle(defaultQuery);


        switch (defaultQuery) {
            case "popular":
                setTitle("Popular");

                    getMovieData();



                break;
            case "top_rated":
                setTitle("Top rated");
                    getMovieData();

                break;
            case "Favorite":
                setTitle("Favorite");
                    getFavoriteMovieData();


                break;
        }

        }


    private void getFavoriteMovieData(){
        setTitle("Favorite");
        MainViewModel viewModel= ViewModelProviders.of(this).get(MainViewModel.class);
       viewModel.getMovies().observe(this, new Observer<Movie[]>() {
           @Override
           public void onChanged(@Nullable Movie[] Movies) {
               movies=Movies;
               movieAdapter=new movieAdapter(Movies,MainActivity.this) ;
               recyclerView.setAdapter(movieAdapter);
           }
       });

    }

    public  boolean isConnected(){
        ConnectivityManager connectivityManager=(ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager!=null){
            NetworkInfo info=connectivityManager.getActiveNetworkInfo();
            return info != null && info.isConnected();
        }
        return false;
    }

    @Override
    public void onItemClick(int clickedItem) {
        Context context = this;
        Class destinationClass = DetailActivity.class;

        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, clickedItem);
        intentToStartDetailActivity.putExtra("id",movies[clickedItem].getId());
        intentToStartDetailActivity.putExtra("title", movies[clickedItem].getTitle());
        intentToStartDetailActivity.putExtra("poster", movies[clickedItem].getPoster());
        intentToStartDetailActivity.putExtra("rate", movies[clickedItem].getRate());
        intentToStartDetailActivity.putExtra("release", movies[clickedItem].getRelease());
        intentToStartDetailActivity.putExtra("overview", movies[clickedItem].getOverview());
        startActivity(intentToStartDetailActivity);

    }


    private void getMovieData() {

        String querySearch = defaultQuery;
        if (isConnected()) {
            new MovieQueryTask().execute(querySearch);
        }else {
            Toast.makeText(MainActivity.this, "No network connection", Toast.LENGTH_SHORT).show();

        }
    }


    private void showErrorMessage() {
        recyclerView.setVisibility(View.INVISIBLE);
        Toast.makeText(this, "something went wrong :(", Toast.LENGTH_SHORT).show();
    }
    //calculate number of column fit to screen
    public static int spanCount(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int columns = (int) (dpWidth / 180);
        return columns;
    }


    public class MovieQueryTask extends AsyncTask<String,Void,Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... strings) {
           if (strings.length==0){
               return null;
           }
           String sortType=strings[0];
           URL movieUrl= NetworkUtils.buildUrl(sortType);
            Log.d(TAG,"url Query "+movieUrl.toString());
           try{
              String urlResponseStr=NetworkUtils.getResponseFromHttpUrl(movieUrl);
               Log.d(TAG,"Url response "+urlResponseStr);
               movies= JSONUtlis.getMovieInfo(urlResponseStr);
                return movies;
           } catch (IOException | JSONException e) {
               e.printStackTrace();
               return null;
           }

        }



        @Override
        protected void onPostExecute(Movie[] movies) {
            progressBar.setVisibility(View.INVISIBLE);
            if (movies !=null){
               movieAdapter=new movieAdapter(movies,MainActivity.this) ;
                recyclerView.setAdapter(movieAdapter);
                Objects.requireNonNull(recyclerView.getLayoutManager()).onRestoreInstanceState(savedRecyclerLayoutState);

            }else{
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int menuItemSelected = item.getItemId();

        if (menuItemSelected == R.id.action_popular) {
            defaultQuery = "popular";
            setTitle("Popular");
            getMovieData();
            return true;
        }

        if (menuItemSelected == R.id.top_rated) {
            defaultQuery = "top_rated";
            setTitle("Top rated");
            getMovieData();
            return true;
        }
        if (menuItemSelected == R.id.action_refresh) {
            actionCheck();
            return true;
        }if (menuItemSelected==R.id.favorites){
            defaultQuery="Favorite";
            getFavoriteMovieData();
        }


        return super.onOptionsItemSelected(item);
    }
}
