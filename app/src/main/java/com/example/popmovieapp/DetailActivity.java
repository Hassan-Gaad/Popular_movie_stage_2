package com.example.popmovieapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popmovieapp.Database.DetailActivityViewModel;
import com.example.popmovieapp.Database.DetailActivityViewModelfactory;
import com.example.popmovieapp.Database.MainViewModel;
import com.example.popmovieapp.Database.MovieDatabase;
import com.example.popmovieapp.Objects.Reviews;
import com.example.popmovieapp.Objects.Trailer;
import com.example.popmovieapp.Utils.JSONUtlis;
import com.example.popmovieapp.Utils.MovieExcuter;
import com.example.popmovieapp.Utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.itemClickListener,reviewsAdapter.itemClickListenerReview{
    TextView movieRating,movieTitle,movieRelease,moviePlotSynopsis;
    ImageView moviePoster;
    ImageView iv_favoriteBtn;
    final static String REVIEWS="reviews";
    final static String VIDEOS="videos";
    private TrailerAdapter trailerAdapter;
    private RecyclerView rvTrailer;
    private reviewsAdapter reviewsAdapter;
    private RecyclerView rvReviews;
    private Trailer[] trailer;
    private Reviews[] reviews;
    int DEFAULT_MIVIE_ID=550;
    int movieID;
    String poster;
    String overview;
    String rate;
    String release;
    String title;
    boolean favorite;
    private static final String TAG = "detailActivity";
    MovieDatabase mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        iv_favoriteBtn=findViewById(R.id.imageViewFavorite);
        moviePlotSynopsis=findViewById(R.id.tv_plot_synopsis);
        moviePoster=findViewById(R.id.iv_detail_movie_poster);
        movieRating=findViewById(R.id.tv_detail_rate);
        movieRelease=findViewById(R.id.tv_detail_release_date);
        movieTitle=findViewById(R.id.tv_detail_title);
        rvTrailer=findViewById(R.id.rv_recyclerViewTrailers);
        setTitle("Details");
        if (savedInstanceState != null) {

                trailer = (Trailer[]) savedInstanceState.getParcelableArray("trailer");
                reviews=(Reviews[]) savedInstanceState.getParcelableArray("review");
                poster=savedInstanceState.getString("poster");
                overview=savedInstanceState.getString("overview");
                rate=savedInstanceState.getString("rate");
                title=savedInstanceState.getString("title");
                release=savedInstanceState.getString("release");
                movieID=savedInstanceState.getInt("id");

        }

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        rvTrailer.setLayoutManager(linearLayoutManager);
        rvTrailer.setHasFixedSize(true);
        trailerAdapter=new TrailerAdapter(trailer,this);
        rvTrailer.setAdapter(trailerAdapter);

        rvReviews=findViewById(R.id.rv_recyclerViewReviews);
        LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(this);
        rvReviews.setLayoutManager(linearLayoutManager1);
        rvReviews.setHasFixedSize(true);
        reviewsAdapter=new reviewsAdapter(reviews,this);

         poster = getIntent().getStringExtra("poster");
         overview = getIntent().getStringExtra("overview");
         rate = getIntent().getStringExtra("rate");
         release = getIntent().getStringExtra("release");
         title = getIntent().getStringExtra("title");
         movieID=getIntent().getIntExtra("id",DEFAULT_MIVIE_ID);
         favorite=getIntent().getBooleanExtra("favorite",false);
        if (favorite){
            iv_favoriteBtn.setImageResource(R.drawable.favorite_red);
        }
        movieRating.setText(rate);
        moviePlotSynopsis.setText(overview);
        movieTitle.setText(title);
        movieRelease.setText(release);

        Picasso.get()
                .load(poster)
                .placeholder(R.drawable.loading)
                .into(moviePoster);

        mDb=MovieDatabase.getInstance(getApplicationContext());
        checkIfMovieIsFavorite();
        new TrailerQueryTask().execute(VIDEOS);
        new ReviewsAsyncTask().execute(REVIEWS);



    }
    Movie queeredMove;
    public void checkIfMovieIsFavorite(){

        DetailActivityViewModelfactory modelfactory=new DetailActivityViewModelfactory(mDb,movieID);
        final DetailActivityViewModel viewModel=ViewModelProviders.of(this,modelfactory).get(DetailActivityViewModel.class);
        viewModel.getmMovie().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                viewModel.getmMovie().removeObserver(this);
                if (movie!=null){
                    iv_favoriteBtn.setImageResource(R.drawable.favorite_red);
                    x=1;
                }

            }
        });

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArray("trailer",trailer);
        outState.putParcelableArray("review",reviews);
        outState.putString("poster",poster);
        outState.putInt("movieID",movieID);
        outState.putString("title",title);
        outState.putString("release",release);
        outState.putString("overview",overview);
        outState.putString("rate",rate);
    }

    int x=0;
    public void btnFavorite(View view){

        iv_favoriteBtn.setImageResource(checkFavoriteImg(x));


    }


    public int checkFavoriteImg(int i){
         final Movie movie=new Movie(movieID,title,release,poster,overview,rate);
        //Red
        if (i==0){
            x++;
            MovieExcuter.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.movieDao().insertMovie(movie);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() { Toast.makeText(DetailActivity.this, "Movie added", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            return R.drawable.favorite_red;

        }else
            //blue
            x=0;
        MovieExcuter.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDao().deleteMovie(movie);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() { Toast.makeText(DetailActivity.this, "Movie deleted", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        return  R.drawable.favorite_blue;

    }

    @Override
    public void onItemClick(int clickedItem) {
        String KEY=trailer[clickedItem].getKey();
        watchYoutubeVideo(this,KEY);

    }



    public static void watchYoutubeVideo(Context context, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    @Override
    public void onReviewItemClicked(int clickedItem) {


    }


    public class ReviewsAsyncTask extends AsyncTask<String,Void,Reviews[]>{


        @Override
        protected Reviews[] doInBackground(String... strings) {
            if (strings==null){
                return null;
            }
            String reviewsStr=strings[0];

            URL reviewsUrl= NetworkUtils.buildUrlForReviewOrVideos(reviewsStr,String.valueOf(movieID));
            Log.d(TAG,"url Query "+reviewsUrl.toString());

            try{
                String urlResponseStr=NetworkUtils.getResponseFromHttpUrl(reviewsUrl);
                Log.d(TAG,"Url response "+urlResponseStr);
                reviews= JSONUtlis.getReviewsInfo(urlResponseStr);
                return reviews;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(Reviews[] reviews) {
            super.onPostExecute(reviews);
            if (reviews!=null){
                reviewsAdapter=new reviewsAdapter(reviews,DetailActivity.this);
                rvReviews.setAdapter(reviewsAdapter);
            }else{
                Toast.makeText(DetailActivity.this, "NO Reviews found", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public  class TrailerQueryTask extends AsyncTask<String,Void,Trailer[]> {

        @Override
        protected Trailer[] doInBackground(String... strings) {
            if (strings==null){
                return null;
            }
            String videos=strings[0];
            URL trailersUrl= NetworkUtils.buildUrlForReviewOrVideos(videos,String.valueOf(movieID));
            Log.d(TAG,"url Query "+trailersUrl.toString());

            try{
                String urlResponseStr=NetworkUtils.getResponseFromHttpUrl(trailersUrl);
                Log.d(TAG,"Url response "+urlResponseStr);
                trailer= JSONUtlis.getTrailerInfo(urlResponseStr);
                return trailer;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }

        }
        @Override
        protected void onPostExecute(Trailer[] trailers) {
            super.onPostExecute(trailers);

            if (trailers !=null){
                trailerAdapter=new TrailerAdapter(trailers,DetailActivity.this) ;
                rvTrailer.setAdapter(trailerAdapter);

            }else
                Toast.makeText(DetailActivity.this, "NO trailers found", Toast.LENGTH_SHORT).show();
        }


    }
}
