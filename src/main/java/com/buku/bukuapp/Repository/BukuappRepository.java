package com.buku.bukuapp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.buku.bukuapp.Model.BukuappModel;

@Repository
public interface BukuappRepository extends JpaRepository<BukuappModel, Integer> {
    
}
