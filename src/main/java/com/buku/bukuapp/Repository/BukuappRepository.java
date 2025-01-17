package com.buku.bukuapp.Repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.buku.bukuapp.Model.BukuappModel;

@Repository
public interface BukuappRepository extends JpaRepository<BukuappModel, Integer> {
    Page<BukuappModel> findByJudulContainingIgnoreCaseOrPengarangContainingIgnoreCaseOrPenerbitContainingIgnoreCaseOrJenisContainingIgnoreCase(
        String judul, String pengarang, String penerbit, String jenis, Pageable pageable);
}
