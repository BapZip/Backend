package com.example.BapZip.repository;

import com.example.BapZip.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface PointRepository extends JpaRepository<Point, Long> {
}
