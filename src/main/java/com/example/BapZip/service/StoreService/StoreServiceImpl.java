package com.example.BapZip.service.StoreService;

import com.example.BapZip.apiPayload.code.status.ErrorStatus;
import com.example.BapZip.apiPayload.exception.GeneralException;
import com.example.BapZip.domain.PrintedMenu;
import com.example.BapZip.domain.Store;
import com.example.BapZip.domain.User;
import com.example.BapZip.domain.mapping.UserStore;
import com.example.BapZip.repository.PrintedMenuRepository;
import com.example.BapZip.repository.StoreRepository;
import com.example.BapZip.repository.UserRepository;
import com.example.BapZip.repository.UserStoreRepository;
import com.example.BapZip.security.TokenProvider;
import com.example.BapZip.web.dto.MypageResponseDTO;
import com.example.BapZip.web.dto.StoreResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        // 인쇄된 메뉴가 없는 경우 예외 처리
        if (printedMenus.isEmpty()) {
            throw new GeneralException(ErrorStatus.PRINTED_MENU_NOT_FOUND_ERROR);
        }

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
        if (userStoreList.isEmpty()) {
            throw new GeneralException(ErrorStatus.STORE_NOT_FOUND_FOR_USER_ERROR);
        }

        List<Store> storeList = userStoreList.stream()
                .map(UserStore::getStore)
                .filter(store -> !(store.getImages() == null || store.getImages().isEmpty()))
                .toList();

        if (storeList.isEmpty()) {
            throw new GeneralException(ErrorStatus.STORE_IMAGE_NOT_EXIST_ERROR);
        }

        return storeList.stream().limit(3)
                .map(store -> new StoreResponseDTO.MyZipDTO(store.getId(), store.getName(), store.getImages().get(0).getImageURL()))
                .collect(Collectors.toList());
    }

}
