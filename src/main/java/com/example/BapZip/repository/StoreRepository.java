package com.example.BapZip.repository;

import com.example.BapZip.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface StoreRepository extends JpaRepository<Store, Long> {
}
