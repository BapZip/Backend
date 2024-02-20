package com.example.BapZip.service.StoreService;

import com.example.BapZip.domain.*;
import com.example.BapZip.domain.enums.CategoryName;
import com.example.BapZip.domain.enums.StoreListStaus;
import com.example.BapZip.domain.enums.hashtagName;
import com.example.BapZip.repository.*;
import com.example.BapZip.web.dto.CongestionResponseDTO;
import com.example.BapZip.web.dto.StoreResponseDTO;
import jakarta.persistence.criteria.CriteriaBuilder;
import com.example.BapZip.apiPayload.code.status.ErrorStatus;
import com.example.BapZip.apiPayload.exception.GeneralException;
import com.example.BapZip.domain.PrintedMenu;
import com.example.BapZip.domain.Review;
import com.example.BapZip.domain.Store;
import com.example.BapZip.domain.User;
import com.example.BapZip.domain.mapping.UserStore;
import com.example.BapZip.repository.*;
import com.example.BapZip.security.TokenProvider;
import com.example.BapZip.web.dto.MypageResponseDTO;
import com.example.BapZip.web.dto.StoreResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService{

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final StoreImageRepository storeImageRepository;
    private final UserStoreRepository userStoreRepository;
    private final ReviewRepository reviewRepository;
    private final CongestionRepository congestionRepository;
    private final HashtagRepository hashtagRepository;
    private final PrintedMenuRepository printedMenuRepository;
    private final NoticeRepository noticeRepository;
    private final CategoryRepository categoryRepository;
    private final UserReviewRepository userReviewRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuRepository menuRepository;
    private final SchoolRepository schoolRepository;

    @Override
    public StoreResponseDTO.StoreInfoDTO getStoreInfo(String userId, Long storeId) {

        Store store = storeRepository.findById(storeId).orElseThrow(()-> new GeneralException(ErrorStatus.STORE_NOT_EXIST));
        User user = userRepository.findById(Long.valueOf(userId)).get();

        // ** 1. 가게 이름, 내외부 ** //
        StoreResponseDTO.StoreInfoDTO result = StoreResponseDTO.StoreInfoDTO.builder()
                .name(store.getName()).inOrOut(store.getOutin().getName()).build();


        // ** 2. 가게 이미지 ** //
        List<String> imageURLs = new ArrayList<>();
        storeImageRepository.findAllByStore(store).stream().forEach((entity)->imageURLs.add(entity.getImageURL()));
        result.setImages(imageURLs);



        // ** 3. 웨이팅 시간 ** //
        Integer waitingTimeAv = null;
        List<Congestion> congestionList =
                congestionRepository.findByStoreAndCreatedAtAfter
                        (store, LocalDateTime.now(TimeZone.getTimeZone("Asia/Seoul").toZoneId()).minusMonths(1));
        if(!congestionList.isEmpty()) waitingTimeAv = calculateTime(congestionList);
        result.setWaitTime(waitingTimeAv);


        // ** 4. 북마크 여부 ** //
        userStoreRepository.findByStoreAndUser(store,user).ifPresentOrElse(
                bookmark -> result.setBookmark(true), () -> result.setBookmark(false) );


        // ** 5. 리뷰 평점 ** //
        List<Review> reviewList = reviewRepository.findAllByStore(store);
        if (reviewList.isEmpty()){result.setScore(null);} // 리뷰없으면 null
        else{
            double sum=0.0;
            for(Review review : reviewList){
                sum+=review.getScore();
            }
            DecimalFormat df = new DecimalFormat("#.#"); // 소수점 두 자리까지 표시
            result.setScore(Double.parseDouble(df.format(sum/(double)reviewList.size())));
        }

        // ** 해시태그 ** //
        List<String> hashtags = calculateHashtag(hashtagRepository.
                findByStore(store).orElseThrow(()-> new GeneralException(ErrorStatus.STORE_HASHTAG_TABLE_NOT_EXIST)));
        result.setHashtag(hashtags);

        // **카테고리 ** //
//        Optional<Category> category = categoryRepository.findByStore(store);
//        if(category.isPresent())
        //result.setCategory(store.getCategory().getName());
        //String categoryName= store.getCategory().getName();
        result.setCategory(CategoryName.valueOf(store.getCategory().getName()).getName());
        return result;

    }

    // 오늘의 공지 가져오기
    @Override
    public StoreResponseDTO.NoticeDTO getNotice(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(()-> new GeneralException(ErrorStatus.STORE_NOT_EXIST));
        LocalDateTime startOfDay = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MAX);
        List<Notice> notices = noticeRepository.findByStoreAndCreatedAtBetween(store,startOfDay,endOfDay);
        String content;
        if(notices.isEmpty()) content = "등록된 공지가 없습니다.";
        else content =  notices.get(0).getContent();
        return StoreResponseDTO.NoticeDTO.builder().notice(content).build();
    }

    @Override
    public void zipStore(String userId, Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(()-> new GeneralException(ErrorStatus.STORE_NOT_EXIST));
        User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(()-> new GeneralException(ErrorStatus.USER_NOT_FOUND_ERROR));
        if(userStoreRepository.findByStoreAndUser(store,user).isPresent()) throw new GeneralException(ErrorStatus.STORE_ALREADY_ZIP);
        userStoreRepository.save(UserStore.builder().user(user).store(store).build());
    }

    @Override
    public void unzipStore(String userId, Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(()-> new GeneralException(ErrorStatus.STORE_NOT_EXIST));
        User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(()-> new GeneralException(ErrorStatus.USER_NOT_FOUND_ERROR));
        Optional<UserStore> userStore = userStoreRepository.findByStoreAndUser(store,user);
        if(userStore.isEmpty()) throw new GeneralException(ErrorStatus.STORE_NOT_ZIP);
        userStoreRepository.delete(userStore.get());
    }

    @Override
    public List<StoreResponseDTO.searchStore> searchStore(String name) {
        List<Store> stores = storeRepository.findByNameContains(name);
        List<StoreResponseDTO.searchStore> result = new ArrayList<>();
        for(Store store:stores){
            StoreResponseDTO.searchStore dto = StoreResponseDTO.searchStore.builder()
                    .storeName(store.getName()).position(store.getPosition()).id(store.getId()).build();
            result.add(dto);
        }
        return result;
    }

    //가게 상세 정보 조회
    @Override
    public StoreResponseDTO.StoreDetailInfoDTO getStoreDetailInfo(Long storeId) {
        Optional<Store> store = storeRepository.findById(storeId);
        if (store.isPresent()) {
            String waitingAverage = store.get().getWaitingAverage();
            String businessHours = store.get().getBusinessHours();
            String closedDay = store.get().getClosedDay();
            String position = store.get().getPosition();

            return StoreResponseDTO.StoreDetailInfoDTO.builder()
                    .waitingAverage(waitingAverage)
                    .BusinessHours(businessHours)
                    .closedDay(closedDay)
                    .position(position)
                    .build();
        } else {
            throw new GeneralException(ErrorStatus.STORE_NOT_EXIST_ERROR);
        }
    }

    //가게 정보 내 메뉴판 조회
    @Override
    public List<StoreResponseDTO.PrintedMenuDTO> getStorePrintedMenu(Long storeId) {
        storeRepository.findById(storeId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_NOT_EXIST_ERROR));

        List<PrintedMenu> printedMenus = printedMenuRepository.findByStore_Id(storeId);

        // 인쇄된 메뉴가 있는 경우, DTO로 변환하여 반환
        return printedMenus.stream()
                .map(printedMenu -> new StoreResponseDTO.PrintedMenuDTO(printedMenu.getId(), printedMenu.getImageURL()))
                .collect(Collectors.toList());
    }

    //내가 좋아하는 가게 조회
    @Override
    public List<StoreResponseDTO.MyZipDTO> getStoreMyZip(Long userId) {


        userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND_ERROR));

        List<UserStore> userStoreList = userStoreRepository.findByUser_Id(userId);


        List<Store> storeList = userStoreList.stream()
                .map(UserStore::getStore)
                .toList();



        return storeList.stream().limit(3)
                .map(store -> new StoreResponseDTO.MyZipDTO(store.getId(), store.getName(), store.getImages().get(0).getImageURL()))
                .collect(Collectors.toList());
    }
    //가게별 주간 평균 평점 계산
    public Double calculateWeeklyHotPlaceScore(Long storeId){
        LocalDate endDate=LocalDate.now();
        LocalDate startDate=endDate.minus(Period.ofDays(7));

        List<Review> reviewList= reviewRepository.findByStore_IdAndPaymentTimeBetween(storeId,startDate,endDate);
        if(reviewList.isEmpty()) return 0.0;

        double totalScore=reviewList.stream().mapToInt(Review::getScore).sum();
        return totalScore/reviewList.size();
    }

    //핫플레이스 API
    @Override
    public List<StoreResponseDTO.HotPlaceDTO> getHotPlace(Long schoolId) {
        //List<Store> storeList = storeRepository.findAll();
        School school = schoolRepository.findById(schoolId).orElseThrow(()->new GeneralException(ErrorStatus.SCHOOL_NOT_EXIST));
        List<Store> storeList = storeRepository.findBySchool(school);
        List<StoreResponseDTO.HotPlaceDTO> resultList = new ArrayList<>();

        for (Store store : storeList) {
            double score = calculateWeeklyHotPlaceScore(store.getId());
            score= (double) Math.round(score * 10) /10;

            StoreResponseDTO.HotPlaceDTO hotPlaceDTO = StoreResponseDTO.HotPlaceDTO.builder()
                    .score(score)
                    .storeId(store.getId())
                    .name(store.getName())
                    .imageUrl(store.getImages().get(0).getImageURL())
                    .category(store.getCategory().getName())
                    .inOut(store.getOutin())
                    .build();


            resultList.add(hotPlaceDTO);
        }
        // 'score'를 기준으로 resultList를 내림차순 정렬
        resultList.sort((dto1, dto2) -> Double.compare(dto2.getScore(), dto1.getScore()));
        long i=0;
        for(StoreResponseDTO.HotPlaceDTO dto : resultList){
            dto.setRanking(++i);
        }
        if(resultList.size()<=10){
            return resultList;
        }

        return new ArrayList<>(resultList.subList(0,10));
    }

    @Override
    public List<StoreResponseDTO.StoreListReviewCountDTO> getStoreListByReviewCount(Long userId,Long schoolId){
        //List<Store> storeList=storeRepository.findAllStoresOrderByReviewCountDesc();
        List<Store> storeList=storeRepository.findAllStoresBySchoolOrderByReviewCountDesc(schoolId);
        List<StoreResponseDTO.StoreListReviewCountDTO> resultList = new ArrayList<>();



        long i=0;
        for (Store store : storeList) {


            User user = userRepository.findById(userId).get();
            AtomicBoolean isMyZip = new AtomicBoolean(false);

            userStoreRepository.findByStoreAndUser(store, user).ifPresentOrElse(
                    bookmark -> isMyZip.set(true),
                    () -> {}
            );

            StoreResponseDTO.StoreListReviewCountDTO storeListReviewCountDTO = StoreResponseDTO.StoreListReviewCountDTO.builder()
                    .ReviewCount((long) store.getReviewList().size())
                    .storeId(store.getId())
                    .name(store.getName())
                    .imageUrl(store.getImages().get(0).getImageURL())
                    .category(store.getCategory().getName())
                    .inOut(store.getOutin())
                    .Ranking(++i)
                    .isMyZip(isMyZip.get())
                    .storeListStaus(StoreListStaus.REVIEWCOUNT)
                    .build();

            resultList.add(storeListReviewCountDTO);
        }
        return resultList;

    }
    public Double calculateStoreAverageScore(Store store){

        List<Review> reviewList= reviewRepository.findAllByStore(store);
        if(reviewList.isEmpty()) return 0.0;

        double totalScore=reviewList.stream().mapToInt(Review::getScore).sum();
        return totalScore/reviewList.size();
    }

    @Override
    public List<StoreResponseDTO.StoreListScoreDTO> getStoreListByScore(Long userId,Long schoolId){
        //List<Store> storeList=storeRepository.findAll();
        School school = schoolRepository.findById(schoolId).orElseThrow(()->new GeneralException(ErrorStatus.SCHOOL_NOT_EXIST));
        List<Store> storeList = storeRepository.findBySchool(school);
        List<StoreResponseDTO.StoreListScoreDTO> resultList=new ArrayList<>();
        long i=0;
        for(Store store: storeList){
            User user = userRepository.findById(userId).get();
            AtomicBoolean isMyZip = new AtomicBoolean(false);

            userStoreRepository.findByStoreAndUser(store, user).ifPresentOrElse(
                    bookmark -> isMyZip.set(true),
                    () -> {}
            );

            double score = calculateStoreAverageScore(store);
            score= (double) Math.round(score * 10) /10;

            StoreResponseDTO.StoreListScoreDTO storeListScoreDTO=StoreResponseDTO.StoreListScoreDTO.builder()
                    .score((score))
                    .storeId(store.getId())
                    .name(store.getName())
                    .imageUrl(store.getImages().get(0).getImageURL())
                    .category(store.getCategory().getName())
                    .storeListStaus(StoreListStaus.SCORE)
                    .inOut(store.getOutin())
                    .isMyZip(isMyZip.get())
                    .build();
            resultList.add(storeListScoreDTO);

        }
        // 'score'를 기준으로 resultList를 내림차순 정렬
        resultList.sort((dto1, dto2) -> Double.compare(dto2.getScore(), dto1.getScore()));
        i=0;
        for(StoreResponseDTO.StoreListScoreDTO dto : resultList){
            dto.setRanking(++i);
        }

        return resultList;
    }



    // ** 대기 시간 계산 함수 ** //
    private Integer calculateTime(List<Congestion> congestionList){
        float waitingTimeSum=0; int num=0;
        for(int k =0 ;k<congestionList.size();k++){
            if(congestionList.get(k).getOccupancyCount() != null)
            {
                waitingTimeSum+=congestionList.get(k).getWaitTime(); num++;
            }
        }
        if (num==0) return null;
        return Math.round(waitingTimeSum/congestionList.size());
    }

    // ** 해시태그 계산 ** //
    List<String> calculateHashtag(Hashtag hashtag) {
        Integer[] hashtags = {hashtag.getH1(), hashtag.getH2(), hashtag.getH3(),
                hashtag.getH4(), hashtag.getH5(), hashtag.getH6(),
                hashtag.getH7(), hashtag.getH8()};
        List<Integer> indexList = new ArrayList<Integer>();
        int maxValue = Integer.MIN_VALUE; // 가장 작은 값을 최대값으로 초기화합니다.
        int maxIndex = -1;
        for(int k=0;k<3;k++){
            for (int i = 0; i < hashtags.length; i++) {
                if (hashtags[i] > maxValue) {
                    maxValue = hashtags[i]; // 일단 가장 큰값에 넣음
                    maxIndex = i;
                }
            }
            if(hashtags[maxIndex] == 0) break; // 0이면 담지 않음
            System.out.println(maxIndex);
            indexList.add(maxIndex);
            hashtags[maxIndex] = -1; // 넣어둔건 -1로 해둬서 최대값으로 찾지 못하게 함
            maxValue=Integer.MIN_VALUE; // 다시 max value 초기화
        }
        List<String> result = new ArrayList<>();
        indexList.stream().forEach((i)->result.add(hashtagName.values()[i].toString()));
        return result;
    }

    @Override
    public StoreResponseDTO.RecommandDTO getRecommendStoresByLikes(Long userId,String categoryName,Long schoolId) {
        // 탑 리뷰 찾기
        Review topReview = userReviewRepository.findTopReviewByLikesPerCategory(categoryName,schoolId);
        User currentUser = userRepository.findById(userId).get();
        // 리뷰가 없는 경우 예외 처리
        if(topReview == null) {
            throw new GeneralException(ErrorStatus.REVIEW_NOT_EXIST);
        }


        // 탑 리뷰에서 스토어 정보 가져오기.
        Store store = topReview.getStore();

        // 탑 리뷰에서 사용자 정보 가져오기.
        User user = topReview.getUser();

        // 북마크 있는지 확인
        UserStore userStore = userStoreRepository.findByUserAndStore(currentUser, store);

        boolean bookmark = false;

        if(userStore != null){
            bookmark = true;
        }

        Optional<StoreImage> storeImage = storeImageRepository.findByStore(store);



        // DTO에 정보를 매핑하여 반환합니다.
        return StoreResponseDTO.RecommandDTO.builder()
                .storeId(store.getId())
                .storeName(store.getName())
                .userName(user.getNickname())
                .bookmark(bookmark)
                .content(topReview.getContent())
                .imageURL(storeImage.get().getImageURL())
                .build();

    }

    @Override
    public List<List<StoreResponseDTO.menuDTO>> getMenuList(Long storeId) {
        storeRepository.findById(storeId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_NOT_EXIST_ERROR));

        List<Menu_Group> menuGroups = menuGroupRepository.findByStoreId(storeId);

        List<List<StoreResponseDTO.menuDTO>> menuList = new ArrayList<>();
        for (Menu_Group menuGroup : menuGroups) {
            List<Menu> menus = menuRepository.findByMenuGroupId(menuGroup.getId());
            List<StoreResponseDTO.menuDTO> menuDTOs = menus.stream()
                    .map(menu -> StoreResponseDTO.menuDTO.builder()
                            .groupName(menuGroup.getName())
                            .menuName(menu.getMenuName())
                            .price(menu.getPrice())
                            .explanation(menu.getExplanation())
                            .imageURL(menu.getImageURL())
                            .build())
                    .collect(Collectors.toList());
            menuList.add(menuDTOs);
        }

        return menuList;
    }
}
