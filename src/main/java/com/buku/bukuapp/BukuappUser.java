package com.buku.bukuapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BukuappUser {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Method untuk Signup
    public String signup(User user) {
        // Periksa apakah username sudah ada
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            return "Username already exists!";
        }

        // Enkripsi password sebelum disimpan
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Simpan user ke database
        userRepository.save(user);
        return "Signup successful!";
    }

    // Method untuk Signin
    public String signin(User user) {
        // Cari user berdasarkan username
        Optional<User> foundUser = userRepository.findByUsername(user.getUsername());
        if (foundUser.isEmpty()) {
            return "Invalid username or password!";
        }

        // Periksa apakah password cocok
        boolean passwordMatch = passwordEncoder.matches(user.getPassword(), foundUser.get().getPassword());
        if (!passwordMatch) {
            return "Invalid username or password!";
        }

        return "Signin successful!";
    }
}
