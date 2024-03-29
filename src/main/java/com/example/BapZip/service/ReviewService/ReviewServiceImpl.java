package com.example.BapZip.service.ReviewService;

import com.example.BapZip.apiPayload.code.status.ErrorStatus;
import com.example.BapZip.apiPayload.exception.GeneralException;
import com.example.BapZip.domain.*;
import com.example.BapZip.domain.enums.hashtagName;
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

import static com.example.BapZip.domain.enums.hashtagName.깨끗함;

@Service
@RequiredArgsConstructor

@Component
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final UserReviewRepository userReviewRepository;
    private final PointRepository pointRepository;
    private final SchoolRepository schoolRepository;
    private final HashtagRepository hashtagRepository;

    // 리뷰 작성
    @Override
    @Transactional
    public Long save(Long userId, ReviewRequestDTO.RegisterReviewDTO registerReviewDTO, List<String> urls) {
        // User와 Store를 찾는 로직 필요 (getUser, getStore등을 위해)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND_ERROR));
        Store store = storeRepository.findByName(registerReviewDTO.getStoreName())
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_NOT_EXIST_ERROR));

        // 사진 최대 5개 저장
        if (urls != null && urls.size() > 5) {
            throw new GeneralException(ErrorStatus.REVIEW_IMAGES_EXCEEDED_ERROR);
        }


        Review review = Review.builder()
                .user(user)
                .store(store)
                .score(registerReviewDTO.getRating())
                .content(registerReviewDTO.getReviewText())
                .paymentTime(registerReviewDTO.getVisitDate())
                .images(new ArrayList<>())  // 'images' 필드를 초기화
                .build();

        // ReviewImage 엔티티에 이미지 URL 저장
        if (urls != null) {
            for (String imageUrl : urls) {
                ReviewImage reviewImage = ReviewImage.builder()
                        .review(review)
                        .imageUrl(imageUrl)
                        .build();
                review.getImages().add(reviewImage);  // ReviewImage 엔티티를 images 필드에 추가한다.
            }
        }

        Point point = Point.builder()
                .user(user)
                .note(store.getName()+"리뷰 작성")
                .point(10L)
                .classification("획득")
                .build();

        pointRepository.save(point);

        // 해시태그 부분 !!
        Hashtag hashtag = hashtagRepository.findByStore(store)
                .orElseGet(() -> Hashtag.builder()
                        .store(store)
                        .h1(0) // 생성과 동시에 0 으로 초기화 해줘야 된다고 해서 추가함
                        .h2(0)
                        .h3(0)
                        .h4(0)
                        .h5(0)
                        .h6(0)
                        .h7(0)
                        .h8(0)
                        .h9(0)
                        .h10(0)
                        .build());
        List<String> hashtagNames = registerReviewDTO.getHashtags();

        for(String i:hashtagNames){

            switch(i) {
                case "혼밥":
                    hashtag.setH1(hashtag.getH1()+1); break;
                case "양많음":
                    hashtag.setH2(hashtag.getH2()+1); break;
                case "빠름":
                    hashtag.setH3(hashtag.getH3()+1); break;
                case "저렴함":
                    hashtag.setH4(hashtag.getH4()+1); break;
                case "깨끗함":
                    hashtag.setH5(hashtag.getH5()+1); break;
                case "단체석":
                    hashtag.setH6(hashtag.getH6()+1); break;
                case "맛있음":
                    hashtag.setH7(hashtag.getH7()+1); break;
                case "친절함":
                    hashtag.setH8(hashtag.getH8()+1); break;
                case "넓음":
                    hashtag.setH9(hashtag.getH9()+1); break;
                case "조용함":
                    hashtag.setH10(hashtag.getH10()+1); break;
            }

        }

        hashtagRepository.save(hashtag);



        return reviewRepository.save(review).getId(); // Review 엔티티를 저장합니다.

    }

    // 나의 리뷰 조회
    @Override
    public List<ReviewResponseDTO.MyReviewsDTO> getMyReview(Long userId) {
        // 1. 사용자 정보 조회
        userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND_ERROR));
        // 2. 사용자의 리뷰 목록 조회(최신순 정렬)
        List<Review> reviewList = reviewRepository.findAllByUserIdOrderByCreatedAtDesc(userId);

        // 3. MyReviewsDTO로 변환
        List<ReviewResponseDTO.MyReviewsDTO> myReviewsList = new ArrayList<>();
        for (Review review : reviewList) {
            ReviewResponseDTO.MyReviewsDTO myReview = new ReviewResponseDTO.MyReviewsDTO();
            myReview.setVisitDate(review.getPaymentTime());
            myReview.setStoreId(review.getStore().getId());
            myReview.setStoreName(review.getStore().getName());
            myReview.setRating(review.getScore());
            myReview.setNickname(review.getUser().getNickname());
            myReview.setUserImage(review.getUser().getImageUrl());
            myReview.setReviewText(review.getContent());

            // 이미지가 있는 경우, """여러 장의 이미지 조회"""를 위해 List<String>으로 설정
            List<String> imageUrls = new ArrayList<>();
            for(ReviewImage image : review.getImages()) {
                imageUrls.add(image.getImageUrl());
            }
            myReview.setImageUrls(imageUrls);

            myReview.setCreatedAt(review.getCreatedAt());
            myReview.setReviewId(review.getId());

            myReviewsList.add(myReview);
        }

        return myReviewsList;


    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.REVIEW_NOT_EXIST_ERROR));

        // 외래키 제약 조건 ERROR -> UserReview에서 해당 리뷰를 참조하는 데이터를 먼저 삭제
        List<UserReview> userReviews = userReviewRepository.findAllByReviewId(reviewId);
        userReviewRepository.deleteAll(userReviews);
        // 그 후 리뷰 삭제
        reviewRepository.delete(review);
    }

    // 리뷰 좋아요 하기
    @Override
    @Transactional
    public void addLike(Long userId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.REVIEW_NOT_EXIST_ERROR));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND_ERROR));

        // 내가 쓴 글에 좋아요 못 누르게
        if (review.getUser().equals(user)) {
            throw new GeneralException(ErrorStatus.CANNOT_LIKE_OWN_REVIEW_ERROR);
        }

        // 이미 좋아요가 눌러져 있는지 확인
        if (userReviewRepository.findByUserAndReview(user, review).isPresent()) {
            throw new GeneralException(ErrorStatus.REVIEW_ZIP_ALREADY_ERROR);
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
    public void deleteLike(Long userId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.REVIEW_NOT_FOUND_ERROR));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND_ERROR));

        UserReview userReview = userReviewRepository.findByUserAndReview(user, review)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USERREVIEW_NOT_EXIST_ERROR));

        userReviewRepository.delete(userReview);

    }


    // 좋아요 한 리뷰 조회
    @Override
    public List<ReviewResponseDTO.ZipReviewDTO> findLikedReviews(Long userId) {
        // 1. 사용자 정보 조회
        userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND_ERROR));

        // 2. 해당 사용자가 좋아요 한 리뷰 목록 조회 (최신순 정렬)
        List<UserReview> userReviewList = userReviewRepository.findByUser_Id(userId);


        // 3. 좋아요 한 리뷰 목록에서 리뷰 객체 가져오기
        List<Review> reviewList = userReviewList.stream()
                .map(UserReview::getReview)
                .filter(review -> review.getCreatedAt() != null)
                .sorted(Comparator.comparing(Review::getCreatedAt).reversed()) // 리뷰 최신순 정렬
                .toList();

        return reviewList.stream()
                .map(review -> {
                    List<String> imageUrls = review.getImages().stream()
                            .map(ReviewImage::getImageUrl)
                            .toList();
                    return new ReviewResponseDTO.ZipReviewDTO(review.getStore().getId(), review.getStore().getName(), review.getUser().getNickname(), review.getScore(), review.getContent(), review.getUser().getImageUrl() ,imageUrls, review.getCreatedAt(), review.getId());
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
            List<Review> reviews = reviewRepository.findTop3ByStoreAndCreatedAtIsNotNullOrderByCreatedAtDesc(store);

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
                dto.setReviewCreateDate(review.getCreatedAt());
                dto.setCategoryId(review.getStore().getCategory().getId());

                // UserReview에서 userId, reviewId값으로 조회를 해서 값이 나오면 true로 세팅
                UserReview userReview = userReviewRepository.findByUserIdAndReviewId(userId, review.getId());
                dto.setLike(userReview != null);

                // UserReview에서 reviewId값으로 조회를 해서 좋아요 수를 세팅
                Long likesCount = userReviewRepository.countByReviewId(review.getId());
                dto.setLikesCount(likesCount);


                dto.setReviewId(review.getId());

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
        //School school = schoolRepository.findById(schoolId).orElseThrow(() -> new GeneralException(ErrorStatus.SCHOOL_NOT_EXIST));
        // storeId, school로 store[1개] 선택
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new GeneralException(ErrorStatus.STORE_NOT_EXIST));

        List<Review> reviews = reviewRepository.findAllByStoreOrderByCreatedAtDesc(store);
        List<ReviewResponseDTO.StoreReviewDTO> storeReviews = new ArrayList<>();

        for(Review review : reviews){
            User user = review.getUser();
            List<String> reviewImageUrls = review.getImages().stream()
                    .map(ReviewImage::getImageUrl)
                    .toList();

            // UserReview에서 userId, reviewId값으로 조회를 해서 값이 나오면 true로 세팅
            UserReview userReview = userReviewRepository.findByUserIdAndReviewId(userId, review.getId());
            boolean isLiked = (userReview != null); // 좋아요 유무 판단


            ReviewResponseDTO.StoreReviewDTO storeReviewDTO = ReviewResponseDTO.StoreReviewDTO.builder()
                        .storeId(store.getId())
                        .storeName(store.getName())
                        .nickname(user.getNickname())
                        .rating(review.getScore())
                        .reviewText(review.getContent())
                        .userImage(user.getImageUrl())
                        .reviewImages(reviewImageUrls)
                        .createdAt(review.getCreatedAt())
                        .like(isLiked)
                        .reviewId(review.getId())
                        .build();

                storeReviews.add(storeReviewDTO);

        }

        return storeReviews;


    }


}
