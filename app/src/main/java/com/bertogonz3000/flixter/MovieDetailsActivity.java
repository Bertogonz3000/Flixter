package com.bertogonz3000.flixter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bertogonz3000.flixter.models.Movie;

import org.parceler.Parcels;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;
    TextView tvTitle, tvOverView;
    RatingBar rbVoteAverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        //Unwrap the movie passed via internet, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));
        //resolve view objects
        tvOverView = (TextView) findViewById(R.id.tvOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        //set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverView.setText(movie.getOverview());

        //vote average is 0....10, convert 0-5
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);
    }

    public void onClick(View view){
        //create intent for new activity
        Intent intent = new Intent(this, MovieTrailerActivity.class);
        //serialize the movie user parceler, use its short name as the key
        intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
        //Show the activity
        startActivity(intent);
    }
}
