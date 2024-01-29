package com.example.BapZip.repository;

import com.example.BapZip.domain.StoreImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface StoreImageRepository extends JpaRepository<StoreImage, Long> {
}
