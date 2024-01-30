package com.example.BapZip.repository;

import com.example.BapZip.domain.Hashtag;
import com.example.BapZip.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Optional<Hashtag> findByStore(Store store);
}
