package com.example.BapZip.repository;

import com.example.BapZip.domain.Store;
import com.example.BapZip.domain.StoreImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface StoreImageRepository extends JpaRepository<StoreImage, Long> {
    Optional<StoreImage> findByStore(Store store);
    List<StoreImage> findAllByStore(Store store);
}
