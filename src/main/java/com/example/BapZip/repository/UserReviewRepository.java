package com.example.BapZip.repository;

import com.example.BapZip.domain.Review;
import com.example.BapZip.domain.User;
import com.example.BapZip.domain.mapping.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserReviewRepository extends JpaRepository<UserReview, Long> {

    // findByUserAndReview 메서드 (Optional인 이유 = null반환 X(명시적 결과없음 O), null체크 회피, 메서드 시그니처 표현)
    Optional<UserReview> findByUserAndReview(User user, Review review);

    List<UserReview> findByUser(User user);
}
