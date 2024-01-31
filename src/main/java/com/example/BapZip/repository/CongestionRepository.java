package com.example.BapZip.repository;

import com.example.BapZip.domain.Congestion;
import com.example.BapZip.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CongestionRepository extends JpaRepository<Congestion, Long> {
    List<Congestion> findByStore(Store store);

    List<Congestion> findByStoreAndCreatedAtAfter(Store store, LocalDateTime localDateTime);
}
