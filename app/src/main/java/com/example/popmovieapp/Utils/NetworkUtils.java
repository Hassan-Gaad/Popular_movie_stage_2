package com.example.popmovieapp.Utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    // https://api.themoviedb.org/3/movie/popular?api_key={.....}&language=en-US&page=1
    final static String BASE_URL = "https://api.themoviedb.org/3/movie";
    final static String PARAM_API_KEY = "api_key";
    final static String apiKey = "3e394383a24dd9ffb4a086d6bc02d6a5";
    final static String PARAM_LANGUAGE = "language";
    final static String language = "en-US";


    public static URL buildUrl(String query){
        Uri buildUri=Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(query)
                .appendQueryParameter(PARAM_API_KEY,apiKey)
                .appendQueryParameter(PARAM_LANGUAGE,language)
                .build();
        URL url=null;
        try {
            url=new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    // https://api.themoviedb.org/3/movie  /5/  videos?api_key=...&language=en-US


    public static URL buildUrlForReviewOrVideos(String revOrVid,String movieID){
        Uri buildUri=Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(movieID)
                .appendEncodedPath(revOrVid)
                .appendQueryParameter(PARAM_API_KEY,apiKey)
                .appendQueryParameter(PARAM_LANGUAGE,language)
                .build();
        URL url=null;
        try {
            url=new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            //java.lang.System.setProperty("https.protocols", "TLSv1");
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }

        }finally {
            urlConnection.disconnect();
        }
    }
}
