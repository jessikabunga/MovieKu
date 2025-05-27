package com.example.movieku;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.UploadCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public class AddReview extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imageUpload;
    private EditText etJudul, etSinopsis, etTahun, etDurasi, etGenre, etUlasan;
    private RatingBar ratingBar;
    private Button btnSimpan;

    private Uri imageUri;
    private String imageUrl;

    private DatabaseReference dbRef;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        // Inisialisasi view
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
        progressDialog.setMessage("Uploading...");

        dbRef = FirebaseDatabase.getInstance().getReference("movies");

        imageUpload.setOnClickListener(v -> openGallery());

        btnSimpan.setOnClickListener(v -> {
            if (imageUri != null) {
                uploadToCloudinary();
            } else {
                Toast.makeText(this, "Silakan pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageUpload.setImageURI(imageUri);
        }
    }

    private void uploadToCloudinary() {
        progressDialog.show();

        MediaManager.get().upload(imageUri)
                .unsigned("movieku")
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) { }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) { }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        imageUrl = resultData.get("secure_url").toString();
                        simpanDataKeFirebase();
                    }

                    @Override
                    public void onError(String requestId, com.cloudinary.android.callback.ErrorInfo error) {
                        progressDialog.dismiss();
                        Toast.makeText(AddReview.this, "Gagal upload gambar", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReschedule(String requestId, com.cloudinary.android.callback.ErrorInfo error) { }
                }).dispatch();
    }

    private void simpanDataKeFirebase() {
        String id = dbRef.push().getKey();
        String judul = etJudul.getText().toString().trim();
        String sinopsis = etSinopsis.getText().toString().trim();
        String tahun = etTahun.getText().toString().trim();
        String durasi = etDurasi.getText().toString().trim();
        String genre = etGenre.getText().toString().trim();
        String ulasan = etUlasan.getText().toString().trim();
        float rating = ratingBar.getRating();

        Movie movie = new Movie(
                id, judul, sinopsis, tahun, durasi, genre, ulasan, rating, imageUrl
        );

        dbRef.child(id).setValue(movie).addOnCompleteListener(task -> {
            progressDialog.dismiss();
            if (task.isSuccessful()) {
                Toast.makeText(this, "Review berhasil disimpan", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Gagal menyimpan review", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
