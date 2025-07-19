package com.example.demo.repo;


import com.example.demo.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.sql.rowset.serial.SerialBlob;
import java.util.List;


public interface ImageRepo extends JpaRepository<Image, Long> {
    List<Image> findByProductId(Long id);
}
