package com.example.BapZip.repository;

import com.example.BapZip.domain.Review;
import com.example.BapZip.domain.User;
import com.example.BapZip.domain.mapping.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserReviewRepository extends JpaRepository<UserReview, Long> {

    // findByUserAndReview 메서드 (Optional인 이유 = null반환 X(명시적 결과없음 O), null체크 회피, 메서드 시그니처 표현)
    Optional<UserReview> findByUserAndReview(User user, Review review);

    // 리뷰 삭제 시, UserReview 먼저 삭제하기 위한 메서드
    List<UserReview> findAllByReviewId(Long reviewId);

    // 좋아요 한 리뷰 조회
    List<UserReview> findByUser_Id(Long userId);

    // 리뷰 타임라인
    UserReview findByUserIdAndReviewId(Long userId, Long id);
    List<UserReview> findByUser(User user);

    @Query("SELECT ur.review, COUNT(ur.review) as likeCount\n" +
            "FROM UserReview ur\n" +
            "JOIN ur.review r\n" +
            "JOIN r.store s\n" +
            "JOIN s.category c\n" +
            "WHERE c.name = :categoryName AND s.school.id = :schoolId\n" +
            "GROUP BY ur.review\n" +
            "ORDER BY likeCount DESC\n" +
            "LIMIT 1")
    Review findTopReviewByLikesPerCategory(@Param("categoryName") String categoryName,@Param("schoolId") Long schoolId);

    Long countByReviewId(Long id);



}
