package com.example.BapZip.service.StoreService;

import com.example.BapZip.domain.*;
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
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;
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


    @Override
    public StoreResponseDTO.StoreInfoDTO getStoreInfo(String userId, Long storeId) {

        Store store = storeRepository.findById(storeId).get();
        User user = userRepository.findById(Long.valueOf(userId)).get();

        // ** 1. 가게 이름, 내외부 ** //
        StoreResponseDTO.StoreInfoDTO result = StoreResponseDTO.StoreInfoDTO.builder()
                .name(store.getName()).inOrOut(store.getOutin().toString()).build();


        // ** 2. 가게 이미지 ** //
        List<String> imageURLs = new ArrayList<>();
        storeImageRepository.findAllByStore(store).stream().forEach((entity)->imageURLs.add(entity.getImageURL()));
        result.setImages(imageURLs);



        // ** 3. 웨이팅 시간 ** //
        Integer waitingTimeAv = null;
        List<Congestion> congestionList =
                congestionRepository.findByStoreAndCreatedAtAfter
                        (store, LocalDateTime.now(TimeZone.getTimeZone("Asia/Seoul").toZoneId()).minusMinutes(60));
        if(!congestionList.isEmpty()) waitingTimeAv = calculateTime(congestionList);
        result.setWaitTime(waitingTimeAv);


        // ** 4. 북마크 여부 ** //
        userStoreRepository.findByStoreAndUser(store,user).ifPresentOrElse(
                bookmark -> result.setBookmark(true), () -> result.setBookmark(false) );


        // ** 5. 리뷰 평점 ** //
        List<Review> reviewList = reviewRepository.findAllByStore(store);
        if (reviewList.isEmpty()){result.setScore(null);} // 리뷰없으면 null
        else{
            AtomicInteger reviewAv = new AtomicInteger(0);
            reviewList.stream().forEach((review) -> reviewAv.addAndGet(review.getScore()));
            result.setScore((double)(reviewAv.get() / reviewList.size()));
        }

        // ** 해시태그 ** //
        List<String> hashtags = calculateHashtag(hashtagRepository.findByStore(store).get());
        result.setHashtag(hashtags);
        return result;

    }
     //가게 상세 정보 조회
    @Override
    public StoreResponseDTO.StoreInfoDTO getStoreDetailInfo(Long storeId) {
        Optional<Store> store = storeRepository.findById(storeId);
        if (store.isPresent()) {
            String waitingAverage = store.get().getWaitingAverage();
            String businessHours = store.get().getBusinessHours();
            String closedDay = store.get().getClosedDay();
            String position = store.get().getPosition();

            return StoreResponseDTO.StoreInfoDTO.builder()
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
    public List<StoreResponseDTO.HotPlaceDTO> getHotPlace() {
        List<Store> storeList = storeRepository.findAll();
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


}
