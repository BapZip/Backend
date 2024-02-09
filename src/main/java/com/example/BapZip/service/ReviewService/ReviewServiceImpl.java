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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
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

    // 리뷰 랭킹 조회(합산 좋아요 순)
    // userReview(좋아요) 테이블에서 review_id로 데이터 몇개 나오는지 개수 세는게 좋아요 수

    @Override
    public List<ReviewResponseDTO.ReviewRankingDTO> reviewRanking(Long userId) {

        LocalDate oneWeekAgoDate = LocalDate.now().minusWeeks(1);
        // ↓ 자료형 오류로 LocalDateTime 타입으로 바꿔주기
        LocalDateTime oneWeekAgoDateTime = oneWeekAgoDate.atStartOfDay();
        List<Object[]> userRankingData = reviewRepository.countLikesByUserId(oneWeekAgoDateTime);

        if(userRankingData.isEmpty()) {
            throw new GeneralException(ErrorStatus.NO_DATA_FOUND_ERROR);
        }

        return userRankingData.stream()
                .limit(3)
                .map(data -> {
                    Long topUserId = (Long) data[0];
                    Long likesCount = (Long) data[1];
                    User topUser = userRepository.findById(topUserId)
                            .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND_ERROR));
                    return ReviewResponseDTO.ReviewRankingDTO.builder()
                            .nickname(topUser.getNickname())
                            .imageUrl(topUser.getImageUrl() != null ? topUser.getImageUrl() : "default image URL")
                            .likesCount(likesCount)
                            .build();
                })
                .collect(Collectors.toList());
    }




    // 리뷰 타임라인 조회 (카테고리별)
    @Override
    public List<ReviewResponseDTO.TimelineDTO> reviewTimeline(Long userId, Long schoolId, String categoryName) {

        // 방법 1
        School school = schoolRepository.findById(schoolId).orElseThrow(() -> new GeneralException(ErrorStatus.SCHOOL_NOT_EXIST));
        List<Store> stores = storeRepository.findBySchool(school); // store엔티티에 school school로 들어있기 때문


        // 카테고리 이름에 맞는 Store를 찾는다.
        List<Store> filteredStores;
        if (categoryName.equals("ALL")) {
            filteredStores = stores; // 모든 가게를 선택
        } else {
            filteredStores = stores.stream()
                    .filter(store -> store.getCategory().getName().equals(categoryName))
                    .toList();
        }

        List<ReviewResponseDTO.TimelineDTO> result = new ArrayList<>();

        for (Store store : filteredStores) {
            // 각 Store에서 최신 리뷰 3개를 가져온다.
            List<Review> reviews = reviewRepository.findTop3ByStoreOrderByCreatedAtDesc(store);

            // 가져온 리뷰들을 DTO로 변환하고, 좋아요 여부를 세팅한다.
            List<ReviewResponseDTO.TimelineDTO> dtos = reviews.stream().map(review -> {
                ReviewResponseDTO.TimelineDTO dto = new ReviewResponseDTO.TimelineDTO();
                dto.setStoreId(review.getStore().getId());
                dto.setStoreName(review.getStore().getName());

                // Store에서 가져온 첫 번째 이미지의 URL을 세팅한다.
                List<StoreImage> images = review.getStore().getImages();
                if(!images.isEmpty()) {
                    dto.setImageUrl(images.get(0).getImageURL()); // (StoreImage 접근)
                }
                dto.setReviewText(review.getContent());
                dto.setNickname(review.getUser().getNickname());

                // 리뷰의 생성 날짜가 null인 경우를 처리
                if (review.getCreatedAt() != null) {
                    dto.setReviewCreateDate(review.getCreatedAt());
                } else {
                    // created_at이 null인 경우 기본값 혹은 적절한 값을 설정. 일단 현재시간으로 설정 <- 변동 가능성 O
                    dto.setReviewCreateDate(LocalDateTime.now());
                }

                //dto.setReviewCreateDate(review.getCreatedAt());
                dto.setCategoryId(review.getStore().getCategory().getId());

                // UserReview에서 userId, reviewId값으로 조회를 해서 값이 나오면 true로 세팅
                UserReview userReview = userReviewRepository.findByUserIdAndReviewId(userId, review.getId());
                dto.setLike(userReview != null);

                return dto;
            }).toList();

            result.addAll(dtos);

        }

        // 최신순으로 정렬한다.
        result.sort(Comparator.comparing(ReviewResponseDTO.TimelineDTO::getReviewCreateDate).reversed());

        // 최신 3개만 반환합니다.
        return result.stream().limit(3).collect(Collectors.toList());
    }



    // 가게 리뷰 조회
    @Override
    public List<ReviewResponseDTO.StoreReviewDTO> findStoreReview(Long userId, Long storeId, Long schoolId) {
        // schoolId 받아서, 학교 선택
        School school = schoolRepository.findById(schoolId).orElseThrow(() -> new GeneralException(ErrorStatus.SCHOOL_NOT_EXIST));
        // storeId, school로 store[1개] 선택
        Store store = storeRepository.findByIdAndSchool(storeId, school).orElseThrow(() -> new GeneralException(ErrorStatus.STORE_NOT_EXIST));

        List<Review> reviews = reviewRepository.findAllByStoreOrderByCreatedAtDesc(store);
        List<ReviewResponseDTO.StoreReviewDTO> storeReviews = new ArrayList<>();

        for(Review review : reviews){
            User user = review.getUser();

            ReviewResponseDTO.StoreReviewDTO storeReviewDTO = ReviewResponseDTO.StoreReviewDTO.builder()
                        .storeId(store.getId())
                        .storeName(store.getName())
                        .nickname(user.getNickname())
                        .rating(review.getScore())
                        .reviewText(review.getContent())
                        .userImage(user.getImageUrl())
                        .build();

                storeReviews.add(storeReviewDTO);

        }

        return storeReviews;


    }


}
