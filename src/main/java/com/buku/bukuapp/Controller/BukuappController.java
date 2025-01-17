package com.buku.bukuapp.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import com.buku.bukuapp.Model.BukuappModel;
import com.buku.bukuapp.Repository.BukuappRepository;

@Controller
public class BukuappController {
    @Autowired
    private BukuappRepository bukuappRepository;

    // Halaman beranda dengan pagination
    @GetMapping("/")
    public String beranda(@RequestParam(defaultValue = "1") int page, Model model) {
        int pageSize = 10; // Jumlah data per halaman
        Page<BukuappModel> bukuPage = bukuappRepository.findAll(PageRequest.of(page - 1, pageSize));
        
        // Menghitung nomor urut berdasarkan halaman
        int startNumber = (page - 1) * pageSize + 1;
        
        model.addAttribute("daftarbuku", bukuPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bukuPage.getTotalPages());
        model.addAttribute("startNumber", startNumber); // Menambahkan startNumber ke model
        return "index";
    }

    @GetMapping("/cari")
    public String cariBuku(
        @RequestParam(required = false) String keyword,
        @RequestParam(defaultValue = "1") int page,
        Model model) {

        int pageSize = 10; // Jumlah data per halaman
        Page<BukuappModel> bukuPage;

        if (keyword != null && !keyword.isEmpty()) {
            // Lakukan pencarian berdasarkan keyword
            bukuPage = bukuappRepository.findByJudulContainingIgnoreCaseOrPengarangContainingIgnoreCaseOrPenerbitContainingIgnoreCaseOrJenisContainingIgnoreCase(
                keyword, keyword, keyword, keyword, PageRequest.of(page - 1, pageSize));
        } else {
            // Jika tidak ada keyword, tampilkan semua data
            bukuPage = bukuappRepository.findAll(PageRequest.of(page - 1, pageSize));
        }

        // Menghitung nomor urut berdasarkan halaman
        int startNumber = (page - 1) * pageSize + 1;

        model.addAttribute("daftarbuku", bukuPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bukuPage.getTotalPages());
        model.addAttribute("startNumber", startNumber);
        model.addAttribute("keyword", keyword); // Menyimpan keyword untuk pagination

        return "index";
    }

    // Simpan buku baru
    @PostMapping("/dashboard-admin/tampilbuku")
    public String simpanBukuAdmin(@ModelAttribute BukuappModel buku, Model model) {
        // Simpan buku pertama kali untuk mendapatkan ID
        bukuappRepository.save(buku);

        // Generate kode buku berdasarkan jenis
        String prefix = "";
        switch (buku.getJenis()) {
            case "Novel" -> prefix = "NV";
            case "Sains" -> prefix = "SN";
            case "Teknik" -> prefix = "TK";
            case "Ekonomi" -> prefix = "EK";
            case "Biografi" -> prefix = "BG";
            case "Sejarah" -> prefix = "SH";
        }

        // Buat kode buku
        String kodebuku = prefix + String.format("%05d", buku.getId());
        buku.setKodebuku(kodebuku);

        // Simpan ulang buku dengan kode buku
        bukuappRepository.save(buku);

        // Tambahkan buku ke model
        model.addAttribute("daftarbuku", bukuappRepository.findAll());
        model.addAttribute("successMessage", "Data buku berhasil ditambahkan!");
        return "dashboard-admin/tampilbuku";
    }


    // Revisi data buku
    @GetMapping("/dashboard-admin/revisibuku/{id}")
    public String revisiBukuAdmin(@PathVariable("id") int id, Authentication authentication, Model model) {
        BukuappModel buku = bukuappRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Id buku tidak valid: " + id));
        model.addAttribute("buku", buku);
        model.addAttribute("username", authentication.getName());
        model.addAttribute("successMessage", "Data buku berhasil diperbarui!");
        return "dashboard-admin/inputbuku";
    }

    // Hapus data buku
    @GetMapping("/dashboard-admin/hapusbuku/{id}")
    public String hapusBukuAdmin(@PathVariable("id") int id, Model model) {
        BukuappModel buku = bukuappRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Id buku tidak valid: " + id));
        bukuappRepository.delete(buku);
        model.addAttribute("daftarbuku", bukuappRepository.findAll());
        model.addAttribute("successMessage", "Data buku berhasil dihapus!");
        return "dashboard-admin/tampilbuku";
    }
}
