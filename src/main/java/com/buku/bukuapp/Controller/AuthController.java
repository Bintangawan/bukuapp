package com.buku.bukuapp.Controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.buku.bukuapp.Model.Role;
import com.buku.bukuapp.Model.User;
import com.buku.bukuapp.Repository.RoleRepository;
import com.buku.bukuapp.Repository.UserRepository;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(User user, Model model) {
    // Cek apakah username sudah ada
    if (userRepository.findByUsername(user.getUsername()).isPresent()) {
        model.addAttribute("error", "Username is already taken");
        return "register";
    }

    // Cek apakah email sudah ada
    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
        model.addAttribute("error", "Email is already registered");
        return "register";
    }

    // Cek dan buat role "ROLE_USER" jika belum ada
    Role userRole = roleRepository.findByName("ROLE_USER").orElseGet(() -> {
        Role role = new Role();
        role.setName("ROLE_USER");
        return roleRepository.save(role);
    });

    // Encode password dan set role
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRoles(Collections.singleton(userRole));

    // Simpan user ke database
    userRepository.save(user);

    System.out.println("User registered: " + user.getUsername()); // Debug log
    return "redirect:/login";
}


    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}
