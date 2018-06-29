package com.bertogonz3000.flixter.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Movie {

    //Values from API
    private String title;
    private String overview;
    private String posterPath; //only the path, not full URl
    private String backdropPath;

    //initialize from JSON data
    public Movie(JSONObject object) throws JSONException { //let throw cuz handle error back in activity
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        backdropPath = object.getString(("backdrop_path"));

    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }
}
