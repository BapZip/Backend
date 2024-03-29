package com.example.BapZip.repository;

import com.example.BapZip.domain.Region;
import com.example.BapZip.domain.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
}
