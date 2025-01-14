package com.buku.bukuapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BukuappController {
    @Autowired
    private BukuappRepository bukuappRepository;

    @GetMapping("/")
    public String beranda() {
        return "index";
    }

    @GetMapping("/inputbuku")
    public String halamanInputBuku(Model model) {
        model.addAttribute("buku", new BukuappModel());
        return "inputbuku";
    }

    @PostMapping("/tampilbuku")
    public String simpanBuku(@ModelAttribute BukuappModel buku, Model model) {
        bukuappRepository.save(buku);

        String prefix = "";
        switch (buku.getJenis()) {
            case "Novel" -> prefix = "NV";
            case "Sains" -> prefix = "SN";
            case "Teknik" -> prefix = "TK";
            case "Ekonomi" -> prefix = "EK";
            case "Biografi" -> prefix = "BG";
            case "Sejarah" -> prefix = "SH";
        }

        String kodebuku = prefix + String.format("%05d", buku.getId());
        buku.setKodebuku(kodebuku);

        bukuappRepository.save(buku);

        model.addAttribute("daftarbuku", bukuappRepository.findAll());
        model.addAttribute("successMessage", "Data buku berhasil ditambahkan!");
        return "tampilbuku";
    }

    @GetMapping("/tampilbuku")
    public String tampilkanBuku(Model model) {
        model.addAttribute("daftarbuku", bukuappRepository.findAll());
        return "tampilbuku";
    }

    @GetMapping("/revisibuku/{id}")
    public String revisiBuku(@PathVariable("id") int id, Model model) {
        BukuappModel buku = bukuappRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id buku tidak valid:" + id));
        model.addAttribute("buku", buku);
        return "inputbuku";
    }

    @GetMapping("/hapusbuku/{id}")
    public String hapusBuku(@PathVariable("id") int id, Model model) {
        BukuappModel buku = bukuappRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id buku tidak valid:" + id));
        bukuappRepository.delete(buku);
        model.addAttribute("daftarbuku", bukuappRepository.findAll());
        return "tampilbuku";
    }
}
