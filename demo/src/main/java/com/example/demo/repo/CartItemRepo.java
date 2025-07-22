package com.example.demo.repo;

import com.example.demo.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepo extends JpaRepository<CartItem, Long> {

    void deleteAllByCartId(Long id);
}
