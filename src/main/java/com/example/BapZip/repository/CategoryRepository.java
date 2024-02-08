package com.example.BapZip.repository;

import com.example.BapZip.domain.Category;
import com.example.BapZip.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByStore(Store store);

    List<Category> findByName(String categoryName);
}
