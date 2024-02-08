package com.example.BapZip.service.ReviewService;

import com.example.BapZip.apiPayload.code.status.ErrorStatus;
import com.example.BapZip.apiPayload.exception.GeneralException;
import com.example.BapZip.domain.*;
import com.example.BapZip.domain.mapping.UserReview;
import com.example.BapZip.repository.*;
import com.example.BapZip.web.dto.ReviewRequestDTO;
import com.example.BapZip.web.dto.ReviewResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

@Component
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final UserReviewRepository userReviewRepository;
    private final PointRepository pointRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final SchoolRepository schoolRepository;
    private final CategoryRepository categoryRepository;

    // 리뷰 작성
    @Override
    @Transactional
    public Long save(Long userId, ReviewRequestDTO.RegisterReviewDTO registerReviewDTO, List<String> urls) {
        // User와 Store를 찾는 로직 필요 (getUser, getStore등을 위해)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND_ERROR));
        Store store = storeRepository.findByName(registerReviewDTO.getStoreName())
                .orElseThrow(() -> new IllegalArgumentException("해당 Store가 없습니다. storeName=" + registerReviewDTO.getStoreName()));


        Review review = Review.builder()
                .user(user)
                .store(store)
                .price(registerReviewDTO.getPrice())
                .score(registerReviewDTO.getRating())
                .content(registerReviewDTO.getReviewText())
                .paymentTime(registerReviewDTO.getVisitDate())
                .menuName(registerReviewDTO.getMenuName())
                .build();

        // ReviewImage 엔티티에 이미지 URL 저장
        for (String imageUrl : urls) {
            ReviewImage reviewImage = ReviewImage.builder()
                    .review(review)
                    .imageUrl(imageUrl)
                    .build();
            reviewImageRepository.save(reviewImage);
        }

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
    public List<ReviewResponseDTO.MyReviewsDTO> getMyReview(Long userId) {
        // 1. 사용자 정보 조회
        userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND_ERROR));
        // 2. 사용자의 리뷰 목록 조회
        List<Review> reviewList = reviewRepository.findAllByUserId(userId);

        // 3. MyReviewsDTO로 변환
        List<ReviewResponseDTO.MyReviewsDTO> myReviewsList = new ArrayList<>();
        for (Review review : reviewList) {
            ReviewResponseDTO.MyReviewsDTO myReview = new ReviewResponseDTO.MyReviewsDTO();
            myReview.setStoreId(review.getStore().getId());
            myReview.setStoreName(review.getStore().getName());
            myReview.setNickname(review.getUser().getNickname());
            myReview.setRating(review.getScore());
            myReview.setReviewText(review.getContent());
            // 이미지가 있는 경우에만 첫 번째 이미지를 설정
            if (!review.getImages().isEmpty()) {
                myReview.setImageUrl(review.getImages().get(0).getImageUrl());
            }
            myReview.setPaymentTime(review.getPaymentTime());

            myReviewsList.add(myReview);
        }

        return myReviewsList;


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
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND_ERROR));

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
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND_ERROR));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND_ERROR));

        UserReview userReview = userReviewRepository.findByUserAndReview(user, review)
                .orElseThrow(() -> new IllegalArgumentException("해당 UserReview가 없습니다."));

        userReviewRepository.delete(userReview);

    }


    // 좋아요한 리뷰 조회
    @Override
    public List<ReviewResponseDTO.ZipReviewDTO> findLikedReviews(Long userId) {
        // 1. 사용자 정보 조회
        userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND_ERROR));

        // 2. 해당 사용자가 좋아요한 리뷰 목록 조회
        List<UserReview> userReviewList = userReviewRepository.findByUser_Id(userId);


        // 3. 좋아요한 리뷰 목록에서 리뷰 객체 가져오기
        List<Review> reviewList = userReviewList.stream()
                .map(UserReview::getReview)
                .toList();

        return reviewList.stream()
                .map(review -> {
                    String imageUrl = review.getImages().isEmpty() ? "default-image-url" : review.getImages().get(0).getImageUrl();
                    return new ReviewResponseDTO.ZipReviewDTO(review.getStore().getId(), review.getStore().getName(), review.getUser().getNickname(), review.getScore(), review.getContent(), imageUrl, review.getPaymentTime());
                })
                .collect(Collectors.toList());

    }

    // 리뷰 랭킹 조회(좋아요 순)
    @Override
    public List<ReviewResponseDTO.ReviewRankingDTO> reviewRanking(Long userId) {
        LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);
        List<Object[]> reviewRankingData = userReviewRepository.countLikesByReviewId(oneWeekAgo);

        if(reviewRankingData.isEmpty()) {
            throw new GeneralException(ErrorStatus.NO_DATA_FOUND_ERROR);
        }

        return reviewRankingData.stream()
                .limit(3)
                .map(data -> {
                    Long topReviewId = (Long) data[0];
                    Long likesCount = (Long) data[1];
                    Review topReview = reviewRepository.findById(topReviewId)
                            .orElseThrow(() -> new GeneralException(ErrorStatus.REVIEW_NOT_FOUND_ERROR));
                    User topUser = topReview.getUser();
                    return ReviewResponseDTO.ReviewRankingDTO.builder()
                            .nickname(topUser.getNickname())
                            .imageUrl(topUser.getImageUrl() != null ? topUser.getImageUrl() : "default image URL")
                            .likesCount(likesCount)
                            .build();
                })
                .collect(Collectors.toList());
    }



    /*
    // 리뷰 타임라인 조회
    @Override
    public List<ReviewResponseDTO.TimelineDTO> reviewTimeline(Long userId, Long schoolId, String categoryName) {

        // 방법 1
        School school = schoolRepository.findById(schoolId).get();
        List<Store> stores = storeRepository.findBySchool(school); // store엔티티에 school school로 들어있기 때문

        // 카테고리이름 받은걸로 스토어즈에서 (for문돌려서 store.getCategory.getName() 이 .equals.CategoryName인걸로 고르자.)
        // if store 걸러서 찾음. 카테고리네임, 스쿨아이디 찾은 스토어 5개 찾음, 그 스토어아이디값을 가진 리뷰들을 최근 3개 찾는거야.
        // 리뷰 레포지터리에서 이 스토어아이디값을 가진거 최근 3개값을 찾음

        // 방법 2
        // List<Category> categories = categoryRepository.findByName(categoryName);
        //findBySchoolsandCategoryNAme 같은걸로 store걸러내는거 알아서 생각해봐라



        // 카테고리에 맞는 리뷰를 최신순으로 3개 가져온다. // 카테고리 질문 (Store엔티티에 매핑되어 있음)
        // List<Review> reviews = reviewRepository.findTop3ByCategoryIdOrderByCreatedAtDesc(categoryId);

        // 가져온 리뷰들을 DTO로 변환하고, 좋아요 여부를 세팅한다.
        return reviews.stream().map(review -> {
            ReviewResponseDTO.TimelineDTO dto = new ReviewResponseDTO.TimelineDTO();
            dto.setStoreId(review.getStore().getId());
            dto.setStoreName(review.getStore().getName());
            dto.setImageUrl(review.getStore().getStoreImage().get(0).getUrl());
            dto.setReviewText(review.getContent());
            dto.setNickname(review.getUser().getNickname());
            dto.setReviewCreateDate(review.getCreatedAt());
            dto.setCategoryId(review.getStore().getCategory().getId());

            // UserReview에서 userId, reviewId값으로 조회를 해서 값이 나오면 true로 세팅
            UserReview userReview =  userReviewRepository.findByUserIdAndReviewId(userId, review.getId());
            dto.setLike(userReview != null);

            return dto;

        }).collect(Collectors.toList());
    }

     */


}
