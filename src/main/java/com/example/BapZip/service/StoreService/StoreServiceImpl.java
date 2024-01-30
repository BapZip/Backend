package com.example.BapZip.service.StoreService;

import com.example.BapZip.domain.*;
import com.example.BapZip.domain.enums.hashtagName;
import com.example.BapZip.repository.*;
import com.example.BapZip.web.dto.CongestionResponseDTO;
import com.example.BapZip.web.dto.StoreResponseDTO;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
