package com.bertogonz3000.flixter.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Movie {

    //Values from API
    String title;
    String overview;
    String posterPath; //only the path, not full URl
    String backdropPath;
    Double voteAverage;
    Integer id;

    //initialize from JSON data
    public Movie(JSONObject object) throws JSONException { //let throw cuz handle error back in activity
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        backdropPath = object.getString(("backdrop_path"));
        voteAverage = object.getDouble("vote_average");
        id = object.getInt("id");
    }

    //default constructor
    public Movie() {
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

    public Double getVoteAverage() {
        return voteAverage;
    }

    public Integer getId() {
        return id;
    }
}
