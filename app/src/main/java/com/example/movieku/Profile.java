package com.example.movieku;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class Profile extends AppCompatActivity {

    private TextView tvNama, tvEmail, tvPhone, tvLocation;
    private Button btnKeluar;

    private FirebaseAuth auth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile); // Sesuaikan nama file XML jika perlu

        tvNama = findViewById(R.id.tvNama);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvLocation = findViewById(R.id.tvLocation);
        btnKeluar = findViewById(R.id.btnKeluar);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            tvEmail.setText(user.getEmail());

            userRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String nama = snapshot.child("nama").getValue(String.class);
                    String phone = snapshot.child("noHp").getValue(String.class);
                    String lokasi = snapshot.child("lokasi").getValue(String.class);

                    tvNama.setText(nama != null ? nama : "Nama");
                    tvPhone.setText(phone != null ? phone : "No. HP");
                    tvLocation.setText(lokasi != null ? lokasi : "Lokasi");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Profile.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
                }
            });
        }

        btnKeluar.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(Profile.this, Login.class));
            finish();
        });
    }
}
