package com.buku.bukuapp.Repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.buku.bukuapp.Model.BorrowModel;
import com.buku.bukuapp.Model.BukuappModel;
import com.buku.bukuapp.Model.User;

@Repository
public interface BorrowRepository extends JpaRepository<BorrowModel, Long> {
    List<BorrowModel> findByUser(User user);
    boolean existsByUserAndBukuAndStatus(User user, BukuappModel buku, String status);
}
