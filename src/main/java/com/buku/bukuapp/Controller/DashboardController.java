package com.buku.bukuapp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.buku.bukuapp.Model.BukuappModel;
import com.buku.bukuapp.Repository.BukuappRepository;

@Controller
public class DashboardController {

    // Inject BukuappRepository
    @Autowired
    private BukuappRepository bukuappRepository;

    @GetMapping("/dashboard-admin")
    public String adminDashboard(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());
        return "dashboard-admin/dashboard-admin"; // Path baru
    }

    @GetMapping("/dashboard-admin/dashboard")
    public String dashboardAdmin(Authentication authentication, Model model) {
        // Ambil username dari Authentication
        String username = authentication.getName();
        model.addAttribute("username", username);

        // Kembalikan template dashboard-admin.html
        return "dashboard-admin/dashboard-admin";
    }
    @GetMapping("/dashboard-admin/inputbuku")
    public String halamanInputBukuAdmin(Authentication authentication, Model model) {
        String username = authentication.getName();
        model.addAttribute("username", username);
        model.addAttribute("buku", new BukuappModel());
        return "dashboard-admin/inputbuku"; // Path baru
    }

    @GetMapping("/dashboard-admin/tampilbuku")
    public String tampilkanBukuAdmin(Authentication authentication, Model model) {
        String username = authentication.getName();
        model.addAttribute("username", username);
        model.addAttribute("daftarbuku", bukuappRepository.findAll());
        return "dashboard-admin/tampilbuku"; // Path baru
    }

    // Dashboard User
    @GetMapping("/dashboard-user")
    public String userDashboard(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());
        return "dashboard-user/dashboard-user"; // Path baru
    }

    @GetMapping("/dashboard-user/dashboard")
    public String dashboardUser(Authentication authentication, Model model) {
        // Ambil username dari Authentication
        String username = authentication.getName();
        model.addAttribute("username", username);

        // Kembalikan template dashboard-user.html
        return "dashboard-user/dashboard-user";
    }

    @GetMapping("/dashboard-user/tampilbuku")
    public String tampilkanBukuUser(Authentication authentication, Model model) {
        String username = authentication.getName();
        model.addAttribute("username", username);
        model.addAttribute("daftarbuku", bukuappRepository.findAll());
        return "dashboard-user/tampilbuku"; // Path baru
    }

}
