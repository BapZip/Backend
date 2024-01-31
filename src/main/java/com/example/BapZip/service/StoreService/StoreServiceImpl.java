package com.example.BapZip.service.StoreService;

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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final PrintedMenuRepository printedMenuRepository;
    private final UserRepository userRepository;
    private final UserStoreRepository userStoreRepository;
    private final ReviewRepository reviewRepository;


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


}
