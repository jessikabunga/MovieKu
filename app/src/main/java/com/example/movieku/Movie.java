package com.example.movieku;

import java.io.Serializable;

public class Movie implements Serializable {
    private String id;
    private String judul;
    private String sinopsis;
    private String tahun;
    private String durasi;
    private String genre;
    private String ulasan;
    private float rating;
    private String imageUrl;

    // Constructor kosong dibutuhkan Firebase
    public Movie() {}

    // Constructor lengkap (bisa dipakai saat tambah/edit review)
    public Movie(String id, String judul, String sinopsis, String tahun, String durasi,
                 String genre, String ulasan, float rating, String imageUrl) {
        this.id = id;
        this.judul = judul;
        this.sinopsis = sinopsis;
        this.tahun = tahun;
        this.durasi = durasi;
        this.genre = genre;
        this.ulasan = ulasan;
        this.rating = rating;
        this.imageUrl = imageUrl;
    }

    // Getter & Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }

    public String getSinopsis() { return sinopsis; }
    public void setSinopsis(String sinopsis) { this.sinopsis = sinopsis; }

    public String getTahun() { return tahun; }
    public void setTahun(String tahun) { this.tahun = tahun; }

    public String getDurasi() { return durasi; }
    public void setDurasi(String durasi) { this.durasi = durasi; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getUlasan() { return ulasan; }
    public void setUlasan(String ulasan) { this.ulasan = ulasan; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
