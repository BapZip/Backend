package com.example.BapZip.repository;

import com.example.BapZip.domain.mapping.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReviewRepository extends JpaRepository<UserReview, Long> {
}
