package com.example.movieku;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Detail extends AppCompatActivity {

    private Movie movie;
    private ImageView imgPoster;
    private TextView tvTitle, tvDetailsYear, tvDetailsDuration, tvDetailsGenre, tvRating, tvSinopsis, tvUlasan;
    private LinearLayout starsContainer;
    private Button btnEdit, btnDelete;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        movie = (Movie) getIntent().getSerializableExtra("movie");

        imgPoster = findViewById(R.id.imgPoster);
        tvTitle = findViewById(R.id.tvTitle);
        tvDetailsYear = findViewById(R.id.tvDetailsYear);
        tvDetailsDuration = findViewById(R.id.tvDetailsDuration);
        tvDetailsGenre = findViewById(R.id.tvDetailsGenre);
        tvRating = findViewById(R.id.tvRating);
        tvSinopsis = findViewById(R.id.tvSinopsis);
        tvUlasan = findViewById(R.id.tvUlasan);
        starsContainer = findViewById(R.id.starsContainer);
        btnEdit = findViewById(R.id.btnEditUlasan);
        btnDelete = findViewById(R.id.btnHapus);
        btnBack = findViewById(R.id.btnBack);

        if (movie != null) {
            Glide.with(this).load(movie.getImageUrl()).into(imgPoster);
            tvTitle.setText(movie.getJudul());
            tvDetailsYear.setText(movie.getTahun());
            tvDetailsDuration.setText(movie.getDurasi());
            tvDetailsGenre.setText(movie.getGenre());
            tvRating.setText(String.valueOf(movie.getRating()));
            tvSinopsis.setText(movie.getSinopsis());
            tvUlasan.setText(movie.getUlasan());

            // Tampilkan rating bintang
            showStars(movie.getRating());
        }

        btnBack.setOnClickListener(v -> onBackPressed());

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditReview.class);
            intent.putExtra("movie", movie);
            startActivity(intent);
        });

        btnDelete.setOnClickListener(v -> showDeleteDialog());
    }

    private void showStars(Float rating) {
        starsContainer.removeAllViews();
        int filledStars = rating != null ? Math.round(rating) : 0;

        for (int i = 0; i < 5; i++) {
            ImageView star = new ImageView(this);
            star.setImageResource(i < filledStars ? R.drawable.ic_star_filled : R.drawable.ic_star_outline);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    48, 48);
            params.setMargins(4, 0, 4, 0);
            star.setLayoutParams(params);
            starsContainer.addView(star);
        }
    }

    private void showDeleteDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Review")
                .setMessage("Apakah kamu yakin ingin menghapus review ini?")
                .setPositiveButton("Ya", (dialog, which) -> deleteReview())
                .setNegativeButton("Batal", null)
                .show();
    }

    private void deleteReview() {
        if (movie != null && movie.getId() != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("movies").child(movie.getId());
            ref.removeValue().addOnSuccessListener(aVoid -> {
                Toast.makeText(this, "Review dihapus", Toast.LENGTH_SHORT).show();
                finish();
            }).addOnFailureListener(e -> Toast.makeText(this, "Gagal menghapus", Toast.LENGTH_SHORT).show());
        }
    }
}
