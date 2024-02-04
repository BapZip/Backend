package com.example.BapZip.service.ReviewService;

import com.example.BapZip.domain.Point;
import com.example.BapZip.domain.Review;
import com.example.BapZip.domain.Store;
import com.example.BapZip.domain.User;
import com.example.BapZip.domain.mapping.UserReview;
import com.example.BapZip.repository.*;
import com.example.BapZip.web.dto.ReviewRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor

@Component
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final UserReviewRepository userReviewRepository;
    private final PointRepository pointRepository;

    // 리뷰 작성
    @Override
    @Transactional
    public Long save(Long userId, ReviewRequestDTO.RegisterReviewDTO registerReviewDTO) {
        // User와 Store를 찾는 로직 필요 (getUser, getStore등을 위해)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 User가 없습니다. id=" + userId));
        Store store = storeRepository.findByName(registerReviewDTO.getStoreName())
                .orElseThrow(() -> new IllegalArgumentException("해당 Store가 없습니다. storeName=" + registerReviewDTO.getStoreName()));

        Review review = Review.builder()
                .user(user)
                .store(store)
                .price(registerReviewDTO.getPrice())
                .score(registerReviewDTO.getRating())
                .content(registerReviewDTO.getReviewText())
                .paymentTime(registerReviewDTO.getVisitDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .menuName(registerReviewDTO.getMenuName())
                .build();

        Point point = Point.builder()
                .user(user)
                .note(store.getName()+"리뷰 작성")
                .point(10L)
                .classification("획득")
                .build();

        pointRepository.save(point);


        return reviewRepository.save(review).getId();

    }

    // 나의 리뷰 조회
    @Override
    @Transactional
    public List<Review> findByUserId(Long userId) {
        return reviewRepository.findAllByUserId(userId);
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 없습니다. id=" + reviewId));
        reviewRepository.delete(review);
    }

    // 리뷰 좋아요 하기
    @Override
    @Transactional
    public void addLike(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 Review가 없습니다. id=" + reviewId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 User가 없습니다. id=" + userId));

        // 이미 좋아요가 눌러져 있는지 확인
        if (userReviewRepository.findByUserAndReview(user, review).isPresent()) {
            throw new IllegalStateException("이미 좋아요가 눌러져 있습니다.");
        }

        UserReview userReview = UserReview.builder()
                .user(user)
                .review(review)
                .build();

        userReviewRepository.save(userReview);
    }

    // 리뷰 좋아요 해제
    @Override
    @Transactional
    public void deleteLike(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 Review가 없습니다. id=" + reviewId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 User가 없습니다. id=" + userId));

        UserReview userReview = userReviewRepository.findByUserAndReview(user, review)
                .orElseThrow(() -> new IllegalArgumentException("해당 UserReview가 없습니다."));

        userReviewRepository.delete(userReview);
    }






}
