package com.buku.bukuapp.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buku.bukuapp.Model.BorrowModel;
import com.buku.bukuapp.Repository.BorrowRepository;

import java.util.List;

@Service
public class BorrowService {

    @Autowired
    private BorrowRepository borrowRepository;

    public List<BorrowModel> findAllBorrows() {
        return borrowRepository.findAll();
    }

    public BorrowModel saveBorrow(BorrowModel borrow) {
        return borrowRepository.save(borrow);
    }

    public void updateStatus(Long id, String status) {
        BorrowModel borrow = borrowRepository.findById(id).orElseThrow();
        borrow.setStatus(status);
        borrowRepository.save(borrow);
    }
}
