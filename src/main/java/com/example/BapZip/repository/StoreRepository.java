package com.example.BapZip.repository;

import com.example.BapZip.domain.Store;
import com.example.BapZip.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findById(Long storeId);
}
