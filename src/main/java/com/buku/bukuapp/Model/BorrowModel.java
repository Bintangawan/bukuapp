package com.buku.bukuapp.Model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "borrow")
public class BorrowModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Ganti Optional<User> menjadi User

    @ManyToOne
    @JoinColumn(name = "buku_id", nullable = false)
    private BukuappModel buku;

    private LocalDate borrowDate;
    private LocalDate returnDate;

    private String status; // Dipinjam / Dikembalikan

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() { // Tidak lagi membungkus dalam Optional
        return user;
    }

    public void setUser(User user) { // Tidak lagi membungkus dalam Optional
        this.user = user;
    }

    public BukuappModel getBuku() {
        return buku;
    }

    public void setBuku(BukuappModel buku) {
        this.buku = buku;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
