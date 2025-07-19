package com.example.demo.repo;


import com.example.demo.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CategoryRepo extends JpaRepository<Category, Long> {

    Category findByName(String name);

    boolean existsByName(String name);
}
