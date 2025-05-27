package com.example.movieku;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.example.movieku.Movie;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditReview extends AppCompatActivity {

    private ImageView imageUpload;
    private EditText etJudul, etSinopsis, etTahun, etDurasi, etGenre, etUlasan;
    private RatingBar ratingBar;
    private Button btnSimpan;

    private Uri imageUri;
    private String imageUrl = "";
    private Movie movie;
    private ProgressDialog progressDialog;

    private final int IMAGE_PICK_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review); // layout reuse dari AddReview

        // Ambil data movie dari intent
        movie = (Movie) getIntent().getSerializableExtra("movie");

        // Inisialisasi UI
        imageUpload = findViewById(R.id.imageUpload);
        etJudul = findViewById(R.id.etJudul);
        etSinopsis = findViewById(R.id.etSinopsis);
        etTahun = findViewById(R.id.etTahun);
        etDurasi = findViewById(R.id.etDurasi);
        etGenre = findViewById(R.id.etGenre);
        etUlasan = findViewById(R.id.etUlasan);
        ratingBar = findViewById(R.id.ratingBar);
        btnSimpan = findViewById(R.id.btnSimpan);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Menyimpan...");

        // Set data lama ke form
        if (movie != null) {
            etJudul.setText(movie.getJudul());
            etSinopsis.setText(movie.getSinopsis());
            etTahun.setText(movie.getTahun());
            etDurasi.setText(movie.getDurasi());
            etGenre.setText(movie.getGenre());
            etUlasan.setText(movie.getUlasan());
            ratingBar.setRating(movie.getRating());
            imageUrl = movie.getImageUrl();
            Glide.with(this).load(imageUrl).into(imageUpload);
        }

        // Upload ulang gambar
        imageUpload.setOnClickListener(v -> openGallery());

        btnSimpan.setOnClickListener(v -> {
            progressDialog.show();
            if (imageUri != null) {
                uploadToCloudinary(imageUri);
            } else {
                updateReview(imageUrl);
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageUpload.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadToCloudinary(Uri uri) {
        MediaManager.get().upload(uri).callback(new com.cloudinary.android.callback.UploadCallback() {
            @Override
            public void onStart(String requestId) { }

            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) { }

            @Override
            public void onSuccess(String requestId, Map resultData) {
                imageUrl = resultData.get("secure_url").toString();
                updateReview(imageUrl);
            }

            @Override
            public void onError(String requestId, com.cloudinary.android.callback.ErrorInfo error) {
                progressDialog.dismiss();
                Toast.makeText(EditReview.this, "Upload gagal: " + error.getDescription(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReschedule(String requestId, com.cloudinary.android.callback.ErrorInfo error) { }
        }).dispatch();
    }

    private void updateReview(String imageUrl) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("movies").child(movie.getId());

        Map<String, Object> updates = new HashMap<>();
        updates.put("judul", etJudul.getText().toString());
        updates.put("sinopsis", etSinopsis.getText().toString());
        updates.put("tahun", etTahun.getText().toString());
        updates.put("durasi", etDurasi.getText().toString());
        updates.put("genre", etGenre.getText().toString());
        updates.put("ulasan", etUlasan.getText().toString());
        updates.put("rating", ratingBar.getRating());
        updates.put("imageUrl", imageUrl);

        dbRef.updateChildren(updates).addOnCompleteListener(task -> {
            progressDialog.dismiss();
            if (task.isSuccessful()) {
                Toast.makeText(this, "Review berhasil diupdate", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Gagal update", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
