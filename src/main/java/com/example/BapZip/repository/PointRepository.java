package com.example.BapZip.repository;

import com.example.BapZip.domain.Point;
import com.example.BapZip.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface PointRepository extends JpaRepository<Point, Long> {
    Optional<User> findByUserId(String userId);
}
