package com.example.BapZip.service.CongestionService;

import com.example.BapZip.apiPayload.code.status.ErrorStatus;
import com.example.BapZip.apiPayload.exception.GeneralException;
import com.example.BapZip.domain.*;
import com.example.BapZip.domain.enums.CongestionLevel;
import com.example.BapZip.domain.enums.InOrOut;
import com.example.BapZip.domain.enums.VisitStatus;
import com.example.BapZip.domain.mapping.UserStore;
import com.example.BapZip.repository.*;
import com.example.BapZip.web.dto.CongestionRequestDTO;
import com.example.BapZip.web.dto.CongestionResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CongestionServiceImpl implements CongestionService{

    private final CongestionRepository congestionRepository;
    private final UserRepository userRepository;
    private final PointRepository pointRepository;
    private final StoreRepository storeRepository;
    private final SchoolRepository schoolRepository;
    private final StoreImageRepository storeImageRepository;
    private  final UserStoreRepository userStoreRepository;
    @Override
    public CongestionResponseDTO.registerCongestion registerCongestion(CongestionRequestDTO.registerCongestion congestionDTO, Long storeId, String userId) throws GeneralException {

        Store store = storeRepository.findById(storeId).orElseThrow(()-> new GeneralException(ErrorStatus.STORE_NOT_EXIST));

        User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new GeneralException(ErrorStatus._FORBIDDEN));
        try {
            // 1. 혼잡도 등록
            Congestion congestion = Congestion.builder()
                    .congestionLevel(CongestionLevel.valueOf(congestionDTO.getCongestionLevel()))
                    .occupancyCount(congestionDTO.getOccupancyCount())
                    .visitStatus(VisitStatus.valueOf(congestionDTO.getVisitStatus())).user(user).store(store)
                    .waitTime(congestionDTO.getWaitTime()).build();
            congestionRepository.save(congestion);

            // 2. 포인트 등록
            Point point = Point.builder().point(Long.valueOf(5)).user(user).classification("획득").note(store.getName()+" 혼잡도 등록").build();
            pointRepository.save(point);

            return CongestionResponseDTO.registerCongestion.builder().Point(point.getPoint()).build();
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.CONGESTION_REGISTER_ERROR);
        }

    }

    @Override
    public List<CongestionResponseDTO.getCongestionRanking> getRanking(String userId, String classification, Long schoolId) {

        // 반환할 dto 리스트
        List<CongestionResponseDTO.getCongestionRanking> result=new ArrayList<>();

        // 사용자
        User user = userRepository.findById(Long.valueOf(userId)).get();

        // 학교
        School school=schoolRepository.findById(schoolId).get();



        // Request에 대한 가게 리스트 가져옴
        List<Store> stores;
        if(classification.equals("ALL")) stores= storeRepository.findBySchool(schoolRepository.findById(schoolId).get());
        else stores= storeRepository.findBySchoolAndOutin(school, InOrOut.valueOf(classification));

        // 각 가게에 대한 혼잡도 계산
        for(int i =0 ;i<stores.size();i++){
            Store store = stores.get(i);
            CongestionResponseDTO.getCongestionRanking congstionDTO;
            Integer waitingTimeAv = null;
            Integer occupyCountAv = null;
            Integer levelAv = null;
            List<Congestion> congestionList =
                    congestionRepository.findByStoreAndCreatedAtAfter
                            (store, LocalDateTime.now(TimeZone.getTimeZone("Asia/Seoul").toZoneId()).minusMinutes(60));


            // 함수에 넣어서 혼잡도 계산 후 각 변수에 저장
            if(!congestionList.isEmpty()){
                waitingTimeAv = calculateTime(congestionList);
                occupyCountAv = calculateCount(congestionList);
                levelAv = calculateCongestionLevel(congestionList);
            }
            // 계산한 혼잡도 값을 세팅해서 dto 만듬
            congstionDTO =
                    CongestionResponseDTO.getCongestionRanking.builder().storeId(store.getId()).storeName(store.getName())
                            .occupancyCount(occupyCountAv).waitingTime(waitingTimeAv).congestionAV(levelAv)
                            .storeImageURL(storeImageURL(store)).bookmark(bookmarkBoolean(store,user)).build();
            // result list에 추가
            result.add(congstionDTO);
        }

        // 반환전 sorting 한 후 반환
        return sortByCongestion(result);
    }

    @Override
    public List<CongestionResponseDTO.getCongestionRankingTop5> getRankingTop5(String userId, Long schoolId) {
        School school=schoolRepository.findById(schoolId).get();
        List<Store> stores= storeRepository.findBySchool(schoolRepository.findById(schoolId).get());
        List<CongestionResponseDTO.getCongestionRankingTop5> result = new ArrayList<>();
        // 각 가게에 대한 혼잡도 계산
        for(int i =0 ;i<stores.size();i++){
            Store store = stores.get(i);
            CongestionResponseDTO.getCongestionRankingTop5 congstionDTO;
            Integer waitingTimeAv = null;
            Integer occupyCountAv = null;
            Integer levelAv = null;
            List<Congestion> congestionList =
                    congestionRepository.findByStoreAndCreatedAtAfter
                            (store, LocalDateTime.now(TimeZone.getTimeZone("Asia/Seoul").toZoneId()).minusMinutes(60));


            // 함수에 넣어서 혼잡도 계산 후 각 변수에 저장
            if(!congestionList.isEmpty()){
                waitingTimeAv = calculateTime(congestionList);
                occupyCountAv = calculateCount(congestionList);
                levelAv = calculateCongestionLevel(congestionList);
            }

            // 계산한 혼잡도 값을 세팅해서 dto 만듬
            congstionDTO =
                    CongestionResponseDTO.getCongestionRankingTop5.builder().storeId(store.getId()).storeName(store.getName())
                            .waitingTime(waitingTimeAv).congestionAV(levelAv).build();
            // result list에 추가
            result.add(congstionDTO);
        }
        if(result.size()<=5) return sortTop5ByCongestion(result);
        else return sortTop5ByCongestion(result).subList(0,5);
    }

    // ** 대기 인원 계산 ** //
    private Integer calculateCount(List<Congestion> congestionList){
        float occupyCountSum=0; int num=0;
        for(int k =0 ;k<congestionList.size();k++){
            if(congestionList.get(k).getOccupancyCount() != null)
            {
                occupyCountSum+=congestionList.get(k).getOccupancyCount(); num++;
            }
        }
        if (num==0) return null;
        return Math.round(occupyCountSum/congestionList.size());
    }

    // ** 대기 시간 계산 ** //
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

    // ** 혼잡 정도 계산 ** //
    private Integer calculateCongestionLevel(List<Congestion> congestionList){
        float congestionLevelSum=0; int num=0;
        for(int k =0 ;k<congestionList.size();k++){
            if(congestionList.get(k).getCongestionLevel() != null)
            {
                congestionLevelSum+=congestionList.get(k).getCongestionLevel().getValue(); num++;
            }
        }
        if (num==0) return null;
        return Math.round(congestionLevelSum/congestionList.size());
    }


    // 랭킹 계산
    private List<CongestionResponseDTO.getCongestionRanking> sortByCongestion(List<CongestionResponseDTO.getCongestionRanking> dtos) {
        System.out.println(dtos.size());
        for (CongestionResponseDTO.getCongestionRanking dto : dtos) {
            if(dto.getCongestionAV()!=null) dto.setCongestionAV(dto.getCongestionAV() + dto.getWaitingTime());
            // 혼잡도 정보가 없어 null 값이라면, -1으로 세팅
        }
        dtos.sort(new Comparator<CongestionResponseDTO.getCongestionRanking>() {
            @Override
            public int compare(CongestionResponseDTO.getCongestionRanking o1, CongestionResponseDTO.getCongestionRanking o2) {
                if(o1.getCongestionAV()==null || o1.getCongestionAV() < o2.getCongestionAV() ) return 1;
                else if (o1.getCongestionAV() > o2.getCongestionAV()) return -1;
                else return 0;
            }
        });
        return dtos;
    }
    // top5 게산
    private List<CongestionResponseDTO.getCongestionRankingTop5> sortTop5ByCongestion(List<CongestionResponseDTO.getCongestionRankingTop5> dtos) {
        System.out.println(dtos.size());
        for (CongestionResponseDTO.getCongestionRankingTop5 dto : dtos) {
            if(dto.getCongestionAV()!=null) dto.setCongestionAV(dto.getCongestionAV() + dto.getWaitingTime());
            // 혼잡도 정보가 없어 null 값이라면, -1으로 세팅
        }
        dtos.sort(new Comparator<CongestionResponseDTO.getCongestionRankingTop5>() {
            @Override
            public int compare(CongestionResponseDTO.getCongestionRankingTop5 o1, CongestionResponseDTO.getCongestionRankingTop5 o2) {
                if(o1.getCongestionAV()==null || o1.getCongestionAV() < o2.getCongestionAV() ) return 1;
                else if (o1.getCongestionAV() > o2.getCongestionAV()) return -1;
                else return 0;
            }
        });
        return dtos;
    }

    // 북마크 여부 세팅
    private Boolean bookmarkBoolean(Store store , User user){
        Optional<UserStore> userStore = userStoreRepository.findByStoreAndUser(store,user);
        if(userStore.isPresent()) return true;
        else return false;
    }

    // 이미지 세팅
    private String storeImageURL(Store store){
        Optional<StoreImage> image = storeImageRepository.findByStore(store);
        if(image.isPresent()) return image.get().getImageURL();
        else return null;
    }



}
