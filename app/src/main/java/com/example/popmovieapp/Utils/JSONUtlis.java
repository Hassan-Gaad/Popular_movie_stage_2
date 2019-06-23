package com.example.popmovieapp.Utils;

import com.example.popmovieapp.Movie;
import com.example.popmovieapp.Objects.Reviews;
import com.example.popmovieapp.Objects.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtlis {
    final static String RESULTS="results";

    public static Reviews[] getReviewsInfo(String JsonResult)throws JSONException{
        final String AUTHOR="author";
        final String CONTENT="content";

        JSONObject reviewsJSONObj=new JSONObject(JsonResult);
        JSONArray ReviewsJSONArr=reviewsJSONObj.getJSONArray(RESULTS);
         Reviews[] allReviews=new Reviews[ReviewsJSONArr.length()];
        for (int i = 0; i < ReviewsJSONArr.length(); i++){
            Reviews reviews=new Reviews();
            String author=ReviewsJSONArr.getJSONObject(i).getString(AUTHOR);
            String content=ReviewsJSONArr.getJSONObject(i).getString(CONTENT);

            reviews.setAuthor(author);
            reviews.setContent(content);

            allReviews[i]=reviews;
        }
        return allReviews;
    }


    public static Trailer[] getTrailerInfo(String JsonResult)throws JSONException{
        final String KEY="key";
        final String NAME="name";
        final String SIZE="size";
        final String SITE="site";

        JSONObject trailerJSONObj=new JSONObject(JsonResult);
        JSONArray trailerJSONArr=trailerJSONObj.getJSONArray(RESULTS);
        Trailer[] allTrailer=new Trailer[trailerJSONArr.length()];
        for (int i = 0; i < trailerJSONArr.length(); i++){
          Trailer trailer=new Trailer();
          String name=trailerJSONArr.getJSONObject(i).getString(NAME);
          String key=trailerJSONArr.getJSONObject(i).getString(KEY);
          String site=trailerJSONArr.getJSONObject(i).getString(SITE);
          String size=trailerJSONArr.getJSONObject(i).getString(SIZE);

          trailer.setName(name);
          trailer.setKey(key);
          trailer.setSite(site);
          trailer.setSize(size);

          allTrailer[i]=trailer;
        }
        return allTrailer;
    }
    public static Movie[] getMovieInfo(String JsonResult) throws JSONException {

        final String MOVIE_ID="id";
        final String RELEASE_DATE = "release_date";
        final String VOTE = "vote_average";
        final String TITLE = "title";
        final String OVERVIEW = "overview";
        final String POSTER_PATH = "poster_path";
        final String BASE_URL = "https://image.tmdb.org/t/p/";
        final String POSTER_SIZE = "w500";
        JSONObject moviesJSONObj=new JSONObject(JsonResult);
        JSONArray movieJSONArr=moviesJSONObj.getJSONArray(RESULTS);
        Movie[] allMoviesArr = new Movie[movieJSONArr.length()];
        for (int i = 0; i < movieJSONArr.length(); i++){

            Movie movie = new Movie();
            int id=movieJSONArr.getJSONObject(i).getInt(MOVIE_ID);
            String title = movieJSONArr.getJSONObject(i).getString(TITLE);
            String posterPath = movieJSONArr.getJSONObject(i).getString(POSTER_PATH);
            String overview = movieJSONArr.getJSONObject(i).getString(OVERVIEW);
            String releaseDate = movieJSONArr.getJSONObject(i).getString(RELEASE_DATE);
            String voteAverage = movieJSONArr.getJSONObject(i).getString(VOTE);


            movie.setId(id);
            movie.setTitle(title);
            //Combining these three parts gives us a final url of
            //http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg
            movie.setPoster(BASE_URL + POSTER_SIZE + posterPath);
            movie.setOverview(overview);
            movie.setRelease(releaseDate);
            movie.setRate(voteAverage);

            allMoviesArr[i] = movie;
        }

        return allMoviesArr;
    }


}
