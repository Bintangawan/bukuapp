package com.buku.bukuapp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BukuappRepository extends JpaRepository<BukuappModel, Integer> {
    
}
