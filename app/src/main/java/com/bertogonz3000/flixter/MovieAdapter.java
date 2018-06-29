package com.bertogonz3000.flixter;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bertogonz3000.flixter.models.Config;
import com.bertogonz3000.flixter.models.GlideApp;
import com.bertogonz3000.flixter.models.Movie;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    //List of movies
    ArrayList<Movie> movies;

    //config needed for ima ge urls
    Config config;

    //Context for rendering
    Context context;

    //initialize with list
    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    //Creates and inflates a new view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //get the context andcreate the inflated
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //Create the view using the item_movie layout
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);
        //return a new ViewHolder
        return new ViewHolder(movieView);
    }

    //Binds an inflated view to a new item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    //get the movie data at the specfied position
        Movie movie = movies.get(position);
        //Populate the view with the movie data
         holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());

        //Determine the current orientation
        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        //Build url for poster image
        String imageUrl = null;

        //If in portrait mode, load the poster image
        if (isPortrait){
            imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());
        } else {
            //If orientation is landscape...
            imageUrl = config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath());
        }

        //Get the correct placeholder and an imageview for the current orientation
        int placeholderId = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
        ImageView imageView = isPortrait ? holder.ivPosterImage : holder.ivBackdropImage;



        //Load image using Glide
        GlideApp.with(context)
                .load(imageUrl)
                .transform(new RoundedCornersTransformation(25, 0))
                .placeholder(placeholderId)
                .error(placeholderId)
                .into(imageView);


    }

    // Returns the size of the entire dataset
    @Override
    public int getItemCount() {
        return movies.size();
    }

    //Create the viewholder as a static inner class
    public static class ViewHolder extends RecyclerView.ViewHolder {

        //track view objects - nullable because its possible that they won't exist
       @Nullable @BindView(R.id.ivPosterImage) ImageView ivPosterImage;
       @Nullable @BindView(R.id.ivBackdropImage)ImageView ivBackdropImage;
        @BindView(R.id.tvTitle)TextView tvTitle;
        @BindView(R.id.tvOverview)TextView tvOverview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //lookup view objects by id
            ButterKnife.bind(this, itemView);

        }
    }
}
