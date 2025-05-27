package com.example.movieku;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList;
    private Context context;

    public MovieAdapter(List<Movie> movieList, Context context) {
        this.movieList = movieList;
        this.context = context;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.tvTitle.setText(movie.getJudul());

        // Set rating ke rating bar, default 0 jika null
        Float rating = movie.getRating();
        holder.ratingBar.setRating(rating != null ? rating : 0f);

        // Set ulasan, jika null tampil string kosong
        String ulasan = movie.getUlasan();
        holder.tvReview.setText(ulasan != null ? ulasan : "");

        Glide.with(context)
                .load(movie.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imgMovie);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Detail.class);
            intent.putExtra("movie", movie);  // Pastikan Movie implements Serializable atau Parcelable
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvReview;
        RatingBar ratingBar;
        ImageView imgMovie;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvReview = itemView.findViewById(R.id.tvReview);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            imgMovie = itemView.findViewById(R.id.imgMovie);
        }
    }
}
