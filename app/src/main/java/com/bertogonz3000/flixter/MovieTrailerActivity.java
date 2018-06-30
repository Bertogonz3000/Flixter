package com.bertogonz3000.flixter;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bertogonz3000.flixter.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class MovieTrailerActivity extends YouTubeBaseActivity {

    //The base URL for the API site
    String API_BASE_URL = "https://api.themoviedb.org/3";

    String API_KEY_PARAM = "api_key";

    Movie movie;

    AsyncHttpClient client;

    public final static String TAG = "MovieTrailerActivity";

    String videoId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_trailer);

        client = new AsyncHttpClient();

        //unwrap the movie passed in via intent, using its simple name
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));

        getTrailer();
    }


    //Get the list of currently playing movies from the API
    private void getTrailer() {
        //create the URL to access
        String url = API_BASE_URL + "/movie/" + movie.getId() + "/videos";

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

                    //Set default video
                    JSONObject trailer_first = results.getJSONObject(0);
                    videoId = trailer_first.getString("key");

                    //Iterate through result set and create Movie objects
                    for (int i = 0; i < results.length(); i++){
                        JSONObject trailer = results.getJSONObject(i);
                        String type = trailer.getString("type");

                        if(type.equals("Trailer")){
                            videoId = trailer.getString("key");
                            break;
                        }
                    }


                    Log.i(TAG, String.format("Loaded %s trailers", results.length()));
                    loadTrailer();

                } catch (JSONException e) {
                    logError("Failed to get Trailer", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from nowPlaying endpoint", throwable, true);
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


    public void loadTrailer(){
        // resolve the player view from the layout
        YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.player);

        // initialize with API key stored in secrets.xml
        playerView.initialize(getString(R.string.youtube_api_key), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer, boolean b) {
                // do any work here to cue video, play video, etc.
                youTubePlayer.cueVideo(videoId);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {
                // log the error
                Log.e("MovieTrailerActivity", "Error initializing YouTube player");
            }
        });
    }

}
