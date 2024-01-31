package com.example.BapZip.repository;

import com.example.BapZip.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByName(String name); // 리뷰 작성 위해 추가된 부분

}
