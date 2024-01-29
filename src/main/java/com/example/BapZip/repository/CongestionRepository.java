package com.example.BapZip.repository;

import com.example.BapZip.domain.Congestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CongestionRepository extends JpaRepository<Congestion, Long> {
}
