package com.example.movieku;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    private static final String TAG = "Home";

    private RecyclerView rvMovies;
    private ImageButton buttonProfile, addItemIcon;
    private MovieAdapter movieAdapter;
    private ArrayList<Movie> movieList;

    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        rvMovies = findViewById(R.id.rvMovies);
        buttonProfile = findViewById(R.id.button_profile);
        addItemIcon = findViewById(R.id.addItemIcon);

        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvMovies.getContext(),
                DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        rvMovies.addItemDecoration(dividerItemDecoration);

        movieList = new ArrayList<>();
        movieAdapter = new MovieAdapter(movieList, this);
        rvMovies.setAdapter(movieAdapter);

        dbRef = FirebaseDatabase.getInstance().getReference("movies");

        loadReviewsFromFirebase();

        buttonProfile.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Profile.class);
            startActivity(intent);
        });

        addItemIcon.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, AddReview.class);
            startActivity(intent);
        });
    }

    private void loadReviewsFromFirebase() {
        Log.d(TAG, "loadReviewsFromFirebase called");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "onDataChange triggered");
                movieList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Movie movie = ds.getValue(Movie.class);
                    if (movie != null) {
                        Log.d(TAG, "Movie found: " + movie.getJudul());
                        movieList.add(movie);
                    } else {
                        Log.d(TAG, "Movie is NULL! Snapshot key: " + ds.getKey() + " Data: " + ds.getValue());
                    }
                }
                Log.d(TAG, "Total movies loaded: " + movieList.size());
                movieAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to read data", error.toException());
            }
        });
    }
}
