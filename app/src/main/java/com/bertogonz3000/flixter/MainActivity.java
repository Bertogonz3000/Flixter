package com.bertogonz3000.flixter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.bertogonz3000.flixter.models.Config;
import com.bertogonz3000.flixter.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    //constants
    //base URL for the API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";

    //The parameter name for the API key
    public static final String API_KEY_PARAM = "api_key";

    //tag for logging from this activity
    public final static String TAG = "MainActivity";

    //Instance fields
    AsyncHttpClient client;

    //The list of currently playing movies
    ArrayList<Movie> movies;

    //The recycler view
    RecyclerView rvMovies;

    //the adapter weired to the recycler view
    MovieAdapter adapter;

    //image config
    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize client
        client = new AsyncHttpClient();

        //Initialize the list of movies
        movies = new ArrayList<>();

        //Initialize adapter after list -- movies array cannot be reinitialized after this point
        adapter = new MovieAdapter(movies);

        //Resolve the recycler view and connect a layout manager and the adapter
        rvMovies = (RecyclerView) findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);

        //get the configuration on app creation
        getConfiguration();


    }

    //Get the list of currently playing movies from the API
    private void getNowPlaying() {
        //create the URL to access
        String url = API_BASE_URL + "/movie/now_playing";

        //Set the request parameters (appended to URL)
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); //API key, always required
        //execute a GET request using the client and expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Load the results into movies list
                try {
                    JSONArray results = response.getJSONArray(("results"));

                    //Iterate through result set and create Movie objects
                    for (int i = 0; i < results.length(); i++){
                        Movie movie = new Movie(results.getJSONObject(i));
                        movies.add(movie);

                        //notify adapter that a row was added
                        adapter.notifyItemInserted(movies.size() - 1);
                    }

                    Log.i(TAG, String.format("Loaded %s movies", results.length()));

                } catch (JSONException e) {
                    logError("Failed to parse now playing", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from nowPlaying endpoint", throwable, true);
            }
        });
    }

    //get the configuration from the API
    private void getConfiguration() {
        //create the URL to access
        String url = API_BASE_URL + "/configuration";

        //Set the request parameters (appended to URL)
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); //API key, always required
        //execute a GET request using the client and expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //get the imaage base url
                try {

                    config = new Config(response);

                    Log.i(TAG
                            , String.format
                            ("Loaded configuration with imageBaseUrl %s and postersize %s",
                                    config.getImageBaseUrl()
                                    , config.getPosterSize()));

                    // pass config to adapter
                    adapter.setConfig(config);

                    //get the now playing movie list
                    getNowPlaying();

                } catch (JSONException e) {
                    logError("Failed parsing configuration", e, true);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed getting configuration", throwable, true);
            }
        });
    }

    //Handle Errors, log and alert user
    private void logError(String message, Throwable error, boolean alertUser){
        //always log the error
        Log.e(TAG, message, error);
        //Alert the user to avoid silent errors
        if (alertUser){
            //Show a long toast with error message
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT). show();
        }
    }

}
