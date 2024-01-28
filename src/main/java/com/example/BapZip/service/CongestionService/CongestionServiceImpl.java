package com.example.BapZip.service.CongestionService;

import com.example.BapZip.apiPayload.code.status.ErrorStatus;
import com.example.BapZip.apiPayload.exception.GeneralException;
import com.example.BapZip.domain.*;
import com.example.BapZip.domain.enums.CongestionLevel;
import com.example.BapZip.domain.enums.InOrOut;
import com.example.BapZip.domain.enums.VisitStatus;
import com.example.BapZip.repository.*;
import com.example.BapZip.web.dto.CongestionRequestDTO;
import com.example.BapZip.web.dto.CongestionResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;
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

        TimeZone koreaTimeZone = TimeZone.getTimeZone("Asia/Seoul");;

        // 각 가게에 대한 혼잡도 계산
        for(int i =0 ;i<stores.size();i++){
            Store store = stores.get(i);
            CongestionResponseDTO.getCongestionRanking congstionDTO;
            Integer waitingTimeAv = null;
            Integer occupyCountAv = null;

            List<Congestion> congestionList =
                    congestionRepository.findByStoreAndCreatedAtAfter(store, LocalDateTime.now(koreaTimeZone.toZoneId()).minusMinutes(60));


            if(!congestionList.isEmpty()){
                waitingTimeAv = calculateTime(congestionList); occupyCountAv = calculateCount(congestionList);
            }

            congstionDTO =
                    CongestionResponseDTO.getCongestionRanking.builder().storeId(store.getId()).storeName(store.getName())
                            .waitingCount(occupyCountAv).waitingTime(waitingTimeAv).build();

            // DTO에 가게 이미지 추가
            storeImageRepository.findByStore(store).ifPresentOrElse(
                    storeImage -> congstionDTO.setStoreImageURL(storeImage.getImageURL()),
                    () -> congstionDTO.setStoreImageURL(null)
            );

            // DTO에 북마크 여부 추가
            userStoreRepository.findByStoreAndUser(store,user).ifPresentOrElse(
                    bookmark -> congstionDTO.setBookmark(true),
                    () -> congstionDTO.setBookmark(false)
            );

            // dto 리스트에 추가
            result.add(congstionDTO);
        }

        return result;
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

    // 랭킹 계산





}
