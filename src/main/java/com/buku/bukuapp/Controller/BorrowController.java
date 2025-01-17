package com.buku.bukuapp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.buku.bukuapp.Model.BorrowModel;
import com.buku.bukuapp.Model.BukuappModel;
import com.buku.bukuapp.Repository.BorrowRepository;
import com.buku.bukuapp.Repository.BukuappRepository;
import com.buku.bukuapp.Repository.UserRepository;
import com.buku.bukuapp.Model.User;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
public class BorrowController {

    @Autowired
    private BorrowRepository borrowRepository;

    @Autowired
    private BukuappRepository bukuappRepository;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/dashboard-user/peminjaman")
@PreAuthorize("hasRole('USER')")
public String showBorrowForm(Model model, Principal principal) {
    // Ambil user yang sedang login
    User user = userRepository.findByUsername(principal.getName())
                              .orElseThrow(() -> new RuntimeException("User not found"));

    // Ambil daftar buku yang tersedia
    model.addAttribute("daftarbuku", bukuappRepository.findAll());

    // Ambil daftar peminjaman oleh user
    List<BorrowModel> listPinjaman = borrowRepository.findByUser(user);
    model.addAttribute("listPinjaman", listPinjaman);

    // Model kosong untuk form peminjaman
    model.addAttribute("borrow", new BorrowModel());

    // Tambahkan username ke model untuk ditampilkan di navbar
    model.addAttribute("username", principal.getName());

    return "dashboard-user/peminjaman";
}

    @PostMapping("/borrow")
    public String borrowBook(@ModelAttribute BorrowModel borrow, Principal principal, Model model) {
        try {
            // Mendapatkan user dari principal
            User user = userRepository.findByUsername(principal.getName())
                                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Mendapatkan buku berdasarkan ID
            BukuappModel buku = bukuappRepository.findById(borrow.getBuku().getId())
                                                .orElseThrow(() -> new RuntimeException("Buku not found"));

            // Validasi: Cek apakah buku sudah dipinjam oleh user yang sama dan belum dikembalikan
            boolean sudahDipinjam = borrowRepository.existsByUserAndBukuAndStatus(user, buku, "Dipinjam");
            if (sudahDipinjam) {
                model.addAttribute("error", "Anda sudah meminjam buku ini dan belum mengembalikannya.");
                return "redirect:/dashboard-user/peminjaman";
            }

            // Validasi: Cek stok buku
            if (buku.getStok() <= 0) {
                model.addAttribute("error", "Stok buku habis.");
                return "redirect:/dashboard-user/peminjaman";
            }

            // Kurangi stok buku
            buku.setStok(buku.getStok() - 1);
            bukuappRepository.save(buku);

            // Set data peminjaman
            borrow.setUser(user);
            borrow.setBuku(buku);
            borrow.setBorrowDate(borrow.getBorrowDate()); // Tanggal pinjam diambil dari form
            borrow.setStatus("Dipinjam");

            // Simpan data peminjaman
            borrowRepository.save(borrow);

            model.addAttribute("successMessage", "Buku berhasil dipinjam!");
            return "redirect:/dashboard-user/peminjaman";
        } catch (Exception e) {
            model.addAttribute("error", "Terjadi kesalahan: " + e.getMessage());
            return "redirect:/dashboard-user/peminjaman";
        }
    }

    @GetMapping("/dashboard-admin/datapeminjam")
    @PreAuthorize("hasRole('ADMIN')")
    public String viewBorrowedBooks(Authentication authentication, Model model) {
        // Ambil semua data peminjaman
        List<BorrowModel> listPeminjam = borrowRepository.findAll();
        model.addAttribute("listPeminjam", listPeminjam);

        // Tambahkan username ke model untuk ditampilkan di navbar
        model.addAttribute("username", authentication.getName()); // Ganti dengan username admin yang sesuai
        return "dashboard-admin/datapeminjam";
    }

    @PostMapping("/update-status/{id}")
@PreAuthorize("hasRole('ADMIN')")
public String updateBorrowStatus(@PathVariable("id") Long id, @RequestParam("status") String status) {
    BorrowModel borrow = borrowRepository.findById(id)
                                         .orElseThrow(() -> new RuntimeException("Borrow record not found"));
    
    // Jika status diubah menjadi "Dikembalikan", tambahkan stok buku
    if ("Dikembalikan".equals(status)) {
        BukuappModel buku = borrow.getBuku();
        buku.setStok(buku.getStok() + 1); // Tambah stok buku
        bukuappRepository.save(buku);

        borrow.setReturnDate(LocalDate.now()); // Set tanggal kembali
    } else {
        // Jika status bukan "Dikembalikan", set returnDate ke null
        borrow.setReturnDate(null);
    }

    borrow.setStatus(status);
    borrowRepository.save(borrow);
    return "redirect:/dashboard-admin/datapeminjam";
    }
}
