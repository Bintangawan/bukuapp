package com.buku.bukuapp.Model;

import javax.validation.constraints.NotBlank;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "buku")
public class BukuappModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Judul tidak boleh kosong")
    @Column(name = "judul")
    private String judul;

    @NotBlank(message = "Pengarang tidak boleh kosong")
    @Column(name = "pengarang")
    private String pengarang;

    @NotBlank(message = "Penerbit tidak boleh kosong")
    @Column(name = "penerbit")
    private String penerbit;

    @NotBlank(message = "Jenis tidak boleh kosong")
    @Column(name = "jenis")
    private String jenis;

    @Column(name = "kodebuku", unique = true)
    private String kodebuku;

    @Column(name = "harga")
    private double harga;

    @Column(name = "stok")
    private int stok;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getPengarang() {
        return pengarang;
    }

    public void setPengarang(String pengarang) {
        this.pengarang = pengarang;
    }

    public String getPenerbit() {
        return penerbit;
    }

    public void setPenerbit(String penerbit) {
        this.penerbit = penerbit;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getKodebuku() {
        return kodebuku;
    }

    public void setKodebuku(String kodebuku) {
        this.kodebuku = kodebuku;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public Integer getStok() {
        return stok;
    }

    public void setStok(Integer stok) {
        this.stok = stok;
    }
}
